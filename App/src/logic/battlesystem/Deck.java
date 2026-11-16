//carpetas
package logic.battlesystem;
//funciones de java
import java.util.ArrayList; //listas
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
        return cardsDeck.isEmpty();
    }

        public int size() { //devuelve el tamaño del mazo
        return cardsDeck.size();
    }


    public void initializeDeck(ArrayList<AbstractCard> cardsDeck) { //iniciaza el mazo
        this.cardsDeck = cardsDeck;
    }

    public void shuffle() { //baraja el mazo
        for (int i = cardsDeck.size() - 1; i > 0; i--) {
            int randomIndex = (int) (Math.random() * (i + 1));
            AbstractCard currentCard = cardsDeck.get(i);
            cardsDeck.set(i, cardsDeck.get(randomIndex));
            cardsDeck.set(randomIndex, currentCard);
        }
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
            if (card instanceof PokemonCard) {
                PokemonCard pokemon = (PokemonCard) card;
                if (pokemon.isBasicPokemon()) {
                    cardsDeck.remove(i);
                    return pokemon;
                }
            }
        }
        return null;
    }


    public boolean validateDeck() {
        if (cardsDeck.size() != 20) {
            return false;
        }

        byte pokemonCount = 0;

        for (int i = 0; i < cardsDeck.size(); i++) {
            AbstractCard card = cardsDeck.get(i);
            if (card == null) {
                return false;
            }

            if (card instanceof PokemonCard) {
                PokemonCard pokemon = (PokemonCard) card;
                if (pokemon.isBasicPokemon()) {
                    pokemonCount++;
                }
            }

            String cardName = card.getName();
            byte count = 0;
            for (int j = 0; j < cardsDeck.size(); j++) {
                AbstractCard comparedCard = cardsDeck.get(j);
                if (comparedCard == null) {
                    return false;
                }
                if (cardName == null) {
                    if (comparedCard.getName() == null) {
                        count++;
                    }
                } else if (cardName.equals(comparedCard.getName())) {
                    count++;
                }
            }
            if (count > 2) {
                return false; // Excede el maximo de 2 copias por carta
            }
        }

        return pokemonCount >= 6;
    }

   //mulligan
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
                    if (drawn instanceof PokemonCard) {
                        PokemonCard pokemon = (PokemonCard) drawn;
                        if (pokemon.isBasicPokemon()) {
                            hasBasicPokemon = true;
                        }
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
