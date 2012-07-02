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
public class GameServerMonitor {
private ServerFinder serverFinder;
private String gameName;
private InetAddress host;
private int port;
private GameServer lastServer;
private boolean freshStart;
    public GameServerMonitor(ServerFinder serverFinder, String gameName, InetAddress host, int port) {
        if (!serverFinder.getSupportedGames().contains(gameName)) throw new IllegalArgumentException("Game not supported by given ServerFinder instance");
        this.gameName = gameName;
        this.serverFinder = serverFinder;
        this.host = host;
        if (port < 1 || port > 65535) throw new IllegalArgumentException ("port must be within range 1-65535");
        this.port = port;
        lastServer = null;
        freshStart = true;
    }


    public void scan(GameServerListener listener, int times, long interval) {

        for (int i = 0; i < times; i++) {
        
            scan(listener);
        if (i+1<times) {
            try {Thread.sleep(interval);
        } catch (InterruptedException ex) {System.err.println(ex);}}
        }

    }

    synchronized public void scan(GameServerListener listener) {

        GameServer newServer = serverFinder.getServerInfo(gameName, host, port);
        if (freshStart){
        freshStart = false;
        lastServer = newServer;
        return;
        }
        if (lastServer == null && newServer != null) {
            listener.onStart(newServer);
        } else if (lastServer != null && newServer != null) {
        scanForChanges(listener, newServer);
        } else if (lastServer != null && newServer == null) {
        listener.onShutdown(lastServer);
        } else {
        //Keeps shut down
        }
        lastServer = newServer;

    }

    private void scanForChanges(GameServerListener listener, GameServer newServer) {
    if (lastServer.getName() != null && newServer.getName() != null && !lastServer.getName().equals(newServer.getName())) {
    listener.onServerNameChange(newServer, lastServer.getName(),newServer.getName());
    }
    
    
    if (lastServer.getMap() != null && newServer.getMap() != null && !lastServer.getMap().equals(newServer.getMap())) {
    listener.onMapChange(newServer, lastServer.getMap(),newServer.getMap());
    }
    
        if (lastServer.getGametype() != null && newServer.getGametype() != null && !lastServer.getGametype().equals(newServer.getGametype())) {
        listener.onGametypeChange(newServer, lastServer.getGametype(),newServer.getGametype());
    }

    scanForPlayerChanges(listener, newServer);



    }

    private void scanForPlayerChanges(GameServerListener listener, GameServer newServer) {
    List<Player> oldList = lastServer.getPlayers();
    List<Player> newList = newServer.getPlayers();
    if (oldList.isEmpty() && newList.isEmpty()) {
        int newPlayers = newServer.getPlayerCount()-lastServer.getPlayerCount();
        if (newPlayers != 0) {
        listener.onPlayerCountChange(newServer, newPlayers);
        }
        return;
    }
    for (Player po : oldList) {
    if (!containsPlayer(newList, po)) listener.onLeave(newServer, po);
    }

    for (Player pn : newList) {
    if (!containsPlayer(oldList, pn)) listener.onJoin(newServer, pn);
    }

    }

    private boolean containsPlayer(List<Player> list, Player player) { //Not very efficient
    for (Player p : list) {
    if (p.getName() != null && player.getName() != null && p.getName().equals(player.getName())) return true;
    }
    return false;
    }



}
