package gui;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import logic.battlesystem.Deck;
import logic.battlesystem.DeckFactory;
import logic.battlesystem.Game;
import logic.battlesystem.Hand;
import logic.battlesystem.Player;
import logic.cards.AbstractCard;
import logic.cards.Attack;
import logic.cards.PokemonCard;
import logic.cards.TrainerCard;
import logic.cards.Type;

public class GameWindows extends JPanel {

    // Rutas de recursos
    private static final String BG_PATH = "/media/images/backgrounds/Bosque_Verde_HGSS.png";
    private static final String LOGO3_PATH = "/media/images/windowsAccessories/logo3.png";
    private static final String ENERGY_BASE_PATH = "/media/images/typesPokemons/typesEnergy/";

    // Colores principales
    private static final Color RED_COLOR = new Color(255, 56, 49);
    private static final Color BLUE_COLOR = new Color(14, 76, 117);
    private static final Color CARD_COLOR = new Color(208, 214, 222, 235);
    private static final Color SELECTION_BORDER_COLOR = new Color(255, 204, 0);

    // Modos de acción
    private static final String MODE_NONE = "NONE";
    private static final String MODE_ENERGY = "ENERGY";
    private static final String MODE_EVOLVE = "EVOLVE";

    // Componentes de la interfaz
    private Image backgroundImage;
    private JPanel topRedBar;
    private JPanel bottomRedBar;
    private JLabel logoLabel;

    // Marcadores y turno
    private RoundedPanel playerTwoScorePanel;
    private RoundedPanel playerOneScorePanel;
    private RoundedPanel turnPanel;
    private JLabel playerTwoScoreLabel;
    private JLabel playerOneScoreLabel;
    private JLabel turnLabel;

    // Espacios del campo (0 = activo, 1 = banca 1, 2 = banca 2)
    private PokemonSlotPanel playerTwoActiveSlot;
    private PokemonSlotPanel playerTwoBenchSlotOne;
    private PokemonSlotPanel playerTwoBenchSlotTwo;
    private PokemonSlotPanel playerOneActiveSlot;
    private PokemonSlotPanel playerOneBenchSlotOne;
    private PokemonSlotPanel playerOneBenchSlotTwo;

    // Mano desplazable
    private RoundedPanel handContainerPanel;
    private JPanel handCardsPanel;
    private JScrollPane handScrollPane;

    // Botones de acción y energía de campo
    private JButton energyButton;
    private JButton evolveButton;
    private JButton attackButton;
    private JButton retreatButton;
    private JButton endTurnButton;
    private RoundedPanel fieldEnergyPanel;
    private JLabel fieldEnergyTextLabel;
    private JLabel fieldEnergyIconLabel;

    // Estado de la partida
    private String playerOneName;
    private String playerOneDeck;
    private String playerTwoName;
    private String playerTwoDeck;
    private int playerOnePoints = 0;
    private int playerTwoPoints = 0;
    private boolean gameFinished;
    private int currentPlayer;
    private boolean fieldEnergyAvailable = true;
    private String currentFieldEnergy = "Fire";

    private List<VisualCardData> playerOneHand = new ArrayList<>();
    private List<VisualCardData> playerTwoHand = new ArrayList<>();

    private String currentMode = MODE_NONE;
    private JLabel selectedCardLabel = null;

    // Modelo del juego: aqui viven las reglas, la ventana solo lo consulta
    private Game game;
    private Player playerOne;
    private Player playerTwo;
    private boolean playerOneStarts;

    public GameWindows(String playerOneName, String playerOneDeck, String playerTwoName, String playerTwoDeck, boolean playerOneStarts) {
        this.playerOneName = playerOneName;
        this.playerOneDeck = playerOneDeck;
        this.playerTwoName = playerTwoName;
        this.playerTwoDeck = playerTwoDeck;
        if (playerOneStarts) {
            this.currentPlayer = 1;
            this.currentFieldEnergy = mapDeckToEnergy(playerOneDeck);
        } else {
            this.currentPlayer = 2;
            this.currentFieldEnergy = mapDeckToEnergy(playerTwoDeck);
        }

        this.playerOneStarts = playerOneStarts;
        loadBackground();
        initComponents();
        BackgroundMusic.playBattle();
    }

    // Empieza la partida. Se llama después de mostrar el tablero en la ventana,
    // así las ventanitas de preparación quedan encima del juego y no se esconden
    public void startMatch() {
        setupMatch(playerOneStarts);
        refreshBoardFromGame();
    }

    // Crea la partida real con los nombres y mazos elegidos en la ventana de inicio
    private void setupMatch(boolean playerOneStarts) {
        playerOne = new Player(playerOneName, createDeckFromName(playerOneDeck), new Hand(),
                new ArrayList<>(), null, (byte) 0, (byte) 0);
        playerTwo = new Player(playerTwoName, createDeckFromName(playerTwoDeck), new Hand(),
                new ArrayList<>(), null, (byte) 0, (byte) 0);
        game = new Game(playerOne, playerTwo);

        // Reparte 5 cartas a cada uno; el mulligan lo hace el modelo (Deck)
        if (!game.prepareInitialHands()) {
            JOptionPane.showMessageDialog(this, "No se pudieron repartir las manos iniciales.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cada jugador coloca su Pokémon activo y su banca (RF2)
        chooseInitialField(playerOne);
        chooseInitialField(playerTwo);

        // El resultado de la moneda decide quién empieza
        game.setCurrentPlayer(playerOneStarts ? playerOne : playerTwo);
        game.startTurn();
    }

    // Traduce el nombre del mazo del JComboBox al mazo del modelo
    private Deck createDeckFromName(String deckName) {
        if (deckName == null) {
            return null;
        }
        switch (deckName) {
            case "Fuego":
                return DeckFactory.createFireDeck();
            case "Agua":
                return DeckFactory.createWaterDeck();
            case "Eléctrico":
                return DeckFactory.createElectricDeck();
            case "Psíquico":
                return DeckFactory.createPsychicDeck();
            default:
                return null;
        }
    }

    // Pide al jugador elegir su Pokémon activo y hasta 2 Pokémon para la banca
    private void chooseInitialField(Player player) {
        JOptionPane.showMessageDialog(this,
                "Preparación de " + player.getName() + ".\nQue el otro jugador no mire la pantalla.",
                "Preparación", JOptionPane.INFORMATION_MESSAGE);

        // Activo obligatorio: se repite hasta que elija uno
        PokemonCard active = null;
        while (active == null) {
            active = askBasicPokemon(player, "Elige tu Pokémon activo:", false);
        }
        player.selectInitialActivePokemon(active);

        // Banca opcional: hasta 2 Pokémon o hasta que el jugador termine
        while (player.getBank().size() < 2 && !player.getHand().getBasicPokemonCards().isEmpty()) {
            PokemonCard benchPokemon = askBasicPokemon(player, "¿Quieres poner un Pokémon en la banca?", true);
            if (benchPokemon == null) {
                break;
            }
            player.placeOnBench(benchPokemon);
        }
    }

    // Muestra los Pokémon básicos de la mano como botones y devuelve el elegido
    private PokemonCard askBasicPokemon(Player player, String message, boolean allowFinish) {
        ArrayList<PokemonCard> basics = player.getHand().getBasicPokemonCards();
        int optionCount = allowFinish ? basics.size() + 1 : basics.size();
        String[] options = new String[optionCount];
        for (int i = 0; i < basics.size(); i++) {
            PokemonCard pokemon = basics.get(i);
            options[i] = pokemon.getName() + " (" + pokemon.getHealthPoints() + " PS)";
        }
        if (allowFinish) {
            options[optionCount - 1] = "Terminar";
        }

        int choice = JOptionPane.showOptionDialog(this, message, player.getName(),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice < 0 || choice >= basics.size()) {
            return null;
        }
        return basics.get(choice);
    }

    // Lee el estado del modelo y lo pinta en el tablero
    private void refreshBoardFromGame() {
        if (game == null || game.getCurrentPlayer() == null) {
            return;
        }
        loadPlayerField(1, playerOne, playerOneDeck);
        loadPlayerField(2, playerTwo, playerTwoDeck);

        currentPlayer = (game.getCurrentPlayer() == playerOne) ? 1 : 2;
        currentFieldEnergy = mapDeckToEnergy(getCurrentPlayerDeck());
        fieldEnergyAvailable = game.getCurrentPlayer().getAvailableEnergy() > 0;
        clearSelection();
        updateTurnVisualState();
    }

    // Pasa el activo, la banca, la mano y los puntos de un jugador a la ventana
    private void loadPlayerField(int playerNumber, Player player, String deckName) {
        String energyType = mapDeckToEnergy(deckName);
        ArrayList<PokemonCard> bank = player.getBank();

        setPokemonSlot(playerNumber, 0, toVisualPokemon(player.getActivePokemon(), energyType));
        setPokemonSlot(playerNumber, 1, bank.size() > 0 ? toVisualPokemon(bank.get(0), energyType) : null);
        setPokemonSlot(playerNumber, 2, bank.size() > 1 ? toVisualPokemon(bank.get(1), energyType) : null);
        setPlayerHand(playerNumber, toVisualHand(player.getHand()));
        setPlayerPoints(playerNumber, player.getPrizePoints());
    }

    // Convierte una carta Pokémon del modelo en los datos que dibuja la ventana
    private VisualPokemonData toVisualPokemon(PokemonCard pokemon, String energyType) {
        if (pokemon == null) {
            return null;
        }
        ArrayList<Attack> attackList = pokemon.getAttacks();
        String[] attackNames = new String[attackList.size()];
        int[] attackDamages = new int[attackList.size()];
        int[] energyCosts = new int[attackList.size()];
        for (int i = 0; i < attackList.size(); i++) {
            attackNames[i] = attackList.get(i).getName();
            attackDamages[i] = attackList.get(i).getBaseDamage();
            energyCosts[i] = attackList.get(i).getEnergyCost();
        }

        VisualPokemonData data = new VisualPokemonData(pokemon.getName(), pokemon.getImagePath(),
                pokemon.getAudioPath(),
                pokemon.getHealthPoints(), pokemon.getMaxHealthPoints(), pokemon.getRetreatCost(),
                attackNames, attackDamages, energyCosts);
        data.energyType = energyType;
        data.energyCount = pokemon.getAttachedEnergy();
        return data;
    }

    // Convierte la mano del modelo en la lista de cartas que dibuja la ventana
    private List<VisualCardData> toVisualHand(Hand hand) {
        List<VisualCardData> visualHand = new ArrayList<>();
        for (AbstractCard card : hand.getCards()) {
            if (card instanceof PokemonCard) {
                PokemonCard pokemon = (PokemonCard) card;
                VisualPokemonData pokemonData = toVisualPokemon(pokemon, "");
                visualHand.add(new VisualCardData(pokemon.getName(), "POKEMON", pokemonData.spritePath,
                        pokemonData.audioPath,
                        pokemonData.currentHp, pokemonData.maxHp, pokemonData.retreatCost,
                        pokemon.getEvolvesFrom(), pokemonData.attacks, pokemonData.attackDamages,
                        pokemonData.energyCosts));
            } else {
                String category = (card instanceof TrainerCard) ? "ENTRENADOR" : "OBJETO";
                visualHand.add(new VisualCardData(card.getName(), category, buildSpritePath(card), null,
                        0, 0, 0, null, null, null, null));
            }
        }
        return visualHand;
    }

    // Arma la ruta de la imagen de cada carta (temporal hasta que las cartas traigan su propia ruta)
    private String buildSpritePath(AbstractCard card) {
        if (card instanceof PokemonCard) {
            PokemonCard pokemon = (PokemonCard) card;
            return pokemon.getImagePath();
        }
        switch (card.getName()) {
            case "Pocion":
                return "/media/images/objects/potion.png";
            case "Poke Ball":
                return "/media/images/objects/pokeball.png";
            case "Turbo Velocidad":
                return "/media/images/objects/x-speed.png";
            case "Investigacion del Profesor":
                return "/media/images/trainers/oak.png";
            case "Misty":
                return "/media/images/trainers/misty.png";
            default:
                return null;
        }
    }

    private void loadBackground() {
        URL resourceUrl = getClass().getResource(BG_PATH);
        if (resourceUrl != null) {
            backgroundImage = new ImageIcon(resourceUrl).getImage();
        } else {
            System.err.println("Background image resource not found: " + BG_PATH);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 26, getWidth(), 418, this);
        }
    }

    private void initComponents() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 500));

        // Barras superior e inferior
        topRedBar = new JPanel();
        topRedBar.setBackground(RED_COLOR);
        topRedBar.setBounds(0, 0, 800, 26);
        add(topRedBar);

        bottomRedBar = new JPanel();
        bottomRedBar.setBackground(RED_COLOR);
        bottomRedBar.setLayout(null);
        bottomRedBar.setBounds(0, 444, 800, 56);
        add(bottomRedBar);

        logoLabel = new JLabel();
        ImageIcon logoIcon = loadIcon(LOGO3_PATH, 215, 50);
        if (logoIcon != null) logoLabel.setIcon(logoIcon);
        logoLabel.setBounds(10, 3, 215, 50);
        bottomRedBar.add(logoLabel);

        // Energía del campo
        fieldEnergyPanel = new RoundedPanel(20, CARD_COLOR);
        fieldEnergyPanel.setLayout(null);
        fieldEnergyPanel.setBounds(455, 8, 195, 40);
        bottomRedBar.add(fieldEnergyPanel);

        fieldEnergyTextLabel = new JLabel("Energía del campo", SwingConstants.CENTER);
        fieldEnergyTextLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        fieldEnergyTextLabel.setBounds(8, 10, 120, 20);
        fieldEnergyPanel.add(fieldEnergyTextLabel);

        fieldEnergyIconLabel = new JLabel("", SwingConstants.CENTER);
        fieldEnergyIconLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        fieldEnergyIconLabel.setBounds(130, 8, 56, 24);
        fieldEnergyPanel.add(fieldEnergyIconLabel);

        // Botón para pasar el turno
        endTurnButton = createBlueButton("Pasar turno", 15);
        endTurnButton.setBounds(660, 6, 130, 44);
        bottomRedBar.add(endTurnButton);

        // Marcadores y turno
        playerTwoScorePanel = new RoundedPanel(25, CARD_COLOR);
        playerTwoScorePanel.setLayout(null);
        playerTwoScorePanel.setBounds(12, 32, 205, 34);
        add(playerTwoScorePanel);

        playerTwoScoreLabel = new JLabel(playerTwoName + ": 0/3", SwingConstants.CENTER);
        playerTwoScoreLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        playerTwoScoreLabel.setBounds(5, 4, 195, 26);
        playerTwoScorePanel.add(playerTwoScoreLabel);

        turnPanel = new RoundedPanel(20, BLUE_COLOR);
        turnPanel.setLayout(null);
        turnPanel.setBounds(305, 34, 190, 30);
        add(turnPanel);

        turnLabel = new JLabel("", SwingConstants.CENTER);
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        turnLabel.setBounds(5, 3, 180, 24);
        turnPanel.add(turnLabel);

        playerOneScorePanel = new RoundedPanel(25, CARD_COLOR);
        playerOneScorePanel.setLayout(null);
        playerOneScorePanel.setBounds(583, 32, 205, 34);
        add(playerOneScorePanel);

        playerOneScoreLabel = new JLabel(playerOneName + ": 0/3", SwingConstants.CENTER);
        playerOneScoreLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        playerOneScoreLabel.setBounds(5, 4, 195, 26);
        playerOneScorePanel.add(playerOneScoreLabel);

        // Espacios del jugador 2 (izquierda)
        playerTwoBenchSlotOne = new PokemonSlotPanel(2, 1);
        playerTwoBenchSlotOne.setBounds(15, 74, 158, 84);
        add(playerTwoBenchSlotOne);

        playerTwoBenchSlotTwo = new PokemonSlotPanel(2, 2);
        playerTwoBenchSlotTwo.setBounds(15, 165, 158, 84);
        add(playerTwoBenchSlotTwo);

        addFieldLabel("Pokémon en banca", 32, 253, 124);

        playerTwoActiveSlot = new PokemonSlotPanel(2, 0);
        playerTwoActiveSlot.setBounds(188, 130, 168, 92);
        add(playerTwoActiveSlot);

        addFieldLabel("Pokémon activo", 217, 226, 110);

        // Espacios del jugador 1 (derecha)
        playerOneActiveSlot = new PokemonSlotPanel(1, 0);
        playerOneActiveSlot.setBounds(444, 130, 168, 92);
        add(playerOneActiveSlot);

        addFieldLabel("Pokémon activo", 473, 226, 110);

        playerOneBenchSlotOne = new PokemonSlotPanel(1, 1);
        playerOneBenchSlotOne.setBounds(627, 74, 158, 84);
        add(playerOneBenchSlotOne);

        playerOneBenchSlotTwo = new PokemonSlotPanel(1, 2);
        playerOneBenchSlotTwo.setBounds(627, 165, 158, 84);
        add(playerOneBenchSlotTwo);

        addFieldLabel("Pokémon en banca", 644, 253, 124);

        // Mano con barra de desplazamiento
        addFieldLabel("Mano", 15, 336, 75);

        handContainerPanel = new RoundedPanel(25, CARD_COLOR);
        handContainerPanel.setLayout(null);
        handContainerPanel.setBounds(15, 360, 590, 76);
        add(handContainerPanel);

        handCardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        handCardsPanel.setOpaque(false);

        handScrollPane = new JScrollPane(handCardsPanel);
        handScrollPane.setOpaque(false);
        handScrollPane.getViewport().setOpaque(false);
        handScrollPane.setBorder(null);
        handScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        handScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        handScrollPane.setBounds(10, 4, 570, 68);
        handContainerPanel.add(handScrollPane);

        // Botones de acción
        energyButton = createBlueButton("Energía", 12);
        energyButton.setBounds(615, 360, 84, 35);
        add(energyButton);

        evolveButton = createBlueButton("Evolucionar", 12);
        evolveButton.setBounds(704, 360, 86, 35);
        add(evolveButton);

        attackButton = createBlueButton("Atacar", 12);
        attackButton.setBounds(615, 401, 84, 35);
        add(attackButton);

        retreatButton = createBlueButton("Retirada", 12);
        retreatButton.setBounds(704, 401, 86, 35);
        add(retreatButton);

        configureButtonActions();
        updateTurnVisualState();
    }

    private void addFieldLabel(String text, int x, int y, int width) {
        RoundedPanel pill = new RoundedPanel(16, CARD_COLOR);
        pill.setLayout(null);
        pill.setBounds(x, y, width, 20);
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setBounds(2, 2, width - 4, 16);
        pill.add(lbl);
        add(pill);
    }

    private JButton createBlueButton(String text, int fontSize) {
        JButton btn = new JButton(text);
        btn.setBackground(BLUE_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        return btn;
    }


    private void configureButtonActions() {
        energyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameFinished) return;
                // Si ya no queda energía, el modelo lo explica antes de elegir Pokémon
                Player current = game.getCurrentPlayer();
                String error = current.getAttachEnergyError(current.getActivePokemon());
                if (error != null && current.getAvailableEnergy() <= 0) {
                    showWarning(error);
                    return;
                }
                toggleActionMode(MODE_ENERGY, energyButton);
            }
        });

        evolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameFinished) return;
                toggleActionMode(MODE_EVOLVE, evolveButton);
                if (currentMode.equals(MODE_EVOLVE)) {
                    JOptionPane.showMessageDialog(GameWindows.this, "Selecciona un Pokémon de tu equipo para evolucionarlo.", "Evolucionar", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        retreatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameFinished) return;
                clearSelection();
                retreatActivePokemon();
            }
        });

        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameFinished) return;
                clearSelection();
                attackWithActivePokemon();
            }
        });

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameFinished) return;

                if (game.getCurrentPlayer().getActivePokemon() == null) {
                    JOptionPane.showMessageDialog(GameWindows.this, "El juego no puede seguir si tu puesto activo está vacío.", "Puesto activo vacío", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                clearSelection();
                // El modelo cambia de jugador y empieza su turno (roba carta y recibe energía)
                game.endTurn();
                boolean turnStarted = game.startTurn();
                refreshBoardFromGame();

                // Si no pudo robar carta o alguien ya ganó, termina la partida
                if (!turnStarted && game.isFinished() && game.getWinner() != null) {
                    finishGame(game.getWinner().getName());
                }
            }
        });
    }

    // Muestra el aviso de por qué una acción no se puede hacer
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    // Busca en el modelo el Pokémon que corresponde a un espacio del tablero
    private PokemonCard getPokemonInSlot(PokemonSlotPanel slot) {
        Player owner = (slot.owner == 1) ? playerOne : playerTwo;
        if (slot.slotType == 0) {
            return owner.getActivePokemon();
        }
        int benchIndex = slot.slotType - 1;
        if (benchIndex < owner.getBank().size()) {
            return owner.getBank().get(benchIndex);
        }
        return null;
    }

    // Ataque: el jugador elige el ataque y el modelo calcula daño, debilidad y puntos
    private void attackWithActivePokemon() {
        Player current = game.getCurrentPlayer();
        Player opponent = game.getOpponentOf(current);
        PokemonCard attacker = current.getActivePokemon();
        if (attacker == null || attacker.getAttacks().isEmpty()) {
            showWarning(current.getAttackError(opponent, null));
            return;
        }

        ArrayList<Attack> attacks = attacker.getAttacks();
        String[] options = new String[attacks.size()];
        for (int i = 0; i < attacks.size(); i++) {
            Attack attack = attacks.get(i);
            options[i] = attack.getName() + " (" + attack.getBaseDamage() + " de daño - Costo: " + attack.getEnergyCost() + ")";
        }
        int choice = JOptionPane.showOptionDialog(this, "Selecciona el ataque de " + attacker.getName() + ":",
                "Atacar", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice < 0) {
            return;
        }

        Attack attack = attacks.get(choice);
        String error = current.getAttackError(opponent, attack);
        if (error != null) {
            showWarning(error);
            return;
        }

        PokemonCard defender = opponent.getActivePokemon();
        // El modelo calcula el daño (base + debilidad) antes de atacar
        int damage = current.getAttackDamage(opponent, attack);
        int pointsBefore = current.getPrizePoints();
        current.attackOpponent(opponent, attack);

        String result = attacker.getName() + " usó " + attack.getName() + " y le hizo " + damage + " de daño a " + defender.getName() + ".";
        if (damage > attack.getBaseDamage()) {
            result += "\n¡Es débil a este tipo: +" + (damage - attack.getBaseDamage()) + " de daño!";
        }
        if (defender.isDefeated()) {
            int pointsWon = current.getPrizePoints() - pointsBefore;
            result += "\n" + defender.getName() + " fue derrotado. " + current.getName() + " gana " + pointsWon + " punto(s) de premio.";
        }
        JOptionPane.showMessageDialog(this, result, "Ataque", JOptionPane.INFORMATION_MESSAGE);

        refreshBoardFromGame();
        if (game.checkWinner()) {
            finishGame(game.getWinner().getName());
            return;
        }
        // Si el rival se quedó sin activo, debe subir uno de su banca
        if (opponent.getActivePokemon() == null) {
            promoteFromBench(opponent);
            refreshBoardFromGame();
        }
    }

    // El jugador elige qué Pokémon de su banca pasa a ser el activo (obligatorio)
    private void promoteFromBench(Player player) {
        ArrayList<PokemonCard> bench = player.getBank();
        String[] options = new String[bench.size()];
        for (int i = 0; i < bench.size(); i++) {
            options[i] = bench.get(i).getName() + " (" + bench.get(i).getHealthPoints() + " PS)";
        }
        int choice = -1;
        while (choice < 0) {
            choice = JOptionPane.showOptionDialog(this, player.getName() + ", elige tu nuevo Pokémon activo:",
                    "Nuevo Pokémon activo", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        }
        player.promotePokemon(bench.get(choice));
    }

    // Retirada: el jugador elige el reemplazo y el modelo cobra el costo en energía
    private void retreatActivePokemon() {
        Player current = game.getCurrentPlayer();
        ArrayList<PokemonCard> bench = current.getBank();
        if (bench.isEmpty()) {
            showWarning(current.getRetreatError(null));
            return;
        }

        String[] options = new String[bench.size()];
        for (int i = 0; i < bench.size(); i++) {
            options[i] = bench.get(i).getName() + " (" + bench.get(i).getHealthPoints() + " PS)";
        }
        int choice = JOptionPane.showOptionDialog(this,
                "¿Qué Pokémon de la banca reemplaza a " + current.getActivePokemon().getName()
                        + "? (costo: " + current.getActiveRetreatCost() + " energía)",
                "Retirada", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice < 0) {
            return;
        }

        PokemonCard replacement = bench.get(choice);
        String error = current.getRetreatError(replacement);
        if (error != null) {
            showWarning(error);
            return;
        }
        current.retreatActive(replacement);
        refreshBoardFromGame();
    }

    private void toggleActionMode(String newMode, JButton targetButton) {
        if (currentMode.equals(newMode)) {
            clearSelection();
        } else {
            clearSelection();
            currentMode = newMode;
            targetButton.setBorder(BorderFactory.createLineBorder(SELECTION_BORDER_COLOR, 3));
        }
    }

    private void clearSelection() {
        currentMode = MODE_NONE;
        if (selectedCardLabel != null) {
            selectedCardLabel.setBorder(null);
            selectedCardLabel = null;
        }
        energyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        evolveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        retreatButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    // Clic sobre un Pokémon del tablero: según el modo, adjunta energía o evoluciona
    private void handleFieldSlotClick(PokemonSlotPanel slot) {
        if (gameFinished) return;
        Player current = game.getCurrentPlayer();
        boolean isAlly = (slot.owner == currentPlayer);
        PokemonCard pokemon = getPokemonInSlot(slot);

        if (currentMode.equals(MODE_ENERGY)) {
            if (!isAlly || pokemon == null) {
                showWarning("Elige uno de tus Pokémon para adjuntarle la energía.");
                return;
            }
            String error = current.getAttachEnergyError(pokemon);
            if (error != null) {
                showWarning(error);
            } else {
                current.attachEnergy(pokemon);
            }
            refreshBoardFromGame();
            return;
        }

        if (currentMode.equals(MODE_EVOLVE)) {
            if (!isAlly || pokemon == null) {
                showWarning("Elige uno de tus Pokémon para evolucionarlo.");
                return;
            }
            // Busca en la mano una carta que diga "evoluciona de" este Pokémon
            PokemonCard evolution = null;
            for (AbstractCard card : current.getHand().getCards()) {
                if (card instanceof PokemonCard && pokemon.getName().equals(((PokemonCard) card).getEvolvesFrom())) {
                    evolution = (PokemonCard) card;
                    break;
                }
            }
            if (evolution == null) {
                showWarning("No tienes en la mano una evolución de " + pokemon.getName() + ".");
                clearSelection();
                return;
            }
            String error = current.getEvolveError(evolution, pokemon);
            if (error != null) {
                showWarning(error);
            } else {
                current.evolvePokemon(evolution, pokemon);
                JOptionPane.showMessageDialog(this, "¡" + pokemon.getName() + " evolucionó a " + evolution.getName() + "!",
                        "Evolución", JOptionPane.INFORMATION_MESSAGE);
            }
            refreshBoardFromGame();
            return;
        }

        // Reproducir el grito al hacer clic en un Pokémon
        if (slot.data != null) {
            playAudio(slot.data.audioPath);
        }
    }

    // Clic sobre una carta de la mano: Pokémon básico a la banca o carta de entrenador
    private void handleHandCardClick(int index, JLabel cardLabel) {
        if (gameFinished) return;
        clearSelection();
        Player current = game.getCurrentPlayer();
        AbstractCard card = current.getHand().getCard(index);
        if (card == null) {
            return;
        }

        if (card instanceof PokemonCard) {
            PokemonCard pokemon = (PokemonCard) card;
            if (!pokemon.isBasicPokemon()) {
                showWarning("Para usar " + pokemon.getName() + ", toca el botón Evolucionar y luego el Pokémon que quieres evolucionar.");
                return;
            }
            int confirmation = JOptionPane.showConfirmDialog(this, "¿Poner a " + pokemon.getName() + " en la banca?",
                    "Banca", JOptionPane.YES_NO_OPTION);
            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }
            String error = current.getPlaceOnBenchError(pokemon);
            if (error != null) {
                showWarning(error);
                return;
            }
            current.placeOnBench(pokemon);
            refreshBoardFromGame();
            return;
        }

        // Cartas de entrenador y objetos
        int confirmation = JOptionPane.showConfirmDialog(this, "¿Deseas usar " + card.getName() + "?",
                "Usar carta", JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }
        String error = current.getPlayCardError(card);
        if (error != null) {
            showWarning(error);
            return;
        }

        // Misty necesita que el jugador elija a cuál de sus Pokémon de Agua le da la energía
        PokemonCard target = null;
        if ("Misty".equals(card.getName())) {
            target = chooseWaterPokemon(current);
            if (target == null) {
                return;
            }
        }

        if (!current.playPlayableCard(card, game, target)) {
            showWarning(card.getName() + " no tuvo efecto en este momento.");
        }
        refreshBoardFromGame();
    }

    // Muestra los Pokémon de Agua en juego para elegir el objetivo de Misty
    private PokemonCard chooseWaterPokemon(Player player) {
        ArrayList<PokemonCard> waterPokemon = new ArrayList<>();
        if (player.getActivePokemon() != null && player.getActivePokemon().getType() == Type.WATER) {
            waterPokemon.add(player.getActivePokemon());
        }
        for (PokemonCard pokemon : player.getBank()) {
            if (pokemon.getType() == Type.WATER) {
                waterPokemon.add(pokemon);
            }
        }
        if (waterPokemon.isEmpty()) {
            showWarning("No tienes Pokémon de Agua en juego para usar Misty.");
            return null;
        }
        String[] options = new String[waterPokemon.size()];
        for (int i = 0; i < waterPokemon.size(); i++) {
            options[i] = waterPokemon.get(i).getName();
        }
        int choice = JOptionPane.showOptionDialog(this, "¿A qué Pokémon de Agua le das la energía?", "Misty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        return choice < 0 ? null : waterPokemon.get(choice);
    }

    private void updateTurnVisualState() {
        String currentPlayerName = getCurrentPlayerName();
        turnLabel.setText("Turno: " + currentPlayerName);
        updateFieldEnergyIcon();
        refreshHandVisual();
    }

    private void updateFieldEnergyIcon() {
        if (!fieldEnergyAvailable) {
            fieldEnergyIconLabel.setIcon(null);
            fieldEnergyIconLabel.setText("Usada");
            return;
        }
        fieldEnergyIconLabel.setText("");
        ImageIcon icon = loadIcon(ENERGY_BASE_PATH + currentFieldEnergy + ".png", 52, 22);
        if (icon != null) {
            fieldEnergyIconLabel.setIcon(icon);
        } else {
            fieldEnergyIconLabel.setText(getEnergyDisplayName(currentFieldEnergy).toUpperCase());
        }
    }

    private void updateScoreMarkers() {
        playerOneScoreLabel.setText(playerOneName + ": " + playerOnePoints + "/3");
        playerTwoScoreLabel.setText(playerTwoName + ": " + playerTwoPoints + "/3");
    }

    private void finishGame(String winner) {
        gameFinished = true;
        clearSelection();
        energyButton.setEnabled(false);
        evolveButton.setEnabled(false);
        retreatButton.setEnabled(false);
        attackButton.setEnabled(false);
        endTurnButton.setEnabled(false);
        BackgroundMusic.stop();
        JOptionPane.showMessageDialog(this, "¡" + winner + " gana la partida!", "Partida terminada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshHandVisual() {
        handCardsPanel.removeAll();
        List<VisualCardData> currentHand = getCurrentHand();

        for (int i = 0; i < currentHand.size(); i++) {
            final int index = i;
            VisualCardData card = currentHand.get(i);
            final JLabel spriteLabel = new JLabel();
            spriteLabel.setPreferredSize(new Dimension(54, 54));
            spriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
            spriteLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            spriteLabel.setToolTipText(card.name);

            ImageIcon icon = cargarSprite(card.spritePath, 50, 50);
            if (icon != null) {
                spriteLabel.setIcon(icon);
            } else {
                spriteLabel.setText(card.name);
            }

            spriteLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleHandCardClick(index, spriteLabel);
                }
            });

            handCardsPanel.add(spriteLabel);
        }

        handCardsPanel.revalidate();
        handCardsPanel.repaint();
    }

    private List<VisualCardData> getCurrentHand() {
        if (currentPlayer == 1) {
            return playerOneHand;
        }
        return playerTwoHand;
    }

    private String getCurrentPlayerName() {
        if (currentPlayer == 1) {
            return playerOneName;
        }
        return playerTwoName;
    }

    private String getCurrentPlayerDeck() {
        if (currentPlayer == 1) {
            return playerOneDeck;
        }
        return playerTwoDeck;
    }

    private String mapDeckToEnergy(String deck) {
        if (deck == null) {
            return "Normal";
        }
        String deckName = deck.toLowerCase();
        if (deckName.contains("fuego")) {
            return "Fire";
        }
        if (deckName.contains("agua")) {
            return "Water";
        }
        if (deckName.contains("el")) {
            return "Electric";
        }
        if (deckName.contains("ps")) {
            return "Psychic";
        }
        return "Normal";
    }

    private String getEnergyDisplayName(String energyType) {
        if (energyType == null) {
            return "";
        }
        if (energyType.equalsIgnoreCase("Fire")) {
            return "Fuego";
        }
        if (energyType.equalsIgnoreCase("Water")) {
            return "Agua";
        }
        if (energyType.equalsIgnoreCase("Electric")) {
            return "Eléctrica";
        }
        if (energyType.equalsIgnoreCase("Psychic")) {
            return "Psíquica";
        }
        return energyType;
    }

    private void playAudio(String audioPath) {
        if (audioPath == null || audioPath.isEmpty()) {
            return;
        }
        URL audioResource = getClass().getResource(audioPath);
        if (audioResource == null) {
            System.err.println("Audio resource not found: " + audioPath);
            return;
        }
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioResource)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exception) {
            System.err.println("Unable to play audio " + audioPath + ": " + exception.getMessage());
        }
    }

    private ImageIcon loadIcon(String path, int maxWidth, int maxHeight) {
        URL resourceUrl = getClass().getResource(path);
        if (resourceUrl == null) {
            System.err.println("Icon resource not found: " + path);
            return null;
        }
        ImageIcon originalIcon = new ImageIcon(resourceUrl);
        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();
        if (originalWidth <= 0 || originalHeight <= 0) {
            System.err.println("Invalid icon dimensions: " + path);
            return null;
        }
        double scale = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);
        int scaledWidth = (int) (originalWidth * scale);
        int scaledHeight = (int) (originalHeight * scale);
        Image scaledImage = originalIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private ImageIcon cargarSprite(String ruta, int maxAncho, int maxAlto) {
        if (ruta == null || ruta.isEmpty()) {
            System.err.println("Sprite path is empty.");
            return null;
        }
        if (maxAncho <= 0 || maxAlto <= 0) {
            System.err.println("Sprite dimensions must be positive for: " + ruta);
            return null;
        }

        URL resourceUrl = getClass().getResource(ruta);
        if (resourceUrl == null) {
            System.err.println("Sprite resource not found: " + ruta);
            return null;
        }

        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(resourceUrl);
        } catch (IOException exception) {
            System.err.println("Unable to read sprite " + ruta + ": " + exception.getMessage());
            return null;
        }
        if (originalImage == null) {
            System.err.println("Unsupported sprite format: " + ruta);
            return null;
        }

        int minX = originalImage.getWidth();
        int minY = originalImage.getHeight();
        int maxX = -1;
        int maxY = -1;
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int alpha = (originalImage.getRGB(x, y) >>> 24) & 0xFF;
                if (alpha > 0) {
                    if (x < minX) {
                        minX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            System.err.println("Sprite contains no visible pixels: " + ruta);
            return null;
        }

        BufferedImage croppedImage = originalImage.getSubimage(
                minX, minY, maxX - minX + 1, maxY - minY + 1);
        double scale = Math.min((double) maxAncho / croppedImage.getWidth(),
                (double) maxAlto / croppedImage.getHeight());
        int scaledWidth = Math.max(1, (int) Math.round(croppedImage.getWidth() * scale));
        int scaledHeight = Math.max(1, (int) Math.round(croppedImage.getHeight() * scale));
        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaledImage.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            graphics.drawImage(croppedImage, 0, 0, scaledWidth, scaledHeight, null);
        } finally {
            graphics.dispose();
        }
        return new ImageIcon(scaledImage);
    }

    public void setPokemonSlot(int playerNumber, int position, VisualPokemonData data) {
        PokemonSlotPanel targetSlot = getSlot(playerNumber, position);
        if (targetSlot != null) {
            targetSlot.setPokemon(data);
        }
    }

    public void setPlayerHand(int playerNumber, List<VisualCardData> cards) {
        if (playerNumber == 1) {
            this.playerOneHand = cards;
        } else {
            this.playerTwoHand = cards;
        }
        if (this.currentPlayer == playerNumber) {
            refreshHandVisual();
        }
    }

    public void setPlayerPoints(int playerNumber, int points) {
        if (playerNumber == 1) {
            this.playerOnePoints = points;
        } else {
            this.playerTwoPoints = points;
        }
        updateScoreMarkers();
    }

    private PokemonSlotPanel getSlot(int playerNumber, int position) {
        if (playerNumber == 1) {
            if (position == 0) return playerOneActiveSlot;
            if (position == 1) return playerOneBenchSlotOne;
            if (position == 2) return playerOneBenchSlotTwo;
        } else {
            if (position == 0) return playerTwoActiveSlot;
            if (position == 1) return playerTwoBenchSlotOne;
            if (position == 2) return playerTwoBenchSlotTwo;
        }
        return null;
    }

    private class PokemonSlotPanel extends RoundedPanel {
        private int owner;
        private int slotType;
        private VisualPokemonData data;

        private JLabel nameLabel;
        private JLabel spriteLabel;
        private JLabel hpLabel;
        private JLabel energyLabel;

        public PokemonSlotPanel(int owner, int slotType) {
            super(28, CARD_COLOR);
            this.owner = owner;
            this.slotType = slotType;
            setLayout(null);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            nameLabel = new JLabel("");
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            nameLabel.setBounds(12, 6, 145, 16);
            add(nameLabel);

            spriteLabel = new JLabel("", SwingConstants.CENTER);
            spriteLabel.setBounds(8, 24, 54, 54);
            add(spriteLabel);

            hpLabel = new JLabel("");
            hpLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            hpLabel.setBounds(66, 32, 95, 16);
            add(hpLabel);

            energyLabel = new JLabel("");
            energyLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            energyLabel.setBounds(66, 50, 98, 16);
            add(energyLabel);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleFieldSlotClick(PokemonSlotPanel.this);
                }
            });
        }

        public void setPokemon(VisualPokemonData data) {
            this.data = data;
            refresh();
        }

        public void refresh() {
            if (data == null) {
                nameLabel.setText("");
                spriteLabel.setIcon(null);
                hpLabel.setText("");
                energyLabel.setText("");
            } else {
                nameLabel.setText(data.name);
                hpLabel.setText("PS: " + data.currentHp + "/" + data.maxHp);
                if (data.energyCount > 0) {
                    energyLabel.setText("Energía: " + getEnergyDisplayName(data.energyType) + " " + data.energyCount);
                } else {
                    energyLabel.setText("Energía:");
                }
                ImageIcon icon = cargarSprite(data.spritePath, 50, 50);
                spriteLabel.setIcon(icon);
            }
            repaint();
        }
    }

    private static class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }

    public static class VisualPokemonData {
        public String name;
        public String spritePath;
        public String audioPath;
        public int currentHp;
        public int maxHp;
        public int retreatCost;
        public String energyType = "";
        public int energyCount = 0;
        public String[] attacks;
        public int[] attackDamages;
        public int[] energyCosts;

        public VisualPokemonData(String name, String spritePath, String audioPath, int currentHp, int maxHp,
                int retreatCost, String[] attacks, int[] attackDamages, int[] energyCosts) {
            this.name = name;
            this.spritePath = spritePath;
            this.audioPath = audioPath;
            this.currentHp = currentHp;
            this.maxHp = maxHp;
            this.retreatCost = retreatCost;
            this.attacks = attacks;
            this.attackDamages = attackDamages;
            this.energyCosts = energyCosts;
        }
    }

    public static class VisualCardData {
        public String name;
        public String category;
        public String spritePath;
        public String audioPath;
        public int currentHp;
        public int maxHp;
        public int retreatCost;
        public String evolvesFrom;
        public String[] attacks;
        public int[] attackDamages;
        public int[] energyCosts;

        public VisualCardData(String name, String category, String spritePath, String audioPath,
                int currentHp, int maxHp, int retreatCost, String evolvesFrom,
                String[] attacks, int[] attackDamages, int[] energyCosts) {
            this.name = name;
            this.category = category;
            this.spritePath = spritePath;
            this.audioPath = audioPath;
            this.currentHp = currentHp;
            this.maxHp = maxHp;
            this.retreatCost = retreatCost;
            this.evolvesFrom = evolvesFrom;
            this.attacks = attacks;
            this.attackDamages = attackDamages;
            this.energyCosts = energyCosts;
        }
    }

}