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
     * @return number of bytes actually read
     * @throws IOException on communication error
     */
    public int readBytes(byte[] buffer, int offset, int len) throws IOException
    {
        return layer.tryRead(buffer, offset, len);
    }

    /**
     * Attempt to read a packet of a specified size
     * @param size - size of the packet
     * @return the packet
     * @throws IOException on communication error
     */
    public Packet readPacket(int size) throws IOException
    {
        byte[] buffer = new byte[size];
        int res = layer.forceRead(buffer, 0, size, quit);

        Packet pkt = new Packet();
        pkt.data = buffer;

        return pkt;
    }

    /**
     * Read an integer from the communication layer
     * @return the integer
     * @throws IOException on communication error
     */
    public int readInt() throws IOException
    {
        byte[] buffer = new byte[4];
        int res = layer.forceRead(buffer, 0, buffer.length, quit);

        return ByteBuffer.wrap(buffer).getInt();
    }

    /**
     * Read a string from the communication layer
     * @param charset - encoding charset
     * @return the string, null on error
     * @throws IOException on communication error
     */
    public String readString(Charset charset) throws IOException
    {
        int size = readInt();

        // TODO have a configurable upper-bound as well on this, to prevent malicious
        //  clients from specifying very large buffer sizes
        if(size < 0)
            return null;

        byte[] buffer = new byte[size];
        int res = layer.forceRead(buffer, 0, buffer.length, quit);

        return new String(buffer, charset);
    }

    /**
     * Attempt to write a certain number of bytes sourced from a
     * user-provided buffer
     *
     * @param buffer - buffer to read data from
     * @param offset - where to start from in the buffer
     * @param len - number of bytes to write
     * @return number of bytes actually written
     * @throws IOException on communication error
     */
    public int writeBytes(byte[] buffer, int offset, int len) throws IOException
    {
        return layer.tryWrite(buffer, offset, len);
    }

    /**
     * Attempt to write a packet
     * @param pkt - packet to write
     * @throws IOException on communication error
     */
    public void writePacket(Packet pkt) throws IOException
    {
        layer.forceWrite(pkt.data, 0, pkt.data.length, quit);
    }

    /**
     * Write an integer to the communication layer
     * @throws IOException on communication error
     */
    public void writeInt(int num) throws IOException
    {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(num);

        layer.forceWrite(bb.array(), 0, bb.array().length, quit);
    }

    /**
     * Write a string to the communication layer
     * @param s - string to write
     * @param charset - charset for encoding
     * @throws IOException on communication error
     */
    public void writeString(String s, Charset charset) throws IOException
    {
        byte[] buffer = s.getBytes(charset);
        writeInt(buffer.length);

        layer.forceWrite(buffer, 0, buffer.length, quit);
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
