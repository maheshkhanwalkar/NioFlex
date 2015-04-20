package com.inixsoftware.nioflex.examples.echo;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class EchoClient
{
    public static void main(String[] args) throws IOException
    {
        SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8787));
        client.configureBlocking(true);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true)
        {
            ByteBuffer size = ByteBuffer.allocate(4);

            //read input from stdin
            System.out.print("Client: ");

            String input = br.readLine();

            //Q is to quit
            if(input.equals("Q"))
                break;

            //creates a ByteBuffer & puts user input into it
            ByteBuffer text = ByteBuffer.allocate(input.length());

            size.putInt(input.length());
            size.flip();

            text.put(input.getBytes(Charset.forName("UTF-8")));
            text.flip();

            //write data to the server

            //first write the string's length
            //then the actual string

            client.write(size);
            client.write(text);

            //read the data from the server

            //the server first sends the string's length
            //then the actual string

            ByteBuffer lenBuf = ByteBuffer.allocate(4);
            client.read(lenBuf);

            lenBuf.flip();
            int len = lenBuf.getInt();

            ByteBuffer resp = ByteBuffer.allocate(len);
            client.read(resp);

            resp.flip();

            //print out the message
            System.out.println("Server: " + new String(resp.array(), Charset.forName("UTF-8")));
        }

        //clean-up
        client.close();
        br.close();

    }
}
