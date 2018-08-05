package com.takeaway.gamechallenge.game.validation;

import com.google.common.collect.Lists;
import com.takeaway.gamechallenge.game.Game;
import com.takeaway.gamechallenge.game.GameRepository;
import com.takeaway.gamechallenge.player.Player;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class GameValidator implements ConstraintValidator<ValidGame, Game> {

    private final GameRepository gameRepository;

    @Override
    public boolean isValid(Game game, ConstraintValidatorContext context) {
        return playersExist(game);
    }

    private boolean playersExist(Game game) {
        List<UUID> playerUuids = newArrayList(game.getPlayerUuids());
        List<Player> playersOfGame = gameRepository.findPlayersOf(game);
        List<UUID> playersOfGameUuids = newArrayList(playersOfGame.stream().map(Player::getUuid).collect(toList()));

        sort(playerUuids);
        sort(playersOfGameUuids);
        return playerUuids.equals(playersOfGameUuids);
    }
}
