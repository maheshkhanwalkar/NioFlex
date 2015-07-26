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

import com.revtekk.nioflex.utils.NewLineType;
import com.revtekk.nioflex.utils.SocketUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ReadLineLaunch
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        ReadLineServer server = new ReadLineServer(5577);
        Thread t = server.launchThread();

        SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5577));
        SocketUtil util = new SocketUtil(client);

        util.writeLine("Hello world");
        util.writeLine("Another test", NewLineType.CRLF);

        util.writeLine("Yet another", NewLineType.CR);
        util.writeLine("Last thing", NewLineType.LF);

        client.close();

        /* Don't kill the server immediately, wait 100ms */
        Thread.sleep(100);

        server.scheduleShutdown();
        t.join();
    }
}
