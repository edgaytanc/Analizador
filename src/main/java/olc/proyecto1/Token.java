/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package olc.proyecto1;

public class Token {

    int numero;
    String lexema;
    String tipo;
    int linea;
    int columna;

    public Token(int numero, String lexema, String tipo, int linea, int columna) {
        this.numero = numero;
        this.lexema = lexema;
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
    }
}
