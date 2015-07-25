# NioFlex
*Flexible NIO Server Building-Blocks for Java*

### Welcome to the 'dev' branch

The NioFlex 'dev' branch is where all the active development occurs. The latest and greatest
features of NioFlex are staged and developed here before (eventually) making it to the 'master' branch
when a release is ready to be deployed. 

It is highly recommended that if you want to contribute to NioFlex, please make all edits on the 'dev'
branch of your fork. This will make the merging from submitted Pull Requests (PR) a lot easier. 

From time to time, the [NioFlex Version](#version) will change as edits will be collected into "release candidates"
These 'rc's will not be pushed to the Maven Central Repository, however, a .jar will be available for download.

### Purpose & Functionality

NioFlex allows developers to quickly create Java NIO-driven servers, which typically
require a lot more work than traditional blocking I/O servers.

NioFlex makes it so that you don't have to write low-level, boilerplate Java NIO code,
rather you can focus on the server functionality.

### Simple Echo Server

```java
import com.revtekk.nioflex.NIOServer;
import com.revtekk.nioflex.utils.SocketUtils;

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
    public void handleRead(SocketChannel client, SelectionKey key, SocketUtils util)
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

| Mainline Version | Release Date  | Download    | 
| ---------------- | ------------- | ----------- |
| 0.20-rc1         | [TBD]         | [TBD]       |



| Stable Version   | Release Date  | Download    | 
| ---------------- | ------------- | ----------- |
| 0.10             | [7/23/15]     | [TBD]       |

### Maven Import

**Note:** This only applies to stable, non-dev releases (see master branch). No release candidate will
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
