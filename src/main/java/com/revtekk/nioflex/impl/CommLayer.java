package com.revtekk.nioflex.impl;

import java.util.concurrent.atomic.AtomicBoolean;

public interface CommLayer
{
    /**
     * Attempt to read a certain number of bytes and store it within a
     * user-provided buffer
     *
     * @param buffer - buffer to store data in
     * @param offset - where to start from in the buffer
     * @param len - number of bytes to read
     * @return number of bytes actually read, -1 on error
     */
    int tryRead(byte[] buffer, int offset, int len);

    /**
     * Forcibly read a certain number of bytes and store it within a
     * user-provided buffer
     *
     * @param buffer - buffer to store data in
     * @param offset - where to start from in the buffer
     * @param len - number of bytes to read
     * @param quit - give up on the read, if set to true
     * @return 'len' on success, -1 on failure
     */
    int forceRead(byte[] buffer, int offset, int len, AtomicBoolean quit);

    /**
     * Attempt to write a certain number of bytes sourced from a
     * user-provided buffer
     *
     * @param buffer - buffer to read data from
     * @param offset - where to start from in the buffer
     * @param len - number of bytes to write
     * @return number of bytes actually written, -1 on error
     */
    int tryWrite(byte[] buffer, int offset, int len);

    /**
     * Forcibly write a certain number of bytes sourced from a
     * user-provided buffer
     *
     * @param buffer - buffer to read data from
     * @param offset - where to start from in the buffer
     * @param len - number of bytes to write
     * @param quit - give up on the write, if set to true
     * @return 'len' on success, -1 on error
     */
    int forceWrite(byte[] buffer, int offset, int len, AtomicBoolean quit);

    /**
     * Close the communication layer
     */
    void close();
}
