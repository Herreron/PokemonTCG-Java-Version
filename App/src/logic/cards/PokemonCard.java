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
}
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