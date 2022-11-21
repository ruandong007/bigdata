package com.rd.kafka.demo1;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class TopicDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "vm1:9092,vm2:9092,vm3:9092");

        AdminClient adminClient = AdminClient.create(properties);

        try {
            String topicName = "order-topic";

            // 创建topic前，可以先检查topic是否存在，如果已经存在，则不用再创建了
            Set<String> topics = adminClient.listTopics().names().get();

            if (topics.contains(topicName)) {
                return;
            }

            // 创建topic
            NewTopic newTopic = new NewTopic(topicName, 3, (short) 2);
            CreateTopicsResult result = adminClient.createTopics(Collections.singletonList(newTopic));
            result.all().get();

        } finally {
            adminClient.close();
        }

    }
}
