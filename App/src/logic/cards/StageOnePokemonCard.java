package logic.cards;

public class StageOnePokemonCard extends PokemonCard {
    private PokemonCard basicPokemon;

    public StageOnePokemonCard(PokemonCard basicPokemon) {
        this.basicPokemon = basicPokemon;
    }

    public PokemonCard getBasicPokemon() {
        return basicPokemon;
    }   
}   