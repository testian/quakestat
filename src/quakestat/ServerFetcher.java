/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author testi
 */
public interface ServerFetcher {


    String getCfg();

    String getGameName();

    double getInterval();

    double getMasterServerRetryInterval();

    int getRetry();
    int getMaxsim();
    void setMaxsim(int maxsim);

    int getSendinterval();

    List<GameServer> getServers() throws IOException;

    boolean isBroadcast();

    boolean isIncludePlayers();

    boolean isMustBeNotEmpty();

    boolean isMustBeNotFull();

    boolean isMustBeUp();

    void parseServers(final ServerListener l) throws IOException;

    void setBroadcast(boolean broadcast);

    void setCfg(String cfg);

    void setIncludePlayers(boolean P);

    void setInterval(double interval);

    void setMasterServerRetryInterval(double mi);

    void setMustBeNotEmpty(boolean ne);

    void setMustBeNotFull(boolean nf);

    void setMustBeUp(boolean u);

    void setRetry(int retry);

    void setSendinterval(int sendinterval);

}
