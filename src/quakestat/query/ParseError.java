/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;

/**
 *
 * @author testi
 */
public class ParseError extends QueryException {

    public ParseError(Throwable cause) {
        super(cause);
    }

    public ParseError(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseError(String message) {
        super(message);
    }

    public ParseError() {
    }

}
