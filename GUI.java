import javax.swing.*;
import java.awt.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class GUI 
{
    private static JFrame window;

    public static void init()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException |
               IllegalAccessException ex)
        {
            System.out.println(ex.getMessage());
        }

        window = new JFrame("Search Engine");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        window.addWindowListener(new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent)
            {
                SearchEngine.close();
            }
        });

//        SearchEngine.init();

        window.setSize(new Dimension(500, 500));
//        window.setVisible(true);
    }

    public static void run(String searchTerm, String pathToData, float jaccardThreshold)
    {
        final JPanel main = new JPanel();
        main.setLayout(new FlowLayout());

        final JTextField tf_searchTerm = new JTextField(searchTerm.isBlank() ? "Search term" : searchTerm, 30);
        main.add(tf_searchTerm);

        final JTextField tf_pathToData = new JTextField(pathToData.isBlank() ? "Path/to/data" : pathToData, 7);
        main.add(tf_pathToData);

        final JButton btn_loadPathToData = new JButton("...");
        btn_loadPathToData.addActionListener(e ->
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setCurrentDirectory(Paths.get("").toFile());

            if (fileChooser.showOpenDialog(main) == JFileChooser.APPROVE_OPTION) {

                File file = fileChooser.getSelectedFile();
                try
                {
                    tf_pathToData.setText(file.getPath());
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        });
        main.add(btn_loadPathToData);

        final JTextField tf_jaccardThreshold = new JTextField(Float.toString(jaccardThreshold), 3);
        main.add(tf_jaccardThreshold);

        final JTextArea ta_result = new JTextArea("");
        ta_result.setEditable(false);

        final JButton btn_search = new JButton("Search");
        btn_search.addActionListener(a ->
        {
            main.remove(ta_result);

            final List<String> result = SearchEngine.search(tf_searchTerm.getText(), tf_pathToData.getText(),
                    Float.parseFloat(tf_jaccardThreshold.getText()));

            main.add(ta_result);

            ta_result.setText("");
            result.forEach(e -> ta_result.setText(ta_result.getText() + e + "\n"));
        });
        main.add(btn_search);

        main.setVisible(true);
        window.add(main);
        
        window.setVisible(true);
    }
}