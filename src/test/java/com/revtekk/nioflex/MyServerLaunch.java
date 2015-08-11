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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class MyServerLaunch
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        MyServer server = new MyServer(5577);
        Thread t = server.launchThread();

        Thread.sleep(300);

        SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5577));
        SocketUtil util = new SocketUtil(client);

        String t1 = "Hello world", t2 = "Another test";

        util.writeInt(t1.length());
        util.writeString(t1);

        util.writeInt(t2.length());
        util.writeString(t2);

        client.close();

        /* Don't kill the server immediately, wait 100ms */
        Thread.sleep(100);

        server.scheduleShutdown();
        t.join();
    }
}
