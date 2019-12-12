package com.revtekk.nioflex.impl;

import com.revtekk.nioflex.config.SecurityType;
import com.revtekk.nioflex.config.ServerHooks;
import com.revtekk.nioflex.config.ServerOption;
import com.revtekk.nioflex.main.Server;

import java.net.InetAddress;

class TCPServer extends Server
{
    private SecurityType security;
    private ServerOption[] options;

    public TCPServer(InetAddress address, int port, ServerHooks hooks,
            SecurityType security, ServerOption... options)
    {
        super(address, port, hooks, options);

        this.security = security;
        this.options = options;
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
