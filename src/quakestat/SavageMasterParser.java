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
public class SavageMasterParser implements MasterParser {

    public List<String> getServerList(InputStream is) throws IOException {
        List<String> serverList = new LinkedList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
        if (line.endsWith("</td></tr>")) {
        int endIndex = line.lastIndexOf("</td><td>");
        if (endIndex < 0 ) continue;
        int startIndex = line.substring(0, endIndex).lastIndexOf("</td><td>");
        if (startIndex < 0) continue;
        startIndex+=9;
        if (startIndex>endIndex) continue;
        String hostAndPort = line.substring(startIndex,endIndex);
        if (!hostAndPort.contains(":")) continue;
        serverList.add(hostAndPort);
        }
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
