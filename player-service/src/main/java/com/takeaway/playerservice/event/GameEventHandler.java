package com.takeaway.playerservice.event;

import com.takeaway.gamechallenge.events.GameActionEvent;
import com.takeaway.gamechallenge.events.GameEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class GameEventHandler {

    private final GameActionEventDispatcher gameActionEventDispatcher;

    @KafkaListener(topics = "#{'${player.game-event.topic}'}", containerFactory = "gameEventFactory")
    public void handle(GameEvent gameEvent) {
        log.info("Starting game: " + gameEvent.toString());

        UUID firstPlayer = gameEvent.getPlayerUuids().get(0);
        UUID nextPlayer = gameEvent.getPlayerUuids().get(1);

        GameActionEvent initialEvent = new GameActionEvent(gameEvent.getGameUuid(), firstPlayer, nextPlayer, gameEvent.getPlayerUuids(),
                gameEvent.getStartNumber());


        gameActionEventDispatcher.dispatchGameActionEvent(initialEvent);
        gameActionEventDispatcher.dispatchGameActionEventForMonitoring(initialEvent);
    }
}
