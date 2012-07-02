/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;

/**
 *
 * @author testi
 */
public class QueryException extends Exception {

    public QueryException(Throwable cause) {
        super(cause);
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryException(String message) {
        super(message);
    }

    public QueryException() {
    }

}
