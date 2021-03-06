package com.revtekk.nioflex.main;

import com.revtekk.nioflex.config.OptionType;
import com.revtekk.nioflex.config.ServerHooks;
import com.revtekk.nioflex.config.ServerOption;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Server
 *
 * This class represents a server instance, which is associated with an address
 * and port number.
 *
 * Library users will interact directly with this class itself, regardless of
 * what kind of server they are trying to build.
 *
 * To construct a new instance of this class, see ServerBuilder for more details
 */
public abstract class Server
{
    protected InetAddress address;
    protected int port;
    protected ServerHooks hooks;
    private Map<OptionType, String> map;

    protected Server(InetAddress address, int port, ServerHooks hooks, ServerOption... options)
    {
        this.address = address;
        this.port = port;
        this.hooks = hooks;
        this.map = new HashMap<>();

        for(ServerOption opt : options)
            map.put(opt.type, opt.value);
    }

    /**
     * Start the server
     *
     * The underlying server socket will be initialised and will be ready to
     * start processing clients and data.
     */
    public abstract void start() throws IOException;

    /**
     * Shutdown the server
     *
     * Close down the underlying server socket, cleaning up any associated
     * resources and stopping all communication.
     */
    public abstract void shutdown() throws InterruptedException, IOException;

    /**
     * Retrieve a server option, based on its type
     * @param type - requested type
     * @return associated option value
     */
    protected String getOption(OptionType type)
    {
        return map.get(type);
    }
}
