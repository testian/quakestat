/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;
import quakestat.GameServer;
/**
 *
 * @author testi
 */
public interface Condition {
public boolean satisfies(GameServer server);
public void addCondition(Condition condition);
public void removeCondition(Condition condition);
public boolean needsPlayers();
}
