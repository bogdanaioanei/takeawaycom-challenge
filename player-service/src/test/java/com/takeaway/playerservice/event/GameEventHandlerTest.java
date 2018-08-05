package com.takeaway.playerservice.event;

import com.google.common.collect.Lists;
import com.takeaway.gamechallenge.events.GameActionEvent;
import com.takeaway.gamechallenge.events.GameEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GameEventHandlerTest {

    private GameEventHandler gameEventHandler;
    private GameActionEventDispatcher gameActionEventDispatcher;

    @Before
    public void setup() {
        gameActionEventDispatcher = mock(GameActionEventDispatcher.class);
        gameEventHandler = new GameEventHandler(gameActionEventDispatcher);
    }

    @Test
    public void testHandleEvent() {
        //given
        UUID gameUuid = UUID.randomUUID();
        List<UUID> playerUuids = Lists.newArrayList(UUID.randomUUID(), UUID.randomUUID());
        Long startNumber = 56L;
        GameEvent gameEvent = new GameEvent(gameUuid, playerUuids, startNumber);

        //when
        gameEventHandler.handle(gameEvent);

        //then
        GameActionEvent gameActionEvent = new GameActionEvent(gameUuid, playerUuids.get(0), playerUuids.get(1),
                playerUuids, startNumber);
        verify(gameActionEventDispatcher).dispatchGameActionEvent(gameActionEvent);
        verify(gameActionEventDispatcher).dispatchGameActionEventForMonitoring(gameActionEvent);
    }
}
