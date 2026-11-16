package logic.cards;

public enum Type {
    GRASS,
    FIRE,
    WATER,
    ELECTRIC,
    PSYCHIC,
    FIGHTING,
    DARKNESS,
    METAL,
    DRAGON,
    COLORLESS;

    public static final short WEAKNESS_BONUS = 20;

    ///Indica si el tipo de este Pokemon es debil al tipo del ataque recibido.
    public boolean isWeakTo(Type attacker) {
        switch (this) {
            case WATER:
                return attacker == GRASS || attacker == ELECTRIC;
            case FIRE:
                return attacker == WATER;
            case GRASS:
                return attacker == FIRE;
            case COLORLESS:
                return attacker == FIGHTING;
            case FIGHTING:
                return attacker == PSYCHIC;
            case DARKNESS:
                return attacker == FIGHTING;
            case METAL:
                return attacker == FIRE || attacker == FIGHTING;
            case DRAGON:
                return attacker == DRAGON;
            case ELECTRIC:
                return attacker == FIGHTING;
            case PSYCHIC:
                return attacker == DARKNESS;
            default:
                return false;
        }
    }
}