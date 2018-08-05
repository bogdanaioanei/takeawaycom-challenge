package com.takeaway.playerservice.event;

import com.takeaway.gamechallenge.events.GameActionEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class GameActionEventDispatcher {

    private final KafkaTemplate kafkaTemplate;

    public void dispatchGameActionEvent(GameActionEvent event) {
        dispatch("game-action-" + event.getNextPlayerUuid().toString(), event);
    }

    public void dispatchGameActionEventForMonitoring(GameActionEvent event) {
        dispatch("game-monitoring", event);
    }

    private void dispatch(String topic, GameActionEvent event) {
        kafkaTemplate.send(topic, event);
    }
}
