/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package olc.proyecto1;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java_cup.runtime.DefaultSymbolFactory;
import java_cup.runtime.Symbol;

/**
 *
 * @author David Gaytan
 */
public class Proyecto1 {

    public static void main(String[] args) throws Exception {
        String testInput = """ 
         {
          CONJ:conjuntoA->1,2,3,a,b;
          CONJ:conjuntoB->a~z;
          CONJ:conjuntoC->0~9;
          OPERA:operacion1->&{conjuntoA}{conjuntoB};
          OPERA:operacion2->&U{conjuntoB}{conjuntoC}{conjuntoA};
          EVALUAR({a,b,c},operacion1);
          EVALUAR({1,b},operacion2);
         }
         """;

        // Crear un Reader para la entrada de prueba
        Reader inputString = new StringReader(testInput);
        // Crear una instancia del Lexer
        Lexer lexer = new Lexer(inputString);
        // Crear una instancia del Parser pasando el Lexer
        Parser parser = new Parser(lexer, new DefaultSymbolFactory());

        Symbol token;
        try {
            while ((token = lexer.next_token()).sym != sym.EOF) {
                String tipo = token.toString();
                //System.out.println(tipo);
                System.out.println("Token: " + sym.terminalNames[token.sym] + " (" + token.value + ") at line " + token.left + ", column " + token.right);
            }
            parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
