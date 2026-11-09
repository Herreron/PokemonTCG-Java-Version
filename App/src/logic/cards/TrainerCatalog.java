package logic.cards;

import java.util.ArrayList;

public final class TrainerCatalog {

    private TrainerCatalog() {
    }

    public static TrainerCard createProfessorResearch() {
        return new TrainerCard("Investigacion del Profesor", "Soporte");
    }

    public static TrainerCard createMisty() {
        return new TrainerCard("Misty", "Soporte");
    }

    public static ArrayList<TrainerCard> createAllTrainerCards() {
        ArrayList<TrainerCard> cards = new ArrayList<>();
        cards.add(createProfessorResearch());
        cards.add(createMisty());
        return cards;
    }

    public static TrainerCard createCardByName(String cardName) {
        if ("Investigacion del Profesor".equals(cardName)) {
            return createProfessorResearch();
        }
        if ("Misty".equals(cardName)) {
            return createMisty();
        }
        return null;
    }
}
