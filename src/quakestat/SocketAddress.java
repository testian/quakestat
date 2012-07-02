/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;
import java.net.InetAddress;
/**
 *
 * @author testi
 */
public class SocketAddress {
private InetAddress address;
private int port;

    public SocketAddress(InetAddress address, int port) {
        if (address == null) {throw new IllegalArgumentException("address must not be null");}
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
    @Override
    public boolean equals(Object other) {
    if (other == null) return false;
    if (!(other instanceof SocketAddress)) return false;
    SocketAddress os = (SocketAddress)other;
    if (os.getPort() != getPort() || !address.equals(other)) return false;
    return true;
    }
    @Override
    public int hashCode() {
    return address.hashCode() + port;
    }


}
