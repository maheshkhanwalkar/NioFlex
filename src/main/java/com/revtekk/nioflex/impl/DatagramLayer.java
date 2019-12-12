package com.revtekk.nioflex.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class DatagramLayer implements CommLayer
{
    private DatagramSocket socket;

    // FIXME: make this configurable (via options)
    private static final int TIMEOUT = 10000;

    public DatagramLayer(DatagramSocket socket) throws SocketException
    {
        this.socket = socket;
        this.socket.setSoTimeout(TIMEOUT);
    }

    @Override
    public int tryRead(byte[] buffer, int offset, int len)
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
        catch (IOException e)
        {
            e.printStackTrace();
            return -1;
        }
    }


    @Override
    public int forceRead(byte[] buffer, int offset, int len, AtomicBoolean quit)
    {
        try
        {
            DatagramPacket pkt = new DatagramPacket(buffer, offset, len);
            socket.receive(pkt);

            int amt = pkt.getLength();

            while(amt < len) {
                pkt = new DatagramPacket(buffer, offset + amt, len - amt);
                socket.receive(pkt);

                amt += pkt.getLength();
            }

            return amt;
        }
        catch (SocketTimeoutException e)
        {
            // FIXME: we should continue retrying until 'quit' is asserted
            return -1;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int tryWrite(byte[] buffer, int offset, int len)
    {
        DatagramPacket pkt = new DatagramPacket(buffer, offset, len);

        try
        {
            socket.send(pkt);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return -1;
        }

        return len;
    }

    @Override
    public int forceWrite(byte[] buffer, int offset, int len, AtomicBoolean quit)
    {
        // there is no difference for UDP, since we can't really force writes to complete,
        // so it is assumed that the packet was sent fully

        return tryWrite(buffer, offset, len);
    }

    @Override
    public void close()
    {
        socket.close();
    }
}
