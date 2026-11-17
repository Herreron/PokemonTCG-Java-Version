package logic.cards;

public class StageOnePokemonCard extends PokemonCard {
    private PokemonCard basicPokemon;

    public StageOnePokemonCard(PokemonCard basicPokemon) {
        this.basicPokemon = basicPokemon;
    }

    public StageOnePokemonCard(String name, short healthPoints, byte retreatCost,
            java.util.ArrayList<Attack> attacks, boolean isEx, String evolvesFrom) {
        super(name, healthPoints, retreatCost, attacks, isEx, (byte) 2, evolvesFrom);
        this.basicPokemon = null;
    }

    public PokemonCard getBasicPokemon() {
        return basicPokemon;
    }   
}   