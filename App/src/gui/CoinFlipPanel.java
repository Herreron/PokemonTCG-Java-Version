package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class CoinFlipPanel extends JPanel {

    private static final String HEADS_IMAGE_PATH = "/media/images/windowsAccessories/moneda_cara.png";
    private static final String TAILS_IMAGE_PATH = "/media/images/windowsAccessories/moneda_sello.png";
    private static final int TOTAL_FRAMES = 18;
    private static final int COIN_DIAMETER = 170;

    private JPanel redPaneltop;
    private JPanel redPanelbottom;
    private JPanel bluePanelVerticalRight;
    private JLabel pokeballsVerticales;
    private JLabel logoSmall;

    private JLabel startMessage;
    private JLabel winnerTitle;
    private JLabel lblGanador;
    private JButton btnAccion;
    private CoinCanvas coinCanvas;

    private Timer animationTimer;
    private ImageIcon headsImage;
    private ImageIcon tailsImage;

    private int currentFrame;
    private double verticalScale = 1.0;
    private boolean showingHeads = true;
    private boolean monedaLanzada = false;
    private boolean empiezaJugador1 = true;
    private final Random random = new Random();

    private JPanel containerRef;
    private String nombreP1;
    private String deckP1;
    private String nombreP2;
    private String deckP2;

    public CoinFlipPanel(JPanel container, String nombreP1, String deckP1, String nombreP2, String deckP2) {
        this.containerRef = container;
        this.nombreP1 = nombreP1;
        this.deckP1 = deckP1;
        this.nombreP2 = nombreP2;
        this.deckP2 = deckP2;
        initComponents();
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        setLayout(null);

        // Paneles de colores
        bluePanelVerticalRight = new JPanel();
        bluePanelVerticalRight.setBackground(new Color(14, 76, 117));
        bluePanelVerticalRight.setBounds(700, 0, 100, 500);
        add(bluePanelVerticalRight);

        redPaneltop = new JPanel();
        redPaneltop.setBackground(new Color(255, 56, 49));
        redPaneltop.setBounds(0, 0, 700, 110);
        add(redPaneltop);

        redPanelbottom = new JPanel();
        redPanelbottom.setBackground(new Color(255, 56, 49));
        redPanelbottom.setBounds(0, 390, 700, 110);
        add(redPanelbottom);

        // Decoraciones laterales
        pokeballsVerticales = new JLabel("", SwingConstants.CENTER);
        ImageIcon iconPokeballs = cargarIcono("/media/images/windowsAccessories/vertical_pokeballs_recortadas.png", 100, 380);
        if (iconPokeballs != null) pokeballsVerticales.setIcon(iconPokeballs);
        pokeballsVerticales.setBounds(700, 5, 100, 380);
        add(pokeballsVerticales);
        setComponentZOrder(pokeballsVerticales, 0);

        logoSmall = new JLabel();
        ImageIcon iconLogo2 = cargarIcono("/media/images/windowsAccessories/logo2.png", 90, 90);
        if (iconLogo2 != null) logoSmall.setIcon(iconLogo2);
        logoSmall.setBounds(705, 395, 90, 90);
        add(logoSmall);
        setComponentZOrder(logoSmall, 0);

        // Textos y moneda
        startMessage = new JLabel("Lanza la moneda", SwingConstants.CENTER);
        startMessage.setFont(new Font("SansSerif", Font.BOLD, 22));
        startMessage.setBounds(30, 210, 195, 70);
        add(startMessage);
        setComponentZOrder(startMessage, 0);

        winnerTitle = new JLabel("Empieza", SwingConstants.CENTER);
        winnerTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        winnerTitle.setBounds(485, 210, 190, 35);
        winnerTitle.setVisible(false);
        add(winnerTitle);
        setComponentZOrder(winnerTitle, 0);

        lblGanador = new JLabel("", SwingConstants.CENTER);
        lblGanador.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblGanador.putClientProperty("html.disable", Boolean.TRUE);
        lblGanador.setBounds(485, 245, 190, 35);
        lblGanador.setVisible(false);
        add(lblGanador);
        setComponentZOrder(lblGanador, 0);

        headsImage = cargarIcono(HEADS_IMAGE_PATH, COIN_DIAMETER, COIN_DIAMETER);
        tailsImage = cargarIcono(TAILS_IMAGE_PATH, COIN_DIAMETER, COIN_DIAMETER);

        coinCanvas = new CoinCanvas();
        coinCanvas.setOpaque(false);
        coinCanvas.setBounds(255, 160, 190, 180);
        add(coinCanvas);
        setComponentZOrder(coinCanvas, 0);

        // Boton de accion
        btnAccion = new JButton("Lanzar moneda");
        btnAccion.setBackground(new Color(14, 76, 117));
        btnAccion.setForeground(Color.WHITE);
        btnAccion.setFont(new Font("SansSerif", Font.BOLD, 17));
        btnAccion.setFocusPainted(false);
        btnAccion.setBounds(505, 420, 170, 45);
        add(btnAccion);
        setComponentZOrder(btnAccion, 0);

        // Animacion y eventos
        animationTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame++;
                verticalScale = Math.abs(Math.cos(currentFrame * 0.6));

                if (currentFrame % 3 == 0) {
                    showingHeads = !showingHeads;
                }

                coinCanvas.repaint();

                if (currentFrame >= TOTAL_FRAMES) {
                    animationTimer.stop();
                    empiezaJugador1 = random.nextBoolean();
                    showingHeads = empiezaJugador1;
                    verticalScale = 1.0;
                    coinCanvas.repaint();

                    String ganador = empiezaJugador1 ? nombreP1 : nombreP2;
                    lblGanador.setText(ganador);
                    winnerTitle.setVisible(true);
                    lblGanador.setVisible(true);

                    monedaLanzada = true;
                    btnAccion.setText("Siguiente");
                    btnAccion.setEnabled(true);
                }
            }
        });

        btnAccion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!monedaLanzada) {
                    btnAccion.setEnabled(false);
                    currentFrame = 0;
                    animationTimer.start();
                } else {
                    // Paso al tablero de juego con todos los datos recolectados
                    System.out.println("Inicia: " + (empiezaJugador1 ? nombreP1 : nombreP2));
                    CardLayout cl = (CardLayout) containerRef.getLayout();
                    cl.show(containerRef, "GAME_BOARD");
                }
            }
        });
    }

    private ImageIcon cargarIcono(String ruta, int anchoDisponible, int altoDisponible) {
        URL recurso = getClass().getResource(ruta);
        if (recurso == null) return null;
        ImageIcon iconoOriginal = new ImageIcon(recurso);
        int anchoOriginal = iconoOriginal.getIconWidth();
        int altoOriginal = iconoOriginal.getIconHeight();
        if (anchoOriginal <= 0 || altoOriginal <= 0) return null;
        double escala = Math.min((double) anchoDisponible / anchoOriginal, (double) altoDisponible / altoOriginal);
        int anchoEscalado = (int) (anchoOriginal * escala);
        int altoEscalado = (int) (altoOriginal * escala);
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(anchoEscalado, altoEscalado, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }

    private class CoinCanvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon currentImage = showingHeads ? headsImage : tailsImage;
            if (currentImage == null) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = (getWidth() - COIN_DIAMETER) / 2;
            int baseY = (getHeight() - COIN_DIAMETER) / 2;
            int dynamicHeight = Math.max(1, (int) (COIN_DIAMETER * verticalScale));
            int dynamicY = baseY + (COIN_DIAMETER - dynamicHeight) / 2;

            g2d.drawImage(currentImage.getImage(), x, dynamicY, COIN_DIAMETER, dynamicHeight, this);
        }
    }
}