package com.inixsoftware.nioflex.nio.utils;

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

public class NIOUtils
{
    private NIOUtils() {}

    /**
     * Reads data from a SocketChannel into a ByteBuffer
     *
     * @param len - Number of bytes to read
     * @param client - SocketChannel to read from
     * @return ByteBuffer read from SocketChannel
     */
    public static ByteBuffer readBuffer(int len, SocketChannel client)
    {
        ByteBuffer buffer = ByteBuffer.allocate(len);

        int read = 0;

        try
        {
            while((read += client.read(buffer)) < len);
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
     * @param len - Number of bytes to read
     * @param client - SocketChannel to read from
     * @return byte[] read from SocketChannel
     */
    public static byte[] readBytes(int len, SocketChannel client)
    {
        return readBuffer(len, client).array();
    }

    /**
     * Reads raw bytes from a SocketChannel, and returns a String
     * equivalent (encoded using the default Charset)
     *
     * @param len - Number of bytes to read
     * @param client - SocketChannel to read from
     * @return String read from SocketChannel
     */
    public static String readString(int len, SocketChannel client)
    {
        byte[] bytes = readBytes(len, client);
        return new String(bytes); //uses default encoding
    }

    /**
     * Reads raw bytes from a SocketChannel, and returns a String
     * equivalent using the provided Charset
     *
     * @param len - Number of bytes to read
     * @param client - SocketChannel to read from
     * @param charset - Charset to encode bytes to String with
     * @return String read from SocketChannel
     */
    public static String readString(int len, SocketChannel client, Charset charset)
    {
        byte[] bytes = readBytes(len, client);
        return new String(bytes, charset);
    }

    /**
     * Reads 4 bytes (int) from a SocketChannel, and returns
     * the result
     *
     * @param client - SocketChannel to read from
     * @return Integer read from SocketChannel
     */
    public static int readInt(SocketChannel client)
    {
        return readBuffer(4, client).getInt();
    }

    /**
     * Write byte[] to a SocketChannel
     *
     * @param bytes - bytes to be written
     * @param client - SocketChannel where data will be written to
     */
    public static void writeBytes(byte[] bytes, SocketChannel client)
    {
        ByteBuffer buf = ByteBuffer.allocate(bytes.length);
        buf.put(bytes);

        buf.flip();

        try
        {
            client.write(buf);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Write a String to a SocketChannel, converting the String to a
     * bytes, using the default Charset
     *
     * @param str - String to write
     * @param client - SocketChannel where data will be written to
     */
    public static void writeString(String str, SocketChannel client)
    {
        writeBytes(str.getBytes(), client);
    }

    /**
     * Write a String to a SocketChannel, converting the String to a
     * bytes, using the provided Charset
     *
     * @param str - String to write
     * @param client - SocketChannel where data will be written to
     * @param charset - Charset to encode String to bytes
     */
    public static void writeString(String str, SocketChannel client, Charset charset)
    {
        writeBytes(str.getBytes(charset), client);
    }

    /**
     * Write a integer to a SocketChannel
     *
     * @param num - int to write
     * @param client - SocketChannel where data will be written to
     */
    public static void writeInt(int num, SocketChannel client)
    {
        ByteBuffer buf = ByteBuffer.allocate(4); //int is 4 bytes
        buf.putInt(num);

        buf.flip();

        try
        {
            client.write(buf);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
