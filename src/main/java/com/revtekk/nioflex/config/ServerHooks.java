package com.revtekk.nioflex.config;

import com.revtekk.nioflex.main.Client;

/**
 * Server Hooks
 *
 * This interface specifies two methods: onAccept and onRead, which are hooks
 * that are run by the Server instance, allowing the user to perform custom
 * actions and process any data that the server has received.
 */
public interface ServerHooks
{
    /**
     * Action to perform when a new client is accepted by the server
     * @param client - newly accepted client
     */
    void onAccept(Client client);


    /**
     * Action to perform when a client has data that can be read
     * @param client - readable (ready-to-read) client
     */
    void onRead(Client client);
}
