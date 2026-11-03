package logic.cards;

import java.util.ArrayList;
import logic.battlesystem.Game;
import logic.battlesystem.Player;

public class PokemonCard extends AbstractCard {

private int healthPoints;
private final int maxHealthPoints;
private int retreatCost;
private ArrayList<Attack> attacks;
private boolean isEx;
private int attachedEnergy;
private final int phase;
private final String evolvesFrom;
private Type type = Type.COLORLESS;

// Constructors
public PokemonCard(String name, int cardNumber, int healthPoints, int retreatCost,
ArrayList<Attack> attacks, boolean isEx) {
this(name, cardNumber, healthPoints, retreatCost, attacks, isEx, 1, null);
    }

public PokemonCard(String name, int cardNumber, int healthPoints, int retreatCost,
ArrayList<Attack> attacks, boolean isEx, int phase, String evolvesFrom) {
super(name, cardNumber);
this.healthPoints = Math.max(0, healthPoints);
this.maxHealthPoints = this.healthPoints;
this.retreatCost = Math.max(0, retreatCost);
if (attacks == null) {
    this.attacks = new ArrayList<>();
} else {
    this.attacks = attacks;
}
this.isEx = isEx;
this.attachedEnergy = 0;
this.phase = phase >= 1 && phase <= 3 ? phase : 1;
if (this.phase == 1) {
this.evolvesFrom = null;
} else {
this.evolvesFrom = evolvesFrom;
}

    }

// getters
public int getHealthPoints() {
return healthPoints;
    }

public int getMaxHealthPoints() {
return maxHealthPoints;
    }

public int getRetreatCost() {
return retreatCost;
    }

public ArrayList<Attack> getAttacks() {
return attacks;
    }

public boolean isEx() {
return isEx;
    }

public int getAttachedEnergy() {
return attachedEnergy;
    }

public int getPhase() {
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

public void setAttachedEnergy(int attachedEnergy) {
this.attachedEnergy = Math.max(0, attachedEnergy);
    }

// methods
public void receiveDamage(int amount) {
if (amount > 0) {
    healthPoints = Math.max(0, healthPoints - amount);
}
    }

public void receiveDamage(int amount, Type attackerType) {
int total = amount;
if (attackerType != null && type.isWeakTo(attackerType)) {
total += Type.WEAKNESS_BONUS;
        }
receiveDamage(total);
    }

public void heal(int amount) {
if (amount > 0) {
    healthPoints = Math.min(maxHealthPoints, healthPoints + amount);
}
    }

public boolean isDefeated() {
return healthPoints <= 0;
    }

public int calculatePrizePoints() {
return isEx ? 2 : 1;
    }

    @Override
public boolean activateEffect(Game game, Player player) {
    return false;
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