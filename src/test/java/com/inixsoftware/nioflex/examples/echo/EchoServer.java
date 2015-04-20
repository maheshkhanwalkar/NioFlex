package com.inixsoftware.nioflex.examples.echo;

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
import java.nio.charset.Charset;

public class EchoServer extends NIOServer
{
    @Override
    public void handleAccept(SocketChannel client, SelectionKey key)
    {
        System.out.println("Echo Server has accepted a client!");
    }

    @Override
    public void handleRead(SocketChannel client, SelectionKey key)
    {
        //the client first writes the length of the string to read
        //and then the actual string

        int bytesToRead = NIOUtils.readInt(client);
        String str = NIOUtils.readString(bytesToRead, client, Charset.forName("UTF-8"));

        System.out.println("Received: " + str);

        //write back, first the string's length, then the actual string
        NIOUtils.writeInt(str.length(), client);
        NIOUtils.writeString(str, client, Charset.forName("UTF-8"));
    }
}
