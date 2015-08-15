package com.revtekk.nioflex;

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

import com.revtekk.nioflex.utils.SocketUtil;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class MyServer extends NIOServer
{
    public MyServer(int port)
    {
        super(port);
    }

    @Override
    public void handleAccept(SocketChannel client, SelectionKey key)
    {
        System.out.println("Client connected");
    }

    @Override
    public void handleRead(SocketChannel client, SelectionKey key, SocketUtil util)
    {
        System.out.println("Data: " + util.readString(util.readInt()));
    }
}
