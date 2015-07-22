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

import com.revtekk.nioflex.nio.NIOServer;
import com.revtekk.nioflex.nio.utils.NIOUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ServerTest extends NIOServer
{
    public ServerTest(int port)
    {
        super(port);
    }

    @Override
    public void handleAccept(SocketChannel client, SelectionKey key)
    {
        System.out.println("CLIENT CONNECTED");
    }

    @Override
    public void handleRead(SocketChannel client, SelectionKey key)
    {
        try
        {
            BufferedWriter testLog = new BufferedWriter(new FileWriter("results.log"));
            int count = 1;
            int passed = 0;

            System.out.println("TESTING NIOFLEX API");

            for(long i = 0; i < 65536; i++)
            {
                if (i < Short.MAX_VALUE)
                {
                    if (i % 3 == 0)
                    {
                        long res = NIOUtils.readLong(client);
                        if (res == i)
                        {
                            testLog.write("LONG TEST   #" + count++ + " PASSED\n");
                            passed++;
                        }
                        else
                        {
                            testLog.write("LONG TEST   #" + count++ + " FAILED\n");
                        }

                        continue;
                    }

                    if (i % 11 == 0)
                    {
                        short res = NIOUtils.readShort(client);

                        if (res == (short) i)
                        {
                            testLog.write("SHORT TEST  #" + count++ + " PASSED\n");
                            passed++;
                        }
                        else
                        {
                            testLog.write("SHORT TEST  #" + count++ + " FAILED\n");
                        }

                        continue;
                    }

                    int res = NIOUtils.readInt(client);
                    if (res == (int) i)
                    {
                        testLog.write("INT TEST    #" + count++ + " PASSED\n");
                        passed++;
                    }
                    else
                    {
                        testLog.write("INT TEST    #" + count++ + " FAILED\n");
                    }

                    continue;
                }

                if (i % 3 == 0)
                {
                    int res = NIOUtils.readInt(client);
                    if (res == (int) i)
                    {
                        testLog.write("INT TEST    #" + count++ + " PASSED\n");
                        passed++;
                    }
                    else
                    {
                        testLog.write("INT TEST    #" + count++ + " FAILED\n");
                    }

                    continue;
                }

                if (i % 2 == 0)
                {
                    long res = NIOUtils.readLong(client);
                    if (res == i)
                    {
                        testLog.write("LONG TEST   #" + count++ + " PASSED\n");
                        passed++;
                    }
                    else
                    {
                        testLog.write("LONG TEST   #" + count++ + " FAILED\n");
                    }

                    continue;
                }


                int len = NIOUtils.readInt(client);
                if (len != 13)
                {
                    testLog.write("STRING TEST #" + count++ + " FAILED\n");
                }

                String str = NIOUtils.readString(len, client, Charset.forName("UTF-8"));
                if (str.equals("TeSTing 1?2?3"))
                {
                    testLog.write("STRING TEST #" + count++ + " PASSED\n");
                    passed++;
                }
                else
                {
                    testLog.write("STRING TEST #" + count++ + " FAILED\n");
                }

            }

            count--;
            testLog.write("\n" + passed + "/" + count + " TESTS PASSED\n");

            testLog.flush();
            testLog.close();

            System.out.println("RESULTS LOGGED IN results.log");

            scheduleShutdown(); //tell the server to shutdown
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        
        
    }
}
