package com.learn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Hello world!
 */
public class Server {
    public static void main(String[] args) {
        ServerSocketChannel ssChannel = null;
        try {
            //1.获取通道
            ssChannel = ServerSocketChannel.open();

            //2.切换非阻塞模式
            ssChannel.configureBlocking(false);

            //3.绑定连接
            ssChannel.bind(new InetSocketAddress("127.0.0.1", 9898));

            //4.获取选择器
            Selector selector = Selector.open();

            //5.将通道注入到选择器上，并且指定“监听接收事件”
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);

            //6.轮询获取选择器上已经“准备就绪”的事件

            while (selector.select() > 0) {
                //7.
                final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    //8.获取准备“就绪”的事件
                    final SelectionKey selectionKey = iterator.next();

                    //9.判断具体是什么事件
                    if (selectionKey.isAcceptable()) {
                        //10.若准备就绪，获取客户端连接
                        final SocketChannel sChannel = ssChannel.accept();
                        //11.设置非阻塞模式
                        sChannel.configureBlocking(false);
                        //12.将该通道注册到选择器上
                        sChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        //13.获取当前选择器上“读就绪”状态的通道
                        SocketChannel sChannel = (SocketChannel) selectionKey.channel();
                        //14.读取数据
                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        while (sChannel.read(buffer) != -1) {
                            buffer.flip();
                            System.out.println(new String(buffer.array()));
                            buffer.clear();
                        }

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ssChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
