package com.revtekk.nioflex.impl;

import com.revtekk.nioflex.config.ServerHooks;
import com.revtekk.nioflex.config.ServerOption;
import com.revtekk.nioflex.main.Server;

import java.net.InetAddress;

class UDPServer extends Server
{
    public UDPServer(InetAddress address, int port, ServerHooks hooks, ServerOption... options)
    {
        super(address, port, hooks, options);
    }

    @Override
    public void start()
    {

    }

    @Override
    public void shutdown()
    {

    }
}
