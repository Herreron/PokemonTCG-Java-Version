package logic.battlesystem;

import java.util.ArrayList;
import java.util.Scanner;
import logic.cards.AbstractCard;
import logic.cards.Attack;
import logic.cards.ObjectCatalog;
import logic.cards.ObjectCard;
import logic.cards.PokemonCard;
import logic.cards.PokemonCatalog;
import logic.cards.TrainerCard;
import logic.cards.TrainerCatalog;
import logic.cards.Type;

public class ConsoleUI {

    private final Scanner scanner;
    private boolean running;

    public ConsoleUI() {
        scanner = new Scanner(System.in);
    }

// iniciar partida
    public void run() {
        running = true;
        clearConsole();

        while (running) {
            showMainMenu();
            System.out.println("Selecciona una opcion:");
            int choice = readChoice(0, 1);

            if (!running || choice == 0) {
                running = false;
            } else {
                startQuickMatch();
                if (running) {
                    pauseAndClear("Presiona ENTER para volver al menu principal.");
                }
            }
        }
    }

// Menu de inicio
    private void showMainMenu() {
        System.out.println("       POKEMON TCG POCKET Java Version");
        System.out.println("1. Iniciar partida rapida");
        System.out.println("0. Salir");
        System.out.println("-----------------------------------");
    }

    private void startQuickMatch() {
        String firstName = "";
        while (running && firstName.isEmpty()) {
            System.out.println("Nombre del jugador 1:");
            if (!scanner.hasNextLine()) {
                running = false;
                return;
            }
            firstName = scanner.nextLine().trim();
            if (firstName.isEmpty()) {
                System.out.println("El nombre no puede estar vacio.");
            }
        }

        String secondName = "";
        while (running && secondName.isEmpty()) {
            System.out.println("Nombre del jugador 2:");
            if (!scanner.hasNextLine()) {
                running = false;
                return;
            }
            secondName = scanner.nextLine().trim();
            if (secondName.isEmpty()) {
                System.out.println("El nombre no puede estar vacio.");
            } else if (secondName.equalsIgnoreCase(firstName)) {
                System.out.println("Ese nombre ya lo usa el jugador 1. Elige otro nombre.");
                secondName = "";
            }
        }

        Deck firstDeck = chooseThemeDeck(firstName);
        if (!running || firstDeck == null) {
            return;
        }

        Deck secondDeck = chooseThemeDeck(secondName);
        if (!running || secondDeck == null) {
            return;
        }

        // Create the players with their chosen decks
        Player firstPlayer = createPlayer(firstName, firstDeck);
        Player secondPlayer = createPlayer(secondName, secondDeck);

        Game game = new Game(firstPlayer, secondPlayer);
        if (!game.prepareInitialHands()) {
            System.out.println("Could not deal initial hands.");
            return;
        }

        setupInitialField(firstPlayer);
        if (!running) {
            return;
        }

        pauseAndClear("Preparacion del jugador 1 terminada. Entrega la consola al jugador 2 y presiona ENTER.");
        if (!running) {
            return;
        }

        setupInitialField(secondPlayer);
        if (!running) {
            return;
        }

        pauseAndClear("Preparacion terminada. Presiona ENTER para iniciar el duelo.");
        if (!running) {
            return;
        }

        Player firstTurnPlayer = game.flipCoinToStart();
        if (firstTurnPlayer == null || !game.setCurrentPlayer(firstTurnPlayer)) {
            System.out.println("No se pudo seleccionar el jugador inicial.");
            return;
        }
        System.out.println("La moneda favorece a " + firstTurnPlayer.getName() + ".");
        pauseAndClear("Presiona ENTER para comenzar el primer turno.");
        if (running) {
            playMatch(game);
        }
    }

    public ArrayList<String> getAvailableCardNames() {
        ArrayList<String> cardNames = new ArrayList<>();
        for (PokemonCard card : PokemonCatalog.createAllPokemonCards()) {
            cardNames.add(card.getName());
        }
        for (AbstractCard card : ObjectCatalog.createAllObjectCards()) {
            cardNames.add(card.getName());
        }
        for (TrainerCard card : TrainerCatalog.createAllTrainerCards()) {
            cardNames.add(card.getName());
        }
        return cardNames;
    }

    private Player createPlayer(String name, Deck deck) {
        return new Player(name, deck, new Hand(), new ArrayList<>(), null, (byte) 0, (byte) 0);
    }

    private void setupInitialField(Player player) {
        clearConsole();
        System.out.println("\nPreparacion del campo: \n" + player.getName());

        PokemonCard activePokemon = chooseBasicPokemon(player, "activo");
        if (!running || activePokemon == null) {
            return;
        }

        while (!player.selectInitialActivePokemon(activePokemon)) {
            System.out.println("No se pudo elegir ese Pokemon. Intenta de nuevo.");
            activePokemon = chooseBasicPokemon(player, "activo");
            if (!running || activePokemon == null) {
                return;
            }
        }

        chooseInitialBenchPokemon(player);
    }

    private void chooseInitialBenchPokemon(Player player) {
        while (player.getBank().size() < 2) {
            ArrayList<PokemonCard> basicPokemon = player.getHand().getBasicPokemonCards();
            if (basicPokemon.isEmpty()) {
                break;
            }

            System.out.println("¿Quieres colocar un Pokemon basico en la banca? (1=si, 0=no):");
            int addToBank = readChoice(0, 1);
            if (!running || addToBank == 0) {
                break;
            }

            PokemonCard pokemon = chooseBasicPokemon(player, "la banca");
            if (!running || pokemon == null) {
                return;
            }

            if (!player.placeOnBench(pokemon)) {
                System.out.println("No se pudo colocar ese Pokemon en la banca.");
            }
        }
    }

    private PokemonCard chooseBasicPokemon(Player player, String destination) {
        ArrayList<PokemonCard> basicPokemon = player.getHand().getBasicPokemonCards();
        if (basicPokemon.isEmpty()) {
            System.out.println("No hay Pokemon basicos disponibles para elegir como " + destination + ".");
            return null;
        }

        System.out.println("\nElige el Pokemon " + destination + ":");
        for (int i = 0; i < basicPokemon.size(); i++) {
            System.out.println((i + 1) + ". " + basicPokemon.get(i));
        }

        System.out.println("Seleccion:");
        int choice = readChoice(1, basicPokemon.size());
        if (!running) {
            return null;
        }
        return basicPokemon.get(choice - 1);
    }

    private Deck chooseThemeDeck(String playerName) {
        System.out.println("\nElije tu tipo de mazo \n" + playerName);
        System.out.println("1. Mazo de fuego");
        System.out.println("2. Mazo de agua");
        System.out.println("3. Mazo electrico");
        System.out.println("4. Mazo psiquico");
        System.out.print("Escoje tu opcion: (1-4)");

        if (!scanner.hasNextLine()) {
            running = false;
            return null;
        }

        String choice = scanner.nextLine().trim();

        return switch (choice) {
            case "2" -> DeckFactory.createWaterDeck();
            case "3" -> DeckFactory.createElectricDeck();
            case "4" -> DeckFactory.createPsychicDeck();
            default -> {
                if (!choice.equals("1")) {
                    System.out.println("Opcion invalida. Asignando Mazo de fuego por defecto.");
                }
                yield DeckFactory.createFireDeck();
            }
        };
    }

//gestion de turnos y acciones
    private boolean playCurrentTurn(Game game) {
        while (running && !game.isFinished()) {
            game.checkWinner();
            if (game.isFinished()) {
                return false;
            }

            Player currentPlayer = game.getCurrentPlayer();
            showField(game);
            showTurnMenu();
            System.out.println("Elige una accion:");
            int action = readChoice(1, 7);
            if (!running) {
                return false;
            }

            if (executeAction(action, game, currentPlayer)) {
                return true;
            }
            // Se espera ENTER para leer el aviso antes de que se vuelva a dibujar el campo.
            if (running) {
                waitForEnter();
            }
            game.checkWinner();
        }
        return false;
    }

    private void showTurnMenu() {
        System.out.println("\n-----------------------------------");
        System.out.println("Acciones disponibles:");
        System.out.println("1. Adjuntar energia de zona");
        System.out.println("2. Jugar carta de entrenador");
        System.out.println("3. Colocar Pokemon basico en la banca");
        System.out.println("4. Retirar Pokemon activo");
        System.out.println("5. Atacar");
        System.out.println("6. Evolucionar Pokemon");
        System.out.println("7. Pasar turno");
        System.out.println("-----------------------------------");
    }

    private boolean executeAction(int action, Game game, Player player) {
        switch (action) {
            case 1:
                attachEnergy(player);
                return false;
            case 2:
                playPlayableCard(game, player);
                return false;
            case 3:
                placeOnBench(player);
                return false;
            case 4:
                retreat(player);
                return false;
            case 5:
                attack(game, player);
                return false;
            case 6:
                evolve(player);
                return false;
            case 7:
                return true;
            default:
                System.out.println("La opcion de accion no es valida.");
                return false;
        }

    }

    private void changeTurn(Game game) {
        String previousPlayerName = game.getCurrentPlayer().getName();
        game.endTurn();
        Player nextPlayer = game.getCurrentPlayer();
        if (nextPlayer == null) {
            System.out.println("No se pudo cambiar al siguiente jugador.");
            return;
        }
        pauseAndClear(
                "Turno de " + previousPlayerName + " terminado. Entrega la consola a "
                        + nextPlayer.getName() + " y presiona ENTER.");
    }

    private void attachEnergy(Player player) {
        ArrayList<PokemonCard> pokemonInPlay = getPokemonInPlay(player);
        if (pokemonInPlay.isEmpty()) {
            System.out.println("No tienes Pokemon en juego para adjuntar energia.");
            return;
        }
        if (player.getAvailableEnergy() <= 0) {
            System.out.println("No tienes energia disponible: solo recibes 1 por turno y ya la usaste.");
            return;
        }

        System.out.println("\nElige un Pokemon en juego:");
        for (int i = 0; i < pokemonInPlay.size(); i++) {
            System.out.println((i + 1) + ". " + pokemonInPlay.get(i));
        }

        System.out.println("Seleccion:");
        int choice = readChoice(1, pokemonInPlay.size());
        if (!running) {
            return;
        }

        if (player.attachEnergy(pokemonInPlay.get(choice - 1))) {
            System.out.println("Energia adjuntada.");
        } else {
            System.out.println("No se pudo adjuntar energia.");
        }
    }

    private void playPlayableCard(Game game, Player player) {
        ArrayList<AbstractCard> playableCards = new ArrayList<>();
        for (AbstractCard card : player.getHand().getCards()) {
            if (card instanceof TrainerCard || card instanceof ObjectCard) {
                playableCards.add(card);
            }
        }
        if (playableCards.isEmpty()) {
            System.out.println("No tienes cartas de entrenador u objeto en la mano.");
            return;
        }

        System.out.println("\nCartas jugables (objetos sin limite; un soporte por turno):");
        for (int i = 0; i < playableCards.size(); i++) {
            System.out.println((i + 1) + ". " + playableCards.get(i).getName()
                    + " (" + playableCards.get(i).getCardType() + ")");
        }
        System.out.println("Elige la carta:");
        int choice = readChoice(1, playableCards.size());
        if (!running) {
            return;
        }

        AbstractCard card = playableCards.get(choice - 1);
        PokemonCard target = null;
        if ("Misty".equals(card.getName())) {
            ArrayList<PokemonCard> waterPokemon = new ArrayList<>();
            for (PokemonCard pokemon : getPokemonInPlay(player)) {
                if (pokemon.getType() == Type.WATER) {
                    waterPokemon.add(pokemon);
                }
            }
            if (waterPokemon.isEmpty()) {
                System.out.println("No tienes un Pokemon de agua en juego para usar Misty.");
                return;
            }
            System.out.println("Elige el Pokemon de agua que recibira energia:");
            for (int i = 0; i < waterPokemon.size(); i++) {
                System.out.println((i + 1) + ". " + waterPokemon.get(i));
            }
            System.out.println("Seleccion:");
            int targetChoice = readChoice(1, waterPokemon.size());
            if (!running) {
                return;
            }
            target = waterPokemon.get(targetChoice - 1);
        }

        if (player.playPlayableCard(card, game, target)) {
            System.out.println(card.getName() + " se jugo correctamente.");
        } else if (card instanceof TrainerCard
                && ((TrainerCard) card).isSupport()
                && player.hasPlayedTrainer()) {
            System.out.println("Ya jugaste una carta de entrenador durante este turno.");
        } else {
            System.out.println("No se pudo activar el efecto de " + card.getName() + ".");
        }
    }

    private void attack(Game game, Player player) {
        PokemonCard activePokemon = player.getActivePokemon();
        if (activePokemon == null) {
            System.out.println("No tienes un Pokemon activo.");
            return;
        }
        if (game.getTurnNumber() == 1) {
            System.out.println("No se puede atacar durante el primer turno.");
            return;
        }
        if (player.hasAttacked()) {
            System.out.println("Ya atacaste en este turno: solo se puede atacar una vez.");
            return;
        }

        ArrayList<Attack> attacks = activePokemon.getAttacks();
        if (attacks.isEmpty()) {
            System.out.println("El Pokemon activo no tiene ataques disponibles.");
            return;
        }

        System.out.println("\nAtaques de " + activePokemon.getName() + ":");
        for (int i = 0; i < attacks.size(); i++) {
            System.out.println((i + 1) + ". " + attacks.get(i));
        }
        System.out.println("Elige el ataque:");
        int choice = readChoice(1, attacks.size());
        if (!running) {
            return;
        }

        Attack chosenAttack = attacks.get(choice - 1);
        int missingEnergy = chosenAttack.getEnergyCost() - activePokemon.getAttachedEnergy();
        if (missingEnergy > 0) {
            System.out.println("Energia insuficiente: " + chosenAttack.getName() + " cuesta "
                    + chosenAttack.getEnergyCost() + " y " + activePokemon.getName() + " tiene "
                    + activePokemon.getAttachedEnergy() + ". Te faltan " + missingEnergy + ".");
            return;
        }

        Player opponent = game.getOpponentOf(player);
        PokemonCard target = opponent.getActivePokemon();
        if (target == null) {
            System.out.println("El rival no tiene un Pokemon activo.");
            return;
        }

        int healthBefore = target.getHealthPoints();
        if (player.attackOpponent(opponent, chosenAttack)) {
            System.out.println(activePokemon.getName() + " uso " + chosenAttack.getName()
                    + " y causo " + (healthBefore - target.getHealthPoints()) + " de dano a "
                    + target.getName() + " (" + target.getHealthPoints() + "/"
                    + target.getMaxHealthPoints() + " PS).");
            if (target.isDefeated()) {
                System.out.println(target.getName() + " fue derrotado. Ganas "
                        + target.calculatePrizePoints() + " punto(s) de premio.");
            }
        } else {
            System.out.println("No se pudo atacar.");
        }
    }

    private void placeOnBench(Player player) {
        ArrayList<PokemonCard> basicPokemon = player.getHand().getBasicPokemonCards();
        if (basicPokemon.isEmpty()) {
            System.out.println("No tienes Pokemon basicos en la mano.");
            return;
        }
        if (player.getBank().size() >= 2) {
            System.out.println("Tu banca esta llena: el maximo es 2 Pokemon.");
            return;
        }

        System.out.println("\nPokemon basicos en tu mano:");
        for (int i = 0; i < basicPokemon.size(); i++) {
            System.out.println((i + 1) + ". " + basicPokemon.get(i));
        }

        System.out.println("Elige el Pokemon para la banca:");
        int choice = readChoice(1, basicPokemon.size());
        if (!running) {
            return;
        }

        if (player.placeOnBench(basicPokemon.get(choice - 1))) {
            System.out.println("Pokemon colocado en la banca.");
        } else {
            System.out.println("No se pudo colocar ese Pokemon.");
        }
    }

    private void retreat(Player player) {
        if (player.getBank().isEmpty()) {
            System.out.println("No hay Pokemon en la banca para reemplazar al activo.");
            return;
        }
        PokemonCard activePokemon = player.getActivePokemon();
        if (activePokemon != null && activePokemon.getAttachedEnergy() < player.getActiveRetreatCost()) {
            System.out.println("No puedes retirar a " + activePokemon.getName() + ": cuesta "
                    + player.getActiveRetreatCost() + " energia y tiene "
                    + activePokemon.getAttachedEnergy() + " adjunta.");
            return;
        }

        System.out.println("\nElige el nuevo Pokemon activo:");
        for (int i = 0; i < player.getBank().size(); i++) {
            System.out.println((i + 1) + ". " + player.getBank().get(i));
        }

        System.out.println("Seleccion:");
        int choice = readChoice(1, player.getBank().size());
        if (!running) {
            return;
        }

        if (player.retreatActive(player.getBank().get(choice - 1))) {
            System.out.println("Retirada realizada.");
        } else {
            System.out.println("No se pudo retirar: revisa el costo y las energias adjuntas.");
        }
    }

    private void evolve(Player player) {
        ArrayList<PokemonCard> evolutions = getEvolutionsInHand(player);
        if (evolutions.isEmpty()) {
            System.out.println("No tienes cartas de evolucion en la mano.");
            return;
        }

        System.out.println("\nEvoluciones disponibles:");
        for (int i = 0; i < evolutions.size(); i++) {
            System.out.println((i + 1) + ". " + evolutions.get(i));
        }

        System.out.println("Elige la evolucion:");
        int choice = readChoice(1, evolutions.size());
        if (!running) {
            return;
        }

        if (player.evolvePokemon(evolutions.get(choice - 1))) {
            System.out.println("Evolucion realizada.");
        } else {
            System.out.println("Ningun Pokemon en juego cumple el requisito de evolucion.");
        }
    }

    private ArrayList<PokemonCard> getEvolutionsInHand(Player player) {
        ArrayList<PokemonCard> evolutions = new ArrayList<>();
        for (AbstractCard card : player.getHand().getCards()) {
            if (card instanceof PokemonCard) {
                PokemonCard pokemon = (PokemonCard) card;
                if (!pokemon.isBasicPokemon()) {
                    evolutions.add(pokemon);
                }
            }
        }
        return evolutions;
    }
//Campo de juego y mano
    private void showField(Game game) {

        Player current = game.getCurrentPlayer();
        Player opponent;
        if (current == game.getPlayerOne()) {
            opponent = game.getPlayerTwo();
        } else {
            opponent = game.getPlayerOne();
        }

        System.out.println("\n");
        System.out.println("TURNO " + game.getTurnNumber() + " — " + current.getName());
        System.out.println("\n");
        showPlayerField("RIVAL", opponent);
        showPlayerField("TU CAMPO", current);
        System.out.println("Punto de: " + current.getName() + ": " + current.getPrizePoints() + "/3");
        System.out.println("Energia disponible: " + current.getAvailableEnergy());
        showCurrentHand(current);
    }

    private void showPlayerField(String label, Player player) {
        System.out.println("\n--- " + label + " (" + player.getName() + ") ---");
        System.out.println("Puntos de: " + player.getName() + ": " + player.getPrizePoints() + "/3");
        PokemonCard activePokemon = player.getActivePokemon();
        if (activePokemon == null) {
            System.out.println("[Activo] ninguno");
        } else {
            System.out.println("[Activo] " + activePokemon
                    + " | Energia adjunta: " + activePokemon.getAttachedEnergy());
        }

        for (PokemonCard pokemon : player.getBank()) {
            System.out.println("[Banca] " + pokemon
                    + " | Energia adjunta: " + pokemon.getAttachedEnergy());
        }
    }

    private void showCurrentHand(Player player) {
        System.out.println("\n--- Mano de " + player.getName()
                + " (" + player.getHand().size() + " cartas) ---");
        for (int i = 0; i < player.getHand().getCards().size(); i++) {
            System.out.println((i + 1) + ". " + player.getHand().getCard(i).getName());
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

//comprobar ganador y finalizar partida
   private void playMatch(Game game) {

        while (running && !game.isFinished()) {
            if (!game.startTurn()) {
                break;
            }

            boolean turnEnded = playCurrentTurn(game);
            if (running && turnEnded && !game.isFinished()) {
                changeTurn(game);
            }
        }

        if (game.getWinner() != null) {
            clearConsole();
            showVictoryMessage(game);
        }
    }

    private void showVictoryMessage(Game game) {
        Player winner = game.getWinner();
        Player loser = game.getOpponentOf(winner);

        switch (game.getVictoryReason()) {
            case NO_POKEMON_IN_PLAY:
                System.out.println(loser.getName()
                        + " se quedo sin Pokemon en juego: no tiene Pokemon activo ni en la banca.");
                System.out.println("Aunque tenga Pokemon en su mano o mazo, no tiene ninguno en el campo.");
                break;
            case EMPTY_DECK:
                System.out.println(loser.getName()
                        + " no pudo robar una carta porque su mazo esta vacio.");
                break;
            case PRIZE_POINTS:
                System.out.println(winner.getName() + " alcanzo los 3 puntos de premio.");
                break;
            default:
                System.out.println("La partida ha terminado.");
                break;
        }
        System.out.println("¡Felicidades " + winner.getName() + ", ganas el duelo!");
    }

//captura de entrada y manejo de errores

    private int readChoice(int minimum, int maximum) {

        while (running) {
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
                System.out.println("Ingresa un numero valido.");
                System.out.println("Ingresa una opcion:");
                continue;
            }
            System.out.println("Elige un numero entre " + minimum + " y " + maximum + ".");
            System.out.println("Ingresa una opcion:");
        }
        return minimum;
    }

//metodos para esperar y limpiar la consola

    private void waitForEnter() {
        System.out.println("\nPresiona ENTER para continuar.");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        } else {
            running = false;
        }
    }

    private void pauseAndClear(String message) {
        System.out.println("\n" + message);
        if (scanner.hasNextLine()) {
            scanner.nextLine();
            clearConsole();
        } else {
            running = false;
        }
    }

    private void clearConsole() {
        if (System.console() != null && !System.getProperty("os.name").startsWith("Windows")) {
            System.out.flush();
        } else {
            for (int line = 0; line < 40; line++) {
                System.out.println();
            }
        }
    }

}