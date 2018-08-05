package com.takeaway.gamechallenge.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlayerRepository playerRepository;

    @Test
    public void testCreatePlayer() throws Exception {
        //given
        UUID bogdanUuid = UUID.randomUUID();
        Player player = new Player("Bogdan", bogdanUuid);
        given(playerRepository.create(player)).willReturn(Optional.of(bogdanUuid));
        String payload = objectMapper.writeValueAsString(player);

        //when-then
        mockMvc.perform(post("/api/player").content(payload).header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/api/player/" + bogdanUuid.toString()));

    }

    @Test
    public void testCreatePlayerTwice() throws Exception {
        //given
        UUID bogdanUuid = UUID.randomUUID();
        Player player = new Player("Bogdan", bogdanUuid);
        given(playerRepository.create(player)).willReturn(Optional.empty());
        String payload = objectMapper.writeValueAsString(player);

        //when-then
        mockMvc.perform(post("/api/player").content(payload).header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Player already exists"));

    }

    @Test
    public void testCreateInvalidPlayer() throws Exception {
        //given
        UUID bogdanUuid = UUID.randomUUID();
        Player player = new Player("", bogdanUuid);
        String payload = objectMapper.writeValueAsString(player);

        //when-then
        mockMvc.perform(post("/api/player").content(payload).header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testFindPlayer() throws Exception {
        //given
        UUID bogdanUuid = UUID.randomUUID();
        Player player = new Player("Bogdan", bogdanUuid);
        given(playerRepository.findBy(bogdanUuid)).willReturn(Optional.of(player));

        //when-then
        mockMvc.perform(get("/api/player/" + bogdanUuid.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Bogdan")))
                .andExpect(jsonPath("uuid", is(bogdanUuid.toString())));

    }

    @Test
    public void testDoNotFindPlayer() throws Exception {
        //given
        UUID bogdanUuid = UUID.randomUUID();
        given(playerRepository.findBy(bogdanUuid)).willReturn(Optional.empty());

        //when-then
        mockMvc.perform(get("/api/player/" + bogdanUuid.toString()))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testDeletePlayer() throws Exception {
        //given
        UUID bogdanUuid = UUID.randomUUID();

        //when-then
        mockMvc.perform(delete("/api/player/" + bogdanUuid.toString()))
                .andExpect(status().isNoContent());
    }

}
