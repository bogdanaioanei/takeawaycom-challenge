package com.takeaway.gamechallenge.player;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api/player")
@AllArgsConstructor
public class PlayerController {

    private final PlayerRepository playerRepository;

    @PostMapping
    public ResponseEntity<String> createPlayer(@Valid @RequestBody Player player) {
        Optional<UUID> uuidOptional = playerRepository.create(player);
        if (uuidOptional.isPresent()) {
            return created(URI.create("/api/player/" + uuidOptional.get())).build();
        }

        return badRequest().body("Player already exists");
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Player> getPlayer(@PathVariable UUID uuid) {
        Optional<Player> playerOptional = playerRepository.findBy(uuid);
        if (playerOptional.isPresent()) {
            return ok(playerOptional.get());
        }

        return notFound().build();
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(NO_CONTENT)
    public void deletePlayer(@PathVariable UUID uuid) {
        playerRepository.deleteBy(uuid);
    }
}
