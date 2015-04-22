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

/**
 * Initializes and Dispatches NIO Servers on a
 * new thread
 *
 * @author Mahesh Khanwalkar
 */
public class ServerDispatch
{

    /**
     * NIO Server to launch
     */
    private NIOServer nio;

    /**
     * Thread that the NIO Server is launched on
     */
    private Thread dispatch;

    /**
     * Initializes and Binds NIO Server to the provided port
     * @param port port to bind the server on
     * @param nio NIO server to run
     */
    public ServerDispatch(int port, NIOServer nio)
    {
        this.nio = nio;

        try
        {
            ServerSocketChannel server = ServerSocketChannel.open().bind(new InetSocketAddress("localhost", port));
            Selector selector = Selector.open();

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

    /**
     * Launches the server on another thread
     */
    public void startUp()
    {
        dispatch = new Thread(nio);
        dispatch.start();
    }

    /**
     * Calls join() on the Server's thread
     */
    public void joinThread()
    {
        try
        {
            dispatch.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
