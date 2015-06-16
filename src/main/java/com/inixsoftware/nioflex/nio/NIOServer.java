package com.inixsoftware.nioflex.nio;

/*
    Copyright 2015 Mahesh Khanwalkar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import com.inixsoftware.nioflex.nio.utils.NIOUtils;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * NIOServer processes NIO events
 *
 * @author Mahesh Khanwalkar
 */
public abstract class NIOServer implements Runnable
{
    /**
     * Port that the server is bound to
     */
    protected int port;

    /**
     * NIO ServerSocketChannel used by the server
     */
    protected ServerSocketChannel server;

    /**
     * NIO Selector used by the server
     */
    protected Selector selector;

    /**
     * Number of processor cores (visible to the JVM)
     */
    protected int cores = Runtime.getRuntime().availableProcessors();

    /**
     * Thread Pool used by the server to handle events
     */
    protected ThreadPoolExecutor pool = new ThreadPoolExecutor(cores, cores * 2,
            1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());

    /**
     * Flag to shutdown the server
     */
    private volatile boolean SHUTDOWN = false;

    /**
     * Tell the server to shutdown
     */
    protected void scheduleShutdown()
    {
        SHUTDOWN = true;
    }

    /* PRIVATE - Restrict access */

    /**
     * Executed on another thread and passes control
     * to the handleRead(SocketChannel, SelectionKey) methods
     */
    private class IOProcessor implements Runnable
    {
        private SocketChannel client;
        private SelectionKey key;

        public IOProcessor(SocketChannel client, SelectionKey key)
        {
            this.client = client;
            this.key = key;
        }

        public void run()
        {
            handleRead(client, key);

            /* Re-enable read */

            key.interestOps(key.interestOps() ^ SelectionKey.OP_READ);
            selector.wakeup();
        }
    }

    /**
     * This method is invoked after the server accepts a new client
     *
     * @param client SocketChannel corresponding to the client
     * @param key current SelectionKey
     */
    public abstract void handleAccept(SocketChannel client, SelectionKey key);

    /**
     * This method is invoked once data can be read from the client.
     *
     * @param client SocketChannel corresponding to the client
     * @param key current SelectionKey
     */
    public abstract void handleRead(SocketChannel client, SelectionKey key);

    /**
     * NIO Event Handler (e.g. Accepting Clients & Task Scheduling)
     */
    private void mainLoop()
    {
        while(true)
        {
            try
            {
                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> itr = keys.iterator();

                while(itr.hasNext())
                {
                    SelectionKey key = itr.next();
                    itr.remove();

                    if(key.isAcceptable())
                    {
                        SocketChannel client = server.accept();

                        if(client == null)
                            continue;

                        client.configureBlocking(false);

                        client.register(selector, SelectionKey.OP_READ);
                        handleAccept(client, key);
                    }

                    if(key.isReadable())
                    {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer oneByte = ByteBuffer.allocate(1);

                        if (client.read(oneByte) < 0)
                            continue;

                        oneByte.flip();
                        NIOUtils.refund(client, oneByte);

                        key.interestOps(0);

                        IOProcessor proc = new IOProcessor(client, key);
                        pool.submit(proc);

                    }
                }

                if(SHUTDOWN)
                {
                    //Server will shutdown NOW!
                    pool.shutdown();

                    selector.close();
                    server.close();

                    return;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Runs the mainLoop() method which handles
     * NIO events via Selectors
     */
    public void run()
    {
        mainLoop();
    }

    /**
     * Sets the NIOServer's port
     * @param port port to bind to
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Sets NIOServer's internal ServerSocketChannel
     * @param server ServerSocketChannel for the server
     */
    public void setServer(ServerSocketChannel server)
    {
        this.server = server;
    }

    /**
     * Sets NIOServer's internal Selector
     * @param selector Selector for NIO processes
     */
    public void setSelector(Selector selector)
    {
        this.selector = selector;
    }
}
