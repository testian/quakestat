/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;
import java.util.List;
/**
 *
 * @author testi
 */
public interface Request {
    public boolean mustBeNotEmpty();
    public boolean mustBeNotFull();
    public boolean mustBeUp();
    public boolean includePlayers();
    public void prepare(List<InternetGame> gamesList) throws MatchException;
    public boolean acceptServer(GameServer server);
    public boolean proceed();
    public void finalProcessing(List<GameServer> gamesList);
    public boolean finalProcessing();
    
}
