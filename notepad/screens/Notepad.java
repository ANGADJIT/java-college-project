package screens;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import model.LoadingFonts;
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
                NotepadModel.setLastFileOpenedPath(NotepadModel.getLastFileOpenedPath());

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
        final JMenuItem incrFontSize = new JMenuItem("increase size");
        final JMenuItem dcrFontSize = new JMenuItem("decrease size");

        final JMenu about = new JMenu("About");

        final JMenuItem aboutNotepad = new JMenuItem("about notepad");

        about.add(aboutNotepad);

        formatMenu.add(font);
        formatMenu.add(incrFontSize);
        formatMenu.add(dcrFontSize);

        jMenuBar.add(fileMenu);
        jMenuBar.add(editMenu);
        jMenuBar.add(formatMenu);
        jMenuBar.add(about);

        this.setJMenuBar(jMenuBar);

        // * plain text field
        final JTextArea textArea = new JTextArea();
        textArea.setFont(new Font(NotepadModel.getFontStyle(), Font.PLAIN, NotepadModel.getFontSize()));
        textArea.setLineWrap(true);

        textArea.getDocument().addDocumentListener(this);

        final UndoManager manager = new UndoManager();

        textArea.getDocument().addUndoableEditListener(manager);

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

        save.addActionListener((e) -> {
            NotepadModel.save(textArea, this);
        });

        openFile.addActionListener((l) -> {
            NotepadModel.openFile(textArea, this);
        });

        exit.addActionListener((e) -> {
            NotepadModel.exit();
        });

        // * adding functions for second menu

        undo.addActionListener((e) -> {
            manager.undo();
        });

        redo.addActionListener((e) -> {
            manager.redo();
        });

        copy.addActionListener((e) -> {
            textArea.copy();
        });

        paste.addActionListener((e) -> {
            textArea.paste();
        });

        cut.addActionListener((e) -> {
            textArea.cut();
        });

        // * adding functions to third menu

        font.addActionListener((e) -> {
            NotepadModel.fontDailog(textArea);
        });

        incrFontSize.addActionListener((e) -> {
            textArea.setFont(new Font(LoadingFonts.getFont().getFamily(), Font.PLAIN, NotepadModel.getFontSize() + 1));
            NotepadModel.setFontSize(NotepadModel.getFontSize() + 1);

        });

        dcrFontSize.addActionListener((e) -> {
            textArea.setFont(new Font(LoadingFonts.getFont().getFamily(), Font.PLAIN, NotepadModel.getFontSize() - 1));
            NotepadModel.setFontSize(NotepadModel.getFontSize() - 1);

        });

        // * adding function to forth menu
        aboutNotepad.addActionListener((e) -> {
            JOptionPane.showMessageDialog(null, "Simple fully functional notepad written in java swing", "About Dialog",
                    JOptionPane.PLAIN_MESSAGE, new ImageIcon(new ImageIcon("assets/icons/notepad.png").getImage()
                            .getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        });
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        NotepadModel.changeTextAreaTitle(this);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // * nothing to do
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // * nothing to do
    }

}
