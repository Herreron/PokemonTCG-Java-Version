package logic.battlesystem;

import java.util.ArrayList;
import logic.cards.AbstractCard;
import logic.cards.ObjectCatalog;
import logic.cards.ObjectCard;
import logic.cards.PokemonCard;
import logic.cards.PokemonCatalog;
import logic.cards.TrainerCard;
import logic.cards.TrainerCatalog;

public final class DeckFactory {

    private DeckFactory() {
    }

    public static Deck createValidatedDeck(ArrayList<AbstractCard> cards) {
        if (cards == null) {
            return null;
        }

        Deck deck = new Deck(new ArrayList<>(cards));
        if (!deck.validateDeck()) {
            return null;
        }
        return deck;
    }

    
    public static Deck createRandomDeck(ArrayList<String> availableCardNames) {
        if (availableCardNames == null) {
            return null;
        }

        ArrayList<AbstractCard> firstCopies = new ArrayList<>();
        ArrayList<AbstractCard> secondCopies = new ArrayList<>();
        ArrayList<AbstractCard> basicCopies = new ArrayList<>();
        ArrayList<String> processedNames = new ArrayList<>();

        for (String cardName : availableCardNames) {
            if (cardName == null || cardName.trim().isEmpty()) {
                return null;
            }
            if (processedNames.contains(cardName)) {
                continue;
            }
            processedNames.add(cardName);

            AbstractCard firstCopy = createCardByName(cardName);
            AbstractCard secondCopy = createCardByName(cardName);
            if (firstCopy == null || secondCopy == null) {
                return null;
            }
            
            // Garantiza que el catálogo creó dos objetos diferentes en memoria
            if (firstCopy == secondCopy) {
                return null;
            }

            firstCopies.add(firstCopy);
            secondCopies.add(secondCopy);

            if (firstCopy instanceof PokemonCard) {
                PokemonCard pokemon = (PokemonCard) firstCopy;
                if (pokemon.isBasicPokemon()) {
                    basicCopies.add(firstCopy);
                    basicCopies.add(secondCopy);
                }
            }
        }

        // Verifica que cumpla con los requisitos mínimos del RF4 antes de mezclar
        if (basicCopies.size() < 6 || firstCopies.size() + secondCopies.size() < 20) {
            return null;
        }

        // Se mezclan los Pokemon basicos con intercambios aleatorios clasicos.
        for (int i = basicCopies.size() - 1; i > 0; i--) {
            int randomIndex = (int) (Math.random() * (i + 1));
            AbstractCard currentCard = basicCopies.get(i);
            basicCopies.set(i, basicCopies.get(randomIndex));
            basicCopies.set(randomIndex, currentCard);
        }
        ArrayList<AbstractCard> deckCards = new ArrayList<>();
        
        // Aseguramos primero los 6 Pokémon básicos
        for (int i = 0; i < 6; i++) {
            AbstractCard basic = basicCopies.get(i);
            deckCards.add(basic);
            firstCopies.remove(basic);
            secondCopies.remove(basic);
        }

        // Rellenamos el resto hasta llegar a 20 cartas
        ArrayList<AbstractCard> remainingCopies = new ArrayList<>(firstCopies);
        remainingCopies.addAll(secondCopies);
        for (int i = remainingCopies.size() - 1; i > 0; i--) {
            int randomIndex = (int) (Math.random() * (i + 1));
            AbstractCard currentCard = remainingCopies.get(i);
            remainingCopies.set(i, remainingCopies.get(randomIndex));
            remainingCopies.set(randomIndex, currentCard);
        }
        int cardsNeeded = 20 - deckCards.size();
        for (int i = 0; i < cardsNeeded; i++) {
            deckCards.add(remainingCopies.get(i));
        }
        for (int i = deckCards.size() - 1; i > 0; i--) {
            int randomIndex = (int) (Math.random() * (i + 1));
            AbstractCard currentCard = deckCards.get(i);
            deckCards.set(i, deckCards.get(randomIndex));
            deckCards.set(randomIndex, currentCard);
        }

        // Retornará el Deck válido, o null si falla la validación final
        return createValidatedDeck(deckCards);
    }

    private static AbstractCard createCardByName(String cardName) {
        AbstractCard card = PokemonCatalog.createCardByName(cardName);
        if (card != null) {
            return card;
        }
        ObjectCard objectCard = ObjectCatalog.createCardByName(cardName);
        if (objectCard != null) {
            return objectCard;
        }
        TrainerCard trainerCard = TrainerCatalog.createCardByName(cardName);
        return trainerCard;
    }
}