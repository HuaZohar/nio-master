package com.learn;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * NIO
 * 缓存区
 * <p>
 * 1.依据数据类型不同（boolean除外），提供了相应的缓冲区
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * IntBuffer
 * LongBuffer
 * FloatBuffer
 * DoubleBuffer
 * 注：上述缓存区的管理方式几乎一致，通过allocate()获取缓存区
 * <p>
 * 2.缓存区存取数据的两个核心方法
 * put() 写
 * get() 读
 * flip() 切换模式
 * <p>
 * 3.缓存区的四个核心属性
 * private int mark = -1; //标记，表示当前position的位置。可以通过reset()恢复到mark的位置
 * private int position = 0; //位置，表示缓存区正在操作数据的位置。
 * private int limit;   //界限，表示缓存区可以操作数据的大小。（limit后数据不能进行读写）
 * private int capacity; //容量，表示缓存区的最大存储数据的容量。一旦声明不能改变。
 * <p>
 * 0 <= mark <= position <= limit <= capacity
 * <p>
 * 4.直接缓存区和非直接缓存区
 * 非直接缓存区：allocate()方法分配缓存区，将缓存区建立在JVM的内存中
 * 直接缓存区：allocateDirect()方法分配直接缓存区，将缓存区建立在物理内存中，可以提高效率
 */
public class TestBuffer {

    @Test
    public void test3() {
//
//        FileInputStream fileInputStream = new FileInputStream("");
//        FileOutputStream fileOutputStream = new FileOutputStream("");

//        final FileChannel channel = fileInputStream.getChannel();


        final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        
    }

    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        String src = "abcqwe";

        buffer.put(src.getBytes());
        System.out.println("------put------");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        //切换读写模式
        buffer.flip();

        System.out.println("------flip------");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        byte[] dst = new byte[buffer.limit()];
        buffer.get(dst);

        System.out.println("---------get--------");
        System.out.println(new String(dst));
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());


    }
}
