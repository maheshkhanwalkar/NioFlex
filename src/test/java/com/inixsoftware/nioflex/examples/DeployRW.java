package com.inixsoftware.nioflex.examples;

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


import com.inixsoftware.nioflex.nio.ServerDispatch;
import org.junit.Test;

public class DeployRW
{
    @Test
    public static void main(String[] args)
    {
        RWServer server = new RWServer();
        ServerDispatch dispatch = new ServerDispatch(5252, server);

        dispatch.startUp();
    }
}
