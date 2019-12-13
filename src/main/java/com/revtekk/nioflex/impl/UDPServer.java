package com.revtekk.nioflex.impl;

import com.revtekk.nioflex.config.OptionType;
import com.revtekk.nioflex.config.ServerHooks;
import com.revtekk.nioflex.config.ServerOption;
import com.revtekk.nioflex.main.Client;
import com.revtekk.nioflex.main.Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

class UDPServer extends Server
{
    private DatagramSocket socket;
    private CommLayer layer;

    private Thread worker;
    private AtomicBoolean quit = new AtomicBoolean();

    private Client client;

    public UDPServer(InetAddress address, int port, ServerHooks hooks, ServerOption... options)
    {
        super(address, port, hooks, options);
    }

    @Override
    public void start() throws SocketException
    {
        int timeout = -1;

        // check for timeout option
        for (ServerOption option : options)
        {
            if (option.type == OptionType.RECEIVE_TIMEOUT)
                timeout = Integer.parseInt(option.value);
        }

        socket = new DatagramSocket(port, address);
        layer = (timeout == -1) ? new DatagramLayer(socket) : new DatagramLayer(socket, timeout);
        client = new Client(layer, quit);

        worker = new Thread(() ->
        {
            while(!quit.get())
                hooks.onRead(client);
        });

        worker.start();
    }

    @Override
    public void shutdown() throws InterruptedException, IOException
    {
        quit.set(true);
        worker.join();
        socket.close();
        client.close();
    }
}
