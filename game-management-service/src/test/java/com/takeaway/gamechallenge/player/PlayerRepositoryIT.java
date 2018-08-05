package com.takeaway.gamechallenge.player;

import com.takeaway.gamechallenge.DbTest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.Assert.*;

public class PlayerRepositoryIT extends DbTest {

    private PlayerRepository playerRepository;

    @Before
    public void setup() {
        playerRepository = new PlayerRepository(dsl);
    }

    @Test
    public void testCreatePlayer() {
        //given
        UUID uuid = randomUUID();
        Player chuckNorris = new Player("Chuck Norris", uuid);

        //when
        Optional<UUID> createdPlayerUuid = playerRepository.create(chuckNorris);

        //then
        assertTrue(createdPlayerUuid.isPresent());
        assertEquals(uuid, createdPlayerUuid.get());
    }

    @Test
    public void testPlayerExists() {
        //given
        UUID bogdanUuid = UUID.fromString("22bec6bb-880c-492d-80d6-45411460ed6f");

        //when
        boolean exists = playerRepository.exists(bogdanUuid);

        //then
        assertTrue(exists);
    }

    @Test
    public void testPlayerDoesNotExist() {
        //given
        UUID unknown = UUID.fromString("11170682-97dc-11e8-9eb6-529269fb1459");

        //when
        boolean exists = playerRepository.exists(unknown);

        //then
        assertFalse(exists);
    }

    @Test
    public void testFindPlayerByUuid() {
        //given
        UUID bogdanUuid = UUID.fromString("22bec6bb-880c-492d-80d6-45411460ed6f");

        //when
        Optional<Player> playerOptional = playerRepository.findBy(bogdanUuid);

        //then
        assertTrue(playerOptional.isPresent());
        assertEquals("Bogdan", playerOptional.get().getName());
        assertEquals(bogdanUuid, playerOptional.get().getUuid());
    }

    @Test
    public void testPlayersByUuids() {
        //given
        UUID bogdanUuid = UUID.fromString("22bec6bb-880c-492d-80d6-45411460ed6f");
        UUID gilbertoUuid = UUID.fromString("a10da818-6c54-4b91-9271-a16da1eac539");

        //when
        List<Player> playersByUuid = playerRepository.findAllBy(newArrayList(bogdanUuid, gilbertoUuid));
        Optional<Player> bogdan = playersByUuid.stream().filter(player -> "Bogdan".equals(player.getName())).findFirst();
        Optional<Player> gilberto = playersByUuid.stream().filter(player -> "Gilberto".equals(player.getName())).findFirst();

        //then
        assertEquals(2, playersByUuid.size());
        assertTrue(bogdan.isPresent());
        assertTrue(gilberto.isPresent());
        assertEquals("Bogdan", bogdan.get().getName());
        assertEquals(bogdanUuid, bogdan.get().getUuid());
        assertEquals("Gilberto", gilberto.get().getName());
        assertEquals(gilbertoUuid, gilberto.get().getUuid());
    }

    @Test
    public void testDeletePlayerByUuid() {
        //given
        UUID bogdanUuid = UUID.fromString("22bec6bb-880c-492d-80d6-45411460ed6f");

        //when
        playerRepository.deleteBy(bogdanUuid);

        //then
        Optional<Player> playerOptional = playerRepository.findBy(bogdanUuid);
        assertFalse(playerOptional.isPresent());
    }

}
