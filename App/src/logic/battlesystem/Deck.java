//carpetas
package logic.battlesystem;
//funciones de java
import java.util.ArrayList; //listas
import java.util.Collections; //ordenamientos
import java.util.HashMap;//diccionarios
import java.util.Map;
//importes de clases
import logic.cards.AbstractCard;
import logic.cards.PokemonCard;

public class Deck {

    private ArrayList<AbstractCard> cardsDeck;

    // Constructors
    public Deck() {
        this.cardsDeck = new ArrayList<>();
    }

    public Deck(ArrayList<AbstractCard> cardsDeck) {
        if (cardsDeck != null) {
            this.cardsDeck = cardsDeck;
        } else {
            this.cardsDeck = new ArrayList<>();
        }
    }


    // Getters and Setters
    public ArrayList<AbstractCard> getCards() {
        return cardsDeck;
    }

    public void setCards(ArrayList<AbstractCard> cardsDeck) {
        this.cardsDeck = cardsDeck;
    }

    // Methods

    public boolean isEmpty() {//revisa si el mazo esta vacio.
        return cardsDeck.isEmpty();//metodo de arraylist
    }

        public int size() { //devuelve el tamaño del mazo
        return cardsDeck.size();//metodo de arraylist
    }


    public void initializeDeck(ArrayList<AbstractCard> cardsDeck) { //iniciaza el mazo
        this.cardsDeck = cardsDeck;
    }

    public void shuffle() { //baraja el mazo
        Collections.shuffle(cardsDeck); //collections.shuffle() es un metodo de la clase Collections que baraja los elementos de una lista
    }

    public AbstractCard drawCard() {//roba la carta superior del mazo
        if (cardsDeck.isEmpty()) {
            return null;
        }
        return cardsDeck.remove(0);
    }

    public PokemonCard drawBasicPokemon() {
        for (int i = 0; i < cardsDeck.size(); i++) {
            AbstractCard card = cardsDeck.get(i);
            if (card instanceof PokemonCard pokemon && pokemon.isBasicPokemon()) {
                cardsDeck.remove(i);
                return pokemon;
            }
        }
        return null;
    }


    /**
     * Valida el mazo segun los requisitos del RF4:
     * - Exactamente 20 cartas.
     * - Minimo 6 Pokemon basicos.
     * - Maximo 2 copias por carta.
     */
    public boolean validateDeck() {
        if (cardsDeck.size() != 20) {
            return false;
        }

        int pokemonCount = 0;
        Map<String, Integer> cardCounts = new HashMap<>();

        for (AbstractCard card : cardsDeck) {
            if (card == null) {
                return false;
            }

            if (card instanceof PokemonCard pokemon && pokemon.isBasicPokemon()) {
                pokemonCount++;
            }

            String cardName = card.getName();
            int count = cardCounts.getOrDefault(cardName, 0) + 1; //metodo de la clase HashMap que cuenta cuantas veces aparece la carta en el mazo
            if (count > 2) {
                return false; // Excede el maximo de 2 copias por carta
            }
            cardCounts.put(cardName, count);
        }

        return pokemonCount >= 6;
    }

    /**
     * Reparte la mano inicial de 5 cartas.
     * Si entre las 5 cartas repartidas
     * no hay ningun Pokemon basico, se devuelven al mazo, se baraja y se reintenta.
     */
    public ArrayList<AbstractCard> dealInitialHand() {
        ArrayList<AbstractCard> initialHand = new ArrayList<>();
        boolean hasBasicPokemon = false;

        do {
            // Devolver cartas tomadas previamente si fue mulligan
            cardsDeck.addAll(initialHand);
            initialHand.clear();
            hasBasicPokemon = false;

            // Barajar mazo antes de repartir
            shuffle();

            // Robar 5 cartas
            for (int i = 0; i < 5; i++) {
                if (!cardsDeck.isEmpty()) {
                    AbstractCard drawn = drawCard();
                    initialHand.add(drawn);
                    if (drawn instanceof PokemonCard pokemon && pokemon.isBasicPokemon()) {
                        hasBasicPokemon = true;
                    }
                }
            }
        } while (!hasBasicPokemon && !cardsDeck.isEmpty());

        return initialHand;
    }

    public void doMulligan() {//forzar un remezclado y repartir una nueva mano inicial
        dealInitialHand();
    }
}
