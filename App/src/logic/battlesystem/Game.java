package logic.battlesystem;

import java.util.Random;

public class Game {

    public enum VictoryReason {
        PRIZE_POINTS,
        NO_POKEMON_IN_PLAY,
        EMPTY_DECK
    }

    private final Player playerOne;
    private final Player playerTwo;
    private final Random random;
    private Player currentPlayer;
    private Player winner;
    private VictoryReason victoryReason;
    private short turnNumber;
    private boolean finished;

    public Game() {
        this(null, null);
    }

    public Game(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.random = new Random();
    }

    public void start() {
        System.out.println("Nueva partida iniciada.");
    }

    public Player flipCoinToStart() {
        if (playerOne == null || playerTwo == null) {
            return null;
        }
        return random.nextBoolean() ? playerOne : playerTwo;
    }

    public boolean prepareInitialHands() {
        if (!hasValidDeck(playerOne) || !hasValidDeck(playerTwo)
                || playerOne.getHand() == null || playerTwo.getHand() == null) {
            return false;
        }

        return dealInitialHand(playerOne) && dealInitialHand(playerTwo);
    }

    private boolean hasValidDeck(Player player) {
        return player != null && player.getDeck() != null && player.getDeck().validateDeck();
    }

    private boolean dealInitialHand(Player player) {
        player.getHand().getCards().addAll(player.getDeck().dealInitialHand());
        return player.getHand().hasBasicPokemon();
    }

    public boolean startTurn() {
        if (finished || checkWinner() || currentPlayer == null) {
            return false;
        }

        if (turnNumber < Short.MAX_VALUE) {
            turnNumber++;
        }
        currentPlayer.prepareTurn(turnNumber > 1);
        if (currentPlayer.drawCard()) {
            return true;
        }

        winner = getOpponent(currentPlayer);
        victoryReason = VictoryReason.EMPTY_DECK;
        finished = winner != null;
        return false;
    }

    public void endTurn() {
        if (!finished) {
            Player nextPlayer = getOpponent(currentPlayer);
            if (nextPlayer != null) {
                currentPlayer = nextPlayer;
            }
        }
    }

    public boolean checkWinner() {
        if (finished) {
            return true;
        }

        if (playerOne == null || playerTwo == null) {
            return false;
        }

        if (playerOne.getPrizePoints() >= 3) {
            winner = playerOne;
            victoryReason = VictoryReason.PRIZE_POINTS;
        } else if (playerTwo.getPrizePoints() >= 3) {
            winner = playerTwo;
            victoryReason = VictoryReason.PRIZE_POINTS;
        } else if (!hasPokemonInPlay(playerTwo)) {
            winner = playerOne;
            victoryReason = VictoryReason.NO_POKEMON_IN_PLAY;
        } else if (!hasPokemonInPlay(playerOne)) {
            winner = playerTwo;
            victoryReason = VictoryReason.NO_POKEMON_IN_PLAY;
        }

        if (winner != null) {
            finished = true;
        }
        return finished;
    }

    private boolean hasPokemonInPlay(Player player) {
        return player != null && (player.getActivePokemon() != null
                || (player.getBank() != null && !player.getBank().isEmpty()));
    }

    private Player getOpponent(Player player) {
        if (player == playerOne) {
            return playerTwo;
        }
        if (player == playerTwo) {
            return playerOne;
        }
        return null;
    }

    public Player getOpponentOf(Player player) {
        return getOpponent(player);
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean setCurrentPlayer(Player currentPlayer) {
        if (currentPlayer != playerOne && currentPlayer != playerTwo) {
            return false;
        }
        this.currentPlayer = currentPlayer;
        return true;
    }

    public Player getWinner() {
        return winner;
    }

    public VictoryReason getVictoryReason() {
        return victoryReason;
    }

    public short getTurnNumber() {
        return turnNumber;
    }

    public boolean isFinished() {
        return finished;
    }
}
