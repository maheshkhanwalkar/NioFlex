package com.revtekk.nioflex.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class DatagramLayer implements CommLayer
{
    private DatagramSocket socket;
    private static final int DEFAULT_TIMEOUT = 1000;

    public DatagramLayer(DatagramSocket socket) throws SocketException
    {
        this(socket, DEFAULT_TIMEOUT);
    }

    public DatagramLayer(DatagramSocket socket, int timeout) throws SocketException
    {
        this.socket = socket;
        this.socket.setSoTimeout(timeout);
    }

    @Override
    public int tryRead(byte[] buffer, int offset, int len) throws IOException
    {
        try
        {
            DatagramPacket pkt = new DatagramPacket(buffer, offset, len);
            socket.receive(pkt);

            return pkt.getLength();
        }
        catch (SocketTimeoutException e)
        {
            return 0;
        }
    }

    @Override
    public int forceRead(byte[] buffer, int offset, int len, AtomicBoolean quit) throws IOException
    {
        DatagramPacket pkt = new DatagramPacket(buffer, offset, len);
        int amt = 0;

        while(!quit.get())
        {
            try
            {
                socket.receive(pkt);
                amt += pkt.getLength();

                while(amt < len) {
                    pkt = new DatagramPacket(buffer, offset + amt, len - amt);
                    socket.receive(pkt);

                    amt += pkt.getLength();
                }

                return amt;
            }
            catch (SocketTimeoutException e)
            {
                // Try again to see if it will work
            }
        }

        return 0;
    }

    @Override
    public int tryWrite(byte[] buffer, int offset, int len) throws IOException
    {
        DatagramPacket pkt = new DatagramPacket(buffer, offset, len);
        socket.send(pkt);

        return len;
    }

    @Override
    public void forceWrite(byte[] buffer, int offset, int len, AtomicBoolean quit) throws IOException
    {
        // there is no difference for UDP, since we can't really force writes to complete,
        // so it is assumed that the packet was sent fully

        tryWrite(buffer, offset, len);
    }

    @Override
    public void close()
    {
        socket.close();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatagramLayer that = (DatagramLayer) o;
        return socket.equals(that.socket);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(socket);
    }
}
