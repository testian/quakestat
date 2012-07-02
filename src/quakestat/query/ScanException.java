/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;

/**
 *
 * @author testi
 */
public class ScanException extends QueryException {

    public ScanException(Throwable cause) {
        super(cause);
    }

    public ScanException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScanException(String message) {
        super(message);
    }

    public ScanException() {
    }

}
