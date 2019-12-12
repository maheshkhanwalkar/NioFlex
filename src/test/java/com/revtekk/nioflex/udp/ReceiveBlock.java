package com.revtekk.nioflex.udp;

import com.revtekk.nioflex.config.SecurityType;
import com.revtekk.nioflex.config.ServerHooks;
import com.revtekk.nioflex.config.SocketType;
import com.revtekk.nioflex.impl.ServerBuilder;
import com.revtekk.nioflex.main.Client;
import com.revtekk.nioflex.main.Server;
import com.revtekk.nioflex.util.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class MismatchSize
{
    private static final int SERVER_PKT_SIZE = 22;
    private static final int CLIENT_PKT_SIZE = 128;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        ServerHooks hooks = new ServerHooks()
        {
            @Override
            public void onAccept(Client client)
            {
                // This will be unused, since there are no "accepts" in UDP
            }

            @Override
            public void onRead(Client client)
            {
                Packet pkt = client.readPacket(SERVER_PKT_SIZE);
                String equiv = new String(pkt.data, 0, pkt.data.length, StandardCharsets.UTF_8);

                System.out.println(equiv);
            }
        };

        // Create a UDP server
        Server udp = ServerBuilder.build(InetAddress.getLocalHost(),
                4444, SocketType.SOCKET_UDP, SecurityType.SECURITY_NONE, hooks);

        if(udp == null)
        {
            System.err.println("Error. Couldn't build server for some reason!");
            System.exit(-1);
        }

        udp.start();

        DatagramSocket socket = new DatagramSocket();

        String msg = "hello world";
        byte[] equiv = msg.getBytes(StandardCharsets.UTF_8);

        DatagramPacket pkt = new DatagramPacket(equiv, equiv.length, InetAddress.getLocalHost(), 4444);
        socket.send(pkt);

        Thread.sleep(4000);

        socket.send(pkt);

        socket.close();
        udp.shutdown();
    }
}
