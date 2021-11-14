package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NotepadModel {

    // * initial text
    static String text = new String();

    // * setting map
    private final static HashMap<String, String> sMap = new HashMap<String, String>();

    // * making it singleton
    static private final NotepadModel notepadModel = new NotepadModel();

    private NotepadModel() {
        final File file = new File("assets/utils/setting.txt");

        if (file.exists()) {

            final ArrayList<String> fields = new ArrayList<String>();
            fields.add("width");
            fields.add("height");
            fields.add("last_file_opened");
            fields.add("font_style");
            fields.add("font_size");

            try {
                Scanner scanner = new Scanner(file);

                int index = 0;

                while (scanner.hasNextLine()) {
                    sMap.put(fields.get(index), scanner.nextLine());
                    index++;
                }

                scanner.close();
            } catch (FileNotFoundException e) {
                // * nothing to do
            }
        } else {
            try {
                final FileWriter settingsFile = new FileWriter("assets/utils/setting.txt");

                settingsFile.write("900" + "\n" + "600" + "\n" + "" + "\n" + "SANS_SERIF" + "\n" + 16);

                settingsFile.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (sMap.get("last_file_opened").length() == 0) {
            // * set default path
            sMap.put("last_file_opened", System.getenv("SystemDrive"));
        }
    }

    // * getter : for singleton instance
    static public NotepadModel getInstance() {
        return notepadModel;
    }

    // * getters : for setting atributes
    static public int getHeight() {
        return Integer.parseInt(sMap.get("height"));
    }

    static public int getWidth() {
        return Integer.parseInt(sMap.get("width"));
    }

    static public String getLastFileOpenedPath() {
        return sMap.get("last_file_opened");
    }

    static public String getFontStyle() {
        return sMap.get("font_style");
    }

    static public int getFontSize() {
        return Integer.parseInt(sMap.get("font_size"));
    }

    // * setter : for setting attributes
    static public void setHeight(int height) {
        sMap.put("height", Integer.toString(height));
    }

    static public void setWidth(int width) {
        sMap.put("width", Integer.toString(width));
    }

    static public void setFontStyle(String fontStyle) {
        sMap.put("font_style", fontStyle);
    }

    static public void setFontSize(int fontSize) {
        sMap.put("font_size", Integer.toString(fontSize));
    }

    static public void setLastFileOpenedPath(String lastFileOpened) {
        sMap.put("last_file_opened", lastFileOpened);
    }

    // * method : to save details
    public static void saveSettings() {
        try {
            final FileWriter file = new FileWriter("assets/utils/setting.txt");

            file.write(getWidth() + "\n" + getHeight() + "\n" + getLastFileOpenedPath() + "\n" + getFontStyle() + "\n"
                    + getFontSize());

            file.close();

        } catch (IOException e) {
            // * nothing to do
        }
    }

    // * methods : for notepad

    public static void newFile(JTextArea textArea, JFrame frame) {
        textArea.setText("");
        frame.setTitle("Untitled");
        setLastFileOpenedPath("");
    }

    public static void saveFile(JTextArea textArea, JFrame frame) {
        final JFileChooser fileChooser = new JFileChooser();

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                final FileWriter file = new FileWriter(fileChooser.getSelectedFile().getAbsolutePath() + ".txt");
                file.write(textArea.getText());
                
                file.close(); // * close file to save changes

                frame.setTitle(fileChooser.getSelectedFile().getName());
                setLastFileOpenedPath(fileChooser.getSelectedFile().getAbsolutePath() + ".txt");
            } catch (IOException e) {
                // * nothing to do
            }

        }
    }

    public static void exit() {
        System.exit(0);
    }

    public static void setText(String text) {
        NotepadModel.text = text;
    }

    public static void openFile(JTextArea textArea, JFrame frame) {
        final JFileChooser fileChooser = new JFileChooser(getLastFileOpenedPath());
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text files", "*.txt", "pdf"));

        final int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

            Scanner sc;
            try {
                sc = new Scanner(file);

                while (sc.hasNextLine()) {
                    textArea.append(sc.nextLine() + "\n");
                }

                setLastFileOpenedPath(fileChooser.getSelectedFile().getAbsolutePath());

                // * save settings
                saveSettings();

                frame.setTitle(fileChooser.getSelectedFile().getName());
            } catch (FileNotFoundException e) {
                // * nothing to do
            }
        }
    }

    public static void save(JTextArea textArea, JFrame frame) {
        final File file = new File(getLastFileOpenedPath());

        if (file.exists()) {
            try {
                final FileWriter writer = new FileWriter(file);
                writer.write(textArea.getText());
                writer.close();

                frame.setTitle(frame.getTitle().substring(1));
            } catch (IOException e) {
                // * nothing to do
            }
        } else {
            saveFile(textArea, frame);
        }

    }

    static public void loadFile(JTextArea textArea, JFrame frame) {
        final File file = new File(getLastFileOpenedPath());

        Scanner sc;

        if (file.exists()) {
            try {
                sc = new Scanner(file);

                while (sc.hasNextLine()) {
                    textArea.append(sc.nextLine() + "\n");
                }

                frame.setTitle(file.getName());
            } catch (FileNotFoundException e) {
                setLastFileOpenedPath("");
                frame.setTitle("Untitled");
                saveSettings();
            }
        } else {
            setLastFileOpenedPath("");
            frame.setTitle("Untitled");
            saveSettings();
        }
    }

    public static void changeTextAreaTitle(JFrame frame) {
        if (frame.getTitle().contains("*") == false) {
            frame.setTitle("*" + frame.getTitle());
        }
    }

    public static void fontDailog(JTextArea textArea) {
        LoadingFonts font = new LoadingFonts(textArea);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        f.getContentPane().add(font.getPanel(), "North");
        f.getContentPane().add(font.getLabel());
        f.setSize(300, 180);
        f.setLocation(200, 200);
        f.setVisible(true);
    }
}
