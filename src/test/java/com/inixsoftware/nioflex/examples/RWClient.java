package com.inixsoftware.nioflex.examples;

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
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class RWClient
{
    @Test
    public static void main(String[] args) throws IOException
    {
        SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5252));
        client.configureBlocking(true);

        NIOUtils.writeShort((short) 1024, client);
        short resp = NIOUtils.readShort(client);

        System.out.println(resp); //server sends 4096
        NIOUtils.writeInt(256, client);

        int iNum = NIOUtils.readInt(client);
        System.out.println(iNum); //server sends 512

        NIOUtils.writeLong(65536, client);
        long lNum = NIOUtils.readLong(client);

        System.out.println(lNum); //server sends 131072
        client.close();
    }
}
