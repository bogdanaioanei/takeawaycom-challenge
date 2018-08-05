package com.takeaway.gamechallenge.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.takeaway.gamechallenge.game.validation.ValidGame;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@ValidGame
public class Game {

    @JsonProperty("uuid")
    private UUID uuid;

    @Size(min = 2)
    @JsonProperty("playerUuids")
    private List<UUID> playerUuids;

    @Min(2)
    @JsonProperty("startNumber")
    private Long startNumber;

    @JsonCreator
    public Game(@JsonProperty("uuid") UUID uuid,
                @JsonProperty("playerUuids") List<UUID> playerUuids,
                @JsonProperty("startNumber") Long startNumber) {
        this.uuid = uuid;
        this.playerUuids = playerUuids;
        this.startNumber = startNumber;
    }
}
