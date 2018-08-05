package com.takeaway.gamechallenge.game;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GameScheduler {

    private GameEventDispatcher gameEventDispatcher;

    @Scheduled(initialDelay = 5000, fixedDelay = 1000)
    public void run() {
        gameEventDispatcher.dispatchGameEvents();
    }
}
