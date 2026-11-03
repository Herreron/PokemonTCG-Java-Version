package logic.battlesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import logic.cards.AbstractCard;
import logic.cards.Attack;
import logic.cards.ObjectCatalog;
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

    // Menú principal
    public void run() {
        running = true;
        clearConsole();

        while (running) {
            showMainMenu();
            int choice = readChoice("Selecciona una opcion: ", 0, 1);

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

    // Mantiene el menú de inicio separado del flujo de la partida.
    private void showMainMenu() {
        System.out.println("       POKEMON TCG POCKET Java Version");
        System.out.println("1. Iniciar partida rapida");
        System.out.println("0. Salir");
        System.out.println("-----------------------------------");
    }

    private void startQuickMatch() {
        // Se piden ambos nombres antes de crear jugadores o mazos.
        String firstName = readName("Nombre del jugador 1: ");
        if (!running) {
            return;
        }

        String secondName = readName("Nombre del jugador 2: ");
        // Los dos jugadores no pueden llamarse igual, para no confundir los turnos.
        while (running && secondName.equalsIgnoreCase(firstName)) {
            System.out.println("Ese nombre ya lo usa el jugador 1. Elige otro nombre.");
            secondName = readName("Nombre del jugador 2: ");
        }
        if (!running) {
            return;
        }

        // Creación clásica: la fábrica devuelve un Deck directamente, o null si falla.
        Deck firstDeck = DeckFactory.createRandomDeck(getAvailableCardNames());
        Deck secondDeck = DeckFactory.createRandomDeck(getAvailableCardNames());
        
        if (firstDeck == null || secondDeck == null) {
            System.out.println(
                    "No se pudo iniciar: el catalogo necesita mas cartas para crear mazos validos.");
            return;
        }

        Player firstPlayer = createPlayer(firstName, firstDeck);
        Player secondPlayer = createPlayer(secondName, secondDeck);
        
        Game game = new Game(firstPlayer, secondPlayer);
        if (!game.prepareInitialHands()) {
            System.out.println("No se pudieron repartir las manos iniciales.");
            return;
        }

        // La preparación se hace por separado y se limpia la consola entre jugadores.
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

    // Los catálogos se exponen como nombres; DeckFactory crea cada carta con su fábrica correspondiente.
    public List<String> getAvailableCardNames() {
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

    // Cada jugador ve y elige sus propias cartas iniciales sin dejar la mano visible al siguiente.
    private void setupInitialField(Player player) {
        clearConsole();
        System.out.println("===================================");
        System.out.println("Preparacion del campo: " + player.getName());
        System.out.println("===================================");

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

            int addToBank = readChoice(
                    "¿Quieres colocar un Pokemon basico en la banca? (1=si, 0=no): ", 0, 1);
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

    // Presenta solo los básicos que el jugador puede colocar como activo o en banca.
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

        int choice = readChoice("Seleccion: ", 1, basicPokemon.size());
        if (!running) {
            return null;
        }
        return basicPokemon.get(choice - 1);
    }

    private void playMatch(Game game) {
        // Game prepara cada turno; esta interfaz presenta opciones y delega acciones en Player.
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
            System.out.println("¡ Felicidades " + game.getWinner().getName() + " gana el duelo! \n");
        }
    }

    // Repite las acciones de un turno hasta que el jugador pasa o la partida termina.
    private boolean playCurrentTurn(Game game) {
        while (running && !game.isFinished()) {
            game.checkWinner();
            if (game.isFinished()) {
                return false;
            }

            Player currentPlayer = game.getCurrentPlayer();
            showField(game);
            showTurnMenu();
            int action = readChoice("Elige una accion: ", 1, 7);
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

    // Traduce la opción numérica a una acción del jugador sin concentrar el detalle del menú en el ciclo.
    private boolean executeAction(int action, Game game, Player player) {
        switch (action) {
            case 1:
                attachEnergy(player);
                return false;
            case 2:
                playTrainerCard(game, player);
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

    // Presenta solo los Pokémon propios para que el usuario elija dónde adjuntar la energía.
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

        int choice = readChoice("Seleccion: ", 1, pokemonInPlay.size());
        if (!running) {
            return;
        }

        if (player.attachEnergy(pokemonInPlay.get(choice - 1))) {
            System.out.println("Energia adjuntada.");
        } else {
            System.out.println("No se pudo adjuntar energia.");
        }
    }

    private void playTrainerCard(Game game, Player player) {
        ArrayList<TrainerCard> trainerCards = new ArrayList<>();
        for (AbstractCard card : player.getHand().getCards()) {
            if (card instanceof TrainerCard trainerCard) {
                trainerCards.add(trainerCard);
            }
        }
        if (trainerCards.isEmpty()) {
            System.out.println("No tienes cartas de entrenador en la mano.");
            return;
        }

        System.out.println("\nCartas de entrenador:");
        for (int i = 0; i < trainerCards.size(); i++) {
            System.out.println((i + 1) + ". " + trainerCards.get(i).getName()
                    + " (" + trainerCards.get(i).getTrainerType() + ")");
        }
        int choice = readChoice("Elige la carta: ", 1, trainerCards.size());
        if (!running) {
            return;
        }

        TrainerCard card = trainerCards.get(choice - 1);
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
            int targetChoice = readChoice("Seleccion: ", 1, waterPokemon.size());
            if (!running) {
                return;
            }
            target = waterPokemon.get(targetChoice - 1);
        }

        if (player.playTrainerCard(card, game, target)) {
            System.out.println(card.getName() + " se jugo correctamente.");
        } else if ("Soporte".equals(card.getTrainerType()) && player.hasPlayedSupport()) {
            System.out.println("Ya jugaste una carta de soporte durante este turno.");
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
        int choice = readChoice("Elige el ataque: ", 1, attacks.size());
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

        int choice = readChoice("Elige el Pokemon para la banca: ", 1, basicPokemon.size());
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

        int choice = readChoice("Seleccion: ", 1, player.getBank().size());
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

        int choice = readChoice("Elige la evolucion: ", 1, evolutions.size());
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
            if (card instanceof PokemonCard pokemon && !pokemon.isBasicPokemon()) {
                evolutions.add(pokemon);
            }
        }
        return evolutions;
    }

    private void showField(Game game) {
        // Solo se muestra la mano del jugador actual; la mano rival permanece privada.
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
        System.out.println("\nPremios propios: " + current.getPrizePoints() + "/3");
        System.out.println("Energia disponible: " + current.getAvailableEnergy());
        showCurrentHand(current);
    }

    private void showPlayerField(String label, Player player) {
        System.out.println("\n--- " + label + " (" + player.getName() + ") ---");
        System.out.println("Puntos de premio: " + player.getPrizePoints() + "/3");
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

    private String readName(String prompt) {
        // La validación y la lectura quedan juntas para no permitir nombres vacíos en la partida.
        while (running) {
            System.out.println(prompt);
            if (!scanner.hasNextLine()) {
                running = false;
                return "";
            }

            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                return name;
            }
            System.out.println("El nombre no puede estar vacio.");
        }
        return "";
    }

    private int readChoice(String prompt, int minimum, int maximum) {
        // Se vuelve a pedir la opción ante texto no numérico o valores fuera del menú.
        while (running) {
            System.out.println(prompt);
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
                continue;
            }
            System.out.println("Elige un numero entre " + minimum + " y " + maximum + ".");
        }
        return minimum;
    }

    // Deja leer el resultado de una accion antes de volver a mostrar el campo.
    private void waitForEnter() {
        System.out.println("\nPresiona ENTER para continuar.");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        } else {
            running = false;
        }
    }

    private void pauseAndClear(String message) {
        // La pausa permite entregar físicamente la consola antes de ocultar la información anterior.
        System.out.println("\n" + message);
        if (scanner.hasNextLine()) {
            scanner.nextLine();
            clearConsole();
        } else {
            running = false;
        }
    }

    private void clearConsole() {
        // ANSI funciona en terminales compatibles; en consolas sin soporte se desplaza el contenido.
        if (System.console() != null && !System.getProperty("os.name").startsWith("Windows")) {
            System.out.println("\033[H\033[2J");
            System.out.flush();
        } else {
            for (int line = 0; line < 40; line++) {
                System.out.println();
            }
        }
    }
}