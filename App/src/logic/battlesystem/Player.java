package logic.battlesystem;

import java.util.ArrayList;
import logic.cards.PokemonCard;

public class Player {

private String name;
private Deck deck;
private Hand hand;
private ArrayList<PokemonCard> bank;
private PokemonCard activePokemon;
private byte prizePoints;
private byte availableEnergy;

//constructor
public Player(String name, Deck deck, Hand hand, ArrayList<PokemonCard> bank, PokemonCard activePokemon,
        byte prizePoints, byte availableEnergy) {
    this.name = name;
    this.deck = deck;
    this.hand = hand;
    this.bank = bank;
    this.activePokemon = activePokemon;
    this.prizePoints = prizePoints;
    this.availableEnergy = availableEnergy;
}



//getters and setters
public String getName() {
    return name;
}
public void setName(String name) {
    this.name = name;
}
public Deck getDeck() {
    return deck;
}
public void setDeck(Deck deck) {
    this.deck = deck;
}
public Hand getHand() {
    return hand;
}
public void setHand(Hand hand) {
    this.hand = hand;
}
public ArrayList<PokemonCard> getBank() {
    return bank;
}
public void setBank(ArrayList<PokemonCard> bank) {
    this.bank = bank;
}
public PokemonCard getActivePokemon() {
    return activePokemon;
}
public void setActivePokemon(PokemonCard activePokemon) {
    this.activePokemon = activePokemon;
}
public byte getPrizePoints() {
    return prizePoints;
}
public void setPrizePoints(byte prizePoints) {
    this.prizePoints = prizePoints;
}
public byte getAvailableEnergy() {
    return availableEnergy;
}
public void setAvailableEnergy(byte availableEnergy) {
    this.availableEnergy = availableEnergy;
}

//methods
public void drawCard() {  

}

public void placeOnBench(PokemonCard card) {

}

public void attachEnergy(PokemonCard card) {


}


public void retreatActive(){


}

public void attackOpponent(PokemonCard card) {

    
}

























}
