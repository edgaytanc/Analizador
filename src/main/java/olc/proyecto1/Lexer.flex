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
    return new Symbol(type, yyline, yycolumn);
  }

  private Symbol sym(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
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
NOT         = "~"
SYMBOL_COLON    = ":"
SYMBOL_SEMICOLON = ";"
SYMBOL_OPEN_PAREN  = "\("
SYMBOL_CLOSE_PAREN = "\)"
ALPHABET    = [a-zA-Z]
DIGIT       = [0-9]
WHITESPACE  = [ \t\n\r]+
COMMENT_LINE = "#" .*
COMMENT_MULTILINE_START = "<!"
COMMENT_MULTILINE_END = "!>"
OPERATIONS  = ["U" "&" "^" "-"]

%%

<YYINITIAL> {

  {CONJ}                { return sym(sym.CONJ, yytext()); }   // Asegurando que `CONJ` retorne su valor
  {OPERA}               { return sym(sym.OPERA, yytext()); }  // Similar para `OPERA` y `EVALUAR`
  {EVALUAR}             { return sym(sym.EVALUAR, yytext()); }

  {CLOSE_BRACE}         { return sym(sym.CLOSE_BRACE, yytext().charAt(0)); }
  {OPEN_BRACE}          { return sym(sym.OPEN_BRACE, yytext().charAt(0)); }
  {ARROW}               { return sym(sym.ARROW, yytext()); }
  {COMMA}               { return sym(sym.COMMA, yytext().charAt(0)); }
  {NOT}                 { return sym(sym.NOT, yytext().charAt(0)); }
  {SYMBOL_COLON}        { return sym(sym.SYMBOL, yytext().charAt(0)); }
  {SYMBOL_SEMICOLON}    { return sym(sym.SYMBOL, yytext().charAt(0)); }
  {SYMBOL_OPEN_PAREN}   { return sym(sym.SYMBOL, yytext().charAt(0)); }
  {SYMBOL_CLOSE_PAREN}  { return sym(sym.SYMBOL, yytext().charAt(0)); }

  {ALPHABET}+           { return sym(sym.IDENTIFIER, yytext()); }
  {DIGIT}+              { return sym(sym.NUMBER, Integer.parseInt(yytext())); }

  {OPERATIONS}          { return sym(sym.OPERATION, yytext().charAt(0)); }

  {COMMENT_LINE}        { /* skip single line comments */ }

  {COMMENT_MULTILINE_START} { yybegin(COMMENT); }
  
  {WHITESPACE}          { /* skip whitespace */ }
  
  .                     { reportError("Illegal character: " + yytext()); }

}

<COMMENT> {

  {COMMENT_MULTILINE_END} { yybegin(YYINITIAL); }

  .|\n|\r  { /* skip multiline comments */ }

}
