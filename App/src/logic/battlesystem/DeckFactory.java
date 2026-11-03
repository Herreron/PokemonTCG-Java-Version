package logic.battlesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import logic.cards.AbstractCard;
import logic.cards.PokemonCard;
import logic.cards.PokemonCatalog;

public final class DeckFactory {

    private DeckFactory() {
    }

    public static Deck createValidatedDeck(List<? extends AbstractCard> cards) {
        if (cards == null) {
            return null; // En lugar de Optional.empty()
        }

        Deck deck = new Deck(new ArrayList<>(cards));
        if (!deck.validateDeck()) {
            return null; // En lugar de Optional.empty()
        }
        return deck; // Retornamos el objeto real en lugar de envolverlo en Optional.of()
    }

    
    public static Deck createRandomDeck(List<String> availableCardNames) {
        if (availableCardNames == null) {
            return null; // En lugar de Optional.empty()
        }

        ArrayList<AbstractCard> firstCopies = new ArrayList<>();
        ArrayList<AbstractCard> secondCopies = new ArrayList<>();
        ArrayList<AbstractCard> basicCopies = new ArrayList<>();
        ArrayList<String> processedNames = new ArrayList<>();

        for (String cardName : availableCardNames) {
            if (cardName == null || cardName.isBlank()) {
                return null;
            }
            if (processedNames.contains(cardName)) {
                continue;
            }
            processedNames.add(cardName);

            if (!isCardAvailable(cardName)) {
                return null;
            }

            AbstractCard firstCopy = PokemonCatalog.createCardByName(cardName);
            AbstractCard secondCopy = PokemonCatalog.createCardByName(cardName);
            
            // Garantiza que el catálogo creó dos objetos diferentes en memoria
            if (firstCopy == secondCopy) {
                return null;
            }

            firstCopies.add(firstCopy);
            secondCopies.add(secondCopy);

            if (firstCopy instanceof PokemonCard pokemon && pokemon.isBasicPokemon()) {
                basicCopies.add(firstCopy);
                basicCopies.add(secondCopy);
            }
        }

        // Verifica que cumpla con los requisitos mínimos del RF4 antes de mezclar
        if (basicCopies.size() < 6 || firstCopies.size() + secondCopies.size() < 20) {
            return null;
        }

        Collections.shuffle(basicCopies);
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
        Collections.shuffle(remainingCopies);
        deckCards.addAll(remainingCopies.subList(0, 20 - deckCards.size()));
        Collections.shuffle(deckCards);

        // Retornará el Deck válido, o null si falla la validación final
        return createValidatedDeck(deckCards);
    }

    private static boolean isCardAvailable(String cardName) {
        switch (cardName) {
            case "Charmander":
            case "Charmeleon":
            case "Charizard":
                return true;
            default:
                return false;
        }
    }
}