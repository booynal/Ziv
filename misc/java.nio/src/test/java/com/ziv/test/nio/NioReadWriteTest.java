package com.ziv.test.nio;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 项目名称:misc
 * 包名称:  com.ziv.test.nio
 * 类描述:  测试Java的NIO的读写数据的方法
 * 创建人:  ziv
 * 创建时间: 2018/3/20 09:49
 */
public class NioReadWriteTest {

	@Test
	public void test_read() throws IOException {
		RandomAccessFile accessFile = new RandomAccessFile("/tmp/a.txt", "r");
		FileChannel fileChannel = accessFile.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(128);
		while (-1 != fileChannel.read(buffer)) {
			buffer.flip();
			while (buffer.hasRemaining()) {
				byte b = buffer.get();
				System.out.print((char) b);
			}
//			buffer.compact();
			buffer.clear();
		}
		accessFile.close();
	}

	@Test
	public void test_copy() throws IOException {
		RandomAccessFile fromFile = new RandomAccessFile("/tmp/from.txt", "r");
		FileChannel fromChannel = fromFile.getChannel();
		RandomAccessFile toFile = new RandomAccessFile("/tmp/to.txt", "rw");
		FileChannel toChannel = toFile.getChannel();

		long count = fromChannel.size();
		//toChannel.transferFrom(fromChannel, 0, count); // 将数据从别的Channel传输FileChannel。transferFrom(ReadableByteChannel, long, long)
		fromChannel.transferTo(0, count, toChannel); // 将数据从FileChannel传输到别的Channel。transferTo(long, long, WritableByteChannel)

		fromFile.close();
		toFile.close();
	}
}
