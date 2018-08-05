package com.takeaway.gamechallenge.game.validation;

import com.takeaway.gamechallenge.game.Game;
import com.takeaway.gamechallenge.game.GameRepository;
import com.takeaway.gamechallenge.player.Player;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class GameValidatorTest {

    private GameValidator gameValidator;
    private GameRepository gameRepository;

    @Before
    public void setup() {
        gameRepository = mock(GameRepository.class);
        gameValidator = new GameValidator(gameRepository);
    }

    @Test
    public void testGameIsValid() {
        //given
        UUID bogdanUuid = randomUUID();
        UUID gilbertoUuid = randomUUID();
        Player bogdan = new Player("Bogdan", bogdanUuid);
        Player gilberto = new Player("Gilberto", gilbertoUuid);
        List<Player> players = newArrayList(bogdan, gilberto);
        List<UUID> playerUuids = newArrayList(bogdanUuid, gilbertoUuid);
        Game game = new Game(randomUUID(), playerUuids, 56L);

        given(gameRepository.findPlayersOf(game)).willReturn(players);

        //when
        boolean valid = gameValidator.isValid(game, mock(ConstraintValidatorContext.class));

        //then
        assertTrue(valid);
    }

    @Test
    public void testGameIsNotValidBecauseDifferentPlayers() {
        //given
        UUID bogdanUuid = randomUUID();
        UUID gilbertoUuid = randomUUID();
        UUID emmyUuid = randomUUID();
        Player bogdan = new Player("Bogdan", bogdanUuid);
        Player gilberto = new Player("Gilberto", gilbertoUuid);
        Player emmy = new Player("Emmy", emmyUuid);
        List<Player> players = newArrayList(bogdan, gilberto, emmy);
        List<UUID> playerUuids = newArrayList(bogdanUuid, gilbertoUuid);
        Game game = new Game(randomUUID(), playerUuids, 56L);

        given(gameRepository.findPlayersOf(game)).willReturn(players);

        //when
        boolean valid = gameValidator.isValid(game, mock(ConstraintValidatorContext.class));

        //then
        assertFalse(valid);
    }

}
