import javax.swing.*;
import javax.swing.filechooser.FileFilter;
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

        SearchEngine.init();

        window.setSize(new Dimension(500, 500));
//        window.setVisible(true);
    }

    public static void run(String pathToData, String searchTerm, float jaccardThreshold)
    {
        final JPanel main = new JPanel();
        main.setLayout(new FlowLayout());

        final JTextField tf_pathToData = new JTextField(pathToData);
        main.add(tf_pathToData);

        final JTextField tf_searchTerm = new JTextField(searchTerm);
        main.add(tf_searchTerm);

        final JButton btn_loadPathToData = new JButton("Choose data directory");
        btn_loadPathToData.addActionListener(e ->
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(Paths.get("Tests/data").toFile());

            if (fileChooser.showOpenDialog(main) == JFileChooser.APPROVE_OPTION) {

                File file = fileChooser.getSelectedFile();
                try
                {
                    tf_pathToData.setText(file.getPath());
                }
                catch(Exception exc)
                {
                    exc.printStackTrace();
                }
            }
        });
        main.add(btn_loadPathToData);

        final JTextField tf_jaccardThreshold = new JTextField(Float.toString(jaccardThreshold));
        main.add(tf_jaccardThreshold);

        final JButton btn_search = new JButton("Search");
        btn_search.addActionListener(a ->
        {
            final List<String> result = SearchEngine.search(tf_pathToData.getText(), tf_searchTerm.getText(),
                    Float.parseFloat(tf_jaccardThreshold.getText()));

            final JTextArea lb_result = new JTextArea("");
            lb_result.setEditable(false);
            main.add(lb_result);

            lb_result.setText("");
            result.forEach(e -> lb_result.setText(lb_result.getText() + e + "\n"));
        });
        main.add(btn_search);

        main.setVisible(true);
        window.add(main);
        
        window.setVisible(true);
    }
}