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
    private static final String MODE_HAND_CARD = "HAND_CARD";
    private static final String MODE_ENERGY = "ENERGY";
    private static final String MODE_EVOLVE = "EVOLVE";
    private static final String MODE_RETREAT = "RETREAT";

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
    private boolean mustPromoteActive = false;

    private List<VisualCardData> playerOneHand = new ArrayList<>();
    private List<VisualCardData> playerTwoHand = new ArrayList<>();

    private String currentMode = MODE_NONE;
    private int selectedCardIndex = -1;
    private JLabel selectedCardLabel = null;

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

        loadBackground();
        initComponents();
        cargarDatosPrueba();
        BackgroundMusic.playBattle();
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
                if (validatePromotionBlock()) return;
                if (!fieldEnergyAvailable) {
                    JOptionPane.showMessageDialog(GameWindows.this, "Ya usaste la energía del campo en este turno.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                toggleActionMode(MODE_ENERGY, energyButton);
            }
        });

        evolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameFinished) return;
                if (validatePromotionBlock()) return;
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
                if (validatePromotionBlock()) return;
                toggleActionMode(MODE_RETREAT, retreatButton);
            }
        });

        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameFinished) return;
                if (validatePromotionBlock()) return;
                clearSelection();

                PokemonSlotPanel attackerSlot = getCurrentActiveSlot();
                PokemonSlotPanel defenderSlot;
                if (currentPlayer == 1) {
                    defenderSlot = playerTwoActiveSlot;
                } else {
                    defenderSlot = playerOneActiveSlot;
                }

                if (attackerSlot.data == null) {
                    JOptionPane.showMessageDialog(GameWindows.this, "No tienes un Pokémon activo para atacar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (defenderSlot.data == null) {
                    JOptionPane.showMessageDialog(GameWindows.this, "El rival no tiene un Pokémon activo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                VisualPokemonData attacker = attackerSlot.data;
                if (attacker.attacks == null || attacker.attacks.length == 0) {
                    JOptionPane.showMessageDialog(GameWindows.this, "Este Pokémon no tiene ataques disponibles.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String[] options = new String[attacker.attacks.length];
                for (int i = 0; i < attacker.attacks.length; i++) {
                    options[i] = attacker.attacks[i] + " (" + attacker.attackDamages[i] + " de daño - Costo: " + attacker.energyCosts[i] + ")";
                }

                int choice = JOptionPane.showOptionDialog(
                        GameWindows.this,
                        "Selecciona el ataque de " + attacker.name + ":",
                        "Atacar",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choice >= 0) {
                    int cost = attacker.energyCosts[choice];
                    if (attacker.energyCount < cost) {
                        JOptionPane.showMessageDialog(GameWindows.this, "Energía insuficiente para realizar este ataque.", "Energía insuficiente", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    playAudio(attacker.audioPath);
                    int damage = attacker.attackDamages[choice];
                    defenderSlot.data.currentHp = Math.max(0, defenderSlot.data.currentHp - damage);
                    defenderSlot.refresh();

                    if (defenderSlot.data.currentHp == 0) {
                        JOptionPane.showMessageDialog(GameWindows.this, defenderSlot.data.name + " ha sido debilitado.", "K.O.", JOptionPane.INFORMATION_MESSAGE);
                        defenderSlot.setPokemon(null);
                        if (currentPlayer == 1) {
                            playerOnePoints++;
                        } else {
                            playerTwoPoints++;
                        }
                        updateScoreMarkers();
                        if (playerOnePoints >= 3 || playerTwoPoints >= 3) {
                            finishGame(playerOnePoints >= 3 ? playerOneName : playerTwoName);
                        }
                    }
                }
            }
        });

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameFinished) return;
                if (validatePromotionBlock()) return;

                PokemonSlotPanel currentActiveSlot = getCurrentActiveSlot();
                if (currentActiveSlot.data == null) {
                    JOptionPane.showMessageDialog(GameWindows.this, "El juego no puede seguir si tu puesto activo está vacío.", "Puesto activo vacío", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                clearSelection();
                if (currentPlayer == 1) {
                    currentPlayer = 2;
                } else {
                    currentPlayer = 1;
                }
                fieldEnergyAvailable = true;
                currentFieldEnergy = mapDeckToEnergy(getCurrentPlayerDeck());
                updateTurnVisualState();
            }
        });
    }

    private boolean validatePromotionBlock() {
        if (mustPromoteActive) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un Pokémon de tu banca para pasarlo a activo.", "Acción obligatoria", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
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
        selectedCardIndex = -1;
        if (selectedCardLabel != null) {
            selectedCardLabel.setBorder(null);
            selectedCardLabel = null;
        }
        energyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        evolveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        retreatButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    private void handleFieldSlotClick(PokemonSlotPanel slot) {
        if (gameFinished) return;
        boolean isAlly = (slot.owner == currentPlayer);

        // Promoción obligatoria tras retirada o K.O.
        if (mustPromoteActive) {
            if (!isAlly || slot.slotType == 0 || slot.data == null) {
                JOptionPane.showMessageDialog(this, "Selecciona uno de tus Pokémon en banca para moverlo a activo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            PokemonSlotPanel activeSlot = getCurrentActiveSlot();
            activeSlot.setPokemon(slot.data);
            slot.setPokemon(null);
            mustPromoteActive = false;
            return;
        }

        // Modo energía
        if (currentMode.equals(MODE_ENERGY)) {
            if (!isAlly) {
                JOptionPane.showMessageDialog(this, "No puedes adjuntar energía a un Pokémon enemigo.", "Acción inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (slot.data == null) {
                JOptionPane.showMessageDialog(this, "No hay ningún Pokémon en este espacio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            slot.data.energyType = currentFieldEnergy;
            slot.data.energyCount++;
            slot.refresh();
            fieldEnergyAvailable = false;
            updateFieldEnergyIcon();
            clearSelection();
            return;
        }

        // Modo de evolucion
        if (currentMode.equals(MODE_EVOLVE)) {
            if (!isAlly) {
                JOptionPane.showMessageDialog(this, "No puedes evolucionar Pokémon enemigos.", "Acción inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (slot.data == null) {
                JOptionPane.showMessageDialog(this, "No hay ningún Pokémon en este espacio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<VisualCardData> currentHand = getCurrentHand();
            int evolutionIndex = -1;
            for (int i = 0; i < currentHand.size(); i++) {
                VisualCardData card = currentHand.get(i);
                if (card.category.equals("POKEMON") && card.evolvesFrom != null && card.evolvesFrom.equalsIgnoreCase(slot.data.name)) {
                    evolutionIndex = i;
                    break;
                }
            }
            if (evolutionIndex == -1) {
                JOptionPane.showMessageDialog(this, "No tienes la evolución de " + slot.data.name + " en tu mano.", "No cumple la condición", JOptionPane.WARNING_MESSAGE);
                clearSelection();
                return;
            }
            VisualCardData evolution = currentHand.remove(evolutionIndex);
            slot.data.name = evolution.name;
            slot.data.maxHp = evolution.maxHp;
            slot.data.currentHp = evolution.maxHp;
            slot.data.spritePath = evolution.spritePath;
            slot.data.audioPath = evolution.audioPath;
            slot.data.retreatCost = evolution.retreatCost;
            slot.data.attacks = evolution.attacks;
            slot.data.attackDamages = evolution.attackDamages;
            slot.data.energyCosts = evolution.energyCosts;
            slot.refresh();
            playAudio(slot.data.audioPath);
            refreshHandVisual();
            clearSelection();
            return;
        }

        // Modo de retirada
        if (currentMode.equals(MODE_RETREAT)) {
            if (!isAlly || slot.slotType != 0 || slot.data == null) {
                JOptionPane.showMessageDialog(this, "Selecciona tu Pokémon activo para retirarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (slot.data.energyCount < slot.data.retreatCost) {
                JOptionPane.showMessageDialog(this, "Energía insuficiente para retirarse (costo: " + slot.data.retreatCost + ").", "No puede retirarse", JOptionPane.WARNING_MESSAGE);
                clearSelection();
                return;
            }
            VisualPokemonData withdrawnPokemon = slot.data;
            withdrawnPokemon.energyCount = 0;
            withdrawnPokemon.energyType = "";

            VisualCardData returnedCard = new VisualCardData(
                    withdrawnPokemon.name, "POKEMON", withdrawnPokemon.spritePath, withdrawnPokemon.audioPath,
                    withdrawnPokemon.currentHp, withdrawnPokemon.maxHp, withdrawnPokemon.retreatCost, null,
                    withdrawnPokemon.attacks, withdrawnPokemon.attackDamages, withdrawnPokemon.energyCosts);

            List<VisualCardData> currentHand = getCurrentHand();
            currentHand.add(returnedCard);
            slot.setPokemon(null);
            refreshHandVisual();
            clearSelection();

            if (hasBenchPokemon(currentPlayer)) {
                mustPromoteActive = true;
                JOptionPane.showMessageDialog(this, "Selecciona un Pokémon de tu banca para ocupar el puesto activo.", "Promover Pokémon", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }

        // Carta seleccionada desde la mano
        if (currentMode.equals(MODE_HAND_CARD) && selectedCardIndex >= 0) {
            List<VisualCardData> currentHand = getCurrentHand();
            VisualCardData card = currentHand.get(selectedCardIndex);

            if (card.category.equals("OBJETO")) {
                if (!isAlly) {
                    JOptionPane.showMessageDialog(this, "No puedes curar a un Pokémon enemigo.", "Acción inválida", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (slot.data == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona un Pokémon válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int confirmation = JOptionPane.showConfirmDialog(this, "¿Deseas usar " + card.name + " en " + slot.data.name + "?", "Confirmar objeto", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    slot.data.currentHp = Math.min(slot.data.maxHp, slot.data.currentHp + 20);
                    slot.refresh();
                    currentHand.remove(selectedCardIndex);
                    refreshHandVisual();
                }
                clearSelection();
                return;
            }

            if (card.category.equals("POKEMON")) {
                if (!isAlly) {
                    JOptionPane.showMessageDialog(this, "No puedes colocar cartas en el campo rival.", "Acción inválida", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (slot.data != null) {
                    JOptionPane.showMessageDialog(this, "Este espacio ya está ocupado.", "Espacio ocupado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                PokemonSlotPanel activeSlot = getCurrentActiveSlot();
                if (activeSlot.data == null && slot.slotType != 0) {
                    JOptionPane.showMessageDialog(this, "Debes colocar primero un Pokémon en el puesto activo.", "Pokémon activo obligatorio", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (card.evolvesFrom != null) {
                    JOptionPane.showMessageDialog(this, "No puedes bajar una evolución directamente sin su fase previa.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                VisualPokemonData newPokemon = new VisualPokemonData(
                        card.name, card.spritePath, card.audioPath,
                        card.currentHp, card.maxHp, card.retreatCost,
                        card.attacks, card.attackDamages, card.energyCosts);

                slot.setPokemon(newPokemon);
                playAudio(newPokemon.audioPath);
                currentHand.remove(selectedCardIndex);
                refreshHandVisual();
                clearSelection();
                return;
            }
        }

        // Reproducir el grito al hacer clic en un Pokémon
        if (slot.data != null) {
            playAudio(slot.data.audioPath);
        }
    }

    private void handleHandCardClick(int index, JLabel cardLabel) {
        if (gameFinished) return;
        if (validatePromotionBlock()) return;

        List<VisualCardData> currentHand = getCurrentHand();
        VisualCardData card = currentHand.get(index);

        if (card.category.equals("ENTRENADOR")) {
            clearSelection();
            int confirmation = JOptionPane.showConfirmDialog(this, "¿Deseas activar la carta de entrenador " + card.name + "?", "Confirmar entrenador", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                currentHand.remove(index);
                refreshHandVisual();
            }
            return;
        }

        if (currentMode.equals(MODE_HAND_CARD) && selectedCardIndex == index) {
            clearSelection();
            return;
        }

        clearSelection();
        currentMode = MODE_HAND_CARD;
        selectedCardIndex = index;
        selectedCardLabel = cardLabel;
        cardLabel.setBorder(BorderFactory.createLineBorder(SELECTION_BORDER_COLOR, 3));
    }

    private void updateTurnVisualState() {
        String currentPlayerName = getCurrentPlayerName();
        turnLabel.setText("Turno: " + currentPlayerName);
        updateFieldEnergyIcon();
        refreshHandVisual();

        PokemonSlotPanel currentActiveSlot = getCurrentActiveSlot();
        if (currentActiveSlot.data == null && hasBenchPokemon(currentPlayer)) {
            mustPromoteActive = true;
        }
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

    private boolean hasBenchPokemon(int playerNumber) {
        if (playerNumber == 1) {
            return playerOneBenchSlotOne.data != null || playerOneBenchSlotTwo.data != null;
        }
        return playerTwoBenchSlotOne.data != null || playerTwoBenchSlotTwo.data != null;
    }

    private PokemonSlotPanel getCurrentActiveSlot() {
        if (currentPlayer == 1) {
            return playerOneActiveSlot;
        }
        return playerTwoActiveSlot;
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

    // Datos de prueba
    private void cargarDatosPrueba() {
        VisualPokemonData charmander = new VisualPokemonData(
                "Charmander", "/media/images/pokemons/fire/charmander.png", "/media/audio/screams/004charmander.wav",
                45, 60, 1, new String[]{"Ascuas", "Lanzallamas"}, new int[]{20, 40}, new int[]{1, 2});
        charmander.energyType = "Fire";
        charmander.energyCount = 1;

        VisualPokemonData growlithe = new VisualPokemonData(
                "Growlithe", "/media/images/pokemons/fire/growlithe.png", "/media/audio/screams/058growlithe.wav",
                50, 50, 1, new String[]{"Mordisco"}, new int[]{20}, new int[]{1});

        VisualPokemonData squirtle = new VisualPokemonData(
                "Squirtle", "/media/images/pokemons/water/squirtle.png", "/media/audio/screams/007squirtle.wav",
                50, 60, 1, new String[]{"Burbuja"}, new int[]{20}, new int[]{1});
        squirtle.energyType = "Water";
        squirtle.energyCount = 1;

        VisualPokemonData psyduck = new VisualPokemonData(
                "Psyduck", "/media/images/pokemons/water/psyduck.png", "/media/audio/screams/054psyduck.wav",
                45, 60, 1, new String[]{"Confusion"}, new int[]{20}, new int[]{1});

        VisualPokemonData magikarp = new VisualPokemonData(
                "Magikarp", "/media/images/pokemons/water/magikarp.png", "/media/audio/screams/129magikarp.wav",
                45, 45, 1, new String[]{"Salpicadura"}, new int[]{0}, new int[]{1});

        playerOneActiveSlot.setPokemon(charmander);
        playerOneBenchSlotOne.setPokemon(growlithe);
        playerTwoActiveSlot.setPokemon(squirtle);
        playerTwoBenchSlotOne.setPokemon(psyduck);
        playerTwoBenchSlotTwo.setPokemon(magikarp);

        playerOneHand.add(new VisualCardData("Charmeleon", "POKEMON", "/media/images/pokemons/fire/charmeleon.png", "/media/audio/screams/005charmeleon.wav", 80, 80, 1, "Charmander", new String[]{"Garra"}, new int[]{30}, new int[]{1}));
        playerOneHand.add(new VisualCardData("Charizard", "POKEMON", "/media/images/pokemons/fire/charizard.png", "/media/audio/screams/006charizard.wav", 120, 120, 2, "Charmeleon", new String[]{"Giro Fuego"}, new int[]{60}, new int[]{2}));
        playerOneHand.add(new VisualCardData("Moltres", "POKEMON", "/media/images/pokemons/fire/moltres.png", "/media/audio/screams/146moltres.wav", 100, 100, 2, null, new String[]{"Pico Taladro"}, new int[]{50}, new int[]{2}));
        playerOneHand.add(new VisualCardData("Poke Ball", "ENTRENADOR", "/media/images/objects/pokeball.png", null, 0, 0, 0, null, null, null, null));
        playerOneHand.add(new VisualCardData("Pocion", "OBJETO", "/media/images/objects/potion.png", null, 0, 0, 0, null, null, null, null));
        playerOneHand.add(new VisualCardData("Prof. Oak", "ENTRENADOR", "/media/images/trainers/oak.png", null, 0, 0, 0, null, null, null, null));

        playerTwoHand.add(new VisualCardData("Pocion", "OBJETO", "/media/images/objects/potion.png", null, 0, 0, 0, null, null, null, null));
        playerTwoHand.add(new VisualCardData("Poke Ball", "ENTRENADOR", "/media/images/objects/pokeball.png", null, 0, 0, 0, null, null, null, null));

        refreshHandVisual();
    }
}