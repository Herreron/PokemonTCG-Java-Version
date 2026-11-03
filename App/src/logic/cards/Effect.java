package logic.cards;

import logic.battlesystem.Game;
import logic.battlesystem.Player;

public interface Effect {
    void activateEffect(Game game, Player player);
}