package logic.cards;

import java.util.ArrayList;

public class PokemonCard extends AbstractCard {

    private int healthPoints;
    private int retreatCost;
    private ArrayList<Attack> attacks;
    private boolean isEx;
    private int attachedEnergy;

    public PokemonCard(String name, int cardNumber, int healthPoints, int retreatCost,
            ArrayList<Attack> attacks, boolean isEx) {
        super(name, cardNumber);
        this.healthPoints = healthPoints;
        this.retreatCost = retreatCost;
        this.attacks = attacks;
        this.isEx = isEx;
        this.attachedEnergy = 0;
    }

    // getters
    public int getHealthPoints() {
        return healthPoints;
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

    public void setAttachedEnergy(int attachedEnergy) {
        this.attachedEnergy = attachedEnergy;
    }

    // methods
    public void receiveDamage(int amount) {
        healthPoints = Math.max(0, healthPoints - amount);
    }

    public boolean isDefeated() {
        return healthPoints <= 0;
    }

    public int calculatePrizePoints() {
        return isEx ? 2 : 1;
    }
}