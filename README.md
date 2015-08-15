# NioFlex
*Flexible NIO Server Building-Blocks for Java*

### Welcome to the 'master' branch

The NioFlex 'master' branch is where all the active development occurs - there is no 'dev' branch (anymore), 
so you will need to look at the versions listed below to find out how to checkout the source and/or zoom back 
in time to the release you want - or just use the pre-made jars or the Maven dependencies (see below).

From time to time, the [NioFlex Version](#version) will change as edits will be collected into "release candidates"
These 'rc's will not be pushed to the Maven Central Repository; however, you can git fetch using the release's last commit SHA-1 hash (listed below)

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

For example, to check out 0.20-rc1, you could do:

```
git clone https://github.com/maheshkhanwalkar/NioFlex
cd NioFlex

git reset --hard 20fcc786774e23ca149775ff3845a8080309e7e2
```

| Mainline Version | Release Date  | Git Commit    |
| ---------------- | ------------- | ------------- |
| 0.20-rc2         | [TBD]         | [TBD]         |
| 0.20-rc1         | [8/13/15]     | [20fcc786774e23ca149775ff3845a8080309e7e2](https://github.com/maheshkhanwalkar/NioFlex/commit/20fcc786774e23ca149775ff3845a8080309e7e2)         |


| Stable Version   | Release Date  | Download                                                                               | 
| ---------------- | ------------- | -------------------------------------------------------------------------------------- |
| 0.10             | [7/23/15]     | [[0.10 JAR](http://central.maven.org/maven2/com/revtekk/nioflex/0.10/nioflex-0.10.jar)]     |

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
