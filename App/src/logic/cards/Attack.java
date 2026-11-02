package logic.cards;

public class Attack {

    private final String name;
    private final int energyCost;
    private final int damage;
    private final String energyType;

    public Attack(String name, int energyCost, int damage, String energyType) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El ataque debe tener nombre.");
        }
        if (energyCost < 0) {
            throw new IllegalArgumentException("El costo de energia no puede ser negativo.");
        }
        if (damage < 0) {
            throw new IllegalArgumentException("El dano no puede ser negativo.");
        }
        if (energyType == null || energyType.isBlank()) {
            throw new IllegalArgumentException("El tipo de energia no puede ser nulo o vacío.");
        }
        this.name = name;
        this.energyCost = energyCost;
        this.damage = damage;
        this.energyType = energyType;
    }

    public String getName() {
        return name;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getDamage() {
        return damage;
    }
    public String getEnergyType() {
        return energyType;
    }

    @Override
    public String toString() {
        return name + " (" + energyCost + " " + energyType + "): " + damage + " dano";
    }
}
