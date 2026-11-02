//carpetas
package logic.battlesystem;

//funciones de java
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
//importes de clases
import logic.cards.AbstractCard;
import logic.cards.PokemonCard;
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

    public static Deck createRandomDeck(List<? extends Supplier<? extends AbstractCard>> cardDefinitions) {
        if (cardDefinitions == null) {
            throw new IllegalArgumentException("La lista de definiciones de cartas no puede ser null.");
        }

        ArrayList<CardCopies> definitions = new ArrayList<>();
        Set<String> cardNames = new HashSet<>();

        for (Supplier<? extends AbstractCard> cardDefinition : cardDefinitions) {
            if (cardDefinition == null) {
                throw new IllegalArgumentException("Las definiciones de cartas no pueden ser null.");
            }

            AbstractCard firstCopy = createCard(cardDefinition);
            if (!cardNames.add(firstCopy.getName())) {
                continue;
            }

            AbstractCard secondCopy = createCard(cardDefinition);
            if (firstCopy == secondCopy) {
                throw new IllegalArgumentException(
                        "Cada definicion debe crear instancias independientes de la carta.");
            }
            definitions.add(new CardCopies(firstCopy, secondCopy));
        }

        ArrayList<CardCopies> basicDefinitions = new ArrayList<>();
        for (CardCopies definition : definitions) {
            if (definition.firstCopy instanceof PokemonCard pokemon && pokemon.isBasicPokemon()) {
                basicDefinitions.add(definition);
            }
        }

        if (basicDefinitions.size() < 6 || definitions.size() * 2 < 20) {
            throw new IllegalArgumentException(
                    "El catalogo disponible no permite crear un mazo valido: se requieren al menos "
                            + "6 Pokemon basicos distintos y capacidad para 20 cartas con maximo 2 copias.");
        }

        Collections.shuffle(basicDefinitions);
        ArrayList<AbstractCard> cards = new ArrayList<>();
        Set<AbstractCard> selectedCards = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            AbstractCard basic = basicDefinitions.get(i).firstCopy;
            cards.add(basic);
            selectedCards.add(basic);
        }

        ArrayList<AbstractCard> remainingCopies = new ArrayList<>();
        for (CardCopies definition : definitions) {
            if (!selectedCards.contains(definition.firstCopy)) {
                remainingCopies.add(definition.firstCopy);
            }
            remainingCopies.add(definition.secondCopy);
        }
        Collections.shuffle(remainingCopies);

        int remainingCards = 20 - cards.size();
        if (remainingCopies.size() < remainingCards) {
            throw new IllegalArgumentException("El catalogo no contiene suficientes cartas para completar el mazo.");
        }
        cards.addAll(remainingCopies.subList(0, remainingCards));

        Collections.shuffle(cards);
        return createValidatedDeck(cards);
    }

    private static AbstractCard createCard(Supplier<? extends AbstractCard> cardDefinition) {
        AbstractCard card = cardDefinition.get();
        if (card == null) {
            throw new IllegalArgumentException("Una definicion de carta produjo un valor null.");
        }
        return card;
    }

    private static final class CardCopies {
        private final AbstractCard firstCopy;
        private final AbstractCard secondCopy;

        private CardCopies(AbstractCard firstCopy, AbstractCard secondCopy) {
            this.firstCopy = firstCopy;
            this.secondCopy = secondCopy;
        }
    }
}
