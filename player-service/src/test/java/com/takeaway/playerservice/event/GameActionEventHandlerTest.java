package com.takeaway.playerservice.event;

import com.google.common.collect.Lists;
import com.takeaway.gamechallenge.events.GameActionEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GameActionEventHandlerTest {

    private GameActionEventHandler gameActionEventHandler;
    private GameActionEventDispatcher gameActionEventDispatcher;

    @Before
    public void setup() {
        gameActionEventDispatcher = mock(GameActionEventDispatcher.class);
        gameActionEventHandler = new GameActionEventHandler(gameActionEventDispatcher);
    }

    @Test
    public void testHandleEvent() {
        //given
        UUID gameUuid = UUID.randomUUID();
        List<UUID> playerUuids = Lists.newArrayList(UUID.randomUUID(), UUID.randomUUID());
        Long number = 56L;
        GameActionEvent gameActionEvent = new GameActionEvent(gameUuid, playerUuids.get(0), playerUuids.get(1),
                playerUuids, number);

        //when
        gameActionEventHandler.handle(gameActionEvent);

        //then
        GameActionEvent nextActionEvent = new GameActionEvent(gameUuid, playerUuids.get(1), playerUuids.get(0),
                playerUuids, 19L);
        verify(gameActionEventDispatcher).dispatchGameActionEvent(nextActionEvent);
        verify(gameActionEventDispatcher).dispatchGameActionEventForMonitoring(nextActionEvent);
        verifyNoMoreInteractions(gameActionEventDispatcher);
    }

    @Test
    public void testHandleWinEvent() {
        //given
        UUID gameUuid = UUID.randomUUID();
        List<UUID> playerUuids = Lists.newArrayList(UUID.randomUUID(), UUID.randomUUID());
        Long number = 3L;
        GameActionEvent gameActionEvent = new GameActionEvent(gameUuid, playerUuids.get(0), playerUuids.get(1),
                playerUuids, number);

        //when
        gameActionEventHandler.handle(gameActionEvent);

        //then
        GameActionEvent nextActionEvent = new GameActionEvent(gameUuid, playerUuids.get(1), playerUuids.get(0),
                playerUuids, 1L);
        verify(gameActionEventDispatcher).dispatchGameActionEventForMonitoring(nextActionEvent);
        verifyNoMoreInteractions(gameActionEventDispatcher);
    }
}
