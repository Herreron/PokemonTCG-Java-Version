package logic.cards;

public class Attack {

    private final String name;
    private final Type energyType;
    private final int energyCost;
    private final int baseDamage;

    public Attack(String name, Type energyType, int energyCost, int baseDamage) {
        this.name = name;
        this.energyType = energyType;
        this.energyCost = Math.max(0, energyCost);
        this.baseDamage = Math.max(0, baseDamage);
    }

    public String getName() {
        return name;
    }

    public Type getEnergyType() {
        return energyType;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    @Override
    public String toString() {
        return name + " (" + energyCost + " " + energyType + "): " + baseDamage + " dano";
    }
}