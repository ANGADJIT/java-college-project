package model;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoadingFonts implements ActionListener {
    JComboBox<String> fontCombo;
    JLabel label;
    Font font;

    static Font fontStore = new Font(NotepadModel.getFontStyle(), Font.PLAIN, NotepadModel.getFontSize());

    JTextArea textArea;

    public LoadingFonts(JTextArea textArea) {
        this.textArea = textArea;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        fontCombo = new JComboBox<String>(fontNames);
        fontCombo.addActionListener(this);

        initilizeItemListener();
    }

    private void initilizeItemListener() {
        fontCombo.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setFont();
                    fontStore = font;
                    NotepadModel.setFontStyle((String) fontCombo.getSelectedItem());
                }
            }

        });
    }

    public static Font getFont() {
        return fontStore;
    }

    private void setFont() {
        String name = (String) fontCombo.getSelectedItem();
        font = Font.decode(name).deriveFont(24f);
        textArea.setFont(font);
    }

    public void actionPerformed(ActionEvent e) {
        String name = (String) fontCombo.getSelectedItem();
        font = Font.decode(name).deriveFont(24f);
        label.setFont(font);
        label.setText(name);
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.add(fontCombo);
        return panel;
    }

    public JLabel getLabel() {
        String name = (String) fontCombo.getItemAt(0);
        font = new Font(name, Font.PLAIN, 24);
        label = new JLabel(name, JLabel.CENTER) {
            protected void paintComponent(Graphics g) {
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        label.setFont(font);
        return label;
    }
}