package com.rd.kafka.demo1;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Properties prop = new Properties();
        prop.put("bootstrap.servers", "vm1:9092,vm2:9092,vm3:9092");
        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("acks", "-1");
        prop.put("retries", 3);

        prop.put("batch.size", 323840);
        prop.put("linger.ms", 10);
        prop.put("buffer.memory", 33554432);
        prop.put("max.block.ms", 3000);

        // 创建一个Producer 实例：线程资源， 跟各个broker建立socket连接资源等 一般全局一个
        KafkaProducer<String,String> producer = new KafkaProducer<>(prop);

        // 异步发送模式
        ProducerRecord<String , String> record = new ProducerRecord<>("test-topic", "key", "value1");
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    // 消息发送成功
                    System.out.println("消息发送成功");
                }else {
                    // 消息发送失败， 需要重新发送
                }
            }
        });

        // 同步发送
        // 你要一直等待人家后续一系列的步骤都做完，发送消息之后
        // 有了消息的会用返回给你，你这个方法才会退出来
//        producer.send(record).get();

        Thread.sleep(10*1000);

        producer.close();

    }
}
