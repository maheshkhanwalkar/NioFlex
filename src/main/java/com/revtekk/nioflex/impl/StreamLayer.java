package com.revtekk.nioflex.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class StreamLayer implements CommLayer
{
    private SocketChannel channel;

    public StreamLayer(SocketChannel channel)
    {
        this.channel = channel;
    }

    @Override
    public int tryRead(byte[] buffer, int offset, int len) throws IOException
    {
        ByteBuffer bb = ByteBuffer.wrap(buffer, offset, len);
        return channel.read(bb);
    }

    @Override
    public int forceRead(byte[] buffer, int offset, int len, AtomicBoolean quit) throws IOException
    {

        ByteBuffer bb = ByteBuffer.wrap(buffer, offset, len);
        int amt = 0;

        // force read 'len' bytes into the buffer
        while(amt < len && !quit.get())
        {
            int res = channel.read(bb);

            if(res == -1)
                return -1;

            amt += res;
        }

        return amt == len ? amt : -1;
    }

    @Override
    public int tryWrite(byte[] buffer, int offset, int len) throws IOException
    {
        ByteBuffer bb = ByteBuffer.wrap(buffer, offset, len);
        return channel.write(bb);
    }

    @Override
    public void forceWrite(byte[] buffer, int offset, int len, AtomicBoolean quit) throws IOException
    {
        ByteBuffer bb = ByteBuffer.wrap(buffer, offset, len);
        int amt = 0;

        // force write 'len' bytes to the channel
        while(amt < len && !quit.get())
            amt += channel.write(bb);

    }

    @Override
    public void close() throws IOException
    {
        channel.close();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreamLayer that = (StreamLayer) o;
        return channel.equals(that.channel);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(channel);
    }
}
