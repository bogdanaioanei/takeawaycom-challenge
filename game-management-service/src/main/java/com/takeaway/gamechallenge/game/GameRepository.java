package com.takeaway.gamechallenge.game;

import com.takeaway.gamechallenge.player.Player;
import com.takeaway.gamechallenge.player.PlayerRepository;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.impl.DSL.*;

@Repository
@AllArgsConstructor
public class GameRepository {

    private static final String GAME_TABLE = "data.game";
    private static final String UUID_FIELD = "uuid";
    private static final String PLAYER_UUIDS_FIELD = "player_uuids";
    private static final String START_NUMBER_FIELD = "start_number";
    private static final String STARTED_FIELD = "started";
    private static final String FINISHED_FIELD = "finished";

    private final DSLContext dsl;
    private final PlayerRepository playerRepository;

    public Optional<UUID> create(Game game) {

        if (!exists(game.getUuid())) {
            UUID gameUuid = dsl.insertInto(table(GAME_TABLE),
                    field(UUID_FIELD), field(PLAYER_UUIDS_FIELD), field(START_NUMBER_FIELD))
                    .values(game.getUuid(), array(game.getPlayerUuids().toArray()), game.getStartNumber())
                    .returning(field(UUID_FIELD))
                    .fetchOne()
                    .getValue(field(UUID_FIELD, UUID.class));

            return Optional.of(gameUuid);
        }

        return Optional.empty();

    }

    public boolean exists(UUID uuid) {
        return dsl.fetchExists(selectFrom(table(GAME_TABLE))
                .where(field(UUID_FIELD).eq(uuid)));
    }

    public List<Game> findFinished(int limit) {
        return dsl.select(field(UUID_FIELD), field(PLAYER_UUIDS_FIELD, UUID[].class), field(START_NUMBER_FIELD))
                .from(table(GAME_TABLE))
                .where(field(FINISHED_FIELD).eq(true))
                .limit(limit)
                .fetchInto(Game.class);
    }

    public List<Game> findNotStarted(int limit) {
        return dsl.select(field(UUID_FIELD), field(PLAYER_UUIDS_FIELD, UUID[].class), field(START_NUMBER_FIELD))
                .from(table(GAME_TABLE))
                .where(field(STARTED_FIELD).eq(false))
                .limit(limit)
                .fetchInto(Game.class);

    }

    public List<Player> findPlayersOf(Game game) {
        return playerRepository.findAllBy(game.getPlayerUuids());
    }

    public void markAsStarted(UUID uuid) {
        dsl.update(table(GAME_TABLE))
                .set(field(STARTED_FIELD), true)
                .where(field(UUID_FIELD).eq(uuid))
                .execute();
    }

    public void markAsFinished(UUID uuid) {
        dsl.update(table(GAME_TABLE))
                .set(field(FINISHED_FIELD), true)
                .where(field(UUID_FIELD).eq(uuid))
                .execute();
    }
}
