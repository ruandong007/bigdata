package com.rd.kafka.demo1;

import com.alibaba.fastjson2.JSONObject;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ProducerDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        String topicName = "order-topic";

        KafkaProducer<String, String> producer = createKafkaProduce();

        JSONObject order = createOrder();

        // 异步发送模式
        ProducerRecord<String , String> record = new ProducerRecord<>(topicName, order.getString("orderId") , order.toJSONString());
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

    /**
     * 创建测试订单
     * @return
     */
    private static JSONObject createOrder() {
        JSONObject order = new JSONObject();
        order.put("orderId", 63988);
        order.put("orderNo", UUID.randomUUID().toString());
        order.put("userId", 1147);
        order.put("productId", 380);
        order.put("purchaseCount", 2);
        order.put("productPrice", 50.0);
        order.put("totalAmount", 100.0);
        order.put("_OPERATION_", "PAY");
        return order;
    }

    public static KafkaProducer<String, String> createKafkaProduce() {

        Properties prop = new Properties();
        prop.put("bootstrap.servers", "vm1:9092,vm2:9092,vm3:9092");
        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        // 默认16K 可以适当提大 32k 64K
        prop.put("batch.size", 323840);
        // 发送一条消息出去， 10ms内没有凑成一个batch发送，必须立刻发送出去
        prop.put("linger.ms", 10);

        //最大可以发送多大的消息  默认1M
        prop.put("max.request.size", 10*1024*1024);

        // 32M 缓冲区大小
        prop.put("buffer.memory", 33554432);
        prop.put("max.block.ms", 3000);

        // acks = 0 -1 1
        // acks = 0 : producer 发送就认为成功 可能丢数据
        // acks = 1 : Leader写入成功 可能丢数据
        // acks = -1 / all : leader 写入成功 和 ISR 中的 follower同步成功
        // 想要数据不丢失 则 1. min.insync.replicas = 2 ISR里必须有两个副本， 一个leader一个follower ;
        //                 2. acks = -1
        //                 3. retires = Integer.Max_Value 无限重试 保障数据必须成功发送给两个副本，
        // 如果做不到，则不停地重试  -> 有可能导致一个batch不停重试发送， producer的bufferPool满了，后面的数据都无法发送
        prop.put("acks", "1");

        // 重试 一般3 ~ 5次可以cover住一般的异常场景
        prop.put("retries", 3);
        //每次重试间隔500ms
        prop.put("retry.backoff.ms", 500);

        // 创建一个Producer 实例：线程资源， 跟各个broker建立socket连接资源等 一般全局一个
        KafkaProducer<String,String> producer = new KafkaProducer<>(prop);

        return producer;
    }
}
