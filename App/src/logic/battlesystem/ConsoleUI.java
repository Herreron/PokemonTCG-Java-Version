package logic.battlesystem;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import logic.cards.AbstractCard;
import logic.cards.PokemonCard;
import logic.cards.PokemonCatalog;

public class ConsoleUI {

    private final Scanner scanner;
    private final PrintStream output;
    private boolean running;

    public ConsoleUI() {
        this(new Scanner(System.in), System.out);
    }

    ConsoleUI(Scanner scanner, PrintStream output) {
        this.scanner = scanner;
        this.output = output;
    }

    public void run() {
        running = true;
        while (running) {
            output.println("\n=== Pokemon TCG Pocket ===");
            output.println("1. Nueva partida rapida");
            output.println("0. Salir");
            int choice = readChoice("Selecciona una opcion: ", 0, 1);
            if (!running || choice == 0) {
                running = false;
            } else {
                startQuickMatch();
            }
        }
    }

    private void startQuickMatch() {
        String firstName = readName("Nombre del jugador 1: ");
        String secondName = readName("Nombre del jugador 2: ");

        try {
            List<Supplier<? extends AbstractCard>> definitions = getAvailableCardDefinitions();
            Deck firstDeck = DeckFactory.createRandomDeck(definitions);
            Deck secondDeck = DeckFactory.createRandomDeck(definitions);
            Player firstPlayer = createPlayer(firstName, firstDeck);
            Player secondPlayer = createPlayer(secondName, secondDeck);
            Game game = new Game(firstPlayer, secondPlayer);
            game.prepareInitialHands();

            setupInitialField(firstPlayer);
            setupInitialField(secondPlayer);

            Player firstTurnPlayer = game.flipCoinToStart();
            game.setCurrentPlayer(firstTurnPlayer);
            output.println("La moneda favorece a " + firstTurnPlayer.getName() + ".");
            playMatch(game);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            output.println("No se pudo iniciar la partida: " + exception.getMessage());
        }
    }

    private List<Supplier<? extends AbstractCard>> getAvailableCardDefinitions() {
        return List.of(
                PokemonCatalog::createCharmander,
                PokemonCatalog::createCharmeleon,
                PokemonCatalog::createCharizard);
    }

    private Player createPlayer(String name, Deck deck) {
        return new Player(name, deck, new Hand(), new ArrayList<>(), null, (byte) 0, (byte) 0);
    }

    private void setupInitialField(Player player) {
        if (!running) {
            return;
        }
        output.println("\n" + player.getName() + ", elige tu Pokemon activo.");
        PokemonCard activePokemon = chooseBasicPokemon(player, "Activo");
        while (!player.selectInitialActivePokemon(activePokemon)) {
            output.println("No se pudo elegir ese Pokemon. Intenta de nuevo.");
            activePokemon = chooseBasicPokemon(player, "Activo");
        }

        while (player.getBank().size() < 2) {
            ArrayList<PokemonCard> basicPokemon = player.getHand().getBasicPokemonCards();
            if (basicPokemon.isEmpty()) {
                break;
            }

            int addToBank = readChoice(
                    "¿Quieres colocar un Pokemon basico en la banca? (1=si, 0=no): ", 0, 1);
            if (!running || addToBank == 0) {
                break;
            }

            PokemonCard pokemon = chooseBasicPokemon(player, "Banca");
            if (!player.placeOnBench(pokemon)) {
                output.println("No se pudo colocar ese Pokemon en la banca.");
            }
        }
    }

    private PokemonCard chooseBasicPokemon(Player player, String destination) {
        ArrayList<PokemonCard> basicPokemon = player.getHand().getBasicPokemonCards();
        if (basicPokemon.isEmpty()) {
            throw new IllegalStateException("No hay Pokemon basicos disponibles para " + destination + ".");
        }

        for (int i = 0; i < basicPokemon.size(); i++) {
            output.println((i + 1) + ". " + basicPokemon.get(i));
        }
        int choice = readChoice("Elige una carta para " + destination + ": ", 1, basicPokemon.size());
        return basicPokemon.get(choice - 1);
    }

    private void playMatch(Game game) {
        while (running && !game.isFinished()) {
            if (!game.startTurn()) {
                break;
            }

            boolean turnEnded = false;
            while (running && !turnEnded && !game.isFinished()) {
                game.checkWinner();
                if (game.isFinished()) {
                    break;
                }

                Player currentPlayer = game.getCurrentPlayer();
                showField(game);
                output.println("\nAcciones:");
                output.println("1. Adjuntar energia de zona");
                output.println("2. Jugar carta de entrenador (pendiente)");
                output.println("3. Colocar Pokemon basico en la banca");
                output.println("4. Retirar Pokemon activo");
                output.println("5. Atacar (pendiente de la clase Attack)");
                output.println("6. Evolucionar Pokemon");
                output.println("7. Pasar turno");

                int action = readChoice("Elige una accion: ", 1, 7);
                if (!running) {
                    return;
                }

                switch (action) {
                    case 1 -> attachEnergy(currentPlayer);
                    case 2 -> output.println("Las cartas de entrenador aun no estan implementadas.");
                    case 3 -> placeOnBench(currentPlayer);
                    case 4 -> retreat(currentPlayer);
                    case 5 -> output.println("El ataque estara disponible cuando Attack tenga sus datos.");
                    case 6 -> evolve(currentPlayer);
                    case 7 -> turnEnded = true;
                    default -> throw new IllegalStateException("Opcion de accion no valida.");
                }

                game.checkWinner();
            }

            if (running && !game.isFinished() && turnEnded) {
                game.endTurn();
            }
        }

        if (game.getWinner() != null) {
            output.println("\n¡" + game.getWinner().getName() + " gana el duelo!");
        }
    }

    private void attachEnergy(Player player) {
        ArrayList<PokemonCard> pokemonInPlay = getPokemonInPlay(player);
        if (player.getAvailableEnergy() <= 0 || pokemonInPlay.isEmpty()) {
            output.println("No hay energia disponible o Pokemon en juego.");
            return;
        }

        for (int i = 0; i < pokemonInPlay.size(); i++) {
            output.println((i + 1) + ". " + pokemonInPlay.get(i));
        }
        int choice = readChoice("Elige el Pokemon que recibe energia: ", 1, pokemonInPlay.size());
        if (player.attachEnergy(pokemonInPlay.get(choice - 1))) {
            output.println("Energia adjuntada.");
        } else {
            output.println("No se pudo adjuntar energia.");
        }
    }

    private void placeOnBench(Player player) {
        ArrayList<PokemonCard> basicPokemon = player.getHand().getBasicPokemonCards();
        if (basicPokemon.isEmpty() || player.getBank().size() >= 2) {
            output.println("No hay Pokemon basicos disponibles o la banca esta llena.");
            return;
        }

        for (int i = 0; i < basicPokemon.size(); i++) {
            output.println((i + 1) + ". " + basicPokemon.get(i));
        }
        int choice = readChoice("Elige el Pokemon que colocaras en la banca: ", 1, basicPokemon.size());
        output.println(player.placeOnBench(basicPokemon.get(choice - 1))
                ? "Pokemon colocado en la banca."
                : "No se pudo colocar ese Pokemon.");
    }

    private void retreat(Player player) {
        if (player.getBank().isEmpty()) {
            output.println("No hay Pokemon en la banca.");
            return;
        }

        for (int i = 0; i < player.getBank().size(); i++) {
            output.println((i + 1) + ". " + player.getBank().get(i));
        }
        int choice = readChoice("Elige el Pokemon que sera el nuevo activo: ", 1, player.getBank().size());
        output.println(player.retreatActive(player.getBank().get(choice - 1))
                ? "Retirada realizada."
                : "No se pudo retirar: revisa el costo de retirada y las energias adjuntas.");
    }

    private void evolve(Player player) {
        ArrayList<PokemonCard> evolutions = new ArrayList<>();
        for (AbstractCard card : player.getHand().getCards()) {
            if (card instanceof PokemonCard pokemon && !pokemon.isBasicPokemon()) {
                evolutions.add(pokemon);
            }
        }

        if (evolutions.isEmpty()) {
            output.println("No tienes cartas de evolucion en la mano.");
            return;
        }

        for (int i = 0; i < evolutions.size(); i++) {
            output.println((i + 1) + ". " + evolutions.get(i));
        }
        int choice = readChoice("Elige la evolucion que deseas jugar: ", 1, evolutions.size());
        output.println(player.evolvePokemon(evolutions.get(choice - 1))
                ? "Evolucion realizada."
                : "No hay un Pokemon en juego que cumpla el requisito de evolucion.");
    }

    private void showField(Game game) {
        Player current = game.getCurrentPlayer();
        Player opponent;
        if (current == game.getPlayerOne()) {
            opponent = game.getPlayerTwo();
        } else {
            opponent = game.getPlayerOne();
        }

        output.println("\n===== TURNO " + game.getTurnNumber() + " — " + current.getName() + " =====");
        showPlayerField("RIVAL", opponent);
        showPlayerField("TU CAMPO", current);
        output.println("\nPuntos de premio: " + current.getPrizePoints() + "/3");
        output.println("Energia disponible: " + current.getAvailableEnergy());
        output.println("Tu mano (" + current.getHand().size() + " cartas):");
        for (int i = 0; i < current.getHand().getCards().size(); i++) {
            output.println("  " + (i + 1) + ". " + current.getHand().getCard(i).getName());
        }
    }

    private void showPlayerField(String label, Player player) {
        output.println("\n" + label + " (" + player.getName() + ") — premios: " + player.getPrizePoints() + "/3");
        if (player.getActivePokemon() == null) {
            output.println("  [Activo] ninguno");
        } else {
            output.println("  [Activo] " + player.getActivePokemon()
                    + ", energia: " + player.getActivePokemon().getAttachedEnergy());
        }
        for (PokemonCard pokemon : player.getBank()) {
            output.println("  [Banca] " + pokemon + ", energia: " + pokemon.getAttachedEnergy());
        }
    }

    private ArrayList<PokemonCard> getPokemonInPlay(Player player) {
        ArrayList<PokemonCard> pokemonInPlay = new ArrayList<>();
        if (player.getActivePokemon() != null) {
            pokemonInPlay.add(player.getActivePokemon());
        }
        pokemonInPlay.addAll(player.getBank());
        return pokemonInPlay;
    }

    private String readName(String prompt) {
        while (running) {
            output.print(prompt);
            if (!scanner.hasNextLine()) {
                running = false;
                return "";
            }
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                return name;
            }
            output.println("El nombre no puede estar vacio.");
        }
        return "";
    }

    private int readChoice(String prompt, int minimum, int maximum) {
        while (running) {
            output.print(prompt);
            if (!scanner.hasNextLine()) {
                running = false;
                return minimum;
            }

            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= minimum && choice <= maximum) {
                    return choice;
                }
            } catch (NumberFormatException exception) {
                output.println("Ingresa un numero valido.");
                continue;
            }
            output.println("Elige un numero entre " + minimum + " y " + maximum + ".");
        }
        return minimum;
    }
}
