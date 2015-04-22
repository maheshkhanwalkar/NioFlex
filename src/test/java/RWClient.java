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

import com.inixsoftware.nioflex.nio.utils.NIOUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class RWClient
{
    @Test
    public void clientTest()
    {
        try
        {
            SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 7337));
            client.configureBlocking(true);

            NIOUtils.writeShort((short) 2, client);
            NIOUtils.writeShort((short) 4, client);
            NIOUtils.writeShort((short) 1, client);
            NIOUtils.writeShort((short) 16, client);

            NIOUtils.writeInt(32, client);
            NIOUtils.writeInt(64, client);

            NIOUtils.writeLong(128, client);
            NIOUtils.writeLong(128, client);
            NIOUtils.writeLong(65536, client);
            NIOUtils.writeLong(256, client);
            NIOUtils.writeLong(1024, client);

            NIOUtils.writeInt(12, client);
            NIOUtils.writeString("Hello world!", client);

            client.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
