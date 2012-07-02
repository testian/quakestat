/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;

/**
 *
 * @author testi
 */
public class Token {

public static enum TokenType {
EQ,
COMMA,
CONTAINS,
LIKE,
LT,
GT,
LTE,
GTE,
STRING,
INTEGER,
BRACKET_OPEN,
BRACKET_CLOSE,
SOURCE,
WHERE,
LIMIT,
SORT,
SPACE,
SERVERNAME,
PING,
GAMETYPE,
PLAYERCOUNT,
MAXPLAYERCOUNT,
MAP,
GAMENAME,
AND,
OR,
PLAYERNAME,
LIKE_CONTAINS,
NOT_EQ,
NOT_LIKE,
NOT_CONTAINS,
NOT_LIKE_CONTAINS,
SLOTS,
HOST,
PORT
}
private TokenType tokenType;
private String userData;
private int intUserData;
public Token(TokenType tokenType) {
this.tokenType = tokenType;
userData = null;
intUserData = 0;
}
public Token(TokenType tokenType, String userData) {
this.tokenType = tokenType;
this.userData = userData;
intUserData = 0;
}
public Token(TokenType tokenType, int intUserData) {
this.tokenType = tokenType;
this.intUserData = intUserData;
userData = null;
}
public TokenType tokenType() {return tokenType;}
public String userData() {return userData;}
public int intUserData() {return intUserData;}
public String toString() {
return "(" + tokenType + "," + userData + "," + intUserData + ")";
}


}