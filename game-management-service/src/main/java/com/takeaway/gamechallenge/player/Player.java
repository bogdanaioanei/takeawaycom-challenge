package com.takeaway.gamechallenge.player;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;


@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Player {

    @NotEmpty
    @JsonProperty("name")
    private String name;

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonCreator
    public Player(@JsonProperty("name") String name,
                  @JsonProperty("uuid") UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
