package com.revtekk.nioflex.old;

import com.revtekk.nioflex.old.policy.ThreadPolicy;
import com.revtekk.nioflex.old.utils.SocketUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class MyServerLaunch
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        MyServer server = new MyServer(5577);
        server.setPolicy(ThreadPolicy.THREAD_FOR_ALL);

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
