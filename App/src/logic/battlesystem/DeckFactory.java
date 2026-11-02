package logic.battlesystem;

import java.util.ArrayList;
import java.util.List;
import logic.cards.AbstractCard;

public final class DeckFactory {

    private DeckFactory() {
    }

    public static Deck createValidatedDeck(List<? extends AbstractCard> cards) {
        if (cards == null) {
            throw new IllegalArgumentException("La lista de cartas no puede ser null.");
        }

        Deck deck = new Deck(new ArrayList<>(cards));
        if (!deck.validateDeck()) {
            throw new IllegalArgumentException(
                    "El mazo debe tener 20 cartas, al menos 6 Pokemon basicos y maximo 2 copias por carta.");
        }
        return deck;
    }
}
