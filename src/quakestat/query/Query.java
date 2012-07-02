/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;
import quakestat.GameServer;
import java.util.Comparator;
import quakestat.InternetGame;
import java.util.List;
import quakestat.MatchException;
/**
 *
 * @author testi
 */
public class Query {

    private Condition reqExpr;
    private int limit;
    private Comparator<GameServer> sort;
    private boolean requirePlayer;
    private Source source;

    public Query(Source source, Condition reqExpr, Comparator<GameServer> sort, int limit) {
        this.reqExpr = reqExpr;
        this.limit = limit;
        this.sort = sort;
        //this.requirePlayer = requirePlayer;
        this.source = source;
        requirePlayer = scanForPlayers();
    }
    private boolean scanForPlayers() {
    if (reqExpr == null) return false;
    return reqExpr.needsPlayers();
    }

    public boolean satisfies(GameServer server) {
        if (reqExpr == null) return true;
        return reqExpr.satisfies(server);
    }
    public boolean satisfy(List<InternetGame> game) throws MatchException {
    return source.satisfy(game);
    }

public int limit() {
return limit;
}

public Comparator<GameServer> sort() {
return sort;
}

    public boolean playerInfo() {
        return requirePlayer;
    }

public static Query parseQuery(String queryText) throws QueryException {
QueryParser p = new QueryParser(new QueryScanner().scan(queryText));
p.parseQuery();
return p.getQuery();
}
}
