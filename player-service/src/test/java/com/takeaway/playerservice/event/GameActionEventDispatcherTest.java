package com.takeaway.playerservice.event;

import com.google.common.collect.Lists;
import com.takeaway.gamechallenge.events.GameActionEvent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GameActionEventDispatcherTest {

    private GameActionEventDispatcher gameActionEventDispatcher;
    private KafkaTemplate kafkaTemplate;

    @Before
    public void setup() {
        kafkaTemplate = mock(KafkaTemplate.class);
        gameActionEventDispatcher = new GameActionEventDispatcher(kafkaTemplate);
    }

    @Test
    public void testDispatchGameActionEvent() {
        //given
        //given
        UUID gameUuid = UUID.randomUUID();
        List<UUID> playerUuids = Lists.newArrayList(UUID.randomUUID(), UUID.randomUUID());
        Long number = 56L;
        GameActionEvent gameActionEvent = new GameActionEvent(gameUuid, playerUuids.get(0), playerUuids.get(1),
                playerUuids, number);

        //when
        gameActionEventDispatcher.dispatchGameActionEvent(gameActionEvent);

        //then
        verify(kafkaTemplate).send("game-action-" + gameActionEvent.getNextPlayerUuid(), gameActionEvent);
        verifyNoMoreInteractions(kafkaTemplate);
    }

    @Test
    public void testDispatchGameActionEventForMonitoring() {
        //given
        UUID gameUuid = UUID.randomUUID();
        List<UUID> playerUuids = Lists.newArrayList(UUID.randomUUID(), UUID.randomUUID());
        Long number = 56L;
        GameActionEvent gameActionEvent = new GameActionEvent(gameUuid, playerUuids.get(0), playerUuids.get(1),
                playerUuids, number);

        //when
        gameActionEventDispatcher.dispatchGameActionEventForMonitoring(gameActionEvent);

        //then
        verify(kafkaTemplate).send("game-monitoring", gameActionEvent);
        verifyNoMoreInteractions(kafkaTemplate);
    }
}
