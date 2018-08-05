package com.takeaway.gamechallenge.game;

import com.google.common.collect.Lists;
import com.takeaway.gamechallenge.events.GameActionEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class GameActionEventHandlerTest {

    private GameActionEventHandler gameActionEventHandler;
    private GameRepository gameRepository;


    @Before
    public void setup() {
        gameRepository = mock(GameRepository.class);
        gameActionEventHandler = new GameActionEventHandler(gameRepository);
    }

    @Test
    public void testReceiveEvent() {
        //given
        GameActionEvent gameActionEvent = gameActionEvent(56L);

        //when
        gameActionEventHandler.handle(gameActionEvent);

        //then
        verifyZeroInteractions(gameRepository);
    }

    @Test
    public void testReceiveWinningEvent() {
        //given
        GameActionEvent gameActionEvent = gameActionEvent(1L);

        //when
        gameActionEventHandler.handle(gameActionEvent);

        //then
        verify(gameRepository).markAsFinished(gameActionEvent.getGameUuid());
    }

    private GameActionEvent gameActionEvent(Long number) {
        UUID gameUuid = UUID.randomUUID();
        UUID dispatchingPlayerUuid = UUID.randomUUID();
        UUID nextPlayerUuid = UUID.randomUUID();
        List<UUID> playerUuids = Lists.newArrayList(dispatchingPlayerUuid, nextPlayerUuid);

        return new GameActionEvent(gameUuid, dispatchingPlayerUuid, nextPlayerUuid, playerUuids, number);
    }
}
