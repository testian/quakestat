/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;

/**
 *
 * @author testi
 */
public class Player {
private String name;
private int score;
private int ping;

    public String getName() {
        return name;
    }

    public int getPing() {
        return ping;
    }

    public int getScore() {
        return score;
    }

    public Player(String name, int score, int ping) {
        this.name = name;
        this.score = score;
        this.ping = ping;
    }
    @Override
    public String toString() {
    return playerDescription();
    }
    public String playerDescription() {
    return denull(name) + " - Score: " + scoreDenull(score) + ", Ping: " + denull(ping);
    }

    private String denull(Object denull) {
    if (denull == null) return "<Unknown>";
    return denull.toString();
    }
    private String denull(int denull) {
    if (denull < 0)return "<Unknown>";
    return Integer.toString(denull);
    }
    private String scoreDenull(int denull) {
    if (denull == Integer.MIN_VALUE) return "<Unknown>";
    return Integer.toString(denull);
    }

}
