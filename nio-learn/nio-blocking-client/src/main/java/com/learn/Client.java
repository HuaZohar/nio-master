package com.learn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 客户端
 */
public class Client {
    public static void main(String[] args) {
        SocketChannel sChannel = null;
        FileChannel inChannel = null;
        try {
            //1.获取通道
            sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

            //要获取本地文件缓存
            inChannel = FileChannel.open(Paths.get("d:/1.zip"), StandardOpenOption.READ);

            //2.分配指定大小的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //3.读取本地文件，并发送到服务端
            while (inChannel.read(buffer) != -1) {
                buffer.flip();
                sChannel.write(buffer);
                buffer.clear();
            }

            sChannel.shutdownOutput();

            //4.接收服务端的反馈
            while (sChannel.read(buffer) != -1) {
                buffer.flip();
                System.out.println(new String(buffer.array()));
                buffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sChannel.close();
                inChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
