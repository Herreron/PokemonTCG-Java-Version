package logic.cards;

public class BasicPokemonCard extends PokemonCard {
    public BasicPokemonCard() {
        super();
    }

    public BasicPokemonCard(String name, short healthPoints, byte retreatCost,
            java.util.ArrayList<Attack> attacks, boolean isEx) {
        super(name, healthPoints, retreatCost, attacks, isEx);
    }
}
