package logic.cards;

import logic.battlesystem.Game;
import logic.battlesystem.Player;

public class ObjectCard extends TrainerCard {

    public ObjectCard(String name, int cardNumber) {
        super(name, cardNumber, "Objeto");
    }

    @Override
    public void activateEffect(Game game, Player player) {
        switch (getName()) {
            case "Pocion":
                // TODO: curar 20 PS al Pokemon activo
                break;
            case "Poke Ball":
                // TODO: buscar 1 Pokemon basico en el mazo y ponerlo en la mano
                break;
            case "Turbo Velocidad":
                // TODO: reducir en 1 el costo de retirada del activo este turno
                break;
            default:
                break;
        }
    }
}