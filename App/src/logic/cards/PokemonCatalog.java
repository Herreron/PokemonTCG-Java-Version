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
            throw new IllegalArgumentException("El nombre de la carta no puede ser null.");
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
            default:
                throw new IllegalArgumentException("No existe una carta Pokemon llamada: " + cardName);
        }
    }

    // ---------- Pokemon del PDF (numeros 1 a 10) ----------

    public static PokemonCard createPikachu() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Mordisco", Type.ELECTRIC, 1, 20));
        return new PokemonCard("Pikachu", 1, 60, 1, attacks, false);
    }

    public static PokemonCard createCharmander() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Ascuas", Type.FIRE, 1, 30));
        return new PokemonCard("Charmander", 2, 60, 1, attacks, false);
    }

    public static PokemonCard createSquirtle() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Placaje", Type.WATER, 1, 20));
        return new PokemonCard("Squirtle", 3, 60, 1, attacks, false);
    }

    public static PokemonCard createJigglypuff() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Golpe", Type.COLORLESS, 1, 20));
        return new PokemonCard("Jigglypuff", 4, 70, 1, attacks, false);
    }

    public static PokemonCard createMagikarp() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Chapoteo", Type.WATER, 1, 10));
        return new PokemonCard("Magikarp", 5, 30, 1, attacks, false);
    }

    public static PokemonCard createSnorlax() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Golpe Cuerpo", Type.COLORLESS, 3, 100));
        return new PokemonCard("Snorlax", 6, 150, 3, attacks, false);
    }

    public static PokemonCard createMewtwoEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Ataque Psiquico", Type.PSYCHIC, 2, 50));
        attacks.add(new Attack("Psicorrayo", Type.PSYCHIC, 3, 150));
        return new PokemonCard("Mewtwo ex", 7, 150, 2, attacks, true);
    }

    public static PokemonCard createZapdosEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Picotazo", Type.COLORLESS, 1, 30));
        attacks.add(new Attack("Tormenta Electrica", Type.ELECTRIC, 3, 100));
        return new PokemonCard("Zapdos ex", 8, 130, 2, attacks, true);
    }

    public static PokemonCard createMoltresEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Aleteo", Type.COLORLESS, 1, 30));
        attacks.add(new Attack("Danza Infernal", Type.FIRE, 3, 70));
        return new PokemonCard("Moltres ex", 9, 130, 2, attacks, true);
    }

    public static PokemonCard createArticunoEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Viento Helado", Type.WATER, 1, 30));
        attacks.add(new Attack("Ventisca", Type.WATER, 3, 100));
        return new PokemonCard("Articuno ex", 10, 130, 2, attacks, true);
    }

    // ---------- Evoluciones (numeros 16 a 22, datos de las cartas reales) ----------

    public static PokemonCard createRaichu() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Rayo", Type.ELECTRIC, 3, 140));
        return new PokemonCard("Raichu", 16, 100, 1, attacks, false, 2, "Pikachu");
    }

    public static PokemonCard createCharmeleon() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Garras de Fuego", Type.FIRE, 3, 60));
        return new PokemonCard("Charmeleon", 17, 90, 2, attacks, false, 2, "Charmander");
    }

    public static PokemonCard createCharizard() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Giro Fuego", Type.FIRE, 4, 150));
        return new PokemonCard("Charizard", 18, 150, 2, attacks, false, 3, "Charmeleon");
    }

    public static PokemonCard createWartortle() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Salpicadura", Type.WATER, 2, 40));
        return new PokemonCard("Wartortle", 19, 80, 1, attacks, false, 2, "Squirtle");
    }

    public static PokemonCard createBlastoise() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Hidrobomba", Type.WATER, 3, 80));
        return new PokemonCard("Blastoise", 20, 150, 3, attacks, false, 3, "Wartortle");
    }

    public static PokemonCard createWigglytuff() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Vozarron", Type.COLORLESS, 2, 60));
        return new PokemonCard("Wigglytuff", 21, 100, 1, attacks, false, 2, "Jigglypuff");
    }

    public static PokemonCard createGyarados() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Hiperrayo", Type.WATER, 4, 100));
        return new PokemonCard("Gyarados", 22, 150, 4, attacks, false, 2, "Magikarp");
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
}
