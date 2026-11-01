package logic.battlesystem;
import java.util.ArrayList;
import logic.cards.AbstractCard;

public class Hand {

private ArrayList <AbstractCard> cardsHand;


//constructor
public Hand(ArrayList<AbstractCard> cardsHand) {
    this.cardsHand = cardsHand;
}

//getters and setters
public void setCards(ArrayList<AbstractCard> cardsHand) {
    this.cardsHand = cardsHand;}
public ArrayList<AbstractCard> getCards() {
    return cardsHand;}

//methods

public void addCard(AbstractCard card) {
    cardsHand.add(card);}

public void removeCard(AbstractCard card) {
    cardsHand.remove(card);}

public void clearHand() {
    cardsHand.clear();} 

    



}
