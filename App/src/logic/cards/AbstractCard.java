package logic.cards;

public abstract class AbstractCard {

    private String name;
    private String cardType;

    public AbstractCard(String name, String cardType) {
        this.name = name;
        this.cardType = cardType;
    }

   


    public String getName() {
        return name;
    }

    public String getCardType() {
        return cardType;
    }
}

