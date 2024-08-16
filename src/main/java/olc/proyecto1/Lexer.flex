import java_cup.runtime.Symbol;
%%

%class Lexer
%unicode
%cup
%line
%column
%public

%{

  //public int yyline;
  //public int yycolumn;

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

CONJ       = "CONJ"
OPERA      = "OPERA"
EVALUAR    = "EVALUAR"
CLOSE_BRACE = "}"
OPEN_BRACE = "{"
ARROW      = "->"
COMMA      = ","
NOT        = "~"
ALPHABET   = [a-zA-Z]
DIGIT      = [0-9]
WHITESPACE = [ \t\n\r]+
COMMENT_LINE = "#"[^(\n|\r)]*
COMMENT_MULTILINE_START = "<!"
COMMENT_MULTILINE_END = "!>"
SYMBOLS    = [\!-\~]
OPERATIONS = ["U" "&" "^" "-"]

%%

<YYINITIAL> {

  {CONJ}                { return sym(sym.CONJ); }
  {OPERA}               { return sym(sym.OPERA); }
  {EVALUAR}             { return sym(sym.EVALUAR); }

  {CLOSE_BRACE}         { return sym(sym.CLOSE_BRACE); }
  {OPEN_BRACE}          { return sym(sym.OPEN_BRACE); }
  {ARROW}               { return sym(sym.ARROW); }
  {COMMA}               { return sym(sym.COMMA); }
  {NOT}                 { return sym(sym.NOT); }

  {ALPHABET}+           { return sym(sym.IDENTIFIER, yytext()); }
  {DIGIT}+              { return sym(sym.NUMBER, Integer.parseInt(yytext())); }

  {SYMBOLS}             { return sym(sym.SYMBOL, yytext().charAt(0)); }

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



