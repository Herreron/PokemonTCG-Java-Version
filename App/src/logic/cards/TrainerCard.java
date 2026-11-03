package logic.cards;

public abstract class TrainerCard extends AbstractCard {

    private final String trainerType; // "Soporte" u "Objeto"

    public TrainerCard(String name, int cardNumber, String trainerType) {
        super(name, cardNumber);
        this.trainerType = trainerType;
    }

    public String getTrainerType() {
        return trainerType;
    }
}