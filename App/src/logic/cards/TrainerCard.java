package logic.cards;

public class TrainerCard extends AbstractCard {

    private final String trainerType;

    public TrainerCard(String name, String trainerType) {
        super(name);
        this.trainerType = trainerType;
    }

    public String getTrainerType() {
        return trainerType;
    }
}
