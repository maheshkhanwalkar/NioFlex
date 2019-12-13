package com.revtekk.nioflex.main;

import com.revtekk.nioflex.impl.CommLayer;
import com.revtekk.nioflex.util.Packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
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
     * Read an integer from the communication layer
     * @return the integer
     */
    public int readInt()
    {
        byte[] buffer = new byte[4];
        int res = layer.forceRead(buffer, 0, buffer.length, quit);

        // something went wrong
        // FIXME! This is potentially undetectable -- needs to throw exceptions
        if(res == -1)
            return -1;

        return ByteBuffer.wrap(buffer).getInt();
    }

    /**
     * Read a string from the communication layer
     * @param charset - encoding charset
     * @return the string, null on error
     */
    public String readString(Charset charset)
    {
        int size = readInt();

        // TODO have a configurable upper-bound as well on this, to prevent malicious
        //  clients from specifying very large buffer sizes
        if(size < 0)
            return null;

        byte[] buffer = new byte[size];
        int res = layer.forceRead(buffer, 0, buffer.length, quit);

        // something went wrong
        if(res == -1)
            return null;

        return new String(buffer, charset);
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
     * Write an integer to the communication layer
     * @return true on success, false otherwise
     */
    public boolean writeInt(int num)
    {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(num);

        return layer.forceWrite(bb.array(), 0, bb.array().length, quit) != -1;
    }

    /**
     * Write a string to the communication layer
     * @param s - string to write
     * @param charset - charset for encoding
     * @return true on success, false otherwise
     */
    public boolean writeString(String s, Charset charset)
    {
        byte[] buffer = s.getBytes(charset);
        boolean res = writeInt(buffer.length);

        if(!res)
            return false;

        return layer.forceWrite(buffer, 0, buffer.length, quit) != -1;
    }
    
    /**
     * Close the underlying client
     */
    public void close() throws IOException
    {
        quit.set(true);
        layer.close();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return layer.equals(client.layer);
    }

    @Override
    public int hashCode()
    {
        return layer.hashCode();
    }
}
