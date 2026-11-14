
package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL; // permite hacer rutas de archivos 
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenu extends JPanel {
    private JPanel whitePanel;
    private JLabel logoLabel;
    private JLabel poke1;
    private JLabel poke2;
    private JLabel poke3;
    private JButton btnQuickMatch;
    private JButton btnTutorial;
    private JButton btnExit;

    public MainMenu() {
        this.initComponents();
    }

    private void initComponents() {
        this.setBackground(new Color(14, 76, 117));
        this.setLayout((LayoutManager)null);
        this.whitePanel = new JPanel();
        this.whitePanel.setBackground(Color.WHITE);
        this.whitePanel.setBounds(0, 0, 400, 500);
        this.add(this.whitePanel);
        this.logoLabel = new JLabel();
        this.logoLabel.setIcon(this.cargarIcono("/media/images/windowsAccessories/logo.png", 500, 200));
        this.logoLabel.setHorizontalAlignment(0);
        this.logoLabel.setBounds(150, 30, 500, 200);
        this.add(this.logoLabel);
        this.setComponentZOrder(this.logoLabel, 0);
        this.btnQuickMatch = new JButton("Partida Rapida");
        this.btnQuickMatch.setBounds(300, 260, 200, 40);
        this.add(this.btnQuickMatch);
        this.setComponentZOrder(this.btnQuickMatch, 0);
        this.btnTutorial = new JButton("Tutorial");
        this.btnTutorial.setBounds(300, 320, 200, 40);
        this.add(this.btnTutorial);
        this.setComponentZOrder(this.btnTutorial, 0);
        this.btnExit = new JButton("Salir al escritorio");
        this.btnExit.setBounds(300, 380, 200, 40);
        this.add(this.btnExit);
        this.setComponentZOrder(this.btnExit, 0);
        ImageIcon iconPokeball = this.cargarIcono("/media/images/windowsAccessories/pokeball.png", 40, 40);
        this.poke1 = new JLabel();
        if (iconPokeball != null) {
            this.poke1.setIcon(iconPokeball);
        }

        this.poke1.setBounds(630, 430, 40, 40);
        this.add(this.poke1);
        this.setComponentZOrder(this.poke1, 0);
        this.poke2 = new JLabel();
        if (iconPokeball != null) {
            this.poke2.setIcon(iconPokeball);
        }

        this.poke2.setBounds(680, 430, 40, 40);
        this.add(this.poke2);
        this.setComponentZOrder(this.poke2, 0);
        this.poke3 = new JLabel();
        if (iconPokeball != null) {
            this.poke3.setIcon(iconPokeball);
        }

        this.poke3.setBounds(730, 430, 40, 40);
        this.add(this.poke3);
        this.setComponentZOrder(this.poke3, 0);
        this.btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.btnQuickMatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Boton presionado: Partida Rapida");
                JPanel container = (JPanel)MainMenu.this.getParent();
                container.add(new CreatePlayers(container, 1), "PLAYER1");
                ((CardLayout)container.getLayout()).show(container, "PLAYER1");
            }
        });
    }

    private ImageIcon cargarIcono(String ruta, int anchoDisponible, int altoDisponible) {
        URL recurso = this.getClass().getResource(ruta);
        if (recurso == null) {
            System.err.println("No se encontro el recurso: " + ruta);
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
                System.err.println("No se pudo cargar la imagen: " + ruta);
                return null;
            }
        }
    }
}
