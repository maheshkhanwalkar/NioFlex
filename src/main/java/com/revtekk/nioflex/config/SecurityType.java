package com.revtekk.nioflex.config;

/**
 * Security configuration
 */
public enum SecurityType
{
    /**
     * No security requested
     *
     * The server should not encrypt any of its communication with the
     * connected clients.
     *
     * This policy works for both TCP and UDP servers.
     */
    SECURITY_NONE,

    /**
     * TLS requested
     *
     * The server should use TLS for its communication with the connected
     * clients (encryption is requested).
     *
     * The policy only works for TCP servers, so if this option is passed to
     * a UDP server, then the server creation will fail.
     */
    SECURITY_TLS,
}
