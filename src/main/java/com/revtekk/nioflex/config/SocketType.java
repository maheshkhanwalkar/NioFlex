package com.revtekk.nioflex.config;

/**
 * Socket communication configuration
 */
public enum SocketType
{
    /**
     * TCP -- Transmission Control Protocol
     *
     * Use TCP for all communications. This is a reliable, connection-oriented
     * protocol which operates as a byte-stream.
     */
    SOCKET_TCP,

    /**
     * UDP -- User Datagram Protocol
     *
     * Use UDP for all communications. This an unreliable, connectionless
     * protocol which operates using "packets" (e.g. datagram)
     */
    SOCKET_UDP
}
