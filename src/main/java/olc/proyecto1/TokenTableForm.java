package olc.proyecto1;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException; 

public class TokenTableForm extends JFrame {

    private JEditorPane editorPane; 

    public TokenTableForm() {
        setTitle("Tabla de Tokens");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana

        editorPane = new JEditorPane();
        editorPane.setEditable(false); // No permitir edición

        // Utilizar un JScrollPane para añadir barras de desplazamiento
        JScrollPane scrollPane = new JScrollPane(editorPane);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Intentar cargar el archivo HTML
        try {
            File htmlFile = new File("tokens.html"); // Ruta al archivo tokens.html
            editorPane.setPage(htmlFile.toURI().toURL()); 
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el archivo HTML", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
