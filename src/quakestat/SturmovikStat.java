/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import masterize.Report;
import masterize.Report.Type;
import masterize.modules.SturmovikGame;


/**
 *
 * @author testi
 */
public class SturmovikStat extends QuakeStat {
    public static final int DEFAULT_PORT = 21000;
    
    public SturmovikStat(String gameName, String hostName) throws UnknownHostException {
        super(gameName, hostName);
    }

    public SturmovikStat(String gameName, String hostName, int port) throws UnknownHostException {
        super(gameName, hostName, port);

    }

    public SturmovikStat(String gameName, InetAddress host) {
        super(gameName, host);

    }

    public SturmovikStat(String gameName, InetAddress host, int port) {
        super(gameName, host, port);
    }
    


    @Override
    public void parseServers(ServerListener l) throws IOException {
        SturmovikGame g = new SturmovikGame();
        if (!l.proceed()) return;
     int retries = this.getRetry();
     for (int r = 0 ; r < retries; r++) {
         long time = System.currentTimeMillis();
         Report report = g.createReport(getHost(), getPort(), Type.UPDATE, true);
         int ping = (int)(System.currentTimeMillis() - time);
         if (report != null) {
            List<Player> playerList = new LinkedList<Player>();
            for (masterize.Player s : report.getPlayerList()) {
            playerList.add(new Player(s.getName(),s.getPing(),s.getScore()));
            }
             GameServer gs = new GameServer(getHost(), getPort(),report.getServerName(), report.getGameType(), report.getMap(), report.getPlayerCount(), report.getMaxPlayerCount(), ping, playerList);
             l.onServer(gs);
             break;
         }


     }
    }

 

    @Override
    public int getPort() {
        int port = super.getPort();
        if (port == -1) return DEFAULT_PORT;
        return port;
    }

   
}
