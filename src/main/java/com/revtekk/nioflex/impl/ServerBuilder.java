package com.revtekk.nioflex.impl;

import com.revtekk.nioflex.config.SecurityType;
import com.revtekk.nioflex.config.ServerHooks;
import com.revtekk.nioflex.config.ServerOption;
import com.revtekk.nioflex.config.SocketType;
import com.revtekk.nioflex.main.Server;

import java.net.InetAddress;

/**
 * Server Builder
 *
 * This builder class creates a new server instance using the configuration
 * parameters provided to the build() method.
 */
public class ServerBuilder
{
    // Prevent instantiation of this class
    private ServerBuilder() {}

    /**
     * Construct a new server instance
     *
     * @param address - address to use
     * @param port - port to use
     * @param socket - type of socket requested
     * @param security - type of security requested
     * @param hooks - custom event-handling hooks
     * @param options - any additional server options
     *
     * @return newly constructed server
     */
    public static Server build(InetAddress address, int port,
                               SocketType socket, SecurityType security,
                               ServerHooks hooks, ServerOption... options)
    {
        // TODO: add an actual implementation here
        return null;
    }
}
