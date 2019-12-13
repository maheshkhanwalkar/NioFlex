package com.revtekk.nioflex.impl;

import java.io.IOException;
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
     * @return number of bytes actually read
     * @throws IOException propagated from the underlying layer
     */
    int tryRead(byte[] buffer, int offset, int len) throws IOException;

    /**
     * Forcibly read a certain number of bytes and store it within a
     * user-provided buffer
     *
     * @param buffer - buffer to store data in
     * @param offset - where to start from in the buffer
     * @param len - number of bytes to read
     * @param quit - give up on the read, if set to true
     * @return 'len' on success
     * @throws IOException propagated from the underlying layer
     */
    int forceRead(byte[] buffer, int offset, int len, AtomicBoolean quit) throws IOException;

    /**
     * Attempt to write a certain number of bytes sourced from a
     * user-provided buffer
     *
     * @param buffer - buffer to read data from
     * @param offset - where to start from in the buffer
     * @param len - number of bytes to write
     * @return number of bytes actually written
     * @throws IOException propagated from the underlying layer
     */
    int tryWrite(byte[] buffer, int offset, int len) throws IOException;

    /**
     * Forcibly write a certain number of bytes sourced from a
     * user-provided buffer
     *
     * @param buffer - buffer to read data from
     * @param offset - where to start from in the buffer
     * @param len - number of bytes to write
     * @param quit - give up on the write, if set to true
     * @throws IOException propagated from the underlying layer
     */
    void forceWrite(byte[] buffer, int offset, int len, AtomicBoolean quit) throws IOException;

    /**
     * Close the communication layer
     * @throws IOException propagated from the underlying layer
     */
    void close() throws IOException;
}
