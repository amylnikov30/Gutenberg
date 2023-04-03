import java.util.HashSet;
import java.util.Set;

/**
 * A class that contains methods to calculate Jaccard similarity index.
 */
public class JaccardEngine
{
    /**
     * Calculates and returns the Jaccard similarity index between two sets of type {@code T}.
     * @param a Set a.
     * @param b Set b.
     * @return Jacccard similarity index.
     * @param <T> Type of elements in the sets.
     */
    public static <T> float getJaccardIndex(Set<T> a, Set<T> b)
    {
        final Set<T> intersection = new HashSet<>(a);
        final Set<T> union = new HashSet<>(a);

        intersection.retainAll(b);
        union.addAll(b);

        return (float) intersection.size() / union.size();
    }

    /**
     * Takes in a word, n in n-grams and returns a set of n-grams using top-and-tail tehcnique.
     * @param s String to be split into n-grams.
     * @param n Size of n-grams.
     * @return A set of n-grams.
     */
    public static Set<String> getNgrams(String s, int n)
    {
        final Set<String> result = new HashSet<>();
        for (int i=0; i<s.length()-(n-1); i++)
            result.add(s.substring(i, i+n));

        return result;
    }
}