
package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PrincipalWindows extends JFrame {
    int xMouse;
    int yMouse;
    private JLabel LabelExit;
    private JPanel container;
    private MainMenu mainMenu;
    private JPanel panelExit;
    private JPanel topBar;

    public PrincipalWindows() {
        this.initComponents();
    }

    private void initComponents() {
        this.container = new JPanel();
        this.mainMenu = new MainMenu();
        this.topBar = new JPanel();
        this.panelExit = new JPanel();
        this.LabelExit = new JLabel();
        this.setDefaultCloseOperation(3);
        this.setLocationByPlatform(true);
        this.setName("principalWindows");
        this.setUndecorated(true);
        this.setResizable(false);
        this.container.setPreferredSize(new Dimension(800, 500));
        this.container.addMouseListener(new MouseAdapter() {
        });
        this.container.setLayout(new CardLayout());
        this.container.add(this.mainMenu, "MENU");
        this.getContentPane().add(this.container, "Center");
        this.topBar.setBackground(new Color(0, 0, 0));
        this.topBar.setPreferredSize(new Dimension(800, 36));
        this.topBar.setLayout(new FlowLayout(2, 10, 4));
        this.topBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                PrincipalWindows.this.topBarMouseDragged(evt);
            }
        });
        this.topBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                PrincipalWindows.this.topBarMousePressed(evt);
            }
        });
        this.panelExit.setBackground(new Color(255, 255, 255));
        this.panelExit.setPreferredSize(new Dimension(28, 28));
        this.panelExit.addMouseListener(new MouseAdapter() {
        });
        this.panelExit.setLayout(new BorderLayout());
        this.LabelExit.setFont(new Font("sansserif", 0, 18));
        this.LabelExit.setText("X");
        this.LabelExit.setHorizontalAlignment(0);
        this.LabelExit.setVerticalAlignment(0);
        this.LabelExit.setCursor(new Cursor(12));
        this.LabelExit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                PrincipalWindows.this.LabelExitMouseClicked(evt);
            }

            public void mouseEntered(MouseEvent evt) {
                PrincipalWindows.this.LabelExitMouseEntered(evt);
            }

            public void mouseExited(MouseEvent evt) {
                PrincipalWindows.this.LabelExitMouseExited(evt);
            }
        });
        this.panelExit.add(this.LabelExit, "Center");
        this.topBar.add(this.panelExit);
        this.getContentPane().add(this.topBar, "First");
        this.pack();
    }

    private void topBarMousePressed(MouseEvent evt) {
        this.xMouse = evt.getX();
        this.yMouse = evt.getY();
    }

    private void topBarMouseDragged(MouseEvent evt) {
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - this.xMouse, y - this.yMouse);
    }

    private void LabelExitMouseClicked(MouseEvent evt) {
        System.exit(0);
    }

    private void LabelExitMouseEntered(MouseEvent evt) {
        this.panelExit.setBackground(new Color(220, 53, 69));
    }

    private void LabelExitMouseExited(MouseEvent evt) {
        this.panelExit.setBackground(Color.white);
    }
}
