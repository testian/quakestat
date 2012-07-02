/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;
import java.io.IOException;
import java.util.List;
import quakestat.query.QueryExecution;
import quakestat.query.QueryException;
/**
 *
 * @author testi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        ServerFinder serverFinder = new ServerFinder();
//        serverFinder.addGame(new InternetGame("ETQW","url","q4s","http://etqw-ipgetter.demonware.net/ipgetter/"));
        serverFinder.addGame(new InternetGame("Quake 3 Arena","q3m","q3s","master.quake3arena.com"));
        try {
            
            ServerFetcher qstat =  new ServerFetcherFactory().createMasterServerFetcher("url_savage", "sas", "http://masterserver.savage.s2games.com/");
        serverFinder.findServerByRequest(new QueryExecution("LIMIT 7"), new ServerListener() {
        //qstat.parseServers(new ServerListener() {
                public void onServer(GameServer server) {
                    System.out.println(server);
                }

                public boolean proceed() {
                    return true;
                }

        });
         /*List<GameServer> servers = serverFinder.findServerByRequest(new quakestat.Request() {

                public boolean mustBeNotEmpty() {
                    return false;
                }

                public boolean mustBeNotFull() {
                    return false;
                }

                public boolean mustBeUp() {
                    return true;
                }

                public boolean includePlayers() {
                    return false;
                }

                public void prepare(List<InternetGame> gamesList) {
                    return;
                }

                public boolean acceptServer(GameServer server) {
                    return true;
                }

                public boolean proceed() {
                    return true;
                }

                public void finalProcessing(List<GameServer> gamesList) {
                    return;
                }
            });*/
        //qstat.setMustBeNotFull(true);
        //qstat.setMustBeNotEmpty(true);
        /*qstat.setInterval(0.7);
        qstat.setRetry(500);
        qstat.setSendinterval(501);
        qstat.setMasterServerRetryInterval(0.9);
        qstat.setMustBeUp(true);*/


    }catch (Exception ex){System.out.println("Main program exception: " + ex);
    if (ex instanceof RuntimeException) {
    ex.printStackTrace();
    }
    }

        
    }

}
