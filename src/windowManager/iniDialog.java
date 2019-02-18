package windowManager;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class iniDialog {
    // main-Methode
    public static void main(String[] args){
        // Erzeugung eines neuen Frames mit 
        // dem Titel Beispiel JDialog
        JDialog SetEvalSettings = new JDialog();
        // Titel wird gesetzt
        SetEvalSettings.setTitle("Set Evaluation Settings");
        // Breite und Höhe des Fensters werden 
        // auf 200 Pixel gesetzt
        SetEvalSettings.setSize(200,200);
        // Dialog wird auf modal gesetzt
        SetEvalSettings.setModal(true);
        SetEvalSettings.add(new JLabel("Beispiel JLabel"));
        // Wir lassen unseren Dialog anzeigen
        SetEvalSettings.setVisible(true);
    }
}
