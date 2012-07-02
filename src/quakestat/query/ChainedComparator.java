/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat.query;
import java.util.List;
import java.util.Comparator;
/**
 *
 * @author testi
 */
public class ChainedComparator<T> implements Comparator<T> {
private List<Comparator<T>> comparators;

    public ChainedComparator(List<Comparator<T>> comparators) {
        if (comparators.isEmpty()) throw new IllegalArgumentException("Comparator list must not be empty.");
        this.comparators = comparators;
    }

    public int compare(T o1, T o2) {
        int previousCompare = 0;
        for (Comparator<T> c : comparators) {
        if (previousCompare != 0) break;
        previousCompare = c.compare(o1, o2);
        }
        return previousCompare;
    }


}
