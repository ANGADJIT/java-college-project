package screens;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import model.NotepadModel;

public class Notepad extends JFrame implements DocumentListener {

    public Notepad() {

        // * frame initials
        this.setSize(NotepadModel.getWidth(), NotepadModel.getHeight());
        this.setIconImage(new ImageIcon("assets/icons/notepad.png").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setFont(new Font(NotepadModel.getFontStyle(), Font.PLAIN, NotepadModel.getFontSize()));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                NotepadModel.setWidth(getSize().width);
                NotepadModel.setHeight(getSize().height);
                NotepadModel.setLastFileOpenedPath("");
                NotepadModel.setFontStyle("SANS_SERIF");
                NotepadModel.setFontSize(16);

                NotepadModel.saveSettings();
            }
        });
        ;

        // * menu bar and items
        final JMenuBar jMenuBar = new JMenuBar();

        final JMenu fileMenu = new JMenu("File");

        final JMenuItem newFile = new JMenuItem("new");
        final JMenuItem openFile = new JMenuItem("open");
        final JMenuItem save = new JMenuItem("save");
        final JMenuItem saveAs = new JMenuItem("saveAs...");
        final JMenuItem exit = new JMenuItem("exit");

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        final JMenu editMenu = new JMenu("Edit");

        final JMenuItem undo = new JMenuItem("undo");
        final JMenuItem redo = new JMenuItem("redo");
        final JMenuItem cut = new JMenuItem("cut");
        final JMenuItem copy = new JMenuItem("copy");
        final JMenuItem paste = new JMenuItem("paste");

        editMenu.add(undo);
        editMenu.add(redo);
        editMenu.addSeparator();
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);

        final JMenu formatMenu = new JMenu("Format");

        final JMenuItem font = new JMenuItem("font");

        formatMenu.add(font);

        jMenuBar.add(fileMenu);
        jMenuBar.add(editMenu);
        jMenuBar.add(formatMenu);

        this.setJMenuBar(jMenuBar);

        // * plain text field
        final JTextArea textArea = new JTextArea();
        textArea.setFont(new Font(NotepadModel.getFontStyle(), Font.PLAIN, NotepadModel.getFontSize()));
        textArea.setLineWrap(true);

        textArea.getDocument().addDocumentListener(this);

        // * laod file
        NotepadModel.loadFile(textArea, this);

        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(scrollPane, BorderLayout.CENTER);

        this.setVisible(true);

        // * adding functions to first menu
        newFile.addActionListener((l) -> {
            NotepadModel.newFile(textArea, this);
        });

        saveAs.addActionListener((e) -> {
            NotepadModel.saveFile(textArea, this);
        });

        openFile.addActionListener((l) -> {
            NotepadModel.openFile(textArea, this);
        });

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        NotepadModel.listenTextAreaChanges(this);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        //* nothing to do 
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //* nothing to do
    }

}
