package com.learn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Scanner;

/**
 * Hello world!
 */
public class Client {
    public static void main(String[] args) {

        SocketChannel sChannel = null;
        try {
            //1.获取通道
            sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
            //2.切换非阻塞模式
            sChannel.configureBlocking(false);
            //3.分配指定大小的缓存区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //4.发送数据到服务器

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                final String next = scanner.next();

                buffer.put((new Date().toString() + "\n" + next).getBytes());
                buffer.flip();
                sChannel.write(buffer);
                buffer.clear();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
