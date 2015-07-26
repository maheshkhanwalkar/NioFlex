package com.revtekk.nioflex.security;

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

public class BufferSecurity
{
    private int maxSize;
    private RejectionPolicy policy;

    public BufferSecurity(int maxSize, RejectionPolicy policy)
    {
        this.maxSize = maxSize;
        this.policy = policy;
    }

    /**
     * @return the read-rejection policy
     */
    public RejectionPolicy getPolicy()
    {
        return policy;
    }

    /**
     * @return the maximum allowed buffer size
     */
    public int getMax()
    {
        return maxSize;
    }

    /**
     * This method checks whether the inputted
     * length adheres to the maximum buffer size.
     *
     * @param len a sample buffer length
     * @return whether the length is acceptable
     */
    public boolean isAcceptable(int len)
    {
        return len <= maxSize;
    }
}
