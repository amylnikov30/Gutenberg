import breeze.util.Index;

import java.io.FileNotFoundException;

public class CS1003P4
{
    public static void main(String[] args) throws IndexOutOfBoundsException
    {
        try
        {
            final String DATA_DIR = args[0];
            final String SEARCH_TERM = args[1];
            final float JACCARD_THRESHOLD = Float.parseFloat(args[2]);
        }
        catch (IndexOutOfBoundsException ignored)
        {}

    //    SearchEngine.init();
    //    SearchEngine.search(SEARCH_TERM, DATA_DIR, JACCARD_THRESHOLD).forEach(System.out::println);
    //    SearchEngine.close();

        Tests.init();
        System.out.println("All ok: " + Tests.all());
        Tests.close();
    }
}