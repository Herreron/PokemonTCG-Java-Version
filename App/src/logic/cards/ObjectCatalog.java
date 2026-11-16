package logic.cards;

import java.util.ArrayList;

public final class ObjectCatalog {

    private ObjectCatalog() {
    }

    public static ObjectCard createPotion() {
        return new ObjectCard("Pocion");
    }

    public static ObjectCard createPokeBall() {
        return new ObjectCard("Poke Ball");
    }

    public static ObjectCard createSpeedBoost() {
        return new ObjectCard("Turbo Velocidad");
    }

    public static ArrayList<ObjectCard> createAllObjectCards() {
        ArrayList<ObjectCard> cards = new ArrayList<>();
        cards.add(createPotion());
        cards.add(createPokeBall());
        cards.add(createSpeedBoost());
        return cards;
    }

    public static ObjectCard createCardByName(String cardName) {
        if ("Pocion".equals(cardName)) {
            return createPotion();
        }
        if ("Poke Ball".equals(cardName)) {
            return createPokeBall();
        }
        if ("Turbo Velocidad".equals(cardName)) {
            return createSpeedBoost();
        }
        return null;
    }
}
