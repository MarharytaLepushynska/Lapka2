package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class UserWindow extends JFrame {
    private JPanel northPanel;
    private JPanel centerPanel;
    Storage stor = new Storage("Склад");

    public UserWindow() throws IOException {
        super("Storage");
        GroupOfItems gr = new GroupOfItems("Молочка", "молочні продукти");
        GroupOfItems gr1 = new GroupOfItems("Крупи", "крупи");
        Item item = new Item("Гречка", "крупа", "торчин", 2, 50, "Крупи");
        Item item1 = new Item("Рис", "крупа", "торчин", 2, 50, "Крупи");
        Item item2 = new Item("Булгур", "крупа", "торчин", 2, 50, "Крупи");
        Item item4 = new Item("Манка", "крупа", "торчин", 2, 50, "Крупи");
        gr1.addItem(item);
        gr1.addItem(item1);
        gr1.addItem(item2);
        gr1.addItem(item4);
        stor.addGroup(gr);
        stor.addGroup(gr1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.getContentPane().setBackground(Color.decode("#87CEFA"));
        initializeNorthPanel();
        add(northPanel, BorderLayout.NORTH);
        initializeCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private void initializeNorthPanel() {
        northPanel = new JPanel();
        northPanel.setPreferredSize(new Dimension(1000, 70));
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 60, 0));
        northPanel.setVisible(true);
        northPanel.setBackground(Color.decode("#000080"));
        northPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JComboBox storage = new JComboBox();
        storage.setBorder(BorderFactory.createTitledBorder("Склад"));
        storage.setPreferredSize(new Dimension(250, 45));
        storage.addItem("");
        storage.addItem("Додавання товару");
        storage.addItem("Списання товару");
        storage.addItem("Статистичні дані");
        northPanel.add(storage);
        storage.addActionListener(e -> {int selected = storage.getSelectedIndex();
        switch (selected) {
            case 1:
                addItem();
                break;
            case 2:
                writeoffItem();
                break;
            case 3:
                statistics();
                break;
            default:
                break;
        }});

        JComboBox group = new JComboBox();
        group.setBorder(BorderFactory.createTitledBorder("Група"));
        group.setPreferredSize(new Dimension(250, 45));
        group.addItem("");
        group.addItem("Додати групу");
        group.addItem("Редагувати групу");
        group.addItem("Видалити групу");
        northPanel.add(group);

        JComboBox item = new JComboBox();
        item.setBorder(BorderFactory.createTitledBorder("Товар"));
        item.setPreferredSize(new Dimension(250, 45));
        item.addItem("");
        item.addItem("Додати товар");
        item.addItem("Редагувати товар");
        item.addItem("Видалити товар");
        northPanel.add(item);
    }

    private void initializeCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(800, 300));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 20, 0));
        centerPanel.setBackground(Color.decode("#87CEFA"));

        JLabel lbl = new JLabel("Item search");
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setFont(new Font("Verdana", Font.BOLD, 30));
        centerPanel.add(lbl);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextField search = new JTextField();
        search.setMaximumSize(new Dimension(450, 50));
        search.setAlignmentX(Component.CENTER_ALIGNMENT);
        search.setFont(new Font("Verdana", Font.PLAIN, 25));
        centerPanel.add(search);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btn = new JButton("Search");
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Verdana", Font.BOLD, 30));
        centerPanel.add(btn);

        centerPanel.setVisible(true);
    }

    private void addItem() {
        JFrame frame = new JFrame();
        frame.setSize(300, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lb = new JLabel("Оберіть групу");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        title.add(lb);
        frame.add(title, BorderLayout.NORTH);

        ButtonGroup bg = new ButtonGroup();
        for(int i=0; i<stor.groups.length; i++){
            JRadioButton rb = new JRadioButton(stor.groups[i].getName());
            panel.add(rb);
            bg.add(rb);
        }
        JScrollPane sp = new JScrollPane(panel);
        frame.add(sp, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton btn = new JButton("Далі");
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Verdana", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(100, 40));

        buttonPanel.add(btn);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        btn.addActionListener(e -> {
            boolean select = false;
            int number = -1;
           for(Component cmps: panel.getComponents()){
               number++;
               JRadioButton rb = (JRadioButton) cmps;
               if(rb.isSelected()){
                   increaseAmount(frame, stor.groups[number]);
                   select = true;
                   break;
               }
           }
           if(!select){
               JOptionPane.showMessageDialog(frame, "Потрібно обрати групу перед натисканням кнопки");
           }
        });
    }
    private void writeoffItem() {

    }
    private void statistics() {

    }

    private void increaseAmount(JFrame frame, GroupOfItems gr) {
            frame.getContentPane().removeAll();
            JSpinner[] spiners = new JSpinner[gr.items.length];

            JLabel lb = new JLabel("Додавання товару");
            lb.setFont(new Font("Verdana", Font.BOLD, 20));
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            titlePanel.add(lb);
            frame.add(titlePanel, BorderLayout.NORTH);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            for (int i = 0; i < gr.items.length; i++) {
                JPanel item = new JPanel(new GridLayout(1, 2));
                item.setPreferredSize(new Dimension(250, 20));
                item.add(new JLabel(gr.items[i].getName()));

                JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
                spinner.setPreferredSize(new Dimension(80, 30));
                item.add(spinner);
                spiners[i] = spinner;

                panel.add(item);
            }

            JScrollPane sp = new JScrollPane(panel);
            sp.setPreferredSize(new Dimension(250, 200));
            frame.add(sp, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton btn = new JButton("Додати");
            btn.setFont(new Font("Verdana", Font.BOLD, 20));
            btn.setPreferredSize(new Dimension(200, 40));
            buttonPanel.add(btn);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);

            btn.addActionListener(e -> {
                for(int i = 0; i < spiners.length; i++){
                    gr.items[i].increaseAmount((Integer) spiners[i].getValue());
                }
            });
    }
}
