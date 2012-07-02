package quakestat;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author testi
 */
public interface ServerListener {
public void onServer(GameServer server);
public boolean proceed();
}
