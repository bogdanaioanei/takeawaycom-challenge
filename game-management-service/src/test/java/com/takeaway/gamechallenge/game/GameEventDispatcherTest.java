package com.takeaway.gamechallenge.game;

import com.takeaway.gamechallenge.events.GameEvent;
import com.takeaway.gamechallenge.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.UUID.randomUUID;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GameEventDispatcherTest {

    private static final String FIRST_PLAYER_UUID_TOPIC = "22bec6bb-880c-492d-80d6-45411460ed6f";

    private GameEventDispatcher gameEventDispatcher;
    private GameRepository gameRepository;
    private KafkaTemplate kafkaTemplate;

    @Before
    public void setup() {
        gameRepository = mock(GameRepository.class);
        kafkaTemplate = mock(KafkaTemplate.class);
        gameEventDispatcher = new GameEventDispatcher(gameRepository, kafkaTemplate);
    }

    @Test
    public void testSendMessageOk() {
        //given
        Game unstartedGame = game();
        given(gameRepository.findNotStarted(1000)).willReturn(newArrayList(unstartedGame));

        //when
        gameEventDispatcher.dispatchGameEvents();

        //then
        verify(kafkaTemplate).send("game-" + FIRST_PLAYER_UUID_TOPIC, new GameEvent(unstartedGame.getUuid(),
                unstartedGame.getPlayerUuids(), unstartedGame.getStartNumber()));
        verify(gameRepository).findNotStarted(1000);
        verify(gameRepository).markAsStarted(unstartedGame.getUuid());
        verifyNoMoreInteractions(kafkaTemplate);
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    public void testSendMessageError() {
        //given
        Game unstartedGame = game();
        given(gameRepository.findNotStarted(1000)).willReturn(newArrayList(unstartedGame));
        GameEvent gameEvent = new GameEvent(unstartedGame.getUuid(),unstartedGame.getPlayerUuids(), unstartedGame.getStartNumber());
        given(kafkaTemplate.send("game-" + FIRST_PLAYER_UUID_TOPIC, gameEvent)).willThrow(RuntimeException.class);

        //when
        gameEventDispatcher.dispatchGameEvents();

        //then
        verify(kafkaTemplate).send("game-" + FIRST_PLAYER_UUID_TOPIC, new GameEvent(unstartedGame.getUuid(),
                unstartedGame.getPlayerUuids(), unstartedGame.getStartNumber()));
        verify(gameRepository).findNotStarted(1000);
        verifyNoMoreInteractions(kafkaTemplate);
        verifyNoMoreInteractions(gameRepository);
    }

    private Game game() {
        UUID bogdanUuid = UUID.fromString(FIRST_PLAYER_UUID_TOPIC);
        UUID gilbertoUuid = randomUUID();
        Player bogdan = new Player("Bogdan", bogdanUuid);
        Player gilberto = new Player("Gilberto", gilbertoUuid);
        List<Player> players = newArrayList(bogdan, gilberto);
        List<UUID> playerUuids = newArrayList(bogdanUuid, gilbertoUuid);
        return new Game(randomUUID(), playerUuids, 56L);
    }
}
