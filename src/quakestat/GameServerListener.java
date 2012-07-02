/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;

/**
 *
 * @author testi
 */
public interface GameServerListener {

    public void onJoin(GameServer server, Player joinPlayer);
    public void onLeave(GameServer server, Player leavePlayer);
    public void onMapChange(GameServer server, String oldMapName, String newMapName);
    public void onGametypeChange(GameServer server, String oldGameType, String newGameType);
    public void onServerNameChange(GameServer server, String oldName, String newName);
    public void onShutdown(GameServer server);
    public void onStart(GameServer server);
    /**
     * called when the playerlist is not available
     * @param server
     * @param playersJoined amount of players that newly joined the game, negative if players left
     */
    public void onPlayerCountChange(GameServer server, int playersJoined);
}
