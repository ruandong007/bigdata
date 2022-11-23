package com.rd.kafka.demo1;

import com.alibaba.fastjson2.JSONObject;
import com.rd.kafka.util.ThreadPoolConfiguration;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;


import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {


    public static void main(String[] args) {
        String topicName = "order-topic";

        KafkaConsumer<String, String> consumer = createConsumer();

        consumer.subscribe(Arrays.asList(topicName));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    JSONObject order = JSONObject.parseObject(record.value());
                    ThreadPoolConfiguration.kafkaConsumerPool.execute(new CreditManageTask(order));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            consumer.close();
        }

    }

   static class CreditManageTask implements Runnable {

        private JSONObject order;
        public CreditManageTask(JSONObject order) {
            this.order = order;
        }

        @Override
        public void run() {
            System.out.println("对订单进行积分的维护。。。。。。" + order.toJSONString());
        }
    }

    private static KafkaConsumer<String, String> createConsumer() {
        String groupName = "test-group";

        Properties prop = new Properties();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "vm1:9092,vm2:9092,vm3:9092");
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, groupName);

        // 如果开启自动提交 会间隔一段时间提交一次offset
        // 如果你每次要重启下consumer 他一定会把一些数据重新消费一遍
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // 每次自动提交offset的时间间隔
        prop.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // 每次重启都是从最早的offset开始读取，而不是上一次
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // 每个consumer 向broker 提交心跳时间间隔 可以设置小一点
        // 如果某个consumer挂了，kafka broker感知到了， 触发rebalance的操作
        prop.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 100);
        // 如果说kafka的broker在10秒内感知不到consumer的心跳，则认为该consumer挂了， 此时触发Rebalance
        prop.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10*1000);

        // 如果30秒才去执行下一次poll
        prop.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 30*1000);

        // 10M  如果说你的消费的吞吐量特别大， 可以适当提高
        prop.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 10485760);
        prop.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);

        // 不要去回收socket连接
        prop.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, -1);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);

        return consumer;
    }
}
