package logic.cards;

import logic.battlesystem.Game;
import logic.battlesystem.Player;

public class TrainerCard extends AbstractCard {

    private final String trainerType; // "Soporte" u "Objeto"

    public TrainerCard(String name, int cardNumber, String trainerType) {
        super(name, cardNumber);
        this.trainerType = trainerType;
    }

    public String getTrainerType() {
        return trainerType;
    }

    @Override
    public boolean activateEffect(Game game, Player player) {
        return activateEffect(game, player, null);
    }

    @Override
    public boolean activateEffect(Game game, Player player, PokemonCard target) {
        if (player == null) {
            return false;
        }

        switch (getName()) {
            case "Investigacion del Profesor":
                return player.useProfessorResearch();
            case "Misty":
                return player.attachWaterEnergy(target, 3) > 0;
            default:
                return false;
        }
    }
}