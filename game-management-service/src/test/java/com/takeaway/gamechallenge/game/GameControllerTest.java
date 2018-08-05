package com.takeaway.gamechallenge.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.gamechallenge.player.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.UUID.randomUUID;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameRepository gameRepository;

    @Test
    public void testCreateGameOk() throws Exception {
        //given
        UUID bogdanUuid = randomUUID();
        UUID gilbertoUuid = randomUUID();
        Player bogdan = new Player("Bogdan", bogdanUuid);
        Player gilberto = new Player("Gilberto", gilbertoUuid);
        List<Player> players = newArrayList(bogdan, gilberto);
        List<UUID> playerUuids = newArrayList(bogdanUuid, gilbertoUuid);
        Game game = new Game(randomUUID(), playerUuids, 56L);

        given(gameRepository.findPlayersOf(game)).willReturn(players);
        given(gameRepository.create(game)).willReturn(Optional.of(game.getUuid()));
        String payload = objectMapper.writeValueAsString(game);

        //when-then
        mockMvc.perform(post("/api/game").content(payload).header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/api/game/" + game.getUuid().toString()));

    }

    @Test
    public void testCreateGameTwice() throws Exception {
        //given
        UUID bogdanUuid = randomUUID();
        UUID gilbertoUuid = randomUUID();
        Player bogdan = new Player("Bogdan", bogdanUuid);
        Player gilberto = new Player("Gilberto", gilbertoUuid);
        List<Player> players = newArrayList(bogdan, gilberto);
        List<UUID> playerUuids = newArrayList(bogdanUuid, gilbertoUuid);
        Game game = new Game(randomUUID(), playerUuids, 56L);

        given(gameRepository.findPlayersOf(game)).willReturn(players);
        given(gameRepository.create(game)).willReturn(Optional.empty());
        String payload = objectMapper.writeValueAsString(game);

        //when-then
        mockMvc.perform(post("/api/game").content(payload).header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Game already exists"));
    }

    @Test
    public void testCreateInvalidGame() throws Exception {
        //given
        UUID bogdanUuid = randomUUID();
        UUID gilbertoUuid = randomUUID();
        Player bogdan = new Player("Bogdan", bogdanUuid);
        List<Player> players = newArrayList(bogdan);
        List<UUID> playerUuids = newArrayList(bogdanUuid, gilbertoUuid);
        Game game = new Game(randomUUID(), playerUuids, 56L);

        given(gameRepository.findPlayersOf(game)).willReturn(players);
        String payload = objectMapper.writeValueAsString(game);

        //when-then
        mockMvc.perform(post("/api/game").content(payload).header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
