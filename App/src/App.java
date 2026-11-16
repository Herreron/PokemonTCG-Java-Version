//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import gui.VistaGui;
import java.util.Scanner;
import logic.battlesystem.ConsoleUI;

public class App {
    public App() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Iniciando aplicación...");
        System.out.println("Deseas jugar por consola o por interfaz gráfica?");
        System.out.println("1. Consola");
        System.out.println("2. Interfaz gráfica");
        System.out.print("0. Salir");
        System.out.println();
        byte opcion = scanner.nextByte();
        switch (opcion) {
            case 0:
                System.out.println("Saliendo de la aplicación...");
                break;
            case 1:
                ConsoleUI console = new ConsoleUI();
                console.run();
                break;
            case 2:
                VistaGui vistaGui = new VistaGui();
                vistaGui.run();
                break;
            default:
                System.out.println("Opción inválida porfavor intente de nuevo.");
        }

        scanner.close();
    }
}
