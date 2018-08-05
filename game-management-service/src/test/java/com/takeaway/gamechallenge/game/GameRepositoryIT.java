package com.takeaway.gamechallenge.game;

import com.takeaway.gamechallenge.DbTest;
import com.takeaway.gamechallenge.player.Player;
import com.takeaway.gamechallenge.player.PlayerRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameRepositoryIT extends DbTest {

    private GameRepository gameRepository;

    @Before
    public void setup() {
        gameRepository = new GameRepository(dsl, new PlayerRepository(dsl));
    }

    @Test
    public void testFindPlayersOfGame() {
        //given
        UUID bogdanUuid = UUID.fromString("22bec6bb-880c-492d-80d6-45411460ed6f");
        UUID gilbertoUuid = UUID.fromString("a10da818-6c54-4b91-9271-a16da1eac539");
        Player bogdan = new Player("Bogdan", bogdanUuid);
        Player gilberto = new Player("Gilberto", gilbertoUuid);
        List<Player> players = newArrayList(bogdan, gilberto);
        List<UUID> playerUuids = newArrayList(bogdanUuid, gilbertoUuid);
        Game game = new Game(randomUUID(), playerUuids, 56L);

        //when
        List<Player> existingPlayers = gameRepository.findPlayersOf(game);

        //then
        assertEquals(2, existingPlayers.size());
        assertTrue(existingPlayers.contains(bogdan));
        assertTrue(existingPlayers.contains(gilberto));
    }

    @Test
    public void testGameExists() {
        //given
        UUID uuid = UUID.fromString("90047dfa-9a69-4d30-9704-e888b3fd51ff");

        //when
        boolean exists = gameRepository.exists(uuid);

        //then
        assertTrue(exists);
    }

    @Test
    public void testGameDoesNotExist() {
        //given
        UUID uuid = UUID.fromString("91147dfa-8b58-4d30-9704-e888b3fd51ee");

        //when
        boolean exists = gameRepository.exists(uuid);

        //then
        assertFalse(exists);
    }

    @Test
    public void testCreateGame() {
        //given
        UUID bogdanUuid = randomUUID();
        UUID gilbertoUuid = randomUUID();
        List<UUID> playerUuids = newArrayList(bogdanUuid, gilbertoUuid);
        UUID gameUuid = randomUUID();
        Game game = new Game(gameUuid, playerUuids, 56L);


        //when
        Optional<UUID> uuidOptional = gameRepository.create(game);

        //then
        assertTrue(uuidOptional.isPresent());
        assertEquals(gameUuid, uuidOptional.get());
    }

    @Test
    public void testFindNotStarted() {
        //given
        UUID uuid = UUID.fromString("90047dfa-9a69-4d30-9704-e888b3fd51ff");

        //when
        List<Game> notStartedGames = gameRepository.findNotStarted(5);

        //then
        assertEquals(1, notStartedGames.size());
        assertEquals(uuid, notStartedGames.get(0).getUuid());
    }

    @Test
    public void testMarkAsStarted() {
        //given
        UUID uuid = UUID.fromString("90047dfa-9a69-4d30-9704-e888b3fd51ff");

        //when
        gameRepository.markAsStarted(uuid);

        //then
        List<Game> notStartedGames = gameRepository.findNotStarted(5);
        assertEquals(0, notStartedGames.size());
    }

    @Test
    public void testMarkAsFinished() {
        //given
        UUID uuid = UUID.fromString("90047dfa-9a69-4d30-9704-e888b3fd51ff");

        //when
        gameRepository.markAsFinished(uuid);

        //then
        List<Game> finishedGames = gameRepository.findFinished(5);
        assertEquals(1, finishedGames.size());
    }
}
