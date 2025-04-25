package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class UserWindow extends JFrame {
    private JPanel northPanel;
    private JPanel centerPanel;
    private JPanel southPanel;
    private JLabel totalPrice;
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
        this.getContentPane().setBackground(Color.decode("#cce6ff"));
        initializeNorthPanel();
        add(northPanel, BorderLayout.NORTH);
        initializeCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        stat();
        add(southPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void stat(){
        southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel statPanel = new JPanel();
        statPanel.setBackground(Color.decode("#cce6ff"));
        statPanel.setPreferredSize(new Dimension(400, 50));
        totalPrice = new JLabel(String.format("Ціна всіх товарів: %.2f", + stor.getTotalPrice()));
        totalPrice.setFont(new Font("Verdana", Font.BOLD, 20));
        statPanel.add(totalPrice);
        southPanel.add(statPanel);
        southPanel.setBackground(Color.decode("#cce6ff"));
        southPanel.setVisible(true);
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
        storage.addItem("Інформація по складу");
        storage.addItem("Інформація по групі");
        storage.addItem("Вартість по групі");
        northPanel.add(storage);
        storage.addActionListener(e-> {int selected = storage.getSelectedIndex();
        switch (selected) {
            case 1:
                increaceCount();
                break;
            case 2:
                writeoffItem();
                break;
            case 3:
                storageStatistics();
                break;
            case 4:
                groupStatistics();
                break;
            case 5:
                groupPrice();
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
        group.addActionListener(e -> {int selected = group.getSelectedIndex();
            switch (selected) {
                case 1:
                    addGroup();
                    break;
                case 2:
                    editGroup();
                    break;
                case 3:
                    removeGroup();
                    break;
                default:
                    break;
            }});

        JComboBox item = new JComboBox();
        item.setBorder(BorderFactory.createTitledBorder("Товар"));
        item.setPreferredSize(new Dimension(250, 45));
        item.addItem("");
        item.addItem("Додати товар");
        item.addItem("Редагувати товар");
        item.addItem("Видалити товар");
        northPanel.add(item);
        item.addActionListener(e -> {int selected = item.getSelectedIndex();
        switch (selected) {
            case 1:
                addingItem();
                break;
            case 2:
                editingItem();
                break;
            case 3:
                deletingItem();
                break;
            default:
                break;
        }});
    }

    private void initializeCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(800, 300));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 20, 0));
        centerPanel.setBackground(Color.decode("#cce6ff"));

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
        btn.addActionListener(e -> {
            String item = search.getText();
            findItem(item);
            search.setText("");
        });

        centerPanel.setVisible(true);
    }

    private void addingItem(){
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Додавання товару");
        frame.setSize(300, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));
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
        buttonPanel.setBackground(Color.decode("#cce6ff"));
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        btn.addActionListener(e -> {
            boolean select = false;
            int number = -1;
            for(Component cmps: panel.getComponents()){
                number++;
                JRadioButton rb = (JRadioButton) cmps;
                if(rb.isSelected()){
                    addItem(stor.groups[number].getName(), frame);
                    select = true;
                    break;
                }
            }
            if(!select){
                JOptionPane.showMessageDialog(frame, "Потрібно обрати групу перед натисканням кнопки");
            }
        });

    }

    private void editingItem(){
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Редагування товару");
        frame.setSize(600, 300);
        frame.setLayout(new BorderLayout());

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));

        JLabel lb = new JLabel("Введіть назву товару для редагування");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        title.add(lb);
        frame.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 35, 50, 50));
        centerPanel.add(Box.createVerticalStrut(40));

        JTextField search = new JTextField();
        search.setFont(new Font("Verdana", Font.PLAIN, 25));
        centerPanel.add(search);

        centerPanel.add(Box.createVerticalStrut(40));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#cce6ff"));

        JButton button = new JButton("Шукати");
        button.setPreferredSize(new Dimension(250, 30));
        button.setFont(new Font("Verdana", Font.BOLD, 20));
        buttonPanel.add(button);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        button.addActionListener(e -> {

            String itemName = search.getText().trim();

            if(!itemName.isEmpty()) {
                Item foundItem = stor.findItem(itemName);
                if(foundItem!=null){
                    editChosenItem(foundItem, itemName,frame);
                } else {
                    JOptionPane.showMessageDialog(frame,"Товар не було знайдено!");
                    search.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(frame,"Спочатку введіть назву товару!");
                search.setText("");
            }
        });
    }

    private void deletingItem(){
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Видалення товару");
        frame.setSize(600, 300);
        frame.setLayout(new BorderLayout());

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));

        JLabel lb = new JLabel("Введіть назву товару для видалення");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        title.add(lb);
        frame.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 35, 50, 50));
        centerPanel.add(Box.createVerticalStrut(40));

        JTextField search = new JTextField();
        search.setFont(new Font("Verdana", Font.PLAIN, 25));
        centerPanel.add(search);

        centerPanel.add(Box.createVerticalStrut(40));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#cce6ff"));

        JButton button = new JButton("Видалити");
        button.setPreferredSize(new Dimension(250, 30));
        button.setFont(new Font("Verdana", Font.BOLD, 20));
        buttonPanel.add(button);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        button.addActionListener(e -> {

            String itemName = search.getText().trim();

            if(!itemName.isEmpty()) {
                Item foundItem = stor.findItem(itemName);
                if(foundItem!=null){
                    int groupIndex = stor.findGroup(foundItem.getGroupOfItems());
                    try {
                        stor.groups[groupIndex].removeItem(foundItem.getName());
                        JOptionPane.showMessageDialog(frame,"Товар було успішно видалено!");
                        totalPrice.setText(String.format("Ціна всіх товарів: %.2f", + stor.getTotalPrice()));
                        frame.dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,"Товар не було знайдено!");
                    search.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(frame,"Спочатку введіть назву товару!");
                search.setText("");
            }
        });
    }

    private void addItem(String groupName, JFrame mainFrame){
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Додавання товару");
        frame.setLayout(new BorderLayout());
        frame.setSize(700, 400);

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));
        JLabel lb = new JLabel("Додавання товару");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        title.add(lb);
        frame.add(title, BorderLayout.NORTH);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(6,2));

        //item name
        JLabel nameLabel = new JLabel("Назва:");
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setSize(250, 40);
        nameField.setFont(new Font("Verdana", Font.PLAIN, 25));
        textPanel.add(nameField);

        //item description
        JLabel descriptionLabel = new JLabel("Опис:");
        descriptionLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(descriptionLabel);

        JTextField descriptionField = new JTextField();
        descriptionField.setSize(250, 40);
        descriptionField.setFont(new Font("Verdana", Font.PLAIN, 25));
        textPanel.add(descriptionField);

        //item's producer
        JLabel producerLabel = new JLabel("Виробник:");
        producerLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(producerLabel);

        JTextField producerField = new JTextField();
        producerField.setSize(250, 40);
        producerField.setFont(new Font("Verdana", Font.PLAIN, 25));
        textPanel.add(producerField);

        //item price
        JLabel priceLabel = new JLabel("Ціна за штуку:");
        priceLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(priceLabel);

        JSpinner priceField = new JSpinner(new SpinnerNumberModel(0.1, 0.1, 1000, 0.1));
        priceField.setSize(250, 40);
        priceField.setFont(new Font("Verdana", Font.PLAIN, 25));
        JSpinner.DefaultEditor editor = new JSpinner.DefaultEditor(priceField);
        editor.getTextField().setEditable(false);
        priceField.setEditor(editor);
        textPanel.add(priceField);

        //item amount
        JLabel amountLabel = new JLabel("Кількість:");
        amountLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(amountLabel);

        JSpinner amountField = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        amountField.setSize(250, 40);
        amountField.setFont(new Font("Verdana", Font.PLAIN, 25));
        editor = new JSpinner.DefaultEditor(amountField);
        amountField.setEditor(editor);
        textPanel.add(amountField);

        JLabel groupLabel = new JLabel("Група товарів:");
        groupLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(groupLabel);

        JTextField groupField = new JTextField(groupName);
        groupField.setSize(250, 40);
        groupField.setFont(new Font("Verdana", Font.PLAIN, 25));
        groupField.setEditable(false);
        textPanel.add(groupField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#cce6ff"));

        JButton button = new JButton("Додати");
        button.setPreferredSize(new Dimension(250, 30));
        button.setFont(new Font("Verdana", Font.BOLD, 20));
        buttonPanel.add(button);

        frame.add(textPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        button.addActionListener(e ->{
            String itemName = nameField.getText().trim();
            String itemDescription = descriptionField.getText().trim();
            String itemProducer = producerField.getText().trim();
            int itemAmount = Integer.parseInt(String.valueOf(amountField.getValue()));
            double itemPrice = Double.parseDouble(String.valueOf(priceField.getValue()));
            String itemGroup = groupField.getText();

            boolean success = false;
            boolean exists = false;

            if(!itemName.isEmpty() && !itemDescription.isEmpty() && !itemProducer.isEmpty()){
                int groupNumber = stor.findGroup(itemGroup);
                for (GroupOfItems group : stor.getGroups()) {
                    for(Item item : group.getItems()) {
                        if(item.getName().equalsIgnoreCase(itemName)) {
                            exists = true;
                            break;
                        }
                    }
                }
                if (exists) {
                    JOptionPane.showMessageDialog(frame, "Товар з такою назвою вже існує!");
                    nameField.setText("");
                    descriptionField.setText("");
                    producerField.setText("");
                    amountField.setValue(0);
                    priceField.setValue(0);
                    return;
                }
                try {
                    stor.groups[groupNumber].addItem(new Item(itemName, itemDescription,itemProducer,
                            itemAmount,itemPrice,itemGroup));
                    success = true;
                    totalPrice.setText(String.format("Ціна всіх товарів: %.2f", + stor.getTotalPrice()));
                    mainFrame.dispose();
                    frame.dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Для створення товару усі поля мають бути заповнені!");
            }

            if(success){
                JOptionPane.showMessageDialog(frame, "Товар було успішно створено!");
            }

            nameField.setText("");
            descriptionField.setText("");
            producerField.setText("");
            amountField.setValue(0);
            priceField.setValue(0);
        });

    }

    private void editChosenItem(Item foundItem, String itemName, JFrame mainFrame){
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Редагування товару");
        frame.setLayout(new BorderLayout());
        frame.setSize(700, 400);

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));
        JLabel lb = new JLabel("Редагування товару");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        title.add(lb);
        frame.add(title, BorderLayout.NORTH);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(6,2));

        //item name
        JLabel nameLabel = new JLabel("Назва:");
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setSize(250, 40);
        nameField.setFont(new Font("Verdana", Font.PLAIN, 25));
        nameField.setText(foundItem.getName());
        textPanel.add(nameField);

        //item description
        JLabel descriptionLabel = new JLabel("Опис:");
        descriptionLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(descriptionLabel);

        JTextField descriptionField = new JTextField();
        descriptionField.setSize(250, 40);
        descriptionField.setFont(new Font("Verdana", Font.PLAIN, 25));
        descriptionField.setText(foundItem.getDescription());
        textPanel.add(descriptionField);

        //item's producer
        JLabel producerLabel = new JLabel("Виробник:");
        producerLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(producerLabel);

        JTextField producerField = new JTextField();
        producerField.setSize(250, 40);
        producerField.setFont(new Font("Verdana", Font.PLAIN, 25));
        producerField.setText(foundItem.getProducer());
        textPanel.add(producerField);

        //item price
        JLabel priceLabel = new JLabel("Ціна за штуку:");
        priceLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(priceLabel);

        JSpinner priceField = new JSpinner(new SpinnerNumberModel(0.1, 0.1, 1000, 0.1));
        priceField.setSize(250, 40);
        priceField.setFont(new Font("Verdana", Font.PLAIN, 25));
        priceField.setValue(foundItem.getPrice());
        JSpinner.DefaultEditor editor = new JSpinner.DefaultEditor(priceField);
        editor.getTextField().setEditable(false);
        priceField.setEditor(editor);
        textPanel.add(priceField);

        //item amount
        JLabel amountLabel = new JLabel("Кількість:");
        amountLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(amountLabel);

        JSpinner amountField = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        amountField.setSize(250, 40);
        amountField.setFont(new Font("Verdana", Font.PLAIN, 25));
        amountField.setValue(foundItem.getAmount());
        editor = new JSpinner.DefaultEditor(amountField);
        amountField.setEditor(editor);
        textPanel.add(amountField);

        JLabel groupLabel = new JLabel("Група товарів:");
        groupLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textPanel.add(groupLabel);

        JTextField groupField = new JTextField();
        groupField.setSize(250, 40);
        groupField.setFont(new Font("Verdana", Font.PLAIN, 25));
        groupField.setText(foundItem.getGroupOfItems());
        groupField.setEditable(false);
        textPanel.add(groupField);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#cce6ff"));

        JButton button = new JButton("Редагувати");
        button.setPreferredSize(new Dimension(250, 30));
        button.setFont(new Font("Verdana", Font.BOLD, 20));
        buttonPanel.add(button);

        frame.add(textPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        button.addActionListener(e ->{
            String newItemName = nameField.getText().trim();
            String newItemDescription = descriptionField.getText().trim();
            String newItemProducer = producerField.getText().trim();
            int newItemAmount = Integer.parseInt(String.valueOf(amountField.getValue()));
            double newItemPrice = Double.parseDouble(String.valueOf(priceField.getValue()));
            String newItemGroup = foundItem.getGroupOfItems();

            boolean changed = false;
            boolean exists = false;

            if(!newItemName.isEmpty() && !newItemDescription.isEmpty() && !newItemProducer.isEmpty()){
                for (GroupOfItems group : stor.getGroups()) {
                    for(Item item : group.getItems()) {
                        if(item.getName().equalsIgnoreCase(newItemName) &&
                                !item.getName().equalsIgnoreCase(itemName)) {
                            exists = true;
                            break;
                        }
                    }
                }
                if(exists){
                    JOptionPane.showMessageDialog(frame,"Товар з такою назвою вже існує!");
                    return;
                }
                int groupIndex = stor.findGroup(newItemGroup);
                System.out.println(groupIndex);
                if(!newItemName.equalsIgnoreCase(foundItem.getName())||
                !newItemDescription.equalsIgnoreCase(foundItem.getDescription())||
                !newItemProducer.equalsIgnoreCase(foundItem.getProducer()) ||
                newItemAmount != foundItem.getAmount() || newItemPrice != foundItem.getPrice()) {

                    try {
                        stor.groups[groupIndex].editItem(foundItem.getName(),
                                new Item(newItemName, newItemDescription, newItemProducer, newItemAmount,
                                        newItemPrice, newItemGroup));
                        totalPrice.setText(String.format("Ціна всіх товарів: %.2f", +stor.getTotalPrice()));
                        changed = true;
                        frame.dispose();
                        mainFrame.dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,"Жодного поля не було змінено!");
                }
            } else {
                JOptionPane.showMessageDialog(frame,"Поля не можуть бути пустими!");
            }

            if(changed){
                JOptionPane.showMessageDialog(frame,"Товар було успішно змінено!");
            }
        });
    }

    private void addGroup(){
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Додавання групи");
        frame.setLayout(new BorderLayout());
        frame.setSize(500, 200);

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));
        JLabel lb = new JLabel("Додавання групи");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        title.add(lb);
        frame.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));


        JLabel name = new JLabel("Назва:");
        name.setFont(new Font("Verdana", Font.BOLD, 20));
        panel.add(name);

        JTextField naming = new JTextField();
        naming.setSize(250, 40);
        naming.setFont(new Font("Verdana", Font.PLAIN, 25));
        panel.add(naming);

        JLabel desc = new JLabel("Опис:");
        desc.setFont(new Font("Verdana", Font.BOLD, 20));
        panel.add(desc);

        JTextField descr = new JTextField();
        naming.setSize(250, 40);
        naming.setFont(new Font("Verdana", Font.PLAIN, 20));
        panel.add(descr);

        JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPannel.setBackground(Color.decode("#cce6ff"));

        JButton btn = new JButton("Додати");
        btn.setPreferredSize(new Dimension(250, 30));
        btn.setFont(new Font("Verdana", Font.BOLD, 20));
        buttonPannel.add(btn);

        frame.add(buttonPannel, BorderLayout.SOUTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        btn.addActionListener(e -> {
            String groupName = naming.getText();
            String groupDescr = descr.getText();

            if(groupName!=null && groupDescr!=null && !groupName.trim().isEmpty() && !groupDescr.trim().isEmpty()) {
                boolean exist = false;
                for (GroupOfItems group : stor.getGroups()) {
                    if (groupName.equalsIgnoreCase(group.getName())) {
                        exist = true;
                        break;
                    }
                }

                if (!exist) {
                    GroupOfItems group = new GroupOfItems(groupName, groupDescr);
                    stor.addGroup(group);
                    frame.setVisible(false);
                    JOptionPane.showMessageDialog(frame, "Групу успішно додано");
                } else {
                    JOptionPane.showMessageDialog(frame, "Така група вже існує");
                }
            }else{
                JOptionPane.showMessageDialog(frame, "Введіть коректні значення");
                naming.setText("");
                descr.setText("");
            }

        });
    }

    private void editGroup(){
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Редагування групи");
        frame.setLayout(new BorderLayout());
        frame.setSize(500, 200);

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));
        JLabel lb = new JLabel("Редагування групи");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        title.add(lb);
        frame.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel name = new JLabel("Назва:");
        name.setFont(new Font("Verdana", Font.BOLD, 20));
        panel.add(name);

        JTextField naming = new JTextField();
        naming.setPreferredSize(new Dimension(250, 40));
        naming.setFont(new Font("Verdana", Font.PLAIN, 25));
        panel.add(naming);

        JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPannel.setBackground(Color.decode("#cce6ff"));

        JButton btn = new JButton("Редагувати");
        btn.setPreferredSize(new Dimension(250, 30));
        btn.setFont(new Font("Verdana", Font.BOLD, 20));
        buttonPannel.add(btn);

        frame.add(buttonPannel, BorderLayout.SOUTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        btn.addActionListener(e -> {
            String groupName = naming.getText();
            GroupOfItems gr = null;

            if(groupName!=null && !groupName.trim().isEmpty() ) {
                boolean exist = false;
                for (GroupOfItems group : stor.getGroups()) {
                    if (groupName.equalsIgnoreCase(group.getName())) {
                        exist = true;
                        gr = group;
                        break;
                    }
                }

                if (!exist) {
                    JOptionPane.showMessageDialog(frame, "Такої групи не існує");
                } else {
                    frame.getContentPane().removeAll();
                    JPanel titl = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    titl.setBackground(Color.decode("#cce6ff"));
                    JLabel lbl = new JLabel("Редагування групи");
                    lbl.setFont(new Font("Verdana", Font.BOLD, 20));
                    titl.add(lbl);
                    frame.add(titl, BorderLayout.NORTH);

                    JPanel panel1 = new JPanel();
                    panel1.setLayout(new GridLayout(2, 2));

                    JLabel name1 = new JLabel("Назва:");
                    name1.setFont(new Font("Verdana", Font.BOLD, 20));
                    panel1.add(name1);

                    JTextField nam = new JTextField();
                    nam.setText(gr.getName());
                    nam.setSize(250, 40);
                    nam.setFont(new Font("Verdana", Font.PLAIN, 25));
                    panel1.add(nam);

                    JLabel desc = new JLabel("Опис:");
                    desc.setFont(new Font("Verdana", Font.BOLD, 20));
                    panel1.add(desc);

                    JTextField descr = new JTextField();
                    descr.setText(gr.getDescription());
                    descr.setSize(250, 40);
                    descr.setFont(new Font("Verdana", Font.PLAIN, 20));
                    panel1.add(descr);

                    JPanel btnPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    btnPannel.setBackground(Color.decode("#cce6ff"));

                    JButton bton = new JButton("Редагувати");
                    bton.setPreferredSize(new Dimension(250, 30));
                    bton.setFont(new Font("Verdana", Font.BOLD, 20));
                    btnPannel.add(bton);

                    frame.add(btnPannel, BorderLayout.SOUTH);
                    frame.add(panel1, BorderLayout.CENTER);
                    frame.setVisible(true);
                    frame.repaint();

                    GroupOfItems finalGr = gr;
                    bton.addActionListener(r -> {
                        String groupNaming = nam.getText();
                        String groupDescr = descr.getText();
                        String currentname = finalGr.getName();

                        if(!groupNaming.trim().isEmpty() && !groupDescr.trim().isEmpty()) {
                            boolean exists = false;
                            for (GroupOfItems group : stor.getGroups()) {
                                if (groupNaming.equalsIgnoreCase(group.getName())) {
                                    if(!groupNaming.equalsIgnoreCase(currentname)) {
                                        exists = true;
                                        break;
                                    }
                                }
                            }
                            if (!exists) {
                                try {
                                    stor.editGroup(currentname, groupNaming, groupDescr);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                                frame.setVisible(false);
                                JOptionPane.showMessageDialog(frame, "Групу успішно змінено");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Така група вже існує");
                            }
                        }else{
                            JOptionPane.showMessageDialog(frame, "Введіть коректні значення");
                            naming.setText("");
                            descr.setText("");
                        }

                    });
                }
            }else{
                JOptionPane.showMessageDialog(frame, "Введіть коректні значення");
                naming.setText("");
            }

        });
    }

    private void removeGroup(){
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Видалення групи");
        frame.setLayout(new BorderLayout());
        frame.setSize(500, 200);

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));
        JLabel lb = new JLabel("Видалення групи");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        title.add(lb);
        frame.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel name = new JLabel("Назва:");
        name.setFont(new Font("Verdana", Font.BOLD, 20));
        panel.add(name);

        JTextField naming = new JTextField();
        naming.setPreferredSize(new Dimension(250, 40));
        naming.setFont(new Font("Verdana", Font.PLAIN, 25));
        panel.add(naming);

        JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPannel.setBackground(Color.decode("#cce6ff"));

        JButton btn = new JButton("Видалити");
        btn.setPreferredSize(new Dimension(250, 30));
        btn.setFont(new Font("Verdana", Font.BOLD, 20));
        buttonPannel.add(btn);

        frame.add(buttonPannel, BorderLayout.SOUTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        btn.addActionListener(e -> {
            String groupNaming = naming.getText();

            if(!groupNaming.trim().isEmpty()) {
                boolean exists = false;
                for (GroupOfItems group : stor.getGroups()) {
                    if (groupNaming.equalsIgnoreCase(group.getName())) {
                        exists = true;
                        try {
                            stor.removeGroup(group.getName());
                            frame.setVisible(false);
                            totalPrice.setText(String.format("Ціна всіх товарів: %.2f", + stor.getTotalPrice()));
                            JOptionPane.showMessageDialog(frame, "Групу успішно видалено");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }
                }
                if (!exists) {
                    JOptionPane.showMessageDialog(frame, "Групу не знайдено");
                }
            }else{
                JOptionPane.showMessageDialog(frame, "Введіть коректне значення");
            }
        });
    }

    private void findItem(String item) {
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Пошук товару");
        frame.setSize(500, 200);

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));
        JLabel lb = new JLabel("Інформація про товар");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        title.add(lb);
        frame.add(title, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        Item found = stor.findItem(item);
        if (found != null) {
            textArea.setText("Назва: "+found.getName()
            +"\nОпис: "+found.getDescription()
            +"\nВиробник: "+found.getProducer()
                    +"\nКількість: "+found.getPrice()
                    +String.format("\nЦіна: %.2f грн",+found.getPrice()));
        }else{
            textArea.setText("Такого товару немає");
        }
        textArea.setFont(new Font("Verdana", Font.BOLD, 15));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void increaceCount() {
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Додавання товару");
        frame.setSize(300, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));
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
        buttonPanel.setBackground(Color.decode("#cce6ff"));
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
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Списання товару");
        frame.setSize(300, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));
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
        buttonPanel.setBackground(Color.decode("#cce6ff"));
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        btn.addActionListener(e -> {
            boolean select = false;
            int number = -1;
            for(Component cmps: panel.getComponents()){
                number++;
                JRadioButton rb = (JRadioButton) cmps;
                if(rb.isSelected()){
                    decreaseAmount(frame, stor.groups[number]);
                    select = true;
                    break;
                }
            }
            if(!select){
                JOptionPane.showMessageDialog(frame, "Потрібно обрати групу перед натисканням кнопки");
            }
        });
    }

    private void storageStatistics() {
        JFrame frame = new JFrame();
        frame.setSize(500, 400);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });

        JLabel lb = new JLabel("Інформація по складу");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.decode("#cce6ff"));
        titlePanel.add(lb);
        frame.add(titlePanel, BorderLayout.NORTH);

        JTextArea ta = new JTextArea(stor.getAllInfoAboutStorage());
        ta.setFont(new Font("Verdana", Font.BOLD, 10));
        ta.setEditable(false);

        JScrollPane sp = new JScrollPane(ta);

        frame.add(sp, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void groupStatistics() {
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Статистика по групі");
        frame.setSize(300, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.setBackground(Color.decode("#cce6ff"));
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
        buttonPanel.setBackground(Color.decode("#cce6ff"));
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
                    showGroupInfo(frame, stor.groups[number]);
                    select = true;
                    break;
                }
            }
            if(!select){
                JOptionPane.showMessageDialog(frame, "Потрібно обрати групу перед натисканням кнопки");
            }
        });
    }

    private void groupPrice() {
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setName("Ціни по групах");
        frame.setSize(300, 400);

        JLabel lb = new JLabel("Ціни в групах");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.decode("#cce6ff"));
        titlePanel.add(lb);
        frame.add(titlePanel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for(int i=0; i<stor.groups.length; i++){
            JLabel lbl = new JLabel(String.format("%s: %.2f грн", stor.groups[i].getName(), stor.groups[i].priceForItems()));
            lbl.setFont(new Font("Verdana", Font.PLAIN, 20));
            panel.add(lbl);
        }

        JScrollPane sp = new JScrollPane(panel);
        frame.add(sp, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void showGroupInfo(JFrame frame, GroupOfItems gr){
        frame.setVisible(false);
        JFrame frame2 = new JFrame();
        frame2.setSize(500, 300);

        JLabel lb = new JLabel("Інформація по групі");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.decode("#cce6ff"));
        titlePanel.add(lb);
        frame2.add(titlePanel, BorderLayout.NORTH);

        JTextArea ta = new JTextArea(gr.getInfoAboutItems());
        ta.setFont(new Font("Verdana", Font.BOLD, 10));
        ta.setEditable(false);

        JScrollPane sp = new JScrollPane(ta);

        frame2.add(sp, BorderLayout.CENTER);
        frame2.setVisible(true);
    }

    private void increaseAmount(JFrame frame, GroupOfItems gr) {
            frame.getContentPane().removeAll();
            JSpinner[] spiners = new JSpinner[gr.items.length];

            JLabel lb = new JLabel("Додавання товару");
            lb.setFont(new Font("Verdana", Font.BOLD, 20));
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            titlePanel.setBackground(Color.decode("#cce6ff"));
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
            buttonPanel.setBackground(Color.decode("#cce6ff"));
            JButton btn = new JButton("Додати");
            btn.setFont(new Font("Verdana", Font.BOLD, 20));
            btn.setPreferredSize(new Dimension(200, 40));
            buttonPanel.add(btn);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);

            btn.addActionListener(e -> {
                for(int i = 0; i < spiners.length; i++){
                    try {
                        gr.items[i].increaseAmount((Integer) spiners[i].getValue());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                frame.setVisible(false);
                totalPrice.setText(String.format("Ціна всіх товарів: %.2f", + stor.getTotalPrice()));
            });
    }

    private void decreaseAmount(JFrame frame, GroupOfItems gr) {
        frame.getContentPane().removeAll();
        JSpinner[] spiners = new JSpinner[gr.items.length];

        JLabel lb = new JLabel("Списання товару");
        lb.setFont(new Font("Verdana", Font.BOLD, 20));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.decode("#cce6ff"));
        titlePanel.add(lb);
        frame.add(titlePanel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (int i = 0; i < gr.items.length; i++) {
            JPanel item = new JPanel(new GridLayout(1, 2));
            item.setPreferredSize(new Dimension(250, 20));
            item.add(new JLabel(gr.items[i].getName()));

            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, gr.items[i].getAmount(), 1));
            spinner.setPreferredSize(new Dimension(80, 30));
            item.add(spinner);
            spiners[i] = spinner;

            panel.add(item);
        }

        JScrollPane sp = new JScrollPane(panel);
        sp.setPreferredSize(new Dimension(250, 200));
        frame.add(sp, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#cce6ff"));
        JButton btn = new JButton("Списати");
        btn.setFont(new Font("Verdana", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(200, 40));
        buttonPanel.add(btn);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        btn.addActionListener(e -> {
            for(int i = 0; i < spiners.length; i++){
                try {
                    gr.items[i].decreaseAmount((Integer) spiners[i].getValue());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            frame.setVisible(false);
            totalPrice.setText(String.format("Ціна всіх товарів: %.2f", + stor.getTotalPrice()));
        });
    }
}
