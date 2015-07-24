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

import com.revtekk.nioflex.utils.NIOUtils;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ClientTest
{
    public void testClient()
    {
        try
        {
            SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 7337));
            client.configureBlocking(true);

            NIOUtils util = new NIOUtils(client);

            for(long i = 0; i < 65536; i++)
            {
                if (i < Short.MAX_VALUE)
                {
                    if (i % 3 == 0)
                    {
                        util.writeLong(i);
                        continue;
                    }

                    if (i % 11 == 0)
                    {
                        util.writeShort((short) i);
                        continue;
                    }

                    util.writeInt((int) i);
                    continue;
                }

                if (i % 3 == 0)
                {
                    util.writeInt((int) i);
                    continue;
                }

                if (i % 2 == 0)
                {
                    util.writeLong(i);
                    continue;
                }


                util.writeInt(13);
                util.writeString("TeSTing 1?2?3", Charset.forName("UTF-8"));
            }

            client.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
