/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;
import java.io.InputStream;

import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.LinkedList;
/**
 *
 * @author testi
 */
public class Savage2MasterParser implements MasterParser {

    public List<String> getServerList(InputStream is) throws IOException {
        List<String> serverList = new LinkedList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        int lastTr = -1;
        while ((line = br.readLine()) != null) {
        if (line.endsWith("<tr>")) lastTr = 0;
        //System.out.println(line);
        if (lastTr == 3) {
        int endIndex = line.length()-5;
        int startIndex = 12;
        if (endIndex>=startIndex) {
        String subString = line.substring(startIndex, endIndex);
        if (!subString.equals("IP:Port") && subString.contains(":")) {
        serverList.add(subString);
        //System.out.println("Savage 2 test: " + subString);
        }
        }

        }
        if (lastTr != -1) lastTr++;
        }
        return serverList;
        
    }

    /*public GameServer alternateServerInfo(String host, int port) {
        return null;
    }

    public boolean providesAlternateServerInfo() {
        return false;
    }*/




    




}
