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
import com.inixsoftware.nioflex.nio.utils.NIOUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

//tests read/write of short ints
public class RWServer extends NIOServer
{
    @Override
    public void handleAccept(SocketChannel client, SelectionKey key)
    {
    }

    @Override
    public void handleRead(SocketChannel client, SelectionKey key)
    {
        try
        {
            BufferedWriter testLog = new BufferedWriter(new FileWriter("results.log"));
            
            if(NIOUtils.readShort(client) == 2)
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");

            if(NIOUtils.readShort(client) == 4)
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");

            if(NIOUtils.readShort(client) == 1)
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");

            if(NIOUtils.readShort(client) == 16)
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");

            if(NIOUtils.readInt(client) == 32)
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");
            
            if(NIOUtils.readInt(client) == 64)
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");
            
            if(NIOUtils.readLong(client) == 128)
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");

            if(NIOUtils.readLong(client) == 128)
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");

            if(NIOUtils.readLong(client) == 65536)
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");

            if(NIOUtils.readLong(client) == 256)
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");

            if(NIOUtils.readLong(client) == 1024)
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");

            int bytesToRead = NIOUtils.readInt(client);

            if(NIOUtils.readString(bytesToRead, client).equals("Hello world!"))
                testLog.write("PASSED\n");
            else
                testLog.write("FAILED\n");

            testLog.flush();
            testLog.close();

            System.out.println("Results logged to results.log");
            scheduleShutdown(); //tell the server to shutdown
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        
        
    }
}
