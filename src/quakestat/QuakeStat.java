/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quakestat;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;

import org.jdom.Element;

import java.util.LinkedList;
import java.util.List;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.net.URL;





/**
 *
 * @author testi
 */
public class QuakeStat implements ServerFetcher {

    private String gameName;
    private InetAddress host;
    private boolean u;
    private boolean nf;
    private boolean ne;
    private boolean P;
    private int retry;
    private double interval;
    private double mi;
    private int port;
    private boolean broadcast;
    private int maxsim;
    public static final double DEFAULT_INTERVAL = 0.5;
    public static final double DEFAULT_MI = 2.0;
    public static final int DEFAULT_SENDINTERVAL = 5;
    public static final int DEFAULT_RETRY = 3;
    public static final int DEFAULT_MAXSIM = 20;
    private static String QUAKESTAT_COMMAND = "quakestat";
//private int maxsim;
    private int sendinterval;
    private String cfg;

    /**
     * Initializes the QuakeStat wrapper using gameName the game to query.
     * @param gameName the game name to query servers for. Will as -<gameName> on the command line.
     */
    public QuakeStat(String gameName, InetAddress host, int port) {
        this.gameName = gameName;
        this.host = host;
        this.port = port;
        u = false;
        nf = false;
        ne = false;
        P = false;
        broadcast = false;
        retry = DEFAULT_RETRY;
        interval = DEFAULT_INTERVAL;
        mi = DEFAULT_MI;
        maxsim = DEFAULT_MAXSIM;
        //maxsim = ; //TODO: What is the default?
        sendinterval = DEFAULT_SENDINTERVAL;
        cfg = null;
    }
    public QuakeStat(String gameName, InetAddress host) {
    this(gameName,host,-1);
    }
    public void setCfg(String cfg) {
    this.cfg = cfg;
    }

    public InetAddress getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
    public int getMaxsim() {
    return maxsim;
    }
    public void setMaxsim(int maxsim) {
    this.maxsim = maxsim;
    }
    
    public String getGameName() {
    return gameName;
    }
    public String getCfg() {
    return cfg;
    }
    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    public QuakeStat(String gameName, String hostName, int port) throws UnknownHostException {
    this(gameName, InetAddress.getByName(hostName), port);
    }

    public QuakeStat(String gameName, String hostName) throws UnknownHostException {
    this(gameName,hostName,-1);
    }

    public double getInterval() {
        return interval;
    }

    public void setInterval(double interval) {
        this.interval = interval;
    }

    public double getMasterServerRetryInterval() {
        return mi;
    }

    public boolean isIncludePlayers() {
        return P;
    }

    public void setIncludePlayers(boolean P) {
        this.P = P;
    }

    public void setMasterServerRetryInterval(double mi) {
        this.mi = mi;
    }

    public boolean isMustBeNotEmpty() {
        return ne;
    }

    public void setMustBeNotEmpty(boolean ne) {
        this.ne = ne;
    }

    public boolean isMustBeNotFull() {
        return nf;
    }

    public void setMustBeNotFull(boolean nf) {
        this.nf = nf;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int getSendinterval() {
        return sendinterval;
    }

    public void setSendinterval(int sendinterval) {
        this.sendinterval = sendinterval;
    }





    public boolean isMustBeUp() {
        return u;
    }

    public void setMustBeUp(boolean u) {
        this.u = u;
    }
    public List<GameServer> getServers() throws IOException {

    final LinkedList<GameServer> sl = new LinkedList<GameServer>();
    parseServers(new ServerListener(){

            public boolean proceed() {
                return true;
            }

            public void onServer(GameServer server) {
                sl.add(server);
            }

    });
    return sl;
    }

    public void parseServers(final ServerListener l) throws IOException {
    String[] commandLine = constructCommandLine();
    /*for (String c : commandLine) {
    System.out.print(c + " ");
    }
    System.out.println();*/
    try {
    final Process p = Runtime.getRuntime().exec(commandLine);
    

        
        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(new ElementEmitter(1,new ElementListener() {
            private boolean proceed = true;
                public void onElement(Element e) {
                    if (!l.proceed()) {if (proceed)p.destroy();proceed = false;} else { //Avoid second destruction of process
                    GameServer s = parseServer(e);
                    if (s != null) {
                    l.onServer(s);
                    }}
                    
                }

        }));
        InputSource is = new InputSource(p.getInputStream());
        reader.parse(is);
        } catch (SAXException ex) {
            System.err.println(ex);
        } catch (IOException ex) {/*System.out.println("Regular exception: " + ex.getMessage());*/}
    }

    /*private List<GameServer> parseServers(Element e) throws ParseException {
    if (!e.getName().equals("qstat")) {throw new ParseException("Expected qstat element");}
    List<GameServer> serverList = new LinkedList<GameServer>();
    for (Object o : e.getChildren("server")) {
    Element c = (Element)o;
    GameServer server = parseServer(c);
    if (server != null) serverList.add(server);
    
    }
    return serverList;
    }*/

    protected GameServer parseServer(Element e) {
    if (e.getChildren().isEmpty()) return null;
    String hostname = e.getChildText("hostname");
    int sport = -1;
    String name = hostname;
    if (hostname != null) {
    String[] hostnameFields = hostname.split(":");
    if (hostnameFields.length == 2) {
    name = hostnameFields[0];
    try {
    sport = Integer.parseInt(hostnameFields[1]);
    } catch (NumberFormatException ex){}
    }
    }
    InetAddress shost = null;
    try {
    shost = InetAddress.getByName(name);
    } catch (UnknownHostException ex){}
    int numPlayers = -1;
    int maxPlayers = -1;
    int ping = -1;
    try {numPlayers = Integer.parseInt(e.getChildText("numplayers"));} catch (NumberFormatException ex){}
    try {maxPlayers = Integer.parseInt(e.getChildText("maxplayers"));} catch (NumberFormatException ex){}
    try {ping = Integer.parseInt(e.getChildText("ping"));} catch (NumberFormatException ex){}
    List<Player> playerList = new LinkedList<Player>();
    Element players = e.getChild("players");
    if (players != null) {
    for (Object c : players.getChildren("player")) {
    playerList.add(parsePlayer((Element)c));
    }
    }
    return new GameServer(shost, sport, e.getChildText("name"),e.getChildText("gametype"),e.getChildText("map"),numPlayers,maxPlayers,ping,playerList);
    }
    private Player parsePlayer(Element e) {
    String name = e.getChildText("name");
    int score = Integer.MIN_VALUE;
    int ping = -1;
    try{score = Integer.parseInt(e.getChildText("score"));}catch(NumberFormatException ex){}
    try{ping = Integer.parseInt(e.getChildText("ping"));}catch(NumberFormatException ex){}
    return new Player(name, score, ping);
    }

    private String[] constructCommandLine() {
    String[] ncl = new String[20];
    int i = 0;
    ncl[i++] = QUAKESTAT_COMMAND;
    ncl[i++] = "-xml";
    //ncl[i++] = "-utf8";
    ncl[i++] = uParameter();
    ncl[i++] = nfParameter();
    ncl[i++] = neParameter();
    ncl[i++] = PParameter();
    String[] multiParameter = retryParameter();
    if (multiParameter != null) {
    ncl[i++] = multiParameter[0];
    ncl[i++] = multiParameter[1];
    } else {
    ncl[i++] = null;
    ncl[i++] = null;
    }
    multiParameter = maxsimParameter();
    if (multiParameter != null) {
    ncl[i++] = multiParameter[0];
    ncl[i++] = multiParameter[1];
    } else {
    ncl[i++] = null;
    ncl[i++] = null;
    }
    multiParameter = sendintervalParameter();
    if (multiParameter != null) {
    ncl[i++] = multiParameter[0];
    ncl[i++] = multiParameter[1];
    } else {
    ncl[i++] = null;
    ncl[i++] = null;
    }
    multiParameter = intervalParameter();
    if (multiParameter != null) {
    ncl[i++] = multiParameter[0];
    ncl[i++] = multiParameter[1];
    } else {
    ncl[i++] = null;
    ncl[i++] = null;
    }
    multiParameter = miParameter();
    if (multiParameter != null) {
    ncl[i++] = multiParameter[0];
    ncl[i++] = multiParameter[1];
    } else {
    ncl[i++] = null;
    ncl[i++] = null;
    }
    multiParameter = cfgParameter();
    if (multiParameter != null) {
    ncl[i++] = multiParameter[0];
    ncl[i++] = multiParameter[1];
    } else {
    ncl[i++] = null;
    ncl[i++] = null;
    }
    ncl[i++] = gameNameParameter();
    ncl[i] = (broadcast ? "+" : "") + host.getHostAddress() + ((port>0) ? ":" + port : "");

    String[] cl = deleteNull(ncl);
    return cl;
    }

    private String[] deleteNull(String[] from) {
    int newSize = 0;
    for (int i = 0; i < from.length;i++) {
    if (from[i] != null) {
    newSize++;
    }
    }
    String[] to = new String[newSize];
    int newI = 0;
    for (int i = 0; i < from.length;i++) {
    if (from[i] != null) {
    to[newI] = from[i];
    newI++;
    }
    }
    return to;
    }

    private String gameNameParameter() {
        return "-" + gameName;
    }

    private String uParameter() {
        if (u) {
            return "-u";
        }
        return null;
    }


    private String nfParameter() {
        if (nf) {
            return "-nf";
        }
        return null;
    }
        private String PParameter() {
        if (P) {
            return "-P";
        }
        return null;
    }

    private String neParameter() {
        if (ne) {
            return "-ne";
        }
        return null;
    }

    private String[] retryParameter() {
    if (retry == DEFAULT_RETRY) return null;
    return new String[] {"-retry",Integer.toString(retry)};
    }
    private String[] maxsimParameter() {
    if (maxsim == DEFAULT_MAXSIM) return null;
    return new String[] {"-maxsim", Integer.toString(maxsim)};
    }

    private String[] cfgParameter() {
    if (cfg == null) return null;
    return new String[] {"-cfg",cfg};
    }

    private String[] sendintervalParameter() {
    if (sendinterval == DEFAULT_SENDINTERVAL) return null;
    return new String[] {"-sendinterval",Integer.toString(sendinterval)};
    }

    private String[] intervalParameter() {
    if (interval == DEFAULT_INTERVAL) return null;
    return new String[] {"-interval",Double.toString(interval)};
    }
    private String[] miParameter() {
    if (mi == DEFAULT_MI) return null;
    return new String[] {"-mi",Double.toString(mi)};
    }

    public static String QuakeStatCommand() {
    return QUAKESTAT_COMMAND;
    }
    public static void SetQuakeStatCommand(String command) {
    QUAKESTAT_COMMAND = command;
    }
}
