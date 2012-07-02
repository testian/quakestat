/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;

/**
 *
 * @author testi
 */
public class MatchException extends Exception {

    public MatchException(Throwable cause) {
        super(cause);
    }

    public MatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatchException(String message) {
        super(message);
    }

    public MatchException() {
    }

}
