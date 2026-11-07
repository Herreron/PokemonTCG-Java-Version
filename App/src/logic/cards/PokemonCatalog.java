package logic.cards;

import java.util.ArrayList;

public final class PokemonCatalog {

    private PokemonCatalog() {
    }

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

    public static PokemonCard createPikachu() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Mordisco", Type.ELECTRIC, 1, 20));
        return withType(createPokemon("Pikachu", 60, 1, attacks, false), Type.ELECTRIC);
    }

    public static PokemonCard createCharmander() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Ascuas", Type.FIRE, 1, 30));
        return withType(createPokemon("Charmander", 60, 1, attacks, false), Type.FIRE);
    }

    public static PokemonCard createSquirtle() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Placaje", Type.WATER, 1, 20));
        return withType(createPokemon("Squirtle", 60, 1, attacks, false), Type.WATER);
    }

    public static PokemonCard createJigglypuff() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Golpe", Type.COLORLESS, 1, 20));
        return withType(createPokemon("Jigglypuff", 70, 1, attacks, false), Type.COLORLESS);
    }

    public static PokemonCard createMagikarp() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Chapoteo", Type.WATER, 1, 10));
        return withType(createPokemon("Magikarp", 30, 1, attacks, false), Type.WATER);
    }

    public static PokemonCard createSnorlax() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Golpe Cuerpo", Type.COLORLESS, 3, 100));
        return withType(createPokemon("Snorlax", 150, 3, attacks, false), Type.COLORLESS);
    }

    public static PokemonCard createMewtwoEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Ataque Psiquico", Type.PSYCHIC, 2, 50));
        attacks.add(createAttack("Psicorrayo", Type.PSYCHIC, 3, 150));
        return withType(createPokemon("Mewtwo ex", 150, 2, attacks, true), Type.PSYCHIC);
    }

    public static PokemonCard createZapdosEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Picotazo", Type.COLORLESS, 1, 30));
        attacks.add(createAttack("Tormenta Electrica", Type.ELECTRIC, 3, 100));
        return withType(createPokemon("Zapdos ex", 130, 2, attacks, true), Type.ELECTRIC);
    }

    public static PokemonCard createMoltresEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Aleteo", Type.COLORLESS, 1, 30));
        attacks.add(createAttack("Danza Infernal", Type.FIRE, 3, 70));
        return withType(createPokemon("Moltres ex", 130, 2, attacks, true), Type.FIRE);
    }

    public static PokemonCard createArticunoEx() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Viento Helado", Type.WATER, 1, 30));
        attacks.add(createAttack("Ventisca", Type.WATER, 3, 100));
        return withType(createPokemon("Articuno ex", 130, 2, attacks, true), Type.WATER);
    }

    public static PokemonCard createRaichu() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Rayo", Type.ELECTRIC, 3, 140));
        return withType(createPokemon("Raichu", 100, 1, attacks, false, 2, "Pikachu"), Type.ELECTRIC);
    }

    public static PokemonCard createCharmeleon() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Garras de Fuego", Type.FIRE, 3, 60));
        return withType(createPokemon("Charmeleon", 90, 2, attacks, false, 2, "Charmander"), Type.FIRE);
    }

    public static PokemonCard createCharizard() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Giro Fuego", Type.FIRE, 4, 150));
        return withType(createPokemon("Charizard", 150, 2, attacks, false, 3, "Charmeleon"), Type.FIRE);
    }

    public static PokemonCard createWartortle() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Salpicadura", Type.WATER, 2, 40));
        return withType(createPokemon("Wartortle", 80, 1, attacks, false, 2, "Squirtle"), Type.WATER);
    }

    public static PokemonCard createBlastoise() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Hidrobomba", Type.WATER, 3, 80));
        return withType(createPokemon("Blastoise", 150, 3, attacks, false, 3, "Wartortle"), Type.WATER);
    }

    public static PokemonCard createWigglytuff() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Vozarron", Type.COLORLESS, 2, 60));
        return withType(createPokemon("Wigglytuff", 100, 1, attacks, false, 2, "Jigglypuff"), Type.COLORLESS);
    }

    public static PokemonCard createGyarados() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Hiperrayo", Type.WATER, 4, 100));
        return withType(createPokemon("Gyarados", 150, 4, attacks, false, 2, "Magikarp"), Type.WATER);
    }
    public static PokemonCard createGrowlithe() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Mordisco", Type.COLORLESS, 1, 10));
        return withType(createPokemon("Growlithe", 70, 1, attacks, false), Type.FIRE);
    }

    public static PokemonCard createArcanine() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Lanzallamas", Type.FIRE, 3, 90));
        // Fase 2 (1 evolución), evoluciona de Growlithe
        return withType(createPokemon("Arcanine", 130, 2, attacks, false, 2, "Growlithe"), Type.FIRE);
    }

    // ---------- TIPO AGUA ----------
    public static PokemonCard createPsyduck() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Dolor de Cabeza", Type.WATER, 1, 20));
        return withType(createPokemon("Psyduck", 60, 1, attacks, false), Type.WATER);
    }

    public static PokemonCard createGolduck() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Rayo Aurora", Type.WATER, 2, 60));
        return withType(createPokemon("Golduck", 90, 1, attacks, false, 2, "Psyduck"), Type.WATER);
    }

    // ---------- TIPO ELÉCTRICO ----------
    public static PokemonCard createElectabuzz() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Puño Trueno", Type.ELECTRIC, 2, 40));
        return withType(createPokemon("Electabuzz", 80, 1, attacks, false), Type.ELECTRIC);
    }

    // ---------- TIPO PSÍQUICO ----------
    public static PokemonCard createGastly() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Lengüetazo", Type.PSYCHIC, 1, 10));
        return withType(createPokemon("Gastly", 50, 1, attacks, false), Type.PSYCHIC);
    }

    public static PokemonCard createHaunter() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Tinieblas", Type.PSYCHIC, 2, 40));
        return withType(createPokemon("Haunter", 70, 1, attacks, false, 2, "Gastly"), Type.PSYCHIC);
    }

    public static PokemonCard createGengar() {
        ArrayList<Attack> attacks = new ArrayList<>();
        attacks.add(createAttack("Bola Sombra", Type.PSYCHIC, 3, 80));
        return withType(createPokemon("Gengar", 130, 2, attacks, false, 3, "Haunter"), Type.PSYCHIC);
    }

    private static Attack createAttack(String name, Type type, int energyCost, int baseDamage) {
        if (energyCost < 0 || energyCost > Byte.MAX_VALUE
                || baseDamage < 0 || baseDamage > Short.MAX_VALUE) {
            throw new IllegalArgumentException("El costo o el dano del ataque esta fuera de rango.");
        }
        return new Attack(name, type, (byte) energyCost, (short) baseDamage);
    }

    private static PokemonCard createPokemon(String name, int healthPoints, int retreatCost,
            ArrayList<Attack> attacks, boolean isEx) {
        return createPokemon(name, healthPoints, retreatCost, attacks, isEx, 1, null);
    }

    private static PokemonCard createPokemon(String name, int healthPoints, int retreatCost,
            ArrayList<Attack> attacks, boolean isEx, int phase, String evolvesFrom) {
        if (healthPoints < 0 || healthPoints > Short.MAX_VALUE
                || retreatCost < 0 || retreatCost > Byte.MAX_VALUE
                || phase < 1 || phase > 3) {
            throw new IllegalArgumentException("Los atributos numericos del Pokemon estan fuera de rango.");
        }
        return new PokemonCard(name, (short) healthPoints, (byte) retreatCost, attacks, isEx,
                (byte) phase, evolvesFrom);
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
