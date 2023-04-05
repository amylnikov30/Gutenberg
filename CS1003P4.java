import java.util.List;

public class CS1003P4
{
    public static void main(String[] args) throws IndexOutOfBoundsException
    {
        String DATA_DIR = "";
        String SEARCH_TERM = "";
        float JACCARD_THRESHOLD = 0.75f;

        if (args.length > 0)
        {
            SearchEngine.init();
            if (args.length >= 3)
            {
                DATA_DIR = args[0];
                SEARCH_TERM = args[1];
                JACCARD_THRESHOLD = Float.parseFloat(args[2]);

                SearchEngine.search(SEARCH_TERM, DATA_DIR, JACCARD_THRESHOLD).forEach(System.out::println);
//                if (!List.of(args).contains("--gui"))
//                    SearchEngine.close();
            }

            if (List.of(args).contains("--gui"))
            {
                GUI.init();
                GUI.run(SEARCH_TERM, DATA_DIR, JACCARD_THRESHOLD);
            }

            return;
        }

        Tests.init();
        System.out.println("All ok: " + Tests.all());
        Tests.close();
    }
}