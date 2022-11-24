一. 三大组件： Channel Buffer Selector
1. Channel:
    1. 常见的Channel:
       1) FileChannel : 文件
       2) DatagramChannel : UDP
       3) SocketChannel : TCP(客户端和服务端)
       4) ServerSocketChannel : TCP (服务端)
2. Buffer:
    1. 常见的Buffer
       1) ByteBuffer:
            MappedByteBuffer
            DirectByteBuffer
            HeapByteBuffer
       2) ShortBuffer
       3) IntBuffer
       4) LongBuffer
       5) FloatBuffer
       6) DoubleBuffer
       7) CharBuffer
3. Selector:
      