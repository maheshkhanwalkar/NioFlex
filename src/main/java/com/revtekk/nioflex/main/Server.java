package com.revtekk.nioflex.main;

import com.revtekk.nioflex.config.SecurityType;
import com.revtekk.nioflex.config.ServerHooks;
import com.revtekk.nioflex.config.ServerOption;
import com.revtekk.nioflex.config.SocketType;

import java.net.InetAddress;

public class Server
{
    private Server() {}

    public static Server build(InetAddress address, int port,
                               SocketType socket, SecurityType security,
                               ServerHooks hooks, ServerOption... options)
    {
        return null;
    }
}
