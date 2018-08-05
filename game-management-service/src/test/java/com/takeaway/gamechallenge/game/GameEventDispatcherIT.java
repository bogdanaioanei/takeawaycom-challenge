package com.takeaway.gamechallenge.game;

import com.google.common.collect.Lists;
import com.takeaway.gamechallenge.IntegrationTest;
import com.takeaway.gamechallenge.events.GameEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DirtiesContext
public class GameEventDispatcherIT extends IntegrationTest {

    private static final String FIRST_PLAYER_UUID_TOPIC = "game-22bec6bb-880c-492d-80d6-45411460ed6f";

    @Autowired
    private GameEventDispatcher gameEventDispatcher;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, FIRST_PLAYER_UUID_TOPIC);

    private KafkaMessageListenerContainer<String, GameEvent> container;

    private BlockingQueue<ConsumerRecord<String, GameEvent>> records;


    @Before
    public void setup() throws Exception {

        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("takeaway", "false", embeddedKafka);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<String, GameEvent> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProperties, new StringDeserializer(), new JsonDeserializer<>(GameEvent.class));

        // set the topic that needs to be consumed
        ContainerProperties containerProperties = new ContainerProperties(FIRST_PLAYER_UUID_TOPIC);

        // create a Kafka MessageListenerContainer
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        // create a thread safe queue to store the received message
        records = new LinkedBlockingQueue<>();

        // setup a Kafka message listener
        container.setupMessageListener((MessageListener<String, GameEvent>) record -> records.add(record));

        // start the container and underlying message listener
        container.start();

        // wait until the container has the required number of assigned partitions
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
    }

    @After
    public void cleanUp() {
        container.stop();
    }

    @Test
    public void testGameEventDispatch() throws InterruptedException {
        //given the game saved in the database

        //when
        gameEventDispatcher.dispatchGameEvents();

        //then
        ConsumerRecord<String, GameEvent> received = records.poll(10, SECONDS);
        GameEvent gameEvent = received.value();

        assertNotNull(gameEvent);

        List<UUID> expectedPlayerUuids = Lists.newArrayList(UUID.fromString("22bec6bb-880c-492d-80d6-45411460ed6f"), UUID.fromString("a10da818-6c54-4b91-9271-a16da1eac539"));
        assertEquals(expectedPlayerUuids, gameEvent.getPlayerUuids());
        assertEquals(Long.valueOf(56), gameEvent.getStartNumber());

    }
}
