package logic.battlesystem;

import java.util.Objects;
import java.util.Random;

public class Game {

    private final Player playerOne;
    private final Player playerTwo;
    private final Random random;
    private Player currentPlayer;
    private Player winner;
    private int turnNumber;
    private boolean finished;

    public Game(Player playerOne, Player playerTwo) {
        this(playerOne, playerTwo, new Random());
    }

    Game(Player playerOne, Player playerTwo, Random random) {
        this.playerOne = Objects.requireNonNull(playerOne, "El primer jugador es obligatorio.");
        this.playerTwo = Objects.requireNonNull(playerTwo, "El segundo jugador es obligatorio.");
        if (playerOne == playerTwo) {
            throw new IllegalArgumentException("La partida requiere dos jugadores distintos.");
        }
        this.random = Objects.requireNonNull(random, "La fuente aleatoria es obligatoria.");
    }

    public Player flipCoinToStart() {
        return random.nextBoolean() ? playerOne : playerTwo;
    }

    public void prepareInitialHands() {
        validatePlayerDeck(playerOne);
        validatePlayerDeck(playerTwo);
        if (playerOne.getHand() == null || playerTwo.getHand() == null) {
            throw new IllegalStateException("Cada jugador debe tener una mano inicial.");
        }
        dealInitialHand(playerOne);
        dealInitialHand(playerTwo);
    }

    private void validatePlayerDeck(Player player) {
        if (player.getDeck() == null || !player.getDeck().validateDeck()) {
            throw new IllegalStateException(
                    "El mazo de " + player.getName()
                            + " debe tener 20 cartas, al menos 6 Pokemon basicos y maximo 2 copias por carta.");
        }
    }

    private void dealInitialHand(Player player) {
        player.getHand().getCards().addAll(player.getDeck().dealInitialHand());
        if (!player.getHand().hasBasicPokemon()) {
            throw new IllegalStateException(
                    "No se pudo repartir un Pokemon basico a " + player.getName() + ".");
        }
    }

    public boolean startTurn() {
        if (finished || checkWinner()) {
            return false;
        }
        if (currentPlayer == null) {
            throw new IllegalStateException("Debes definir el jugador inicial antes de iniciar el turno.");
        }

        turnNumber++;
        currentPlayer.prepareTurn(turnNumber > 1);
        if (!currentPlayer.drawCard()) {
            winner = getOpponent(currentPlayer);
            finished = true;
            return false;
        }
        return true;
    }

    public void endTurn() {
        if (!finished) {
            currentPlayer = getOpponent(currentPlayer);
        }
    }

    public boolean checkWinner() {
        if (finished) {
            return true;
        }

        if (playerOne.getPrizePoints() >= 3 || !hasPokemonInPlay(playerTwo)) {
            winner = playerOne;
        } else if (playerTwo.getPrizePoints() >= 3 || !hasPokemonInPlay(playerOne)) {
            winner = playerTwo;
        }

        if (winner != null) {
            finished = true;
        }
        return finished;
    }

    private boolean hasPokemonInPlay(Player player) {
        return player.getActivePokemon() != null
                || (player.getBank() != null && !player.getBank().isEmpty());
    }

    private Player getOpponent(Player player) {
        if (player == playerOne) {
            return playerTwo;
        }
        if (player == playerTwo) {
            return playerOne;
        }
        throw new IllegalArgumentException("El jugador no pertenece a esta partida.");
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

    public void setCurrentPlayer(Player currentPlayer) {
        if (currentPlayer != playerOne && currentPlayer != playerTwo) {
            throw new IllegalArgumentException("El jugador inicial debe pertenecer a esta partida.");
        }
        this.currentPlayer = currentPlayer;
    }

    public Player getWinner() {
        return winner;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public boolean isFinished() {
        return finished;
    }
}
