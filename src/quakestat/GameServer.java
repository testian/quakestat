/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;
import java.net.InetAddress;
import java.util.List;
/**
 *
 * @author testi
 */
public class GameServer {
private InetAddress host;
private int port;
private String name;
private String gametype;
private String map;
private int playerCount;
private int maxPlayerCount;
private int ping;
private List<Player> players;
private String gameName;

    public GameServer(InetAddress host, int port, String name, String gametype, String map, int playerCount, int maxPlayerCount, int ping, List<Player> players) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.gametype = gametype;
        this.map = map;
        this.playerCount = playerCount;
        this.maxPlayerCount = maxPlayerCount;
        this.ping = ping;
        this.players = players;
        this.gameName = null;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getName() {
        return name;
    }



    public String getGametype() {
        return gametype;
    }

    public InetAddress getHost() {
        return host;
    }

    public String getMap() {
        return map;
    }

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public int getPing() {
        return ping;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getPort() {
        return port;
    }

    public String serverDescription() {
    return gameNamePrefix() + denull(this.host) + ":" + denull(this.port) + " Name: " + denull(this.name) + ", Type: " + denull(this.gametype) + ", Map: " + denull(this.map) + ", Latency: " + denull(this.ping) + "ms, Players: " + denull(this.playerCount) + "/" + denull(this.maxPlayerCount);
    }
    @Override
    public String toString() {
    return serverDescription();
    }
    private String gameNamePrefix() {
    if (gameName == null) return "";
    return gameName + " ";
    }

    private String denull(String denull) {
    if (denull == null) return "<Unknown>";
    return denull;
    }
    private String denull(InetAddress denull) {
    if (denull == null) return "<Unknown>";
    return denull.getHostAddress();
    
    }

    private String denull(int denull) {
    if (denull < 0)return "<Unknown>";
    return Integer.toString(denull);
    }
}
