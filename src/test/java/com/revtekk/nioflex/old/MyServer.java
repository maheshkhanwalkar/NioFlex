package com.revtekk.nioflex.old;

import com.revtekk.nioflex.old.utils.SocketUtil;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class MyServer extends NIOServer
{
    public MyServer(int port)
    {
        super("localhost", port);
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
