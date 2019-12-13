package com.revtekk.nioflex.impl;

import com.revtekk.nioflex.config.SecurityType;
import com.revtekk.nioflex.config.ServerHooks;
import com.revtekk.nioflex.config.ServerOption;
import com.revtekk.nioflex.main.Client;
import com.revtekk.nioflex.main.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class TCPServer extends Server
{
    // Security policy
    private SecurityType security;

    // Server management objects
    private ServerSocketChannel server;
    private Selector selector;
    private AtomicBoolean quit;
    private ExecutorService pool;
    private Thread main;
    private Map<SocketChannel, Client> map;

    public TCPServer(InetAddress address, int port, ServerHooks hooks,
            SecurityType security, ServerOption... options)
    {
        super(address, port, hooks, options);

        this.security = security;
        this.options = options;
        this.quit = new AtomicBoolean();
        this.map = new HashMap<>();
    }

    @Override
    public void start() throws IOException
    {
        // TLS support will come later (in another branch)
        if(security != SecurityType.SECURITY_NONE)
            throw new UnsupportedOperationException("TLS support not implemented");

        // Perform initial server setup
        selector = Selector.open();
        server = ServerSocketChannel.open().bind(new InetSocketAddress(address, port));

        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);

        // Initialise the executor service
        // TODO: allow for core count customisation
        int count = Runtime.getRuntime().availableProcessors();
        pool = new ThreadPoolExecutor(count, count, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

        main = new Thread(this::eventLoop);
        main.start();
    }

    private void eventLoop()
    {
        try
        {
            while(!quit.get())
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

                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);

                        Client wrapper = new Client(new StreamLayer(client));
                        map.put(client, wrapper);

                        // FIXME: should this be on the event loop thread?
                        // TODO: maybe make this configurable (like it was previously)
                        hooks.onAccept(wrapper);
                    }

                    if(key.isReadable())
                    {
                        // TODO: add onRead() hook
                        SocketChannel client = (SocketChannel)key.channel();
                        Client wrapper = map.get(client);

                        key.interestOps(0);

                        // process the request with the executor service
                        pool.submit(() -> {
                            hooks.onRead(wrapper);
                            key.interestOps(SelectionKey.OP_READ);
                        });
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() throws InterruptedException, IOException
    {
        quit.set(true);
        main.join();

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);

        server.close();
        selector.close();
    }
}
