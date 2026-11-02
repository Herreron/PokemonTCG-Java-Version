package logic.cards;

public enum Type{
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


public static final int WEAKNESS_BONUS = 20;

public boolean isWeakTo(Type attacker) {
    return switch (this) {
        case WATER -> attacker == FIRE || attacker == ELECTRIC;
        case FIRE -> attacker == WATER;
        case COLORLESS -> attacker == PSYCHIC;
        default -> false;
    };
}
}