/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;
import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author testi
 */
public class QueryScanner {


private List<Token> validTokens(String substring) {
LinkedList validTokens = new LinkedList<Token>();
if (substring.equals(" ")){validTokens.add(new Token(Token.TokenType.SPACE));}
if (substring.equals(",")){validTokens.add(new Token(Token.TokenType.COMMA));}
if (substring.equals("=")){validTokens.add(new Token(Token.TokenType.EQ));}
if (substring.equals("~=")){validTokens.add(new Token(Token.TokenType.LIKE));}
if (substring.equals("CONTAINS")){validTokens.add(new Token(Token.TokenType.CONTAINS));}
if (substring.equals("~CONTAINS")){validTokens.add(new Token(Token.TokenType.LIKE_CONTAINS));}
if (substring.equals("!=")){validTokens.add(new Token(Token.TokenType.NOT_EQ));}
if (substring.equals("!~=")){validTokens.add(new Token(Token.TokenType.NOT_LIKE));}
if (substring.equals("!CONTAINS")){validTokens.add(new Token(Token.TokenType.NOT_CONTAINS));}
if (substring.equals("!~CONTAINS")){validTokens.add(new Token(Token.TokenType.NOT_LIKE_CONTAINS));}
if (substring.equals("<")) validTokens.add(new Token(Token.TokenType.LT));
if (substring.equals(">")) validTokens.add(new Token(Token.TokenType.GT));
if (substring.equals("<=")) validTokens.add(new Token(Token.TokenType.LTE));
if (substring.equals(">=")) validTokens.add(new Token(Token.TokenType.GTE));
if (substring.length()> 1 && ( (substring.charAt(0) == '\"' && substring.charAt(substring.length()-1) == '\"') || (substring.charAt(0) == '\'' && substring.charAt(substring.length()-1) == '\'') )) {

    boolean valid = true;
    for (int i = 1; i < substring.length()-1; i++) {
    if (substring.charAt(i) == substring.charAt(0)) {valid = false; break;}
}
    if (valid) {
    validTokens.add(new Token(Token.TokenType.STRING, substring.substring(1,substring.length()-1)));
    }
}
try{
int iUserData = Integer.parseInt(substring);
validTokens.add(new Token(Token.TokenType.INTEGER, iUserData));
} catch (NumberFormatException ex) {}
if (substring.equals("("))validTokens.add(new Token(Token.TokenType.BRACKET_OPEN));
if (substring.equals(")"))validTokens.add(new Token(Token.TokenType.BRACKET_CLOSE));
if (substring.equals("WHERE")){validTokens.add(new Token(Token.TokenType.WHERE));}
if (substring.equals("LIMIT")){validTokens.add(new Token(Token.TokenType.LIMIT));}
if (substring.equals("SORT BY")){validTokens.add(new Token(Token.TokenType.SORT));}
if (substring.equals("SERVERNAME")){validTokens.add(new Token(Token.TokenType.SERVERNAME));}
if (substring.equals("GAMENAME")){validTokens.add(new Token(Token.TokenType.GAMENAME));}
if (substring.equals("PLAYERNAME")){validTokens.add(new Token(Token.TokenType.PLAYERNAME));}
if (substring.equals("GAMETYPE")){validTokens.add(new Token(Token.TokenType.GAMETYPE));}
if (substring.equals("MAP")){validTokens.add(new Token(Token.TokenType.MAP));}
if (substring.equals("PING")){validTokens.add(new Token(Token.TokenType.PING));}
if (substring.equals("PLAYERCOUNT")){validTokens.add(new Token(Token.TokenType.PLAYERCOUNT));}
if (substring.equals("MAXPLAYERCOUNT")){validTokens.add(new Token(Token.TokenType.MAXPLAYERCOUNT));}
if (substring.equals("SOURCE")){validTokens.add(new Token(Token.TokenType.SOURCE));}
if (substring.equals("AND")){validTokens.add(new Token(Token.TokenType.AND));}
if (substring.equals("OR")){validTokens.add(new Token(Token.TokenType.OR));}
if (substring.equals("SLOTS")){validTokens.add(new Token(Token.TokenType.SLOTS));}
if (substring.equals("HOST")){validTokens.add(new Token(Token.TokenType.HOST));}
if (substring.equals("PORT")){validTokens.add(new Token(Token.TokenType.PORT));}

return validTokens;
}
public LinkedList<Token> scan(String queryText) throws ScanException {
LinkedList<Token> finalTokens = new LinkedList<Token>();
int i = 0;
while (i < queryText.length()) {

    Token validToken = null;
    int k = 0;
    for (int j = i; j < queryText.length(); j++) {
//    System.out.println("Checking: " + queryText.substring(i, j+1));
    List<Token> validTokens = validTokens(queryText.substring(i, j+1));
    if (validTokens.size() == 1) {
    validToken = validTokens.get(0);
    k = j;
    }
    //System.out.println("Tokens:");
    /*for (Token t : validTokens)    {
    System.out.println("Debug: " + t);
    }*/
    }
    i=k+1;

        if (validToken == null) throw new ScanException("Error after scanning " + finalTokens.size() + " tokens. " + (!finalTokens.isEmpty()?"Last token recognized: " + finalTokens.peekLast().tokenType() :""));
        if (validToken.tokenType() != Token.TokenType.SPACE) {finalTokens.add(validToken);}
}
return finalTokens;
}
}
