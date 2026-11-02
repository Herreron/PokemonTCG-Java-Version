package logic.cards;

import java.util.ArrayList;

public final class PokemonCatalog {

    private PokemonCatalog() {
    }

    public static PokemonCard createCharmander() {
        return new PokemonCard("Charmander", 1, 60, 1, new ArrayList<>(), false, 1, null);
    }

    public static PokemonCard createCharmeleon() {
        return new PokemonCard("Charmeleon", 2, 80, 2, new ArrayList<>(), false, 2, "Charmander");
    }

    public static PokemonCard createCharizard() {
        return new PokemonCard("Charizard", 3, 150, 2, new ArrayList<>(), false, 3, "Charmeleon");
    }

    public static ArrayList<PokemonCard> createCharmanderEvolutionLine() {
        ArrayList<PokemonCard> evolutionLine = new ArrayList<>();
        evolutionLine.add(createCharmander());
        evolutionLine.add(createCharmeleon());
        evolutionLine.add(createCharizard());
        return evolutionLine;
    }
}
