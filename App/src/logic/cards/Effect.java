package logic.cards;

import logic.battlesystem.Game;
import logic.battlesystem.Player;

public interface Effect {
    boolean activateEffect(Game game, Player player);

    // Este es para las cartas que necesitan que el jugador elija un Pokemon (por ejemplo Misty).
    // Si la carta no lo necesita, ignora el Pokemon elegido y usa el metodo de arriba,
    // asi las cartas que ya hicimos no hay que tocarlas.
    default boolean activateEffect(Game game, Player player, PokemonCard target) {
        return activateEffect(game, player);
    }
}