package olc.proyecto1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

public class ErrorTableForm extends javax.swing.JFrame {

    public ErrorTableForm() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tabla de Errores");
        setSize(800, 600);

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);

        try {
            String content = new String(Files.readAllBytes(Paths.get("errores.html")));
            editorPane.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
            editorPane.setText("<html><body><h2>Error al cargar la tabla de errores</h2></body></html>");
        }

        JScrollPane scrollPane = new JScrollPane(editorPane);
        add(scrollPane);
    }
}
