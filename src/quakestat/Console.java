/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;

import java.io.IOException;
import quakestat.query.QueryException;
import quakestat.query.QueryExecution;

/**
 *
 * @author testi
 */
public class Console {
public static void main(String[] args) {

    
    if (args.length > 0) {
    boolean includePlayers = false;
    StringBuffer queryString = new StringBuffer();
    boolean startedQuery = false;
    for (int i = 0; i < args.length; i++) {
    if (!startedQuery && args[i].startsWith("-")) {
    if (args[i].equals("-p")) includePlayers = true;
    continue;
    }
    queryString.append(args[i] + " ");
    startedQuery = true;
    }
    final boolean finalIncludePlayers = includePlayers;
    try {
    ServerFinder finder = new ServerFinder("games.list");
    finder.findServerByRequest(new QueryExecution(queryString.toString()), new ServerListener() {

                    public void onServer(GameServer server) {
                        System.out.println(server.serverDescription());
                        if (finalIncludePlayers) {
                        for (Player p : server.getPlayers()) {
                        System.out.println("    " + p.toString());
                        }
                        }
                    }

                    public boolean proceed() {
                        return true;
                    }


    });
    } catch (IOException ex) {
    System.err.println("Error: " + ex.getMessage());
    }
    catch (QueryException ex) {System.err.println("Error: " + ex.getMessage());}
    catch (MatchException ex) {System.err.println("Error: " + ex.getMessage());}
    return;
}

    System.out.println("Java Quakestat Console Version 1.0");



String line;
boolean includePlayers = false;
while ((line = System.console().readLine()) != null) {
    if (line.equals("exit")) break;
    if (line.equals("players")) {
    includePlayers = !includePlayers;
    System.out.println("Listing players is now " + (includePlayers ? "enabled" : "disabled") + ".");
    continue;
    }
    final boolean finalIncludePlayers = includePlayers;
    try {
    
        
    ServerFinder finder = new ServerFinder("games.list");

    finder.findServerByRequest(new QueryExecution(line), new ServerListener() {

                    public void onServer(GameServer server) {
                        System.out.println(server.serverDescription());
                        if (finalIncludePlayers) {
                        for (Player p : server.getPlayers()) {
                        System.out.println("    " + p.toString());
                        }
                        }
                    }

                    public boolean proceed() {
                        return true;
                    }
                    

    });
    System.out.println("End of list");
    } catch (IOException ex) {
    System.err.println("Error: " + ex.getMessage());
    }
    catch (QueryException ex) {System.err.println("Error: " + ex.getMessage());}
    
    catch (MatchException ex) {System.err.println("Error: " + ex.getMessage());}
    }

System.out.println("Exiting");

}
}
