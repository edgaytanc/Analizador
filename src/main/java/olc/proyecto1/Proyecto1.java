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
         # Archivo de prueba intermedio
         
         {
         
             #Definimos los conjuntos
             CONJ: cA -> 0~9;
             CONJ: cB -> 2,4,6,8;
         
             OPERA : op1 -> U U ^ ^ {cA} {cA} {cB}; # ((^^A) U A) U B
             OPERA : op2 -> - U & {cA} {cB} {cA} {cB}; # (A U(A & B)) - B
         
             EVALUAR({$,%,9}, op1);
             EVALUAR({a, 2, C},op2);
         
         }
         
         <!
         op1 se simplifica a: cA U cB
         op2 se simplifica a: cA - cB
         
         En el evaluar de op1 solo 9 es exitoso
         En el evaluar de op2 todos fallan
         !>
         """; 

        // Crear un Reader para la entrada de prueba
        Reader inputString = new StringReader(testInput);
        // Crear una instancia del Lexer
        Lexer lexer = new Lexer(inputString);
        // Crear una instancia del Parser pasando el Lexer
        Parser parser = new Parser(lexer, new DefaultSymbolFactory());

        Symbol token;
        try {
//            while ((token = lexer.next_token()).sym != sym.EOF) {
//                String tipo = token.toString();
//            } 
            parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
