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

    /**
     * Construye un mazo con cartas ya elegidas y verifica que respete las reglas del proyecto.
     */
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

    /**
     * Fabrica un mazo aleatorio usando nombres y el catalogo central de cartas.
     *
     * Cada nombre aporta como maximo dos cartas. Se reservan seis copias de Pokemon basicos
     * antes de completar el resto del mazo, evitando que el azar rompa la regla de RF4.
     */
    public static Deck createRandomDeck(List<String> availableCardNames) {
        if (availableCardNames == null) {
            throw new IllegalArgumentException("La lista de nombres de cartas no puede ser null.");
        }

        ArrayList<AbstractCard> firstCopies = new ArrayList<>();
        ArrayList<AbstractCard> secondCopies = new ArrayList<>();
        ArrayList<AbstractCard> basicCopies = new ArrayList<>();
        ArrayList<String> processedNames = new ArrayList<>();

        // Se crea cada carta por nombre y se descartan nombres repetidos en la lista de entrada.
        for (String cardName : availableCardNames) {
            if (cardName == null || cardName.isBlank()) {
                throw new IllegalArgumentException("Los nombres de cartas no pueden ser null o vacios.");
            }
            if (processedNames.contains(cardName)) {
                continue;
            }
            processedNames.add(cardName);

            AbstractCard firstCopy = PokemonCatalog.createCardByName(cardName);
            AbstractCard secondCopy = PokemonCatalog.createCardByName(cardName);
            if (firstCopy == secondCopy) {
                throw new IllegalStateException("El catalogo debe crear una instancia nueva por carta.");
            }

            firstCopies.add(firstCopy);
            secondCopies.add(secondCopy);

            // Solo los Pokemon de fase 1 cuentan para el mínimo de básicos.
            if (firstCopy instanceof PokemonCard pokemon && pokemon.isBasicPokemon()) {
                basicCopies.add(firstCopy);
                basicCopies.add(secondCopy);
            }
        }

        // Con dos copias máximas por nombre, hacen falta tres básicos distintos como mínimo.
        if (basicCopies.size() < 6) {
            throw new IllegalArgumentException(
                    "El catalogo necesita al menos 3 Pokemon basicos distintos para cumplir "
                            + "el minimo de 6 basicos, con maximo 2 copias por carta.");
        }

        // Diez nombres distintos permiten aportar las veinte cartas sin exceder el límite de copias.
        if (firstCopies.size() + secondCopies.size() < 20) {
            throw new IllegalArgumentException(
                    "El catalogo necesita al menos 10 nombres de cartas distintos para completar un mazo de 20.");
        }

        // Se garantizan seis básicos, retirándolos del conjunto antes de llenar los espacios restantes.
        Collections.shuffle(basicCopies);
        ArrayList<AbstractCard> deckCards = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            AbstractCard basic = basicCopies.get(i);
            deckCards.add(basic);
            firstCopies.remove(basic);
            secondCopies.remove(basic);
        }

        // Se mezclan las copias restantes y se seleccionan las necesarias para completar veinte.
        ArrayList<AbstractCard> remainingCopies = new ArrayList<>(firstCopies);
        remainingCopies.addAll(secondCopies);
        Collections.shuffle(remainingCopies);
        deckCards.addAll(remainingCopies.subList(0, 20 - deckCards.size()));
        Collections.shuffle(deckCards);

        return createValidatedDeck(deckCards);
    }
}
