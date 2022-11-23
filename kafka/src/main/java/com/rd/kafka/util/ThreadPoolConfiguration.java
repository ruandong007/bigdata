package com.rd.kafka.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class ThreadPoolConfiguration {

    /** 获取当前系统的CPU 数目*/
    static int cpuNums = Runtime.getRuntime().availableProcessors();

    /** 线程池核心池的大小*/
    private static int corePoolSize = 10;

    /** 线程池的最大线程数*/
    private static int maximumPoolSize = cpuNums * 5;


    public static ExecutorService kafkaConsumerPool = null;


    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();
    /**
     * 静态方法
     */
    static{
        System.out.println("创建线程数:"+corePoolSize+",最大线程数:"+maximumPoolSize);
        //建立10个核心线程，线程请求个数超过20，则进入队列等待
        kafkaConsumerPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0L,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100),new ThreadFactoryBuilder().setNameFormat("PROS-%d").build());
    }


    public static ThreadPoolTaskExecutor kafkaConsumerExecutor() {
        ThreadPoolTaskExecutor pool= new ThreadPoolTaskExecutor();
        // 核心线程数,线程池创建时候初始化的线程数
        pool.setCorePoolSize(10);
        // 最大线程数,线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        pool.setMaxPoolSize(20);
        // 缓冲队列,用来缓冲执行任务的队列
        pool.setQueueCapacity(500);
        // 允许线程的空闲时间,当超过了核心线程之外的线程在空闲时间到达之后会被销毁
        pool.setKeepAliveSeconds(60);
        // 线程池名的前缀
        pool.setThreadNamePrefix("Thread-");
        // 缓冲队列满了之后的拒绝策略,由调用线程处理（一般是主线程）
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        pool.initialize();
        return pool;
    }
}
