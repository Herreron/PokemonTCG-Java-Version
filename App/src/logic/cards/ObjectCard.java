package logic.cards;

import logic.battlesystem.Game;
import logic.battlesystem.Player;

public class ObjectCard extends AbstractCard implements Effect {

    public ObjectCard(String name) {
        super(name, "Objeto");
    }

    @Override
    public boolean activateEffect(Game game, Player player) {
        return activateEffect(game, player, null);
    }

    @Override
    public boolean activateEffect(Game game, Player player, PokemonCard target) {
        if (player == null) {
            return false;
        }

        switch (getName()) {
            case "Pocion":
                PokemonCard pokemonToHeal = target;
                if (pokemonToHeal == null) {
                    pokemonToHeal = player.getActivePokemon();
                }
                return player.healPokemon(pokemonToHeal, (short) 20);
            case "Poke Ball":
                return player.searchBasicPokemonInDeck();
            case "Turbo Velocidad":
                return player.reduceActiveRetreatCost();
            default:
                return false;
        }
    }

}