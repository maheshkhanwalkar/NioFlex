package com.revtekk.nioflex.tcp;

import com.revtekk.nioflex.config.SecurityType;
import com.revtekk.nioflex.config.ServerHooks;
import com.revtekk.nioflex.config.SocketType;
import com.revtekk.nioflex.impl.ServerBuilder;
import com.revtekk.nioflex.main.Client;
import com.revtekk.nioflex.main.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class StressServer
{
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        ServerHooks hooks = new ServerHooks()
        {
            private AtomicInteger count = new AtomicInteger(0);

            @Override
            public void onAccept(Client client)
            {
                System.out.println("Accepted new client");
            }

            @Override
            public void onRead(Client client)
            {
                byte[] buffer = new byte[BUFFER_SIZE];
                int res = client.readBytes(buffer, 0, buffer.length);

                System.out.println(res);
            }
        };

        Server server = ServerBuilder.build(InetAddress.getLocalHost(), 4444,
                SocketType.SOCKET_TCP, SecurityType.SECURITY_NONE, hooks);

        if(server == null)
        {
            System.err.println("Could not create server");
            System.exit(-1);
        }

        server.start();
        Thread.sleep(5000);
        server.shutdown();
    }
}
