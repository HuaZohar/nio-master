package com.learn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * 服务端
 */
public class Server {
    public static void main(String[] args) {
        ServerSocketChannel ssChannel = null;
        FileChannel outChannel = null;
        SocketChannel sChannel = null;
        try {
            ssChannel = ServerSocketChannel.open();
            ssChannel.bind(new InetSocketAddress("127.0.0.1", 9898));
            outChannel = FileChannel.open(Paths.get("d:/2.zip"), WRITE, CREATE);

            //接收客户端的数据
            sChannel = ssChannel.accept();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (sChannel.read(buffer) != -1) {
                buffer.flip();
                outChannel.write(buffer);
                buffer.clear();
            }


            //给客户端返回数据
            buffer.put("数据接收完成".getBytes());
            buffer.flip();
            sChannel.write(buffer);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ssChannel.close();
                outChannel.close();
                sChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
