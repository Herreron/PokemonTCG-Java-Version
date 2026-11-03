package logic.cards;
import java.util.ArrayList;

public final class PokemonCatalog {

    private PokemonCatalog() {
    }

    /**
     * Crea una carta nueva usando su nombre como clave de búsqueda.
     * El switch delega en los métodos de fábrica para que cada llamada genere un objeto independiente.
     */
    public static AbstractCard createCardByName(String cardName) {
        if (cardName == null) {
            throw new IllegalArgumentException("El nombre de la carta no puede ser null.");
        }

        // El patrón Factory centraliza la selección de la clase concreta por nombre.
        switch (cardName) {
            case "Charmander":
                return createCharmander();
            case "Charmeleon":
                return createCharmeleon();
            case "Charizard":
                return createCharizard();
            default:
                throw new IllegalArgumentException("No existe una carta Pokemon llamada: " + cardName);
        }
    }

    // Cada método de fábrica construye un ejemplar nuevo y evita compartir estado entre copias.
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
