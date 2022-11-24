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
      1. 多线程版: 一个 thread -> socket : 内存占用率高 线程上下文切换成本高 只适合连接数少的场景
      2. 线程池版: 阻塞模式下，线程只能处理一个socket连接 仅适用短连接场景
      3. Selector版: 配合一个线程管理多个Channel,获取这些Channel上发生的事件, 这些Channel工作在非阻塞模式下，不会让线程吊死在一个Channel上。
                     适合连接数特别多，但流浪低的场景(low traffic)
      ---参考网络模型.drowio
