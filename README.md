hive:
nohup hiveserver2 >> /home/soft/hive-3.1.3/logs/hiveserver2.log 2>&1 &

hbase :
hbase-daemon.sh stop master && stop-hbase.sh

flink:
flink run -d -t yarn-per-job -Dyarn.application.queue flink -c com.erayt.app.RfxToHbase ./.jar

kafka:
-- partition 最大offset
kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list vm1:9092 --topic 888 --time -1

-- partition 最小offset
kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list vm1:9092 --topic 888 --time -2

-- 最大offset - 最小offset 就是每个分区的消息数量
    
--kafka 压测 生产消息
    kafka-producer-perf-test.sh --topic test-topic --num-records 50000000 --record-size 200 --throughput -1 --producer-props bootstrap.servers=vm1:9092,vm2:9092,vm3:9092 acks=-1

-- kafka 压测 消费
    kafka-consumer-perf-test.sh  --bootstrap-server vm1:9092,vm2:9092,vm3:9092 --fetch-size 2000 --messages 5000000 --topic test-topic
    