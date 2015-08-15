# NioFlex
*Flexible NIO Server Building-Blocks for Java*

### Purpose & Functionality

NioFlex allows developers to quickly create Java NIO-driven servers, which typically
require a lot more work than traditional blocking I/O servers.

NioFlex makes it so that you don't have to write low-level, boilerplate Java NIO code,
rather you can focus on the server functionality

### Master vs. Dev Branch

This is the master branch, and there are very few edits that occur here directly - 
all of the development occurs on the 'dev' branch, so, if you are looking for the latest
and greatest in NioFlex features - check out the [dev branch](https://github.com/maheshkhanwalkar/NioFlex/tree/dev)

For those who want a stable current build, can either get the JAR from the Maven Central
Repository and checkout the repository and use Maven to carry out the build process. 


### Simple Echo Server

```
import com.revtekk.nioflex.NIOServer;
import com.revtekk.nioflex.utils.NIOUtils;

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
    public void handleRead(SocketChannel client, SelectionKey key, NIOUtils util)
    {
        /*
           This Echo-Server first receives the length from the client,
           then the actual data
         */

        int len = util.readInt();
        
        /* 
           This Echo-Server only accepts data of size 4096 
           (arbitrarily chosen) bytes or less
        */
        
        if(len <= 4096)
        {
            String text = util.readString(len);
            System.out.println("Received: " + text);
            
            /* Time to write it back */
            util.writeInt(len);
            util.writeString(text);
        }
    }
}
```


### Version

The current version of NioFlex is 0.10

### Maven Import

NioFlex has been deployed to the Maven Central Repository. The pom.xml
dependency is:

```
<dependency>
  <groupId>com.revtekk</groupId>
  <artifactId>nioflex</artifactId>
  <version>0.10</version>
</dependency>
```
