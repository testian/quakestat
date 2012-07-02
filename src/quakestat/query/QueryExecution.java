/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;

import quakestat.*;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import java.util.Arrays;
/**
 *
 * @author testi
 */
public class QueryExecution implements Request {
private Query query;
private int serverCount;

public QueryExecution(Query query) {
this.query = query;
serverCount = 0;
}

public QueryExecution(String queryText) throws QueryException {
this(Query.parseQuery(queryText));
}


    public boolean acceptServer(GameServer server) {
        if (query.satisfies(server)){
            serverCount++;
            return true;
        }
        return false;
    }

    public void finalProcessing(List<GameServer> gamesList) {
        if (query.sort() == null) return;
        Collections.sort(gamesList, query.sort());
        if (query.limit() > 0 ) {
        while (gamesList.size()>query.limit()) {
        gamesList.remove(gamesList.size()-1);
        }
        }
    }

    public boolean includePlayers() {
        return query.playerInfo();
    }

    public boolean mustBeNotEmpty() {
        return false;
    }

    public boolean mustBeNotFull() {
        return false;
    }

    public boolean mustBeUp() {
        return true;
    }

    public void prepare(List<InternetGame> gamesList) throws MatchException {

    if (query.satisfy(gamesList)) {

        Collections.sort(gamesList, new Comparator<InternetGame>() {

            public int compare(InternetGame o1, InternetGame o2) {
                return (o1.getName().compareTo(o2.getName()));
            }

        });}
    }

    public boolean proceed() {
        return (query.limit() <1 || serverCount < query.limit() || query.sort() != null);
        //return (query.limit()>0 && serverCount >=query.limit() && query.sort() == null);
    }
    public boolean finalProcessing() {
    return query.sort() != null;
    }

}
