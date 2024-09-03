package olc.proyecto1;
import java_cup.runtime.Symbol;
%%

%class Lexer
%unicode
%cup
%line
%column
%public

%{

  private void reportError(String message) {
    System.err.println("ERROR: " + message + " at line " + (yyline+1) + ", column " + yycolumn);
  }
  
  private Symbol sym(int type) {
    return new Symbol(type, yyline+1, yycolumn);
  }

  private Symbol sym(int type, Object value) {
    return new Symbol(type, yyline+1, yycolumn, value);
  }

%}

%state COMMENT

CONJ        = "CONJ"
OPERA       = "OPERA"
EVALUAR     = "EVALUAR"
CLOSE_BRACE = "}"
OPEN_BRACE  = "{"
ARROW       = "->"
COMMA       = ","
RANGE       = "~"
COLON       = ":"
SEMICOLON   = ";"
OPEN_PAREN  = "\("
CLOSE_PAREN = "\)"
ALPHABET    = [a-zA-Z]
DIGIT       = [0-9]
WHITESPACE  = [ \t\n\r]+
COMMENT_LINE = "#" .*
COMMENT_MULTILINE_START = "<!"
COMMENT_MULTILINE_END = "!>"
AND_OP      = "&"
UNION_OP    = "U"
XOR_OP      = "\^"
DIFF_OP     = "-"

%%

<YYINITIAL> {

  {CONJ}                { 
                          System.out.println("Token: CONJ (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.CONJ, yytext()); 
                        }
  {OPERA}               { 
                          System.out.println("Token: OPERA (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.OPERA, yytext()); 
                        }
  {EVALUAR}             { 
                          System.out.println("Token: EVALUAR (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.EVALUAR, yytext()); 
                        }

  {CLOSE_BRACE}         { 
                          System.out.println("Token: CLOSE_BRACE (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.CLOSE_BRACE, yytext().charAt(0)); 
                        }
  {OPEN_BRACE}          { 
                          System.out.println("Token: OPEN_BRACE (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.OPEN_BRACE, yytext().charAt(0)); 
                        }
  {ARROW}               { 
                          System.out.println("Token: ARROW (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.ARROW, yytext()); 
                        }
  {COMMA}               { 
                          System.out.println("Token: COMMA (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.COMMA, yytext().charAt(0)); 
                        }
  {RANGE}               { 
                          System.out.println("Token: RANGE (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.RANGE, yytext().charAt(0)); 
                        }
  {COLON}               { 
                          System.out.println("Token: COLON (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.COLON, yytext().charAt(0)); 
                        }
  {SEMICOLON}           { 
                          System.out.println("Token: SEMICOLON (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.SEMICOLON, yytext().charAt(0)); 
                        }
  {OPEN_PAREN}          { 
                          System.out.println("Token: OPEN_PAREN (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.OPEN_PAREN, yytext().charAt(0)); 
                        }
  {CLOSE_PAREN}         { 
                          System.out.println("Token: CLOSE_PAREN (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.CLOSE_PAREN, yytext().charAt(0)); 
                        }

  {ALPHABET}({ALPHABET}|{DIGIT})*   { 
                          System.out.println("Token: IDENTIFIER (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.IDENTIFIER, yytext()); 
                        }
  {DIGIT}+              { 
                          System.out.println("Token: NUMBER (" + yytext() + ") at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.NUMBER, Integer.parseInt(yytext())); 
                        }

  {AND_OP}              { 
                          System.out.println("Token: AND_OP (&) at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.AND_OP, yytext().charAt(0)); 
                        }
  {UNION_OP}            { 
                          System.out.println("Token: UNION_OP (U) at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.UNION_OP, yytext().charAt(0)); 
                        }
  {XOR_OP}+             { 
                          System.out.println("Token: XOR_OP (^) at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.XOR_OP, yytext().charAt(0)); 
                        }
  {DIFF_OP}             { 
                          System.out.println("Token: DIFF_OP (-) at line " + (yyline+1) + ", column " + yycolumn);
                          return sym(sym.DIFF_OP, yytext().charAt(0)); 
                        }

  {COMMENT_LINE}        { 
                          System.out.println("Skipping single line comment");
                          /* skip single line comments */ 
                        }

  {COMMENT_MULTILINE_START} { 
                          System.out.println("Starting multiline comment");
                          yybegin(COMMENT); 
                        }
  
  {WHITESPACE}          { 
                          /* skip whitespace */ 
                        }
  
  .                     { 
                          reportError("Illegal character: " + yytext());
                        }

}

<COMMENT> {

  {COMMENT_MULTILINE_END} { 
                          System.out.println("Ending multiline comment");
                          yybegin(YYINITIAL); 
                        }

  .|\n|\r  { 
              System.out.println("Skipping multiline comment content");
              /* skip multiline comments */ 
           }

}
