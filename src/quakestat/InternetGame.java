/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;

/**
 *
 * @author testi
 */
public class InternetGame {
private String name;
private String qstatName;
private String masterserver;
private String qstatNameSingle;
private int masterserverPort;

    public InternetGame(String name, String qstatName, String qstatNameSingle, String masterserver, int masterserverPort) {
        this.name = name;
        this.qstatName = qstatName;
        this.qstatNameSingle = qstatNameSingle;
        this.masterserver = masterserver;
        this.masterserverPort = masterserverPort;
    }
    public InternetGame(String name, String qstatName, String qstatNameSingle, String masterserver) {
    this(name, qstatName, qstatNameSingle, masterserver, -1);
    }

    public String getQstatNameSingle() {
        return qstatNameSingle;
    }

    public String getMasterserver() {
        return masterserver;
    }

    public int getMasterserverPort() {
        return masterserverPort;
    }

    public String getName() {
        return name;
    }

    public String getQstatName() {
        return qstatName;
    }


}
