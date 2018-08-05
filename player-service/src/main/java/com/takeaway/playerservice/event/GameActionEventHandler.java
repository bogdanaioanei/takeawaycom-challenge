package com.takeaway.playerservice.event;

import com.takeaway.gamechallenge.events.GameActionEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class GameActionEventHandler {

    private final GameActionEventDispatcher gameActionEventDispatcher;

    @KafkaListener(topics = "#{'${player.game-action-event.topic}'}", containerFactory = "gameEventFactory")
    public void handle(GameActionEvent gameActionEvent) {
        UUID myUuid = gameActionEvent.getNextPlayerUuid();
        UUID nextPlayerUuid = determineNextPlayerUuid(gameActionEvent.getPlayerUuids(), myUuid);
        Long receivedNumber = gameActionEvent.getNumber();
        Long newNumber = determineNewNumber(receivedNumber);

        GameActionEvent newActionEvent = new GameActionEvent(gameActionEvent.getGameUuid(), myUuid, nextPlayerUuid,
                gameActionEvent.getPlayerUuids(), newNumber);

        if (newNumber != 1) {
            gameActionEventDispatcher.dispatchGameActionEvent(newActionEvent);
        }

        gameActionEventDispatcher.dispatchGameActionEventForMonitoring(newActionEvent);
    }

    private Long determineNewNumber(Long receivedNumber) {
        if (receivedNumber % 3 == 1) {
            return (receivedNumber - 1) / 3;
        } else if(receivedNumber % 3 == 2) {
            return (receivedNumber + 1) / 3;
        }

        return receivedNumber / 3;
    }

    private UUID determineNextPlayerUuid(List<UUID> playerUuids, UUID myUuid) {

        int myIndex = playerUuids.indexOf(myUuid);
        int nextPlayerIndex = (playerUuids.size() - 1) == myIndex ? 0 : myIndex + 1;

        return playerUuids.get(nextPlayerIndex);
    }
}
