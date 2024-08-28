/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package olc.proyecto1;
import java.io.IOException;
import java.io.Writer;
import javax.swing.JTextArea;

public class TextAreaWriter extends Writer {
    private final JTextArea textArea;

    public TextAreaWriter(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        textArea.append(new String(cbuf, off, len));
        textArea.setCaretPosition(textArea.getDocument().getLength()); // Scroll al final
    }

    @Override
    public void flush() throws IOException {
        // No es necesario implementar
    }

    @Override
    public void close() throws IOException {
        // No es necesario implementar
    }
}
