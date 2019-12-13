package com.revtekk.nioflex.main;

import com.revtekk.nioflex.impl.CommLayer;
import com.revtekk.nioflex.util.Packet;

import java.util.concurrent.atomic.AtomicBoolean;

public class Client
{
    /**
     * Underlying communication layer
     *
     * This will be used by the client implementations to actually read and
     * write from the underlying socket.
     */
    private CommLayer layer;

    /**
     * Quit signal
     *
     * When shutting down the client, it may be blocked on a read or write
     * operation, so this signal causes it to unblock and return with failure.
     *
     * Then, the client can be shutdown successfully and resources released
     */
    private AtomicBoolean quit;

    /**
     * Initialise the client with a communication layer
     * @param layer - layer to use
     */
    public Client(CommLayer layer, AtomicBoolean quit)
    {
        this.layer = layer;
        this.quit = quit;
    }

    /**
     * Attempt to read a certain number of bytes and store it within a
     * user-provided buffer
     *
     * @param buffer - buffer to store data in
     * @param offset - where to start from in the buffer
     * @param len - number of bytes to read
     * @return number of bytes actually read, -1 on error
     */
    public int readBytes(byte[] buffer, int offset, int len)
    {
        return layer.tryRead(buffer, offset, len);
    }

    /**
     * Attempt to read a packet of a specified size
     * @param size - size of the packet
     * @return the packet, or null on error
     */
    public Packet readPacket(int size)
    {
        byte[] buffer = new byte[size];
        int res = layer.forceRead(buffer, 0, size, quit);

        // something went wrong
        if(res == -1)
            return null;

        Packet pkt = new Packet();
        pkt.data = buffer;

        return pkt;
    }

    /**
     * Attempt to write a certain number of bytes sourced from a
     * user-provided buffer
     *
     * @param buffer - buffer to read data from
     * @param offset - where to start from in the buffer
     * @param len - number of bytes to write
     * @return number of bytes actually written, -1 on error
     */
    public int writeBytes(byte[] buffer, int offset, int len)
    {
        return layer.tryWrite(buffer, offset, len);
    }

    /**
     * Attempt to write a packet
     * @param pkt - packet to write
     * @return true on success, false on error
     */
    public boolean writePacket(Packet pkt)
    {
        return layer.forceWrite(pkt.data, 0, pkt.data.length, quit) != -1;
    }

    /**
     * Close the underlying client
     */
    public void close()
    {
        quit.set(true);
        layer.close();
    }
}
