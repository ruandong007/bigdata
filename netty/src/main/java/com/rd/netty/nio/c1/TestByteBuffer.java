package com.rd.netty.nio.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件读取
 */
@Slf4j
public class TestByteBuffer {

    public static void main(String[] args) {

        // FileChannel
        //1. 输入输出流 获取  2. RandomAccessFile 获取
        try (FileChannel channel = new FileInputStream("C:\\Users\\admin\\IdeaProjects\\bigdata\\netty\\src\\main\\resources\\c1\\data.txt").getChannel()) {
            // 准备缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);

            while (true) {
                // 从channel 读取数据, 向buffer写入
                int read = channel.read(byteBuffer);
                log.debug("读取到的字节数 {}" , read);
                //  -1 没有内容
                if (read == -1) {
                    break;
                }
                // 打印buffer的内容
                //切换至读模式
                byteBuffer.flip();
                // 获取数据 byteBuffer.get() -> 读一个字节
                // byteBuffer.hasRemaining() -> 检查是否还有剩余的数据
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get();
                    log.debug("读取到的字节 {}" , (char) b);
                }

                // 切换为写模式
                byteBuffer.clear();
            }


        } catch (IOException e) {
        }
    }
}
