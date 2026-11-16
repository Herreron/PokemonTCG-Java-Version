package gui;

public class VistaGui {

    public VistaGui() {
    }

    public void run() {
        // Instancia y muestra la ventana principal directamente
        PrincipalWindows principalWindows = new PrincipalWindows();
        principalWindows.setLocationRelativeTo(null);
        principalWindows.setVisible(true);
    }
}