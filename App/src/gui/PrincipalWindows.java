package gui;

import java.awt.Color;



public class PrincipalWindows extends javax.swing.JFrame {
    
 
    int xMouse,yMouse;    
    
    
    public PrincipalWindows() {
        initComponents();
        
        
        
    }

    
  
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        container = new javax.swing.JPanel();
        mainMenu = new gui.MainMenu();
        topBar = new javax.swing.JPanel();
        panelExit = new javax.swing.JPanel();
        LabelExit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setName("principalWindows"); // NOI18N
        setUndecorated(true);
        setResizable(false);

        container.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                containerMouseEntered(evt);
            }
        });
        container.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout mainMenuLayout = new javax.swing.GroupLayout(mainMenu);
        mainMenu.setLayout(mainMenuLayout);
        mainMenuLayout.setHorizontalGroup(
            mainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        mainMenuLayout.setVerticalGroup(
            mainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 453, Short.MAX_VALUE)
        );

         container.add(mainMenu, "MENU");

        getContentPane().add(container, java.awt.BorderLayout.CENTER);

        topBar.setBackground(new java.awt.Color(0, 0, 0));
        topBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                topBarMouseDragged(evt);
            }
        });
        topBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                topBarMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                topBarMousePressed(evt);
            }
        });

        panelExit.setBackground(new java.awt.Color(255, 255, 255));
        panelExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelExitMouseEntered(evt);
            }
        });
        panelExit.setLayout(new java.awt.BorderLayout());

        LabelExit.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        LabelExit.setText(" X");
        LabelExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LabelExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LabelExitMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                LabelExitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                LabelExitMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                LabelExitMousePressed(evt);
            }
        });
        panelExit.add(LabelExit, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout topBarLayout = new javax.swing.GroupLayout(topBar);
        topBar.setLayout(topBarLayout);
        topBarLayout.setHorizontalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topBarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelExit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        topBarLayout.setVerticalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(topBar, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>                        

    private void LabelExitMousePressed(java.awt.event.MouseEvent evt) {                                       


        // TODO add your handling code here:
    }                                      

    private void topBarMousePressed(java.awt.event.MouseEvent evt) {                                    
        // TODO add your handling code here:
        xMouse = evt.getX();
        yMouse = evt.getY();

    }                                   

    private void topBarMouseDragged(java.awt.event.MouseEvent evt) {                                    
        // TODO add your handling code here:
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x-xMouse,y-yMouse);

    }                                   

    private void topBarMouseClicked(java.awt.event.MouseEvent evt) {                                    
        // TODO add your handling code here:
    }                                   

    private void LabelExitMouseClicked(java.awt.event.MouseEvent evt) {                                       
        // TODO add your handling code here:
        System.exit(0);
    }                                      

    private void containerMouseEntered(java.awt.event.MouseEvent evt) {                                       
        // TODO add your handling code here:
    }                                      

    private void LabelExitMouseEntered(java.awt.event.MouseEvent evt) {                                       
        // TODO add your handling code here:

        panelExit.setBackground(Color.red);

    }                                      

    private void panelExitMouseEntered(java.awt.event.MouseEvent evt) {                                       
        // TODO add your handling code here:

    }                                      

    private void LabelExitMouseExited(java.awt.event.MouseEvent evt) {                                      
        // TODO add your handling code here:
        panelExit.setBackground(Color.white);
    }                                     

    public static void main(String args[]) {
    
        java.awt.EventQueue.invokeLater(() -> new PrincipalWindows().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel LabelExit;
    private javax.swing.JPanel container;
    private gui.MainMenu mainMenu;
    private javax.swing.JPanel panelExit;
    private javax.swing.JPanel topBar;
    // End of variables declaration                   
}
