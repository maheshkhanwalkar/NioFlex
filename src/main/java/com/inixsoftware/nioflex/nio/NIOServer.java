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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class NIOServer implements Runnable
{
    protected int port;
    protected ServerSocketChannel server;

    protected Selector selector;
    protected int cores = Runtime.getRuntime().availableProcessors();

    protected ThreadPoolExecutor pool = new ThreadPoolExecutor(cores, cores,
            10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());


    /* Restrict access to this class from sub-classes */

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

    protected ByteBuffer readBuffer(int len, SocketChannel client)
    {
        ByteBuffer buffer = ByteBuffer.allocate(len);

        int read = 0;

        try
        {
            while((read += client.read(buffer)) < len);
            buffer.flip();

            return buffer;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    protected byte[] readBytes(int len, SocketChannel client)
    {
        return readBuffer(len, client).array();
    }

    protected String readString(int len, SocketChannel client)
    {
        byte[] bytes = readBytes(len, client);
        return new String(bytes); //uses default encoding
    }

    protected String readString(int len, SocketChannel client, Charset charset)
    {
        byte[] bytes = readBytes(len, client);
        return new String(bytes, charset);
    }

    protected int readInt(SocketChannel client)
    {
        return readBuffer(4, client).getInt();
    }

    protected void writeBytes(byte[] bytes, SocketChannel client)
    {
        ByteBuffer buf = ByteBuffer.allocate(bytes.length);
        buf.put(bytes);

        buf.flip();

        try
        {
            client.write(buf);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected void writeString(String str, SocketChannel client)
    {
        writeBytes(str.getBytes(), client);
    }

    protected void writeString(String str, SocketChannel client, Charset charset)
    {
        writeBytes(str.getBytes(charset), client);
    }

    protected void writeInt(int num, SocketChannel client)
    {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(num);

        buf.flip();

        try
        {
            client.write(buf);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method is invoked after the server accepts a new client
     *
     * @param client - SocketChannel corresponding to the client
     * @param key - current SelectionKey
     */
    public abstract void handleAccept(SocketChannel client, SelectionKey key);

    /**
     * This method is invoked once data can be read from the client
     * To prevent out-of-order reads, key.interestOps(0) is called before it is passed
     * to this method
     *
     * @param client - SocketChannel corresponding to the client
     * @param key - current SelectionKey
     */
    public abstract void handleRead(SocketChannel client, SelectionKey key);

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
                        SocketChannel client = (SocketChannel)key.channel();
                        key.interestOps(0);

                        IOProcessor proc = new IOProcessor(client, key);
                        pool.execute(proc);
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void run()
    {
        mainLoop();
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public void setServer(ServerSocketChannel server)
    {
        this.server = server;
    }

    public void setSelector(Selector selector)
    {
        this.selector = selector;
    }
}
