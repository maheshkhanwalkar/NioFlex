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

import com.inixsoftware.nioflex.nio.NIOServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class MyServer extends NIOServer
{
    @Override
    public void handleAccept(SocketChannel client, SelectionKey key)
    {
        System.out.println("Client Connected!");
    }

    @Override
    public void handleRead(SocketChannel client, SelectionKey key)
    {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        try
        {
            while(client.read(buffer) < 12);
            buffer.flip();

            System.out.println(new String(buffer.array()));

            ByteBuffer resp = ByteBuffer.allocate(12);
            resp.put("Hello world\n".getBytes());
            resp.flip();

            client.write(resp);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
