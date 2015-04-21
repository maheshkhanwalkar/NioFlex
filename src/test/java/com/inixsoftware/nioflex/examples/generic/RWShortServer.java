package com.inixsoftware.nioflex.examples.generic;

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

import com.inixsoftware.nioflex.nio.NIOServer;
import com.inixsoftware.nioflex.nio.utils.NIOUtils;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

//tests read/write of short ints
public class RWShortServer extends NIOServer
{
    @Override
    public void handleAccept(SocketChannel client, SelectionKey key)
    {
        System.out.println("Client Accepted!");
    }

    @Override
    public void handleRead(SocketChannel client, SelectionKey key)
    {
        short num = NIOUtils.readShort(client);
        System.out.println(num); //client test sends 1024

        NIOUtils.writeShort((short)4096, client);
    }
}
