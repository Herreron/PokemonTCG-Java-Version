package logic.cards;

import logic.battlesystem.Game;
import logic.battlesystem.Player;

public interface Effect {
    boolean activateEffect(Game game, Player player);
    default boolean activateEffect(Game game, Player player, PokemonCard target) {
        return activateEffect(game, player);
    }
}