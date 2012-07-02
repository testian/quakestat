/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;

import quakestat.GameServer;
import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author testi
 */
public class OrCondition implements Condition {
private List<Condition> conditions;
public OrCondition() {
conditions = new LinkedList<Condition>();
}
public void addCondition(Condition condition) {

    conditions.add(condition);
}
    public boolean satisfies(GameServer server) {
        for (Condition c : conditions) {
        if (c.satisfies(server)) return true;
        }
        return false;
    }

    public void removeCondition(Condition condition) {
        conditions.remove(condition);
    }

        public boolean needsPlayers() {
        for (Condition c : conditions) {
        if (c.needsPlayers()) return true;
        }

        return false;
    }

        @Override
    public String toString() {
    StringBuffer s = new StringBuffer();
    s.append("(");
    for (Condition c : conditions) {
    s.append(c);
    if (c != conditions.get(conditions.size()-1)){
    s.append(" OR ");
    }
    }
    s.append(")");
    return s.toString();
    }

}
