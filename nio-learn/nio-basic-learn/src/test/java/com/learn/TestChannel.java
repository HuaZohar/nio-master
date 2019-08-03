package com.learn;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 1.通道（Channel，用于源节点与目标节点的连接。
 * 在java NIO中负责缓存区中数据的传输。Channel本身不存储数据，因此需要配合缓存区进行传输）
 * <p>
 * 2.通道的主要实现类，java.nio.channels.Channel接口
 * FileChannel
 * SocketChannel
 * ServerSocketChannel
 * DatagramChannel
 * 3.获取通道的方式
 */
public class TestChannel {

    @Test
    public void test6() throws IOException {
        Charset cs1 = Charset.forName("GBK");
        CharsetEncoder ce = cs1.newEncoder();
        CharsetDecoder cd = cs1.newDecoder();
        CharBuffer cBuf = CharBuffer.allocate(1024);
        cBuf.put("尚硅谷威武！");
        cBuf.flip();
        ByteBuffer bBuf = ce.encode(cBuf);

        for (int i = 0; i < 12; ++i) {
            System.out.println(bBuf.get());
        }

        bBuf.flip();
        CharBuffer cBuf2 = cd.decode(bBuf);
        System.out.println(cBuf2.toString());
        System.out.println("------------------------------------------------------");
        Charset cs2 = Charset.forName("GBK");
        bBuf.flip();
        CharBuffer cBuf3 = cs2.decode(bBuf);
        System.out.println(cBuf3.toString());
    }

    @Test
    public void test5() {
        Map<String, Charset> map = Charset.availableCharsets();
        Set<Map.Entry<String, Charset>> set = map.entrySet();
        Iterator var4 = set.iterator();

        while (var4.hasNext()) {
            Map.Entry<String, Charset> entry = (Map.Entry) var4.next();
            System.out.println((String) entry.getKey() + "=" + entry.getValue());
        }

    }

    @Test
    public void test4() throws IOException {
        RandomAccessFile raf1 = new RandomAccessFile("1.txt", "rw");
        FileChannel channel1 = raf1.getChannel();
        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);
        ByteBuffer[] bufs = new ByteBuffer[]{buf1, buf2};
        channel1.read(bufs);
        ByteBuffer[] var9 = bufs;
        int var8 = bufs.length;

        for (int var7 = 0; var7 < var8; ++var7) {
            ByteBuffer byteBuffer = var9[var7];
            byteBuffer.flip();
        }

        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
        System.out.println("-----------------");
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));
        RandomAccessFile raf2 = new RandomAccessFile("2.txt", "rw");
        FileChannel channel2 = raf2.getChannel();
        channel2.write(bufs);
    }

    @Test
    public void test3() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("d:/1.mkv"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("d:/2.mkv"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
        outChannel.transferFrom(inChannel, 0L, inChannel.size());
        inChannel.close();
        outChannel.close();
    }

    @Test
    public void test2() throws IOException {
        long start = System.currentTimeMillis();
        FileChannel inChannel = FileChannel.open(Paths.get("d:/1.rar"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("d:/2.rar"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
        MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0L, inChannel.size());
        MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0L, inChannel.size());
        byte[] dst = new byte[inMappedBuf.limit()];
        inMappedBuf.get(dst);
        outMappedBuf.put(dst);
        inChannel.close();
        outChannel.close();
        long end = System.currentTimeMillis();
        System.out.println("耗费时间为：" + (end - start));
    }

    @Test
    public void test1() {
        long start = System.currentTimeMillis();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            fis = new FileInputStream("d:/1.rar");
            fos = new FileOutputStream("d:/2.rar");
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(1024);

            while (inChannel.read(buf) != -1) {
                buf.flip();
                outChannel.write(buf);
                buf.clear();
            }
        } catch (IOException var28) {
            var28.printStackTrace();
        } finally {
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException var27) {
                    var27.printStackTrace();
                }
            }

            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException var26) {
                    var26.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var25) {
                    var25.printStackTrace();
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var24) {
                    var24.printStackTrace();
                }
            }

        }

        long end = System.currentTimeMillis();
        System.out.println("耗费时间为：" + (end - start));
    }
}
