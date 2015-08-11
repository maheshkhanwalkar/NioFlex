package com.revtekk.nioflex.utils;

/*
    Copyright 2015 Mahesh Khanwalkar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class SocketUtil
{
    private SocketChannel channel;
    private ByteBuffer refund;

    public SocketUtil(SocketChannel channel)
    {
        this.channel = channel;
    }


    /**
     * Refunds a ByteBuffer that will be used when the next call
     * to readBuffer() is performed. Used internally by NIOServer
     *
     * @param buf - ByteBuffer to refund
     */
    public void refund(ByteBuffer buf)
    {
        refund = buf;
    }

    /**
     * Reads data from a SocketChannel into a ByteBuffer
     *
     * @param len Number of bytes to read
     * @return ByteBuffer read from SocketChannel
     */
    public ByteBuffer readBuffer(int len)
    {
        ByteBuffer buffer = ByteBuffer.allocate(len);

        if(refund != null)
        {
            buffer.put(refund.get());
            refund = null;
        }

        int read = 0;

        try
        {
            while((read += channel.read(buffer)) < len - 1);
            buffer.flip();

            return buffer;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Reads data from a SocketChannel, and returns a byte[]
     *
     * @param len Number of bytes to read
     * @return byte[] read from SocketChannel
     */
    public byte[] readBytes(int len)
    {
        ByteBuffer buf = readBuffer(len);

        if(buf != null)
            return buf.array();

        return null;
    }

    /**
     * Reads raw bytes from a SocketChannel, and returns a String
     * equivalent (encoded using the default Charset)
     *
     * @param len Number of bytes to read
     * @return String read from SocketChannel
     */
    public String readString(int len)
    {
        byte[] bytes = readBytes(len);
        if(bytes != null)
            return new String(bytes); //uses default encoding

        return null;
    }

    /**
     * Reads raw bytes from a SocketChannel, and returns a String
     * equivalent using the provided Charset
     *
     * @param len Number of bytes to read
     * @param charset Charset to encode bytes to String with
     * @return String read from SocketChannel
     */
    public String readString(int len, Charset charset)
    {
        byte[] bytes = readBytes(len);
        return new String(bytes, charset);
    }

    /*
       Warning: readLine() and readLine(NewlineType) implementation is *slow*
       TODO: Make faster
    */

    /**
     * readLine() assumes UTF-8 and \n as newline delimiter
     * @return a single line of data (as a String)
     */
    public String readLine()
    {
        StringBuilder line = new StringBuilder();
        String temp;

        int len = 1;

        while (!(temp = readString(1, Charset.forName("UTF-8"))).equals("\n"))
        {
            line.append(temp);
        }

        return line.toString();
    }

    /**
     * readLine(NewlineType) assumes UTF-8
     *
     * @param type what to denote as the end of a line
     * @return a single line of data (as a String)
     */
    public String readLine(NewLineType type)
    {
        StringBuilder line = new StringBuilder();
        String t1, t2 = "";

        if(type != NewLineType.CRLF)
        {
            String compare = type == NewLineType.CR ? "\r" : "\n";

            while (!(t1 = new String(readBytes(1), Charset.forName("UTF-8"))).equals(compare))
            {
                line.append(t1);
            }

            return line.toString();
        }

        while ( !((t1 = readString(1, Charset.forName("UTF-8"))).equals("\r") &&
                (t2 = readString(1, Charset.forName("UTF-8"))).equals("\n"))
                   )
        {
            line.append(t1);
            line.append(t2);

        }


        return line.toString();
    }


    /**
     * Reads 4 bytes (int) from a SocketChannel, and returns
     * the result
     *
     * @return Integer read from SocketChannel
     */
    public int readInt()
    {
        return readBuffer(4).getInt();
    }

    /**
     * Reads 2 bytes (short) from a SocketChannel, and returns
     * the result
     *
     * @return short read from SocketChannel
     */
    public short readShort()
    {
        return readBuffer(2).getShort();
    }

    /**
     * Reads 8 bytes (long) from a SocketChannel, and returns
     * the result
     *
     * @return long read from SocketChannel
     */
    public long readLong()
    {
        return readBuffer(8).getLong();
    }

    /**
     * Write ByteBuffer data to a SocketChannel
     * @param buf data contained in a ByteBuffer
     */
    public void writeBuffer(ByteBuffer buf)
    {
        try
        {
            channel.write(buf);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Write byte[] to a SocketChannel
     * @param bytes bytes to be written
     */
    public void writeBytes(byte[] bytes)
    {
        ByteBuffer buf = ByteBuffer.allocate(bytes.length);
        buf.put(bytes);

        buf.flip();
        writeBuffer(buf);
    }

    /**
     * Write a String to a SocketChannel, converting the String to a
     * bytes, using the default Charset
     *
     * @param str String to write
     */
    public void writeString(String str)
    {
        writeBytes(str.getBytes());
    }

    /**
     * Write a String to a SocketChannel, converting the String to a
     * bytes, using the provided Charset
     *
     * @param str String to write
     * @param charset Charset to encode String to bytes
     */
    public void writeString(String str, Charset charset)
    {
        writeBytes(str.getBytes(charset));
    }

    /**
     * Writes a String (UTF-8 encoded) to a SocketChannel and appends a
     * newline (\n)
     *
     * @param str String to write
     */
    public void writeLine(String str)
    {
        writeString(str + "\n", Charset.forName("UTF-8"));
    }

    /**
     * Writes a String (UTF-8 encoded) to a SocketChannel and appends a
     * newline (\n, \r, or \r\n)
     *
     * @param str String to write
     * @param type Type of newline to use
     */
    public void writeLine(String str, NewLineType type)
    {
        StringBuilder builder = new StringBuilder(str);

        if(type == NewLineType.CR)
            builder.append("\r");
        if(type == NewLineType.LF)
            builder.append("\n");
        if(type == NewLineType.CRLF)
            builder.append("\r\n");

        writeString(builder.toString(), Charset.forName("UTF-8"));
    }

    /**
     * Write a integer to a SocketChannel
     * @param num int to write
     */
    public void writeInt(int num)
    {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(num);

        buf.flip();
        writeBuffer(buf);
    }

    /**
     * Write a short to a SocketChannel
     * @param num short to write
     */
    public void writeShort(short num)
    {
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.putShort(num);

        buf.flip();
        writeBuffer(buf);
    }

    /**
     * Write a long to a SocketChannel
     * @param num long to write
     */
    public void writeLong(long num)
    {
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.putLong(num);

        buf.flip();
        writeBuffer(buf);
    }
}
