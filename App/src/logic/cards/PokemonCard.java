package logic.cards;

import java.util.ArrayList;

public class PokemonCard extends AbstractCard {

private short healthPoints;
private final short maxHealthPoints;
private byte retreatCost;
private ArrayList<Attack> attacks;
private boolean isEx;
private byte attachedEnergy;
private final byte phase;
private final String evolvesFrom;
private Type type = Type.COLORLESS;
private String imagePath;
private String audioPath;

// Constructors
public PokemonCard(String name, short healthPoints, byte retreatCost,
ArrayList<Attack> attacks, boolean isEx) {
this(name, healthPoints, retreatCost, attacks, isEx, (byte) 1, null);
    }

public PokemonCard(String name, short healthPoints, byte retreatCost,
ArrayList<Attack> attacks, boolean isEx, byte phase, String evolvesFrom) {
super(name, "Pokemon");
this.healthPoints = (short) Math.max(0, healthPoints);
this.maxHealthPoints = this.healthPoints;
this.retreatCost = (byte) Math.max(0, retreatCost);
if (attacks == null) {
    this.attacks = new ArrayList<>();
} else {
    this.attacks = attacks;
}
this.isEx = isEx;
this.attachedEnergy = 0;
this.phase = phase >= 1 && phase <= 3 ? phase : (byte) 1;
if (this.phase == 1) {
this.evolvesFrom = null;
} else {
this.evolvesFrom = evolvesFrom;
}
refreshMediaPaths();

    }

// Constructor con tipo: usa el constructor de arriba y despues guarda el tipo.
// Los otros dos constructores siguen dejando COLORLESS por defecto.
public PokemonCard(String name, short healthPoints, byte retreatCost,
ArrayList<Attack> attacks, boolean isEx, byte phase, String evolvesFrom, Type type) {
this(name, healthPoints, retreatCost, attacks, isEx, phase, evolvesFrom);
setType(type);
    }


public PokemonCard() { 
    super("","Pokemon");
    this.healthPoints = 0;
    this.maxHealthPoints = 0;
    this.retreatCost = 0;
    this.attacks = new ArrayList<>();
    this.isEx = false;
    this.attachedEnergy = 0;
    this.phase = 1;
    this.evolvesFrom = null;
    this.type = Type.COLORLESS;
}



// getters
public short getHealthPoints() {
return healthPoints;
    }

public short getMaxHealthPoints() {
return maxHealthPoints;
    }

public byte getRetreatCost() {
return retreatCost;
    }

public ArrayList<Attack> getAttacks() {
return attacks;
    }

public boolean isEx() {
return isEx;
    }

public byte getAttachedEnergy() {
return attachedEnergy;
    }

public byte getPhase() {
return phase;
    }

public String getEvolvesFrom() {
return evolvesFrom;
    }

public Type getType() {
return type;
    }

public void setType(Type type) {
if (type != null) {
    this.type = type;
    refreshMediaPaths();
}
    }

public String getImagePath() {
return imagePath;
    }

public String getAudioPath() {
return audioPath;
    }

public boolean isBasicPokemon() {
return phase == 1;
    }

public void setAttachedEnergy(byte attachedEnergy) {
this.attachedEnergy = (byte) Math.max(0, attachedEnergy);
    }

// methods
public void receiveDamage(short amount) {
if (amount > 0) {
    healthPoints = (short) Math.max(0, healthPoints - amount);
}
    }

public void receiveDamage(short amount, Type attackerType) {
int total = amount;
if (attackerType != null && type.isWeakTo(attackerType)) {
total += Type.WEAKNESS_BONUS;
        }
receiveDamage((short) Math.min(Short.MAX_VALUE, total));
    }

public void heal(short amount) {
if (amount > 0) {
    healthPoints = (short) Math.min(maxHealthPoints, healthPoints + amount);
}
    }

public boolean isDefeated() {
return healthPoints <= 0;
    }

public byte calculatePrizePoints() {
return isEx ? (byte) 2 : (byte) 1;
    }

private void refreshMediaPaths() {
if (getName() == null || getName().isEmpty()) {
    imagePath = null;
    audioPath = null;
    return;
}

String folder;
switch (type) {
    case FIRE:
        folder = "fire";
        break;
    case WATER:
        folder = "water";
        break;
    case ELECTRIC:
        folder = "electric";
        break;
    case PSYCHIC:
        folder = "psico";
        break;
    default:
        folder = "uncolored";
        break;
}

String fileName = getName().toLowerCase().replace(" ex", "");
imagePath = "/media/images/pokemons/" + folder + "/" + fileName + ".png";

int pokedexNumber = getPokedexNumber(getName());
if (pokedexNumber > 0) {
    audioPath = String.format("/media/audio/screams/%03d%s.wav", pokedexNumber, fileName);
} else {
    audioPath = null;
}
    }

private int getPokedexNumber(String pokemonName) {
switch (pokemonName) {
    case "Pikachu":
        return 25;
    case "Raichu":
        return 26;
    case "Charmander":
        return 4;
    case "Charmeleon":
        return 5;
    case "Squirtle":
        return 7;
    case "Wartortle":
        return 8;
    case "Blastoise":
        return 9;
    case "Jigglypuff":
        return 39;
    case "Wigglytuff":
        return 40;
    case "Magikarp":
        return 129;
    case "Gyarados":
        return 130;
    case "Snorlax":
        return 143;
    case "Mewtwo ex":
        return 150;
    case "Zapdos ex":
        return 145;
    case "Moltres ex":
        return 146;
    case "Articuno ex":
        return 144;
    case "Growlithe":
        return 58;
    case "Arcanine":
        return 59;
    case "Psyduck":
        return 54;
    case "Golduck":
        return 55;
    case "Electabuzz":
        return 125;
    case "Gastly":
        return 92;
    case "Haunter":
        return 93;
    case "Gengar":
        return 94;
    default:
        return 0;
}
    }

    @Override
public String toString() {
StringBuilder description = new StringBuilder();
description.append(getName())
                .append(" (Fase ").append(phase)
                .append(", ").append(healthPoints).append('/').append(maxHealthPoints).append(" PS");
if (evolvesFrom != null) {
description.append(", evoluciona de ").append(evolvesFrom);
        }
description.append(')');
return description.toString();
    }
}