package com.takeaway.gamechallenge.events;

import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class GameEvent {

    private UUID gameUuid;
    private List<UUID> playerUuids;
    private Long startNumber;

}
