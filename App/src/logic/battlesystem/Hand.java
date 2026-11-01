//carpetas
package logic.battlesystem;

//funciones de java
import java.util.ArrayList; //listas

//importes de clases
import logic.cards.AbstractCard;
import logic.cards.PokemonCard;

public class Hand {

    private ArrayList<AbstractCard> cardsHand;

    // Constructors
    public Hand() {
        this.cardsHand = new ArrayList<>();
    }

    public Hand(ArrayList<AbstractCard> cardsHand) {
        if (cardsHand != null) {
            this.cardsHand = cardsHand;
        } else {
            this.cardsHand = new ArrayList<>();
        }
    }

    // Getters and Setters
    public ArrayList<AbstractCard> getCards() {
        return cardsHand;
    }

    public void setCards(ArrayList<AbstractCard> cardsHand) {
        if (cardsHand != null) {
            this.cardsHand = cardsHand;
        }
    }

    // Methods

    public void addCard(AbstractCard card) { //agrega una carta a la mano
        if (card != null) {
            cardsHand.add(card); //metodo de arraylist
        }
    }

    public boolean removeCard(AbstractCard card) { //elimina un objeto carta especifico
        return cardsHand.remove(card); //metodo de arraylist
    }

    public AbstractCard removeCard(int index) { //elimina y devuelve una carta por su posicion
        if (index >= 0 && index < cardsHand.size()) {
            return cardsHand.remove(index); //metodo de arraylist
        }
        return null;
    }

    public AbstractCard getCard(int index) { //revisa una carta por su posicion sin eliminarla
        if (index >= 0 && index < cardsHand.size()) {
            return cardsHand.get(index); //metodo de arraylist
        }
        return null;
    }

    public void clearHand() { //vacia toda la mano
        cardsHand.clear(); //metodo de arraylist
    }

    public boolean isEmpty() { //revisa si la mano esta vacia
        return cardsHand.isEmpty(); //metodo de arraylist
    }

    public int size() { //devuelve la cantidad de cartas en la mano
        return cardsHand.size(); //metodo de arraylist
    }

    /**
     * Verifica si la mano contiene al menos un Pokemon basico.
     */
    public boolean hasBasicPokemon() {
        for (AbstractCard card : cardsHand) {
            if (card instanceof PokemonCard pokemon && pokemon.isBasicPokemon()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna una lista con todos los Pokemon basicos presentes en la mano.
     */
    public ArrayList<PokemonCard> getBasicPokemonCards() {
        ArrayList<PokemonCard> basicPokemons = new ArrayList<>();
        for (AbstractCard card : cardsHand) {
            if (card instanceof PokemonCard pokemon && pokemon.isBasicPokemon()) {
                basicPokemons.add(pokemon);
            }
        }
        return basicPokemons;
    }

    /**
     * Limpia la mano y retorna todas las cartas descartadas.
     */
    public ArrayList<AbstractCard> discardAll() {
        ArrayList<AbstractCard> discarded = new ArrayList<>(cardsHand); //crea una copia de la lista actual
        cardsHand.clear(); //metodo de arraylist para vaciar la mano original
        return discarded;
    }
}