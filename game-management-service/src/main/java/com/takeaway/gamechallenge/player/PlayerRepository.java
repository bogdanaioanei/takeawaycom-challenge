package com.takeaway.gamechallenge.player;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.impl.DSL.*;

@Repository
@AllArgsConstructor
@Slf4j
public class PlayerRepository {

    private static final String PLAYER_TABLE = "data.player";
    private static final String UUID_FIELD = "uuid";
    private static final String NAME_FIELD = "name";

    private final DSLContext dsl;

    Optional<UUID> create(Player player) {

        if (!exists(player.getUuid())) {
            UUID uuid = dsl.insertInto(table(PLAYER_TABLE),
                    field(NAME_FIELD), field(UUID_FIELD))
                    .values(player.getName(), player.getUuid())
                    .returning(field(UUID_FIELD))
                    .fetchOne()
                    .getValue(field(UUID_FIELD, UUID.class));

            return Optional.of(uuid);
        }

        return Optional.empty();


    }

    public boolean exists(UUID uuid) {
        return dsl.fetchExists(selectFrom(table(PLAYER_TABLE))
                .where(field(UUID_FIELD).eq(uuid)));
    }

    Optional<Player> findBy(UUID uuid) {
        Player player = dsl.select(field(NAME_FIELD), field(UUID_FIELD))
                .from(table(PLAYER_TABLE))
                .where(field(UUID_FIELD).eq(uuid))
                .fetchOneInto(Player.class);

        return Optional.ofNullable(player);
    }

    public List<Player> findAllBy(List<UUID> uuids) {
        return dsl.select(field(NAME_FIELD), field(UUID_FIELD))
                .from(table(PLAYER_TABLE))
                .where(field(UUID_FIELD).in(uuids))
                .fetchInto(Player.class);
    }

    void deleteBy(UUID uuid) {
        dsl.deleteFrom(table(PLAYER_TABLE))
                .where(field(UUID_FIELD).eq(uuid))
                .execute();
    }
}
