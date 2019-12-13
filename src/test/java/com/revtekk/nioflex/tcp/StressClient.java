package com.revtekk.nioflex.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StressClient
{
    private static final int MAX_CLIENTS = 256;
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        Socket[] clients = new Socket[MAX_CLIENTS];

        // establish the connections to the server
        for(int i = 0; i < clients.length; i++)
        {
            clients[i] = new Socket(InetAddress.getLocalHost(), 4444);
        }

        // fill up a test buffer
        byte[] buffer = new byte[BUFFER_SIZE];

        for(int i = 0; i < buffer.length; i++)
        {
            buffer[i] = (byte)(i % Byte.MAX_VALUE);
        }


        // create a thread pool to send data in parallel
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = new ThreadPoolExecutor(cores, cores, 1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>());

        for(Socket client : clients)
        {
            pool.submit(() ->
            {
                try
                {
                    OutputStream os = client.getOutputStream();
                    InputStream is = client.getInputStream();

                    os.write(buffer, 0, buffer.length);
                    os.flush();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);

        // close the connections down
        for (Socket client : clients)
        {
            client.close();
        }
    }
}
