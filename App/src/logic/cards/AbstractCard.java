package logic.cards;

public abstract class AbstractCard implements Effect {

    private String name;
    private String cardNumber;
    private String cardType;

    public AbstractCard(String name, String cardNumber, String cardType) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
    }

    public String getName() {
        return name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardType() {
        return cardType;
    }
}