package logic.cards;

import java.util.ArrayList;

public final class PokemonCatalog {

    private PokemonCatalog() {
    }

    /**
     * Crea una carta nueva usando su nombre como clave de busqueda.
     * El switch delega en los metodos de fabrica para que cada llamada genere un objeto independiente.
     */
    public static AbstractCard createCardByName(String cardName) {
        if (cardName == null) {
            return null;
        }

        // El patron Factory centraliza la seleccion de la clase concreta por nombre.
        switch (cardName) {
            case "Pikachu":
                return createPikachu();
            case "Raichu":
                return createRaichu();
            case "Charmander":
                return createCharmander();
            case "Charmeleon":
                return createCharmeleon();
            case "Charizard":
                return createCharizard();
            case "Squirtle":
                return createSquirtle();
            case "Wartortle":
                return createWartortle();
            case "Blastoise":
                return createBlastoise();
            case "Jigglypuff":
                return createJigglypuff();
            case "Wigglytuff":
                return createWigglytuff();
            case "Magikarp":
                return createMagikarp();
            case "Gyarados":
                return createGyarados();
            case "Snorlax":
                return createSnorlax();
            case "Mewtwo ex":
                return createMewtwoEx();
            case "Zapdos ex":
                return createZapdosEx();
            case "Moltres ex":
                return createMoltresEx();
            case "Articuno ex":
                return createArticunoEx();
            case "Growlithe":
                return createGrowlithe();
            case "Arcanine":
                return createArcanine();
            case "Psyduck":
                return createPsyduck();
            case "Golduck":
                return createGolduck();
            case "Electabuzz":
                return createElectabuzz();
            case "Gastly":
                return createGastly();
            case "Haunter":
                return createHaunter();
            case "Gengar":
                return createGengar();
            default:
                return null;
        }
    }

    // ---------- Pokemon del PDF (numeros 1 a 10) ----------

    public static PokemonCard createPikachu() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Mordisco", Type.ELECTRIC, 1, 20));
        return withType(new PokemonCard("Pikachu", "1", 60, 1, attacks, false), Type.ELECTRIC);
    }

    public static PokemonCard createCharmander() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Ascuas", Type.FIRE, 1, 30));
        return withType(new PokemonCard("Charmander", "2", 60, 1, attacks, false), Type.FIRE);
    }

    public static PokemonCard createSquirtle() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Placaje", Type.WATER, 1, 20));
        return withType(new PokemonCard("Squirtle", "3", 60, 1, attacks, false), Type.WATER);
    }

    public static PokemonCard createJigglypuff() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Golpe", Type.COLORLESS, 1, 20));
        return withType(new PokemonCard("Jigglypuff", "4", 70, 1, attacks, false), Type.COLORLESS);
    }

    public static PokemonCard createMagikarp() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Chapoteo", Type.WATER, 1, 10));
        return withType(new PokemonCard("Magikarp", "5", 30, 1, attacks, false), Type.WATER);
    }

    public static PokemonCard createSnorlax() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Golpe Cuerpo", Type.COLORLESS, 3, 100));
        return withType(new PokemonCard("Snorlax", "6", 150, 3, attacks, false), Type.COLORLESS);
    }

    public static PokemonCard createMewtwoEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Ataque Psiquico", Type.PSYCHIC, 2, 50));
        attacks.add(new Attack("Psicorrayo", Type.PSYCHIC, 3, 150));
        return withType(new PokemonCard("Mewtwo ex", "7", 150, 2, attacks, true), Type.PSYCHIC);
    }

    public static PokemonCard createZapdosEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Picotazo", Type.COLORLESS, 1, 30));
        attacks.add(new Attack("Tormenta Electrica", Type.ELECTRIC, 3, 100));
        return withType(new PokemonCard("Zapdos ex", "8", 130, 2, attacks, true), Type.ELECTRIC);
    }

    public static PokemonCard createMoltresEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Aleteo", Type.COLORLESS, 1, 30));
        attacks.add(new Attack("Danza Infernal", Type.FIRE, 3, 70));
        return withType(new PokemonCard("Moltres ex", "9", 130, 2, attacks, true), Type.FIRE);
    }

    public static PokemonCard createArticunoEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Viento Helado", Type.WATER, 1, 30));
        attacks.add(new Attack("Ventisca", Type.WATER, 3, 100));
        return withType(new PokemonCard("Articuno ex", "10", 130, 2, attacks, true), Type.WATER);
    }

    // ---------- Evoluciones (numeros 16 a 22, datos de las cartas reales) ----------

    public static PokemonCard createRaichu() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Rayo", Type.ELECTRIC, 3, 140));
        return withType(new PokemonCard("Raichu", "16", 100, 1, attacks, false, 2, "Pikachu"), Type.ELECTRIC);
    }

    public static PokemonCard createCharmeleon() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Garras de Fuego", Type.FIRE, 3, 60));
        return withType(new PokemonCard("Charmeleon", "17", 90, 2, attacks, false, 2, "Charmander"), Type.FIRE);
    }

    public static PokemonCard createCharizard() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Giro Fuego", Type.FIRE, 4, 150));
        return withType(new PokemonCard("Charizard", "18", 150, 2, attacks, false, 3, "Charmeleon"), Type.FIRE);
    }

    public static PokemonCard createWartortle() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Salpicadura", Type.WATER, 2, 40));
        return withType(new PokemonCard("Wartortle", "19", 80, 1, attacks, false, 2, "Squirtle"), Type.WATER);
    }

    public static PokemonCard createBlastoise() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Hidrobomba", Type.WATER, 3, 80));
        return withType(new PokemonCard("Blastoise", "20", 150, 3, attacks, false, 3, "Wartortle"), Type.WATER);
    }

    public static PokemonCard createWigglytuff() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Vozarron", Type.COLORLESS, 2, 60));
        return withType(new PokemonCard("Wigglytuff", "21", 100, 1, attacks, false, 2, "Jigglypuff"), Type.COLORLESS);
    }

    public static PokemonCard createGyarados() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Hiperrayo", Type.WATER, 4, 100));
        return withType(new PokemonCard("Gyarados", "22", 150, 4, attacks, false, 2, "Magikarp"), Type.WATER);
    }
    public static PokemonCard createGrowlithe() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Mordisco", Type.COLORLESS, 1, 10));
        return withType(new PokemonCard("Growlithe", "23", 70, 1, attacks, false), Type.FIRE);
    }

    public static PokemonCard createArcanine() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Lanzallamas", Type.FIRE, 3, 90));
        // Fase 2 (1 evolución), evoluciona de Growlithe
        return withType(new PokemonCard("Arcanine", "24", 130, 2, attacks, false, 2, "Growlithe"), Type.FIRE);
    }

    // ---------- TIPO AGUA ----------
    public static PokemonCard createPsyduck() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Dolor de Cabeza", Type.WATER, 1, 20));
        return withType(new PokemonCard("Psyduck", "25", 60, 1, attacks, false), Type.WATER);
    }

    public static PokemonCard createGolduck() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Rayo Aurora", Type.WATER, 2, 60));
        return withType(new PokemonCard("Golduck", "26", 90, 1, attacks, false, 2, "Psyduck"), Type.WATER);
    }

    // ---------- TIPO ELÉCTRICO ----------
    public static PokemonCard createElectabuzz() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Puño Trueno", Type.ELECTRIC, 2, 40));
        return withType(new PokemonCard("Electabuzz", "27", 80, 1, attacks, false), Type.ELECTRIC);
    }

    // ---------- TIPO PSÍQUICO ----------
    public static PokemonCard createGastly() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Lengüetazo", Type.PSYCHIC, 1, 10));
        return withType(new PokemonCard("Gastly", "28", 50, 1, attacks, false), Type.PSYCHIC);
    }

    public static PokemonCard createHaunter() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Tinieblas", Type.PSYCHIC, 2, 40));
        return withType(new PokemonCard("Haunter", "29", 70, 1, attacks, false, 2, "Gastly"), Type.PSYCHIC);
    }

    public static PokemonCard createGengar() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Bola Sombra", Type.PSYCHIC, 3, 80));
        return withType(new PokemonCard("Gengar", "30", 130, 2, attacks, false, 3, "Haunter"), Type.PSYCHIC);
    }

    private static PokemonCard withType(PokemonCard pokemon, Type type) {
        pokemon.setType(type);
        return pokemon;
    }

    public static ArrayList<PokemonCard> createAllPokemonCards() {
        ArrayList<PokemonCard> cards = new ArrayList<>();
        cards.add(createPikachu());
        cards.add(createRaichu());
        cards.add(createCharmander());
        cards.add(createCharmeleon());
        cards.add(createCharizard());
        cards.add(createSquirtle());
        cards.add(createWartortle());
        cards.add(createBlastoise());
        cards.add(createJigglypuff());
        cards.add(createWigglytuff());
        cards.add(createMagikarp());
        cards.add(createGyarados());
        cards.add(createSnorlax());
        cards.add(createMewtwoEx());
        cards.add(createZapdosEx());
        cards.add(createMoltresEx());
        cards.add(createArticunoEx());
        cards.add(createGrowlithe());
        cards.add(createArcanine());
        cards.add(createPsyduck());
        cards.add(createGolduck());
        cards.add(createElectabuzz());
        cards.add(createGastly());
        cards.add(createHaunter());
        cards.add(createGengar());
        return cards;
    }

    // ---------- Lineas de evolucion ----------

    public static ArrayList<PokemonCard> createPikachuEvolutionLine() {
        ArrayList<PokemonCard> evolutionLine = new ArrayList<>();
        evolutionLine.add(createPikachu());
        evolutionLine.add(createRaichu());
        return evolutionLine;
    }

    public static ArrayList<PokemonCard> createCharmanderEvolutionLine() {
        ArrayList<PokemonCard> evolutionLine = new ArrayList<>();
        evolutionLine.add(createCharmander());
        evolutionLine.add(createCharmeleon());
        evolutionLine.add(createCharizard());
        return evolutionLine;
    }

    public static ArrayList<PokemonCard> createSquirtleEvolutionLine() {
        ArrayList<PokemonCard> evolutionLine = new ArrayList<>();
        evolutionLine.add(createSquirtle());
        evolutionLine.add(createWartortle());
        evolutionLine.add(createBlastoise());
        return evolutionLine;
    }

    public static ArrayList<PokemonCard> createJigglypuffEvolutionLine() {
        ArrayList<PokemonCard> evolutionLine = new ArrayList<>();
        evolutionLine.add(createJigglypuff());
        evolutionLine.add(createWigglytuff());
        return evolutionLine;
    }

    public static ArrayList<PokemonCard> createMagikarpEvolutionLine() {
        ArrayList<PokemonCard> evolutionLine = new ArrayList<>();
        evolutionLine.add(createMagikarp());
        evolutionLine.add(createGyarados());
        return evolutionLine;
    }
    public static ArrayList<PokemonCard> createGrowlitheEvolutionLine() {
        ArrayList<PokemonCard> evolutionLine = new ArrayList<>();
        evolutionLine.add(createGrowlithe());
        evolutionLine.add(createArcanine());
        return evolutionLine;
    }

    public static ArrayList<PokemonCard> createPsyduckEvolutionLine() {
        ArrayList<PokemonCard> evolutionLine = new ArrayList<>();
        evolutionLine.add(createPsyduck());
        evolutionLine.add(createGolduck());
        return evolutionLine;
    }

    public static ArrayList<PokemonCard> createGastlyEvolutionLine() {
        ArrayList<PokemonCard> evolutionLine = new ArrayList<>();
        evolutionLine.add(createGastly());
        evolutionLine.add(createHaunter());
        evolutionLine.add(createGengar());
        return evolutionLine;
    }
}
