/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
/**
 *
 * @author testi
 */
public class QuakestatTest {
public static void main(String[] args) {
try {
    //QuakeStat qstat = new Savage2Stat("sa2s",InetAddress.getByName("193.33.186.10"),11237);//new QuakeStat("q3m",InetAddress.getByName("master.quake3arena.com"));
    ServerFetcher qstat = new ServerFetcherFactory().createServerFetcher("sturmovik", InetAddress.getByName("violetsky.ch"),3080);//; .createMasterServerFetcher("sturmovik_master", "sturmovik", "#unsupported#");
    qstat.setIncludePlayers(true);
    qstat.parseServers(new ServerListener() {

                public boolean proceed() {
                    return true;
                }

                public void onServer(GameServer server) {
                    System.out.println(server);
                    for (Player p : server.getPlayers()) {
                    System.out.println("    " + p.playerDescription());
                    }
                }

    });
    
} catch (UnknownHostException ex) {
System.err.println();
} catch (IOException ex) {
System.err.println(ex);
}
}
}
