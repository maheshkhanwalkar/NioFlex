package com.revtekk.nioflex.impl;

import com.revtekk.nioflex.config.ServerHooks;
import com.revtekk.nioflex.config.ServerOption;
import com.revtekk.nioflex.main.Client;
import com.revtekk.nioflex.main.Server;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

class UDPServer extends Server
{
    private DatagramSocket socket;
    private CommLayer layer;

    private Thread worker;
    private AtomicBoolean flag = new AtomicBoolean();

    public UDPServer(InetAddress address, int port, ServerHooks hooks, ServerOption... options)
    {
        super(address, port, hooks, options);
    }

    @Override
    public void start() throws SocketException
    {
        socket = new DatagramSocket(port, address);
        layer = new DatagramLayer(socket);

        worker = new Thread(() ->
        {
            while(!flag.get())
            {
                Client client = new Client(layer);
                hooks.onRead(client);
            }
        });

        worker.start();
    }

    @Override
    public void shutdown() throws InterruptedException
    {
        flag.set(true);
        worker.join();

        socket.close();
    }
}
