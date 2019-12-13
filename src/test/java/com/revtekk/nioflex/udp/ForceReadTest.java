package com.revtekk.nioflex.udp;

import com.revtekk.nioflex.config.SecurityType;
import com.revtekk.nioflex.config.ServerHooks;
import com.revtekk.nioflex.config.SocketType;
import com.revtekk.nioflex.impl.ServerBuilder;
import com.revtekk.nioflex.main.Client;
import com.revtekk.nioflex.main.Server;
import com.revtekk.nioflex.util.Packet;

import java.io.IOException;
import java.net.*;

public class ForceReadTest
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        final int PKT_SIZE = 2048;

        ServerHooks hooks = new ServerHooks()
        {
            @Override
            public void onAccept(Client client)
            {
                // Not used in UDP -- since there is no real concept of accepting clients
                // or really clients in general
            }

            @Override
            public void onRead(Client client)
            {
                Packet pkt = client.readPacket(PKT_SIZE);

                if(pkt == null)
                    System.err.println("Something failed (or quit was asserted)");
            }
        };

        Server server = ServerBuilder.build(InetAddress.getLocalHost(), 4444,
                SocketType.SOCKET_UDP, SecurityType.SECURITY_NONE, hooks);

        if(server == null)
        {
            System.err.println("Could not start server");
            System.exit(-1);
        }

        server.start();

        byte[] buffer = new byte[PKT_SIZE];

        DatagramPacket pkt = new DatagramPacket(buffer, 0, 256, InetAddress.getLocalHost(), 4444);
        DatagramSocket socket = new DatagramSocket();
        socket.send(pkt);

        Thread.sleep(3000);

        pkt = new DatagramPacket(buffer, 256, buffer.length - 256, InetAddress.getLocalHost(), 4444);
        socket.send(pkt);

        Thread.sleep(100);

        socket.close();
        server.shutdown();
    }
}
