package com.takeaway.gamechallenge.game;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/game")
@Slf4j
@AllArgsConstructor
public class GameController {

    private final GameRepository gameRepository;

    @PostMapping
    @ResponseStatus(OK)
    public ResponseEntity<String> createGame(@Valid @RequestBody Game game) {

        Optional<UUID> uuidOptional = gameRepository.create(game);
        if (uuidOptional.isPresent()) {
            return created(URI.create("/api/game/" + uuidOptional.get())).build();
        }

        return badRequest().body("Game already exists");

    }
}
