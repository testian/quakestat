/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Random;
import java.net.SocketTimeoutException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.util.LinkedList;


/**
 *
 * @author testi
 */
public class Savage2Stat extends QuakeStat {
    public static final int DEFAULT_PORT = 11235;
    private static final byte[] protocolBytes = {(byte)0x9a, (byte)0xde,(byte)0x97,(byte)0xf1,(byte)0x01,(byte)0xc4,(byte)0x80,(byte)0x01};
    private Random random;
    public Savage2Stat(String gameName, String hostName) throws UnknownHostException {
        super(gameName, hostName);
        random = new Random();
    }

    public Savage2Stat(String gameName, String hostName, int port) throws UnknownHostException {
        super(gameName, hostName, port);
        random = new Random();
    }

    public Savage2Stat(String gameName, InetAddress host) {
        super(gameName, host);
        random = new Random();
    }

    public Savage2Stat(String gameName, InetAddress host, int port) {
        super(gameName, host, port);
        random = new Random();
    }
    


    @Override
    public void parseServers(ServerListener l) throws IOException {
     if (!l.proceed()) return;
     int retries = this.getRetry();
     for (int r = 0 ; r < retries; r++) {
     byte[] randomBytes = new byte[4];
     random.nextBytes(randomBytes);
     byte[] sendBytes = new byte[12];
     for (int i = 0; i < protocolBytes.length;i++){sendBytes[i]=protocolBytes[i];}
     for (int i = 0; i < randomBytes.length;i++){sendBytes[i+protocolBytes.length]=randomBytes[i];}
     DatagramPacket sendPacket = new DatagramPacket(sendBytes,sendBytes.length,getHost(),getPort());
     DatagramSocket socket = new DatagramSocket();
     long time = System.currentTimeMillis();
     boolean broadcast = this.isBroadcast();
     socket.setBroadcast(broadcast);
     socket.send(sendPacket);
     socket.setSoTimeout(broadcast ? 1000 : 10000);
     DatagramPacket receivePacket = new DatagramPacket(new byte[512], 512);
     boolean canBreak = false;
     try {
     boolean first = true;

     while (first || broadcast) {
     first = false;
     socket.receive(receivePacket);
     long ping = System.currentTimeMillis()-time;
     if ((!broadcast && !receivePacket.getAddress().equals(this.getHost()) ) || receivePacket.getPort() != this.getPort()) return;
     if (parsePacket(receivePacket, randomBytes,l, ping)) canBreak = true;
     }
     if (canBreak) break;
     } catch (SocketTimeoutException ex) {
     if (canBreak) break;
     }
     }
    }

    private boolean parsePacket(DatagramPacket packet, byte[] randomBytes, ServerListener l, long ping) {
    byte[] data = packet.getData();
    int length = packet.getLength();

    /*for (int i = 0 ; i < length; i++){
    System.out.print(new String(new byte[] {data[i]}));
    if (data[i] == 0) System.out.print("T");
    
    }
    System.out.println();*/
    if (length<50) return false;
    //System.out.println("S2 Packet received");
    for (int i = 0; i < 5; i++) {
    if (protocolBytes[i] != data[i]) return false;
    }
    //System.out.println("S2 Protocol bytes match");
    for (int i = 0; i < 4; i++) {
    if (randomBytes[i] != data[i+8]) return false;
    }
    //System.out.println("Random bytes match");

    int stringLength = scanForTerminator(data, 12, length-12);
    String serverName = parseString(data, 12, stringLength);
    //System.out.println("Servername: " + serverName);
    int c = 12 + stringLength + 1;
    if (c+1>= length) return false;
    int players = data[c];
    if (players<0) players+=128;
    int maxPlayers = data[c+1];
    if (maxPlayers<0) maxPlayers+=128;
    c+=1;
    if (c >= length) return false;
    c+=scanForTerminator(data, c, length-c)+1;
    if (c >= length) return false;
    stringLength = scanForTerminator(data, c, length-c);
    String map = parseString(data, c, stringLength);
    //System.out.println("Map: " + map);
    
    GameServer server = new GameServer(packet.getAddress(), packet.getPort(), serverName, "RTSS", map, players, maxPlayers,(int)ping, new LinkedList<Player>());
    if ((this.isMustBeNotEmpty() && players<1) || (this.isMustBeNotFull() && players>=maxPlayers)) return true;
    l.onServer(server);
    return true;



    }
    private int scanForTerminator(byte[] data, int offset, int length) {
    for (int i = 0; i < length; i++) {
    if (data[i+offset] == 0x00) return i;
    }
    return length;
    }
    private String parseString(byte[] data, int offset, int length) {
    return new String(data, offset, length);
    }

    @Override
    public int getPort() {
        int port = super.getPort();
        if (port == -1) return DEFAULT_PORT;
        return port;
    }

   
}
