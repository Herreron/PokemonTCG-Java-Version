package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class CreatePlayers extends JPanel {
    private JPanel redPaneltop;
    private JPanel redPanelbottom;
    private JPanel bluePanelVerticalRight;
    private JLabel lblJugador;
    private JTextField txtNombre;
    private JComboBox<String> cmbDeck;
    private JButton btnSiguiente;
    private JLabel pokeballsVerticales;
    private JLabel logoSmall;
    private int numeroJugador;
    private String nombreP1;
    private String deckP1;
    private JPanel containerRef;

    public CreatePlayers(JPanel container, int numeroJugador) {
        this.containerRef = container;
        this.numeroJugador = numeroJugador;
        this.initComponents();
    }

    public CreatePlayers(JPanel container, int numeroJugador, String nombreP1, String deckP1) {
        this.containerRef = container;
        this.numeroJugador = numeroJugador;
        this.nombreP1 = nombreP1;
        this.deckP1 = deckP1;
        this.initComponents();
    }

    private void initComponents() {
        this.setBackground(Color.WHITE);
        this.setLayout((LayoutManager)null);
        this.bluePanelVerticalRight = new JPanel();
        this.bluePanelVerticalRight.setBackground(new Color(14, 76, 117));
        this.bluePanelVerticalRight.setBounds(700, 0, 100, 500);
        this.add(this.bluePanelVerticalRight);
        this.redPaneltop = new JPanel();
        this.redPaneltop.setBackground(new Color(255, 56, 49));
        this.redPaneltop.setBounds(0, 0, 700, 110);
        this.add(this.redPaneltop);
        this.redPanelbottom = new JPanel();
        this.redPanelbottom.setBackground(new Color(255, 56, 49));
        this.redPanelbottom.setBounds(0, 390, 700, 110);
        this.add(this.redPanelbottom);
        String[] opcionesDeck = new String[]{"Escoje tu Deck...", "Fuego", "Agua", "Eléctrico", "Psíquico"};
        this.cmbDeck = new JComboBox<>(opcionesDeck);
        this.cmbDeck.setFont(new Font("SansSerif", 1, 18));
        this.cmbDeck.setBackground(Color.WHITE);
        this.lblJugador = new JLabel("Jugador " + this.numeroJugador, 0);
        this.lblJugador.setOpaque(true);
        this.lblJugador.setBackground(new Color(14, 76, 117));
        this.lblJugador.setForeground(Color.WHITE);
        this.lblJugador.setFont(new Font("SansSerif", 1, 18));
        this.txtNombre = new JTextField("Escribe tu nombre |");
        this.txtNombre.setFont(new Font("SansSerif", 1, 22));
        this.txtNombre.setBorder((Border)null);
        this.txtNombre.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (CreatePlayers.this.txtNombre.getText().equals("Escribe tu nombre |")) {
                    CreatePlayers.this.txtNombre.setText("");
                }

            }

            public void focusLost(FocusEvent e) {
                if (CreatePlayers.this.txtNombre.getText().isEmpty()) {
                    CreatePlayers.this.txtNombre.setText("Escribe tu nombre |");
                }

            }
        });
        if (this.numeroJugador == 1) {
            this.lblJugador.setBounds(50, 160, 130, 40);
            this.txtNombre.setBounds(50, 210, 300, 40);
            this.cmbDeck.setBounds(450, 210, 210, 40);
        } else {
            this.cmbDeck.setBounds(80, 210, 210, 40);
            this.lblJugador.setBounds(450, 160, 130, 40);
            this.txtNombre.setBounds(450, 210, 220, 40);
        }

        this.add(this.lblJugador);
        this.setComponentZOrder(this.lblJugador, 0);
        this.add(this.txtNombre);
        this.setComponentZOrder(this.txtNombre, 0);
        this.add(this.cmbDeck);
        this.setComponentZOrder(this.cmbDeck, 0);
        this.btnSiguiente = new JButton("Siguiente");
        this.btnSiguiente.setBackground(new Color(14, 76, 117));
        this.btnSiguiente.setForeground(Color.WHITE);
        this.btnSiguiente.setFont(new Font("SansSerif", 1, 18));
        this.btnSiguiente.setFocusPainted(false);
        this.btnSiguiente.setBounds(530, 420, 140, 45);
        this.add(this.btnSiguiente);
        this.setComponentZOrder(this.btnSiguiente, 0);
        this.pokeballsVerticales = new JLabel("", 0);
        ImageIcon iconPokeballs = this.cargarIcono("/media/images/windowsAccessories/vertical_pokeballs_recortadas.png", 100, 380);
        if (iconPokeballs != null) {
            this.pokeballsVerticales.setIcon(iconPokeballs);
        }

        this.pokeballsVerticales.setBounds(700, 5, 100, 380);
        this.add(this.pokeballsVerticales);
        this.setComponentZOrder(this.pokeballsVerticales, 0);
        this.logoSmall = new JLabel();
        ImageIcon iconLogo2 = this.cargarIcono("/media/images/windowsAccessories/logo2.png", 90, 90);
        if (iconLogo2 != null) {
            this.logoSmall.setIcon(iconLogo2);
        }

        this.logoSmall.setBounds(705, 395, 90, 90);
        this.add(this.logoSmall);
        this.setComponentZOrder(this.logoSmall, 0);
        this.btnSiguiente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreActual = CreatePlayers.this.txtNombre.getText();
                String deckActual = (String)CreatePlayers.this.cmbDeck.getSelectedItem();
                if (!nombreActual.isEmpty() && !nombreActual.equals("Escribe tu nombre |") && CreatePlayers.this.cmbDeck.getSelectedIndex() != 0) {
                    if (CreatePlayers.this.numeroJugador == 1) {
                        CreatePlayers panelP2 = new CreatePlayers(CreatePlayers.this.containerRef, 2, nombreActual, deckActual);
                        CreatePlayers.this.containerRef.add(panelP2, "PLAYER2");
                        CardLayout cl = (CardLayout)CreatePlayers.this.containerRef.getLayout();
                        cl.show(CreatePlayers.this.containerRef, "PLAYER2");
                    } else {
                        CoinFlipPanel coinPanel = new CoinFlipPanel(
                                CreatePlayers.this.containerRef,
                                CreatePlayers.this.nombreP1,
                                CreatePlayers.this.deckP1,
                                nombreActual,
                                deckActual);
                        CreatePlayers.this.containerRef.add(coinPanel, "COIN_FLIP");
                        CardLayout cl = (CardLayout)CreatePlayers.this.containerRef.getLayout();
                        cl.show(CreatePlayers.this.containerRef, "COIN_FLIP");
                    }

                } else {
                    JOptionPane.showMessageDialog(CreatePlayers.this, "Debe ingresar el nombre y seleccionar un deck.", "Advertencia", 2);
                }
            }
        });
    }

    private ImageIcon cargarIcono(String ruta, int anchoDisponible, int altoDisponible) {
        URL recurso = this.getClass().getResource(ruta);
        if (recurso == null) {
            return null;
        } else {
            ImageIcon iconoOriginal = new ImageIcon(recurso);
            int anchoOriginal = iconoOriginal.getIconWidth();
            int altoOriginal = iconoOriginal.getIconHeight();
            if (anchoOriginal > 0 && altoOriginal > 0) {
                double escala = Math.min((double)anchoDisponible / (double)anchoOriginal, (double)altoDisponible / (double)altoOriginal);
                int anchoEscalado = (int)((double)anchoOriginal * escala);
                int altoEscalado = (int)((double)altoOriginal * escala);
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(anchoEscalado, altoEscalado, 4);
                return new ImageIcon(imagenEscalada);
            } else {
                return null;
            }
        }
    }
}
