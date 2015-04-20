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

import java.io.*;
import java.net.Socket;

public class MyClient
{
    public static void main(String[] args) throws IOException
    {
        Socket s = new Socket("localhost", 8585);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        bw.write("Hello world!");
        bw.flush();

        //bw.write(0xEE);
        //bw.flush();

        System.out.println(br.readLine());
        //System.out.println(br.read());

        br.close();
        bw.close();

        s.close();
    }
}
