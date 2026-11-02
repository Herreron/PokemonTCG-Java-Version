package logic.battlesystem;
import logic.cards.AbstractCard;
import java.util.ArrayList;

public class Deck {

private ArrayList <AbstractCard> cardsDeck;

//constructor

public Deck(ArrayList<AbstractCard> cardsDeck) {
    this.cardsDeck = cardsDeck;
}

//getters and setters
public void setCards(ArrayList<AbstractCard> cardsDeck) {
    this.cardsDeck = cardsDeck;
}

public ArrayList<AbstractCard> getCards() {
    return cardsDeck;
}


//methods
public void initializeDeck(ArrayList<AbstractCard> cardsDeck) {
    this.cardsDeck = cardsDeck;
}

public void shuffle() { //barajar
    // Implement shuffle logic here

}

public boolean validateDeck() {
    // Implement deck validation logic here
    return true; // Placeholder return value
}

public void doMulligan() { //hacerMulligan, si no hay pokemons basicos en la mano inicial, se hace mulligan
    // Implement mulligan logic here
}



}
