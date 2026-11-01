package logic.cards;

public abstract class AbstractCard {

    private String name;
    private int cardNumber;

    public AbstractCard(String name, int cardNumber) {
        this.name = name;
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public int getCardNumber() {
        return cardNumber;
    }
}