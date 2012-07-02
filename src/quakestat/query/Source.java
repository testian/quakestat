/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;
import java.util.List;
import quakestat.InternetGame;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import quakestat.MatchException;
/**
 *
 * @author testi
 */
public class Source {

    private List<String> games;

    public Source() {
    games = null;
    }
    public Source(List<String> games) {
    this.games = games;
    }
    

    public boolean satisfy(List<InternetGame> gamesList) throws MatchException {
    if (games == null) return true;


        Set<String> gameSet = new HashSet<String>();

        for (String s : games) {
        gameSet.add(s);
        }




        Iterator<InternetGame> it = gamesList.iterator();

        while (it.hasNext()) {
        InternetGame next = it.next();
        if (!gameSet.contains(next.getName())) it.remove();
        else
        gameSet.remove(next.getName());
        }
        if (!gameSet.isEmpty()) {
        StringBuffer unknownGames = new StringBuffer();
        for (String s : gameSet) {
        unknownGames.append(" " + s + ";");
        }
        throw new MatchException("Unknown games:" + unknownGames.toString());
        }
        Collections.sort(gamesList, new Comparator<InternetGame>() {

            public int compare(InternetGame o1, InternetGame o2) {
                return games.indexOf(o1.getName()) - games.indexOf(o2.getName());
            }

        });


    return false;
    }

}
