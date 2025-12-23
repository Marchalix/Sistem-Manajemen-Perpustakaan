package org.example.gui;

import org.example.gui.*;
import org.example.data.*;
import javax.swing.*;
import java.awt.*;

public class BookPanel extends JPanel {
    public BookPanel() {
        setLayout(new GridLayout(0,3,10,10));

        for (BookData b : BookManager.getAll()) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel img = new JLabel(new ImageIcon(b.coverPath));
            JLabel title = new JLabel(b.judul, SwingConstants.CENTER);

            card.add(img, BorderLayout.CENTER);
            card.add(title, BorderLayout.SOUTH);
            add(card);
        }
    }
}
