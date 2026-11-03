package logic.cards;

import logic.battlesystem.Game;
import logic.battlesystem.Player;

public interface Effect {
    void activateEffect(Game game, Player player);

    // For effects that act on a Pokemon chosen by the player (e.g. Misty).
    // By default the target is ignored, so existing cards do not need to change.
    default void activateEffect(Game game, Player player, PokemonCard target) {
        activateEffect(game, player);
    }
}