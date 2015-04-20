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
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class ServerDispatch
{
    private int port;
    private ServerSocketChannel server;

    private Selector selector;
    private NIOServer nio;

    public ServerDispatch(int port, NIOServer nio)
    {
        this.port = port;
        this.nio = nio;

        try
        {
            server = ServerSocketChannel.open().bind(new InetSocketAddress("localhost", port));
            selector = Selector.open();

            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);

            this.nio.setPort(port);
            this.nio.setServer(server);

            this.nio.setSelector(selector);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void startUp()
    {
        Thread dispatch = new Thread(nio);
        dispatch.start();
    }

    public int getPort()
    {
        return port;
    }

    public Selector getSelector()
    {
        return selector;
    }

    public ServerSocketChannel getServerChannel()
    {
        return server;
    }
}
