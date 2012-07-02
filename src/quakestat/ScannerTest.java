/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;
import quakestat.query.QueryScanner;
import quakestat.query.QueryParser;
import quakestat.query.ScanException;
import quakestat.query.ParseError;

/**
 *
 * @author testi
 */
public class ScannerTest {
public static void main(String[] args) {
    try {
        String queryText = "SOURCE ('Unreal Tournament',' Quake III Arena') WHERE SERVERNAME = 'Fish' AND PING < 5 AND PLAYERNAME = 'Jesus' SORT BY GAMETYPE LIMIT 5";
        QueryScanner s = new QueryScanner();
        QueryParser p = new QueryParser(s.scan(queryText));
        p.parseQuery();

    } catch (ScanException ex){System.err.println(ex);} catch (ParseError ex){
    ex.printStackTrace();
    }
}
}
