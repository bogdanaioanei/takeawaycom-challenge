package com.takeaway.gamechallenge.game;

import com.takeaway.gamechallenge.events.GameActionEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class GameActionEventHandler {

    private final GameRepository gameRepository;

    @KafkaListener(topics = "game-monitoring", containerFactory = "kafkaListenerContainerFactory")
    public void handle(GameActionEvent event) {

        if(event.getNumber() != 1) {
            String message = String.format("Player %s sent %d to next player: %s",
                    event.getDispatchingPlayerUuid(), event.getNumber(), event.getNextPlayerUuid());

            log.info(message);
        } else {
            String message = String.format("Player %s got to number %d and has thus won the game!",
                    event.getDispatchingPlayerUuid(), event.getNumber());

            log.info(message);

            gameRepository.markAsFinished(event.getGameUuid());
        }
    }
}
