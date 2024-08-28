/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package olc.proyecto1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java_cup.runtime.Symbol;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

/**
 *
 * @author David
 */
public class Principal extends javax.swing.JFrame {

    private List<Token> tokens = new ArrayList<>();
    private List<Error> errores = new ArrayList<>();
    private File currentFile;

    public static class Error {

        int numero;
        String tipo;
        String descripcion;
        int linea;
        int columna;

        public Error(int numero, String tipo, String descripcion, int linea, int columna) {
            this.numero = numero;
            this.tipo = tipo;
            this.descripcion = descripcion;
            this.linea = linea;
            this.columna = columna;
        }
    }

    /**
     * Creates new form Principal
     */
    public Principal() {
        initComponents();

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        jSplitPane1.setDividerLocation(0.5);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane2.setDividerLocation(0.5);
        jSplitPane2.setResizeWeight(0.5);

    }

    //Ejecuta lexer y genera la lista de tokens
    private void analizarTokens() {
        String inputText = txtEntrada.getText();
        StringReader reader = new StringReader(inputText);
        Lexer lexer = new Lexer(reader);
        tokens.clear();  // Limpiar la lista de tokens antes de comenzar
        errores.clear(); // Limpia la lista de errores antes de comenzar 

        Symbol token;
        int tokenCount = 1;

        try {
            while ((token = lexer.next_token()).sym != sym.EOF) {
                String lexema = token.value.toString();
                String tipo = sym.terminalNames[token.sym];
                int linea = token.left + 1;  // CUP utiliza 0-based index para líneas, por lo tanto, añadimos 1
                int columna = token.right + 1; // CUP utiliza 0-based index para columnas, por lo tanto, añadimos 1

                // Crear y agregar el token a la lista
                tokens.add(new Token(tokenCount, lexema, tipo, linea, columna));
                tokenCount++;

                // También imprimir el token en la consola para verificar
                txtConsola.append("Token: " + tipo + " (" + lexema + ") at line " + linea + ", column " + columna + "\n");
            }

            // Después de analizar los tokens, puedes generar el reporte HTML
            generarReporteTokensHTML();
            generarReporteErroresHTML(); //anadido aqui para que genere el reporte de errores

        } catch (Exception e) {
            e.printStackTrace(new PrintWriter(new TextAreaWriter(txtConsola)));
        }
    }

    //Ejecuta Parser para el analisis sintactico
    private void analizarSintactico() {
        // Limpiar la consola antes de ejecutar
        txtConsola.setText("");

        // Obtener el texto de entrada del JTextArea
        String inputText = txtEntrada.getText();

        // Crear un StringReader para el Lexer
        StringReader reader = new StringReader(inputText);

        // Crear una instancia del Lexer
        Lexer lexer = new Lexer(reader);

        // Crear una instancia del Parser pasando el Lexer
        Parser parser = new Parser(lexer);

        try {
            // Ejecutar el parser para iniciar el análisis sintáctico
            parser.parse();
            generarReporteErroresHTML(); //anadido aqui para que genere el reporte de errores

            // Enviar mensaje a consola indicando que el análisis se completó correctamente
            txtConsola.append("Análisis sintáctico completado exitosamente.\n");
        } catch (Exception e) {
            // Capturar y mostrar errores en la consola
            txtConsola.append("Error durante el análisis sintáctico: " + e.getMessage() + "\n");
            e.printStackTrace(new PrintWriter(new TextAreaWriter(txtConsola)));
        }
    }

    // Metodo para anadir Tokens
    public void agregarToken(Token token) {
        tokens.add(token);
    }

    // Método para añadir errores
    public void agregarError(Error error) {
        errores.add(error);
    }

    // Métodos para generar HTML
    public void generarReporteTokensHTML() {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Tabla de Tokens</h2>");
        html.append("<table border='1'>");
        html.append("<tr><th>#</th><th>Lexema</th><th>Tipo</th><th>Línea</th><th>Columna</th></tr>");

        for (Token token : tokens) {
            html.append("<tr>")
                    .append("<td>").append(token.numero).append("</td>")
                    .append("<td>").append(token.lexema).append("</td>")
                    .append("<td>").append(token.tipo).append("</td>")
                    .append("<td>").append(token.linea).append("</td>")
                    .append("<td>").append(token.columna).append("</td>")
                    .append("</tr>");
        }

        html.append("</table>");
        html.append("</body></html>");

        guardarComoArchivo("tokens.html", html.toString());
    }

    public void generarReporteErroresHTML() {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Tabla de Errores</h2>");
        html.append("<table border='1'>");
        html.append("<tr><th>#</th><th>Tipo</th><th>Descripción</th><th>Línea</th><th>Columna</th></tr>");

        for (Error error : errores) {
            html.append("<tr>")
                    .append("<td>").append(error.numero).append("</td>")
                    .append("<td>").append(error.tipo).append("</td>")
                    .append("<td>").append(error.descripcion).append("</td>")
                    .append("<td>").append(error.linea).append("</td>")
                    .append("<td>").append(error.columna).append("</td>")
                    .append("</tr>");
        }

        html.append("</table>");
        html.append("</body></html>");

        guardarComoArchivo("errores.html", html.toString());
    }

    private void guardarComoArchivo(String nombreArchivo, String contenido) {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write(contenido);
            System.out.println("Archivo guardado: " + nombreArchivo);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Funcionalidad para "Guardar como"
    private void guardarComo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Como");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Añadir la extensión .ca si no está presente
            if (!fileToSave.getName().endsWith(".ca")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".ca");
            }
            currentFile = fileToSave;
            guardar(); // Ahora guardamos el archivo en la nueva ubicación
        }
    }

    private void guardar() {
        if (currentFile != null) {
            try (FileWriter writer = new FileWriter(currentFile)) {
                // Guardar el contenido de txtEntrada en el archivo
                writer.write(txtEntrada.getText());
                System.out.println("Archivo guardado: " + currentFile.getName());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            guardarComo();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtEntrada = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtConsola = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        nuevoArchivoItem = new javax.swing.JMenuItem();
        guardarItem = new javax.swing.JMenuItem();
        guardarComoItem = new javax.swing.JMenuItem();
        salirItem = new javax.swing.JMenuItem();
        menuHerramientas = new javax.swing.JMenu();
        menuEjecutar = new javax.swing.JMenuItem();
        menuReportes = new javax.swing.JMenu();
        menuTokens = new javax.swing.JMenuItem();
        menuErrores = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Proyecto 1");
        setSize(new java.awt.Dimension(800, 600));

        jSplitPane1.setDividerLocation(50);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setToolTipText("");

        jSplitPane2.setResizeWeight(0.5);

        jLabel1.setText("Entrada");

        txtEntrada.setColumns(20);
        txtEntrada.setRows(5);
        jScrollPane1.setViewportView(txtEntrada);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane2.setLeftComponent(jPanel3);

        jLabel2.setText("Gráfica");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 167, Short.MAX_VALUE)
        );

        jButton1.setText("Anterior");

        jButton2.setText("Siguiente");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(14, 14, 14))
        );

        jSplitPane2.setRightComponent(jPanel2);

        jSplitPane1.setLeftComponent(jSplitPane2);

        jLabel4.setText("Consola");

        txtConsola.setColumns(20);
        txtConsola.setRows(5);
        jScrollPane4.setViewportView(txtConsola);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );

        menuArchivo.setText("Archivo");
        menuArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuArchivoActionPerformed(evt);
            }
        });

        nuevoArchivoItem.setText("Nuevo Archivo");
        nuevoArchivoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoArchivoItemActionPerformed(evt);
            }
        });
        menuArchivo.add(nuevoArchivoItem);

        guardarItem.setText("Guardar");
        guardarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarItemActionPerformed(evt);
            }
        });
        menuArchivo.add(guardarItem);

        guardarComoItem.setText("Guardar como");
        guardarComoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarComoItemActionPerformed(evt);
            }
        });
        menuArchivo.add(guardarComoItem);

        salirItem.setText("Salir");
        salirItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirItemActionPerformed(evt);
            }
        });
        menuArchivo.add(salirItem);

        jMenuBar1.add(menuArchivo);

        menuHerramientas.setText("Herramientas");
        menuHerramientas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHerramientasActionPerformed(evt);
            }
        });

        menuEjecutar.setText("Ejecutar");
        menuEjecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEjecutarActionPerformed(evt);
            }
        });
        menuHerramientas.add(menuEjecutar);

        jMenuBar1.add(menuHerramientas);

        menuReportes.setText("Reportes");

        menuTokens.setText("Tabla de Tokens");
        menuTokens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTokensActionPerformed(evt);
            }
        });
        menuReportes.add(menuTokens);

        menuErrores.setText("Tabla de Errores");
        menuErrores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuErroresActionPerformed(evt);
            }
        });
        menuReportes.add(menuErrores);

        jMenuBar1.add(menuReportes);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuArchivoActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Crear Nuevo Archivo");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Añadir la extensión .ca si no está presente
            if (!fileToSave.getName().endsWith(".ca")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".ca");
            }
            currentFile = fileToSave;
            try {
                // Crear el archivo vacío
                if (currentFile.createNewFile()) {
                    System.out.println("Archivo creado: " + currentFile.getName());
                } else {
                    System.out.println("El archivo ya existe.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_menuArchivoActionPerformed

    private void guardarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarItemActionPerformed
        if (currentFile != null) {
            guardar();
        } else {
            guardarComo();
        }
    }//GEN-LAST:event_guardarItemActionPerformed

    private void guardarComoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarComoItemActionPerformed
        guardarComo();
    }//GEN-LAST:event_guardarComoItemActionPerformed

    private void salirItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirItemActionPerformed
        System.exit(0); // Cierra la aplicación
    }//GEN-LAST:event_salirItemActionPerformed

    private void nuevoArchivoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoArchivoItemActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Crear Nuevo Archivo");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Añadir la extensión .ca si no está presente
            if (!fileToSave.getName().endsWith(".ca")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".ca");
            }
            currentFile = fileToSave;
            try {
                // Crear el archivo vacío
                if (currentFile.createNewFile()) {
                    System.out.println("Archivo creado: " + currentFile.getName());
                    txtEntrada.setText("");
                } else {
                    System.out.println("El archivo ya existe.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_nuevoArchivoItemActionPerformed

    private void menuHerramientasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHerramientasActionPerformed
        //Limpia la consola antes de ejecutar
        txtConsola.setText("");

        // obener el texto de entrada de JTextArea
        String inputText = txtEntrada.getText();

        // Crea un StringReader para el Lexer
        StringReader reader = new StringReader(inputText);

        //Crea una instancia de Lexer
        Lexer lexer = new Lexer(reader);
        System.out.println("Se ejecuto el Lexer.java");

        //Crea una instancia del Parser
        Parser parser = new Parser(lexer);

        try {
            // Ejecutar el parser para iniciar el análisis
            parser.parse();

            // Enviar mensaje a consola indicando que el análisis se completó correctamente
            txtConsola.append("Análisis completado exitosamente.\n");
        } catch (Exception e) {
            // Capturar y mostrar errores en la consola
            txtConsola.append("Error durante el análisis: " + e.getMessage() + "\n");
            e.printStackTrace(new PrintWriter(new TextAreaWriter(txtConsola)));
        }
    }//GEN-LAST:event_menuHerramientasActionPerformed

    private void menuEjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEjecutarActionPerformed
        //Limpia la consola antes de ejecutar
        txtConsola.setText("");
        analizarTokens(); //Analiza los tokens y genera el reporte
        analizarSintactico(); // Analiza sintacticamente el codigo

        // obener el texto de entrada de JTextArea
//        String inputText = txtEntrada.getText();
//
//        // Crea un StringReader para el Lexer
//        StringReader reader = new StringReader(inputText);
//
//        //Crea una instancia de Lexer
//        Lexer lexer = new Lexer(reader);
//        System.out.println("Se ejecuto el Lexer.java");
//
//        //Crea una instancia del Parser
//        Parser parser = new Parser(lexer);
        try {
            // Ejecutar el parser para iniciar el análisis
            //parser.parse();

            // Enviar mensaje a consola indicando que el análisis se completó correctamente
            txtConsola.append("Análisis completado exitosamente.\n");
        } catch (Exception e) {
            // Capturar y mostrar errores en la consola
            txtConsola.append("Error durante el análisis: " + e.getMessage() + "\n");
            e.printStackTrace(new PrintWriter(new TextAreaWriter(txtConsola)));
        }
    }//GEN-LAST:event_menuEjecutarActionPerformed

    private void menuTokensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTokensActionPerformed
        // Crear y mostrar la ventana de la tabla de tokens
        TokenTableForm tokenTableForm = new TokenTableForm();
        tokenTableForm.setVisible(true);
    }//GEN-LAST:event_menuTokensActionPerformed

    private void menuErroresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuErroresActionPerformed
        // Crear y mostrar la ventana de la tabla de errores
        ErrorTableForm errorTableForm = new ErrorTableForm();
        errorTableForm.setVisible(true);
    }//GEN-LAST:event_menuErroresActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem guardarComoItem;
    private javax.swing.JMenuItem guardarItem;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenuItem menuEjecutar;
    private javax.swing.JMenuItem menuErrores;
    private javax.swing.JMenu menuHerramientas;
    private javax.swing.JMenu menuReportes;
    private javax.swing.JMenuItem menuTokens;
    private javax.swing.JMenuItem nuevoArchivoItem;
    private javax.swing.JMenuItem salirItem;
    private javax.swing.JTextArea txtConsola;
    private javax.swing.JTextArea txtEntrada;
    // End of variables declaration//GEN-END:variables
}
