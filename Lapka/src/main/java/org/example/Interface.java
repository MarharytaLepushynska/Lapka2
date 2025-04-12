package org.example;

import javax.swing.*;
import java.awt.*;

public class Interface extends JFrame {
    private JPanel northPanel;
    private JPanel centerPanel;


    public Interface() {
        super("Storage");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        initializeNorthPanel();
        add(northPanel, BorderLayout.NORTH);
        initializeCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private void initializeNorthPanel() {
        northPanel = new JPanel();
        northPanel.setSize(1000, 60);
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 60, 0));
        northPanel.setVisible(true);
        northPanel.setBackground(Color.BLUE);

        JComboBox storage = new JComboBox();
        storage.setBorder(BorderFactory.createTitledBorder("Storage"));
        storage.setPreferredSize(new Dimension(250, 60));
        northPanel.add(storage);

        JComboBox group = new JComboBox();
        group.setBorder(BorderFactory.createTitledBorder("Group"));
        group.setPreferredSize(new Dimension(250, 60));
        northPanel.add(group);

        JComboBox item = new JComboBox();
        item.setBorder(BorderFactory.createTitledBorder("Item"));
        item.setPreferredSize(new Dimension(250, 60));
        northPanel.add(item);
    }

    private void initializeCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(800, 300));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 20, 0));

        JLabel lbl = new JLabel("Item search");
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 30));
        centerPanel.add(lbl);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextField search = new JTextField();
        search.setMaximumSize(new Dimension(450, 50));
        search.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(search);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btn = new JButton("Search");
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Tahoma", Font.BOLD, 30));
        centerPanel.add(btn);

        centerPanel.setVisible(true);
    }

    public static void main(String[] args) {
        new Interface();
    }
}
