//carpetas
package logic.cards;
//funciones de java
import java.util.ArrayList;

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
if (phase < 1 || phase > 3) {
throw new IllegalArgumentException("La fase debe estar entre 1 y 3.");
        }
if (phase == 1 && evolvesFrom != null) {
throw new IllegalArgumentException("Un Pokemon basico no puede evolucionar de otro Pokemon.");
        }
if (phase > 1 && (evolvesFrom == null || evolvesFrom.isBlank())) {
throw new IllegalArgumentException("Un Pokemon evolucionado debe indicar de que Pokemon evoluciona.");
        }

this.healthPoints = healthPoints;
this.maxHealthPoints = healthPoints;
this.retreatCost = retreatCost;
this.attacks = attacks;
this.isEx = isEx;
this.attachedEnergy = 0;
this.phase = phase;
this.evolvesFrom = evolvesFrom;

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
this.type = type;
    }

public boolean isBasicPokemon() {
return phase == 1;
    }

public void setAttachedEnergy(int attachedEnergy) {
this.attachedEnergy = attachedEnergy;
    }

// methods
public void receiveDamage(int amount) {
healthPoints = Math.max(0, healthPoints - amount);
    }

public void receiveDamage(int amount, Type attackerType) {
int total = amount;
if (isWeakTo(attackerType)) {
total += 20;
        }
receiveDamage(total);
    }

private boolean isWeakTo(Type attackerType) {
switch (type) {
case WATER:
return attackerType == Type.FIRE || attackerType == Type.ELECTRIC;
case FIRE:
return attackerType == Type.WATER;
case COLORLESS:
return attackerType == Type.PSYCHIC;
default:
return false;
        }
    }

public boolean isDefeated() {
return healthPoints <= 0;
    }

public int calculatePrizePoints() {
return isEx ? 2 : 1;
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