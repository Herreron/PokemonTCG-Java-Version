package logic.cards;

public class StageTwoPokemonCard extends PokemonCard {
    public StageTwoPokemonCard() {
        super();
    }

    public StageTwoPokemonCard(String name, short healthPoints, byte retreatCost,
            java.util.ArrayList<Attack> attacks, boolean isEx, String evolvesFrom) {
        super(name, healthPoints, retreatCost, attacks, isEx, (byte) 3, evolvesFrom);
    }
}
