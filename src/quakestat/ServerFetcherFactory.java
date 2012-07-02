/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.InetAddress;
/**
 *
 * @author testi
 */
public class ServerFetcherFactory {
    public ServerFetcherFactory() {
    }
    public ServerFetcher createMasterServerFetcher(String masterType, String serverType, String address, int port) throws MalformedURLException, UnknownHostException {
    if (masterType.equals("url")) return new URLQuakeStat(serverType,new URL(address), true);
    if (masterType.equals("url_savage")) return new URLQuakeStat(serverType, new URL(address),new SavageMasterParser());
    if (masterType.equals("url_savage2")) return new URLQuakeStat(serverType, new URL(address),new Savage2MasterParser(), true);
    return new QuakeStat(masterType, InetAddress.getByName(address), port);
    }

    public ServerFetcher createMasterServerFetcher(InternetGame game) throws MalformedURLException, UnknownHostException {
    return createMasterServerFetcher(game.getQstatName(), game.getQstatNameSingle(), game.getMasterserver(), game.getMasterserverPort());
    }

    public ServerFetcher createMasterServerFetcher(String masterType, String serverType, String address) throws MalformedURLException, UnknownHostException {
    return createMasterServerFetcher(masterType, serverType, address, -1);
    }

    public ServerFetcher createServerFetcher(String serverType, InetAddress address, int port) {
        if (serverType.equals("savage2")) return new Savage2Stat(serverType, address, port);
        if (serverType.equals("sturmovik")) return new SturmovikStat(serverType, address, port);
        return new QuakeStat(serverType, address, port);
    }

    public ServerFetcher createServerFetcher(String serverType, InetAddress address) throws UnknownHostException {
    return createServerFetcher(serverType, address, -1);
    }


}
