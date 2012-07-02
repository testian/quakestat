/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;
import java.util.Comparator;
import quakestat.GameServer;
import quakestat.query.Token.TokenType;
/**
 *
 * @author testi
 */
public class SortTypes {
public static Comparator<GameServer> getComparator(TokenType sortType) {
    if (sortType == null) return null;
    if (sortType == TokenType.GAMENAME)
    return new Comparator<GameServer>() {

            public int compare(GameServer o1, GameServer o2) {
                if (o1.getGameName() == null && o2.getGameName() == null) return 0;
                if (o1.getGameName() == null) return 1; else if (o2.getGameName() == null) return -1;
                return o1.getGameName().compareTo(o2.getGameName());
            }

    };

    if (sortType == TokenType.GAMETYPE)
    return new Comparator<GameServer>() {

            public int compare(GameServer o1, GameServer o2) {
                if (o1.getGametype() == null && o2.getGametype() == null) return 0;
                if (o1.getGametype() == null) return 1; else if (o2.getGametype() == null) return -1;
                return o1.getGametype().compareTo(o2.getGametype());
            }

    };

        if (sortType == TokenType.MAP)
    return new Comparator<GameServer>() {

            public int compare(GameServer o1, GameServer o2) {
                if (o1.getMap() == null && o2.getMap() == null) return 0;
                if (o1.getMap() == null) return 1; else if (o2.getMap() == null) return -1;
                return o1.getMap().compareTo(o2.getMap());
            }

    };

            if (sortType == TokenType.MAP)
    return new Comparator<GameServer>() {

            public int compare(GameServer o1, GameServer o2) {
                if (o1.getMap() == null && o2.getMap() == null) return 0;
                if (o1.getMap() == null) return 1; else if (o2.getMap() == null) return -1;
                return o1.getMap().compareTo(o2.getMap());
            }

    };
    
    if (sortType == TokenType.HOST)
    return new Comparator<GameServer>() {

            public int compare(GameServer o1, GameServer o2) {
                if (o1.getHost() == null && o2.getHost() == null) return 0;
                if (o1.getHost() == null) return 1; else if (o2.getHost() == null) return -1;
                byte[] host1 = o1.getHost().getAddress();
                byte[] host2 = o2.getHost().getAddress();
                if (host1.length != host2.length) return host1.length-host2.length; //ipv4 first
                for (int i = 0; i<host1.length; i++) {
                int byte1 = host1[i];
                int byte2 = host2[i];
                if (byte1<0)byte1+=256;
                if (byte2<0)byte2+=256;
                int result = byte1-byte2;
                if (result != 0) return result;
                }
                return 0;
            }

    };




            if (sortType == TokenType.PORT)
    return new Comparator<GameServer>() {

            public int compare(GameServer o1, GameServer o2) {
                if (o1.getPort() == o1.getPort()) return 0;
                if (o1.getPort() == -1) return 1;
                if (o2.getPort() == -1) return -1;
                return o1.getPort()-o2.getPort();
            }

    };

                if (sortType == TokenType.PLAYERCOUNT)
    return new Comparator<GameServer>() {

            public int compare(GameServer o1, GameServer o2) {
                return o2.getPlayerCount()-o1.getPlayerCount();
            }

    };

    if (sortType == TokenType.MAXPLAYERCOUNT)
    return new Comparator<GameServer>() {

            public int compare(GameServer o1, GameServer o2) {
                return o2.getMaxPlayerCount()-o1.getMaxPlayerCount();
            }

    };

                    if (sortType == TokenType.SLOTS)
    return new Comparator<GameServer>() {

            public int compare(GameServer o1, GameServer o2) {
                int slots1;
                if (o1.getMaxPlayerCount() == -1 || o1.getPlayerCount() == -1) {
                slots1 = -1;
                } else {
                slots1 = o1.getMaxPlayerCount()-o1.getPlayerCount();
                }
                int slots2;
                if (o2.getMaxPlayerCount() == -1 || o2.getPlayerCount() == -1) {
                slots2 = -1;
                } else {
                slots2 = o2.getMaxPlayerCount()-o2.getPlayerCount();
                }
                if (slots1 == slots2) return 0;
                if (slots1 == -1) return 1;
                if (slots2 == -1) return -1;
                return slots2-slots1;
            }

    };

                    if (sortType == TokenType.PING)
    return new Comparator<GameServer>() {

            public int compare(GameServer o1, GameServer o2) {
                if (o1.getPing() == -1 && o2.getPing() == -1) return 0;
                if (o1.getPing() == -1) return 1; else if (o2.getPing() == -1) return -1;
                return o1.getPing()-o2.getPing();
            }

    };

    throw new IllegalArgumentException("Not a sort type: " + sortType);

}
    
    

//TokenType.GAMENAME || t == TokenType.GAMETYPE || t == TokenType.MAP || t == TokenType.PLAYERCOUNT || t == TokenType.MAXPLAYERCOUNT || t == TokenType.PING) {

}
