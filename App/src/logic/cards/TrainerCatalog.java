package logic.cards;

import logic.battlesystem.Game;
import logic.battlesystem.Player;

public class TrainerCatalog extends AbstractCard {

    private final String trainerType; // "Soporte" u "Objeto"

    public TrainerCatalog(String name, int cardNumber, String trainerType) {
        super(name, cardNumber);
        this.trainerType = trainerType;
    }

    public String getTrainerType() {
        return trainerType;
    }

    @Override
    public void activateEffect(Game game, Player player) {
        switch (getName()) {
            case "Investigacion del Profesor":
                // TODO: descartar la mano y robar 2 cartas
                break;
            case "Misty":
                // TODO: adjuntar hasta 3 energias de agua a un Pokemon de agua
                break;
            default:
                break;
        }
    }
}