# NioFlex
*Flexible NIO Server Building-Blocks for Java*

### Purpose & Functionality

NioFlex allows developers to quickly create Java NIO-driven servers, which typically
require a lot more work than traditional blocking I/O servers.

NioFlex makes it so that you don't have to write low-level, boilerplate Java NIO code,
rather you can focus on the server functionality.

### NioFlex Tutorials

**Basic/Intro Reads**

1. [Introduction to NioFlex](https://gist.github.com/maheshkhanwalkar/e659a00dc93b4b01eb25)
2. [Using the SocketUtil Helper Class](https://gist.github.com/maheshkhanwalkar/534c7e4f6b0cd1ceb5df)

**Conceptual/Good Reads**

TODO

### Simple Echo Server

```java
import com.revtekk.nioflex.NIOServer;
import com.revtekk.nioflex.utils.SocketUtil;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class EchoServer extends NIOServer
{
    public EchoServer(int port)
    {
        super(port);
    }
    
    @Override
    public void handleAccept(SocketChannel client, SelectionKey key)
    {
        System.out.println("Client Connected!");
    }
    
    @Override
    public void handleRead(SocketChannel client, SelectionKey key, SocketUtil util)
    {
        /*
           This Echo-Server gets a String and echoes it back.
           It is first sent the length of the String, and then the actual data

           The server echoes both the length and the String
        */

         int len = util.readInt();
         if (len < 65536)
         {
            //this a random "buffer-maximum"
            //this server will reject reads of greater than or equal to 65,536 bytes

            String data = util.readString(len);
            System.out.println("Data Received: " + data);

            util.writeInt(len);
            util.writeString(data);
         }
         else
         {
            System.out.println("Too large!");
         }
    }
}
```

### Version

Release candidates (x.y.z-rcN) are not compiled for you or pushed to the Central Repository, 
so you will need to checkout the correct commit that is the last commit of that rc release.

| Mainline Version | Release Date  | Git Commit    |
| ---------------- | ------------- | ------------- |
| 0.30-rc1         | [TBD]         | [TBD]         |


| Stable Version   | Release Date  | Download                                                                               | 
| ---------------- | ------------- | -------------------------------------------------------------------------------------- |
| 0.20             | [12/11/19]    | [TBD]                                                                                  |
| 0.10             | [7/23/15]     | [[0.10 JAR](http://central.maven.org/maven2/com/revtekk/nioflex/0.10/nioflex-0.10.jar)]|

### Maven Import

**Note:** This only applies to stable releases. No release candidate will
be pushed to the Maven Central Repository. 

NioFlex has been deployed to the Maven Central Repository. The pom.xml
dependency is:

```
<dependency>
  <groupId>com.revtekk</groupId>
  <artifactId>nioflex</artifactId>
  <version>0.10</version>
</dependency>
```
