package logic.cards;

public class Attack {

    private final String name;
    private final Type energyType;
    private final byte energyCost;
    private final short baseDamage;

    public Attack(String name, Type energyType, byte energyCost, short baseDamage) {
        this.name = name;
        this.energyType = energyType;
        this.energyCost = (byte) Math.max(0, energyCost);
        this.baseDamage = (short) Math.max(0, baseDamage);
    }

    public String getName() {
        return name;
    }

    public Type getEnergyType() {
        return energyType;
    }

    public byte getEnergyCost() {
        return energyCost;
    }

    public short getBaseDamage() {
        return baseDamage;
    }

    @Override
    public String toString() {
        return name + " (" + energyCost + " " + energyType + "): " + baseDamage + " dano";
    }
}