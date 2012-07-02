/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;

import quakestat.GameServer;
import quakestat.Player;
import quakestat.query.Token.TokenType;
/**
 *
 * @author testi
 */
public class BasicCondition implements Condition {
    TokenType fieldName;
    TokenType operator;
    private int intValue;
    private String stringValue;
    private boolean numeric() {
    return (stringValue == null);
    }

    public BasicCondition(TokenType fieldName, TokenType operator, int intValue) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.intValue = intValue;
        stringValue = null;
    }

    public BasicCondition(TokenType fieldName, TokenType operator, String stringValue) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.stringValue = stringValue;
        intValue = 0;
    }

    public void addCondition(Condition condition) {
        throw new IllegalArgumentException("Cannot add condition to basic condition");
    }
        public void removeCondition(Condition condition) {
           throw new IllegalArgumentException("Cannot remove condition from basic condition");
    }

@Override
public String toString() {
return fieldName + " " + operator + " '" + (numeric() ? intValue : stringValue) + "'";
}


    public boolean satisfies(GameServer server) {
    if (numeric()) {
    int serverField = getIntValue(server);
    if (serverField<0) return false;
    if (operator == TokenType.EQ) return serverField == intValue;
    if (operator == TokenType.LT) return serverField < intValue;
    if (operator == TokenType.GT) return serverField > intValue;
    if (operator == TokenType.LTE) return serverField <= intValue;
    if (operator == TokenType.GTE) return serverField >= intValue;
    if (operator == TokenType.NOT_EQ) return serverField != intValue;
    }
    else if (fieldName != TokenType.PLAYERNAME) {
    String serverField = getStringValue(server);
    if (serverField == null) return false;
    if (operator == TokenType.EQ) return stringValue.equals(serverField);
    if (operator == TokenType.LIKE) return stringValue.toLowerCase().equals(serverField.toLowerCase());
    if (operator == TokenType.CONTAINS) return serverField.contains(stringValue);
    if (operator == TokenType.LIKE_CONTAINS) return serverField.toLowerCase().contains(stringValue.toLowerCase());
    if (operator == TokenType.NOT_EQ) return !stringValue.equals(serverField);
    if (operator == TokenType.NOT_LIKE) return !stringValue.toLowerCase().equals(serverField.toLowerCase());
    if (operator == TokenType.NOT_CONTAINS) return !serverField.contains(stringValue);
    if (operator == TokenType.NOT_LIKE_CONTAINS) return !serverField.toLowerCase().contains(stringValue.toLowerCase());
    }
    else {
    
    for (Player p : server.getPlayers()) {
    String serverField = p.getName();
    if (operator == TokenType.EQ && stringValue.equals(serverField)) return true;
    if (operator == TokenType.LIKE && stringValue.toLowerCase().equals(serverField.toLowerCase())) return true;
    if (operator == TokenType.CONTAINS && serverField.contains(stringValue)) return true;
    if (operator == TokenType.LIKE_CONTAINS && serverField.toLowerCase().contains(stringValue.toLowerCase())) return true;
    
    if (operator == TokenType.NOT_EQ && stringValue.equals(serverField)) return false;
    if (operator == TokenType.NOT_LIKE && stringValue.toLowerCase().equals(serverField.toLowerCase())) return false;
    if (operator == TokenType.NOT_CONTAINS && serverField.contains(stringValue)) return false;
    if (operator == TokenType.NOT_LIKE_CONTAINS && serverField.toLowerCase().contains(stringValue.toLowerCase())) return false;
    }
    }
    return (operator == TokenType.NOT_EQ || operator == TokenType.NOT_LIKE || operator == TokenType.NOT_CONTAINS || operator == TokenType.NOT_LIKE_CONTAINS);
    }
    private String getStringValue(GameServer s) {
    if (fieldName == TokenType.GAMETYPE) return s.getGametype();
    if (fieldName == TokenType.MAP) return s.getMap();
    if (fieldName == TokenType.SERVERNAME) return s.getName();
    if (fieldName == TokenType.HOST) return s.getHost().getHostAddress();
    return null;
    }
    private int getIntValue(GameServer s) {
    if (fieldName == TokenType.PLAYERCOUNT) return s.getPlayerCount();
    if (fieldName == TokenType.MAXPLAYERCOUNT) return s.getMaxPlayerCount();
    if (fieldName == TokenType.PING) return s.getPing();
    if (fieldName == TokenType.SLOTS && s.getPlayerCount() != -1 && s.getMaxPlayerCount() != -1) return s.getMaxPlayerCount()-s.getPlayerCount();
    if (fieldName == TokenType.PORT) return s.getPort();
    return -1;
    }
    public boolean needsPlayers() {
    return fieldName == TokenType.PLAYERNAME;
    }

}
