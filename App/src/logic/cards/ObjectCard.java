package logic.cards;

import logic.battlesystem.Game;
import logic.battlesystem.Player;

public class ObjectCard extends TrainerCard {

    public ObjectCard(String name, int cardNumber) {
        super(name, cardNumber, "Objeto");
    }

    @Override
    public boolean activateEffect(Game game, Player player) {
        if (player == null) {
            return false;
        }

        switch (getName()) {
            case "Pocion":
                PokemonCard activePokemon = player.getActivePokemon();
                if (activePokemon == null || activePokemon.getHealthPoints() >= activePokemon.getMaxHealthPoints()) {
                    return false;
                }
                activePokemon.heal(20);
                return true;
            case "Poke Ball":
                return player.searchBasicPokemonInDeck();
            case "Turbo Velocidad":
                return player.reduceActiveRetreatCost();
            default:
                return false;
        }
    }

    @Override
    public boolean activateEffect(Game game, Player player, PokemonCard target) {
        return activateEffect(game, player);
    }
}