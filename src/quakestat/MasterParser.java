/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;
import java.util.List;
import java.io.InputStream;
import java.io.IOException;
/**
 *
 * @author testi
 */
public interface MasterParser {
public List<String> getServerList(InputStream is) throws IOException;
//public GameServer alternateServerInfo(String host, int port);
//public boolean providesAlternateServerInfo();
}
