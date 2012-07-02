/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;
import java.util.List;
//import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.net.URL;
import java.net.MalformedURLException;
/**
 *
 * @author testi
 */
public class ServerFinder {
private HashMap<String, InternetGame> games;
private List<InternetGame> gamesList;
public ServerFinder() {
games = new HashMap<String, InternetGame>();
gamesList = new ArrayList<InternetGame>();
}
public void addGame(InternetGame game) {
games.put(game.getName(),game);
gamesList.add(game);
}

public List<String> getSupportedGames() {
    List<String> supportedGames = new ArrayList<String>();
    for (InternetGame i : gamesList) {
    supportedGames.add(i.getName());
    }
    java.util.Collections.sort(supportedGames, new java.util.Comparator<String>(){

            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }

    });
    return supportedGames;
}
public GameServer getServerInfo(String gameName, String hostAndPort) {
String[] fields = hostAndPort.split(":");
if (fields.length != 2) {throw new IllegalArgumentException("Argument hostAndPort must be of format host:port");}
try {int port = Integer.parseInt(fields[1]);
return getServerInfo(gameName, fields[0], port);
} catch (NumberFormatException ex) {throw new IllegalArgumentException("port field must be an integer");}

}


public GameServer getServerInfo(String hostAndPort) {
String[] fields = hostAndPort.split(":");
if (fields.length != 2) {throw new IllegalArgumentException("Argument hostAndPort must be of format host:port");}
try {int port = Integer.parseInt(fields[1]);
return getServerInfo(fields[0], port);
} catch (NumberFormatException ex) {throw new IllegalArgumentException("port field must be an integer");}

}

public GameServer getServerInfo(String gameName, String hostname, int port) {
    try {
    return getServerInfo(gameName, InetAddress.getByName(hostname), port); }
    catch (UnknownHostException ex) {return null;}
}

public GameServer getServerInfo(String hostname, int port) {
    try {
    return getServerInfo(InetAddress.getByName(hostname), port); }
    catch (UnknownHostException ex) {return null;}
}

private void configureQuakeStat(ServerFetcher qstat) {
if (new java.io.File("qstat.cfg").exists()){
qstat.setCfg("qstat.cfg");
}
}
public GameServer getServerInfo(String gameName, InetAddress host, int port) {
InternetGame i = games.get(gameName);
if (port != -1 && (port>65535 || port < 1)){throw new IllegalArgumentException("port must be within range 1-65535");}
if (i == null) {throw new IllegalArgumentException("Game is not supported");}
    ServerFetcher quakeStat = instantiateQuakeStat(i.getQstatNameSingle(),host,port);
    //configureQuakeStat(quakeStat);
    quakeStat.setIncludePlayers(true);
    quakeStat.setMustBeUp(true);
    try {
    List<GameServer> servers = setGameName(quakeStat.getServers(),gameName);
    if (servers.isEmpty()) return null;
    return servers.iterator().next();
    } catch (IOException ex){return null;}
}
public GameServer getServerInfo(InetAddress host, int port) {
GameServer bestServer = null;
int max = -1;
for (InternetGame game : gamesList) {
GameServer server = getServerInfo(game.getName(), host, port);
if (server != null) {
int curQuality = informationQuality(server);
    if (curQuality>max){
    max = curQuality;
    bestServer = server;
    if (max >= 9) return bestServer;
}
}
}
return bestServer;
}
private int informationQuality(GameServer server) {
int i = 0;
if (server.getGameName() != null) i++;
if (server.getGametype() != null) i++;
if (server.getHost() != null) i++;
if (server.getMap() != null) i++;
if (server.getPlayerCount() != -1) i++;
if (server.getMaxPlayerCount() != -1) i++;
if (server.getName() != null) i++;
if (server.getPing() != -1) i++;
if (server.getPort() != -1) i++;
return i;
}



public List<GameServer> findServerByRequest(Request request) throws MatchException {
final List<GameServer> serverList = new LinkedList<GameServer>();
findServerByRequest(request, new ServerListener() {

            public void onServer(GameServer server) {
                serverList.add(server);
            }

            public boolean proceed() {
                return true;
            }

});
return serverList;
}
public void findServerByRequest(final Request request, final ServerListener sl) throws MatchException {

boolean includePlayers = request.includePlayers();
boolean up = request.mustBeUp();
boolean nf = request.mustBeNotFull();
boolean ne = request.mustBeNotEmpty();
List<InternetGame> gamesListCopy = new ArrayList<InternetGame>();
for (InternetGame ig : gamesList) {
gamesListCopy.add(ig);
}


request.prepare(gamesListCopy);


    final List<GameServer> serverList = new LinkedList<GameServer>();
    final boolean finalProcessing = request.finalProcessing();
    for (final InternetGame i : gamesListCopy)
    {
     
        try {
    ServerFetcher quakeStat = instantiateQuakeStat(i);
    quakeStat.setIncludePlayers(includePlayers);
    quakeStat.setMustBeUp(up);
    quakeStat.setMustBeNotFull(nf);
    quakeStat.setMustBeNotEmpty(ne);
    //configureQuakeStat(quakeStat);
    if (!request.proceed() || !sl.proceed()) break;
    quakeStat.parseServers(new ServerListener() {

                        public void onServer(GameServer server) {
                            setGameName(server,i.getName());
                            if (request.acceptServer(server)) {
                                if (finalProcessing) {
                                serverList.add(server);
                                } else {
                                sl.onServer(server);
                                }
                            }
                        }

                        public boolean proceed() {
                            return request.proceed() && sl.proceed();
                        }

    });
    
    } catch (UnknownHostException ex) {System.err.println(ex);}
    catch (IOException ex) {System.err.println(ex);}

    }
    if (finalProcessing) {
    request.finalProcessing(serverList);
        for (GameServer server : serverList) {
        if (!sl.proceed()) break;
            sl.onServer(server);
        }
    }
}


public void findServerByLimitedRequest(final LimitedRequest request, final LimitedServerListener sl) throws MatchException {

boolean includePlayers = request.includePlayers();
boolean up = request.mustBeUp();
boolean nf = request.mustBeNotFull();
boolean ne = request.mustBeNotEmpty();
List<InternetGame> gamesListCopy = new ArrayList<InternetGame>();
for (InternetGame ig : gamesList) {
gamesListCopy.add(ig);
}


request.prepare(gamesListCopy);


    final List<GameServer> serverList = new LinkedList<GameServer>();
    final boolean finalProcessing = request.finalProcessing();
    for (final InternetGame i : gamesListCopy)
    {

        try {
    ServerFetcher quakeStat = instantiateQuakeStat(i);
    quakeStat.setIncludePlayers(includePlayers);
    quakeStat.setMustBeUp(up);
    quakeStat.setMustBeNotFull(nf);
    quakeStat.setMustBeNotEmpty(ne);
    //configureQuakeStat(quakeStat);
    if (!request.proceed() || !sl.proceed()) break;
    quakeStat.parseServers(new ServerListener() {

                        public void onServer(GameServer server) {
                            setGameName(server,i.getName());
                            if (request.acceptServer(server)) {
                                if (finalProcessing) {
                                serverList.add(server);
                                } else {
                                sl.onServer(server);
                                }
                            }
                        }

                        public boolean proceed() {
                            return request.proceed() && sl.proceed();
                        }

    });

    } catch (UnknownHostException ex) {System.err.println(ex);}
    catch (IOException ex) {System.err.println(ex);}

    }
    if (finalProcessing) {
    request.finalProcessing(serverList);
        for (GameServer server : serverList) {
        if (!sl.proceed()) break;
            sl.onServer(server);
        }
    }
}



private List<GameServer> setGameName(List<GameServer> serverList, String gameName) {
for (GameServer s : serverList) {
s.setGameName(gameName);
}
return serverList;
}
private GameServer setGameName(GameServer server, String gameName) {
server.setGameName(gameName);
return server;
}
private ServerFetcher instantiateQuakeStat(InternetGame game) throws MalformedURLException, UnknownHostException {
/*if (game.getQstatName().equals("url")) {
    QuakeStat qstat = new URLQuakeStat(game.getQstatNameSingle(), new URL(game.getMasterserver()));
    configureQuakeStat(qstat);
    return qstat;
} else if (game.getQstatName().startsWith("url_")) {
    QuakeStat qstat = new URLQuakeStat(game.getQstatName(), game.getQstatNameSingle(), new URL(game.getMasterserver()));
    configureQuakeStat(qstat);
    return qstat;
}
else {
    QuakeStat qstat = new QuakeStat(game.getQstatName(),InetAddress.getByName(game.getMasterserver()), game.getMasterserverPort());
    configureQuakeStat(qstat);
    return qstat;
}*/
    ServerFetcher fetcher = new ServerFetcherFactory().createMasterServerFetcher(game);
    configureQuakeStat(fetcher);
    return fetcher;
}
private ServerFetcher instantiateQuakeStat(String serverType, InetAddress address, int port) {
ServerFetcher fetcher = new ServerFetcherFactory().createServerFetcher(serverType, address, port);
configureQuakeStat(fetcher);
return fetcher;
}
private ServerFetcher instantiateQuakeStat(String serverType, InetAddress address) {
return instantiateQuakeStat(serverType, address, -1);
}
public List<GameServer> scanLan(String broadcastAddress) {
    return scanLan(broadcastAddress,-1);
}
public List<GameServer> scanLan(String gameName, String broadcastAddress) {

 return scanLan(gameName, broadcastAddress,-1);
}
public List<GameServer> scanLan(InetAddress broadcastAddress) {
return scanLan(broadcastAddress,-1);
}
public List<GameServer>scanLan(String gameName, InetAddress broadcastAddress) {
return scanLan(gameName, broadcastAddress,-1);
}

public List<GameServer> scanLan(String broadcastAddress, int port) {
    try {
    return scanLan(InetAddress.getByName(broadcastAddress), port);
    } catch (UnknownHostException ex) {throw new IllegalArgumentException(ex);}
}
public List<GameServer> scanLan(String gameName, String broadcastAddress, int port) {
    try {
    return scanLan(gameName, InetAddress.getByName(broadcastAddress), port);
    } catch (UnknownHostException ex) {throw new IllegalArgumentException(ex);}
}
public List<GameServer> scanLan(InetAddress broadcastAddress, int port) {
List<GameServer> serverList = new LinkedList<GameServer>();
for (InternetGame i : gamesList) {
serverList.addAll(scanLan(i.getName(),broadcastAddress,port));
}
return serverList;
}
public List<GameServer>scanLan(String gameName, InetAddress broadcastAddress, int port) {
InternetGame game = games.get(gameName);
if (game == null) throw new IllegalArgumentException("game not supported");
if (port != -1 && (port>65535 || port < 1)){throw new IllegalArgumentException("port must be within range 1-65535");}
try {
ServerFetcher qstat = instantiateQuakeStat(game.getQstatNameSingle(),broadcastAddress, port);
//configureQuakeStat(qstat);
qstat.setBroadcast(true);
qstat.setMustBeUp(true);

return setGameName(qstat.getServers(), gameName);
}
catch (IOException ex){System.err.println(ex);return new LinkedList<GameServer>();}
}

public ServerFinder(String file) throws IOException {
games = new HashMap<String, InternetGame>();
gamesList = new ArrayList<InternetGame>();

BufferedReader reader = new BufferedReader(new FileReader(file));
String line = null;
while ((line = reader.readLine()) != null) {
StringTokenizer t = new StringTokenizer(line,"|");
int count = t.countTokens();
if (count > 3) {
int port = -1;
String name = t.nextToken();
String qstatName = t.nextToken();
String qstatNameSingle = t.nextToken();
String masterServer = t.nextToken();
//if ("#unsupported#".equals(masterServer))
//masterServer = null;

try{port = Integer.parseInt(t.nextToken());}catch(NumberFormatException ex) {} catch (NoSuchElementException ex){}
    InternetGame game = new InternetGame(name,qstatName, qstatNameSingle, masterServer,port);
    games.put(game.getName(),game);
    gamesList.add(game);
}

}

}

}
