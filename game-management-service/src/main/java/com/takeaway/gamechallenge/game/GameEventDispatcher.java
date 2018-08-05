package com.takeaway.gamechallenge.game;

import com.takeaway.gamechallenge.events.GameEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class GameEventDispatcher {

    private final int HARDCODED_LIMIT = 1000;

    private final GameRepository gameRepository;
    private final KafkaTemplate kafkaTemplate;

    void dispatchGameEvents() {
        List<Game> notStartedGames = gameRepository.findNotStarted(HARDCODED_LIMIT);

        notStartedGames.forEach(game -> {
            List<UUID> playerUuids = game.getPlayerUuids();
            String topic = "game-" + playerUuids.get(0).toString();

            log.info("Sending event to topic " + topic + " data: " + game.toString());

            try {
                kafkaTemplate.send(topic, new GameEvent(game.getUuid(), playerUuids, game.getStartNumber()));
                gameRepository.markAsStarted(game.getUuid());
            } catch (Exception e) {
                log.error("Error trying to send game event: " + game.getUuid());
            }
        });
    }
}
