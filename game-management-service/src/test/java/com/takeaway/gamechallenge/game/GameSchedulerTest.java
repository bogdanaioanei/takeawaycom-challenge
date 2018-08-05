package com.takeaway.gamechallenge.game;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class GameSchedulerTest {

    private GameScheduler gameScheduler;
    private GameEventDispatcher gameEventDispatcher;

    @Before
    public void setup() {
        gameEventDispatcher = mock(GameEventDispatcher.class);
        gameScheduler = new GameScheduler(gameEventDispatcher);
    }

    @Test
    public void testRun() {
        //when
        gameScheduler.run();

        //then
        verify(gameEventDispatcher).dispatchGameEvents();
        verifyNoMoreInteractions(gameEventDispatcher);
    }
}
