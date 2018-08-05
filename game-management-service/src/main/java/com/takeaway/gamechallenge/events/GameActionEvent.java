package com.takeaway.gamechallenge.events;

import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class GameActionEvent {

    UUID gameUuid;
    UUID dispatchingPlayerUuid;
    UUID nextPlayerUuid;
    List<UUID> playerUuids;
    Long number;
}
