package logic.cards;

import java.util.ArrayList;

public final class ObjectCatalog {

    private ObjectCatalog() {
    }

    public static ObjectCard createPotion() {
        return new ObjectCard("Pocion", 11);
    }

    public static ObjectCard createPokeBall() {
        return new ObjectCard("Poke Ball", 12);
    }

    public static ObjectCard createSpeedBoost() {
        return new ObjectCard("Turbo Velocidad", 13);
    }

    public static ArrayList<ObjectCard> createAllObjectCards() {
        ArrayList<ObjectCard> cards = new ArrayList<>();
        cards.add(createPotion());
        cards.add(createPokeBall());
        cards.add(createSpeedBoost());
        return cards;
    }
}


