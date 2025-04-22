/*
1.Реалізувати графічний інтерфейс користувача
2.Збереження даних в файл/файли. Один з варіантів: Існує файл в якому знаходяться назви всіх груп товарів.
Товари з кожної групи товарів знаходяться в окремому файлі.
3.Назва товару - унікальна (не може зустрічатися більше в жодній групі товарів).
4.Назва групи товарів - унікальна.
5.Реалізувати додавання/редагування/видалення групи товарів - при видаленні групи товарів, видаляти і всі товари.
6.Реалізувати додавання/редагування/видалення товару в групу товарів (мається на увазі назва, опис, виробник, ціна за одиницю).
7.Реалізувати інтерфейс додавання товару (прийшло на склад крупи гречаної - 10 штук), інтерфейс списання товару
(продали крупи гречаної - 5 шт.)
8.Пошук товару.
9.Вивід статистичних даних: вивід всіх товарів з інформацією по складу, вивід усіх товарів по групі товарів з інформацією,
загальна вартість товару на складі (кількість * на ціну), загальна вартість товарів в групі товарів.
 */

package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;

// add amount and delete items
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Item {
    private String name;
    private String description;
    private String producer;
    private int amount;
    private double price;
    private String groupOfItems;

    Item(String name, String description, String producer, int amount, double price, String groupOfItems) {
        this.name = name;
        this.description = description;
        this.producer = producer;
        this.amount = amount;
        this.price = price;
        this.groupOfItems = groupOfItems;
    }

    public void increaseAmount(int number) {
        amount += number;
    }

    public void decreaseAmount(int number) {
        if (amount >= number) {
            amount -= number;
        }
    }

    public String toString() {
        return name+" description: "+description+" producer: "+producer +" amount in stock: "+amount+" price: "+price+" belongs to: "+groupOfItems;
    }

    public double getTotalPrice() {
        return amount * price;
    }

    //чи точно треба, якщо в нас унікальна назва, тобто однакових товарів немає
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return amount == item.amount && Double.compare(price, item.price) == 0 && Objects.equals(name, item.name) && Objects.equals(description, item.description) && Objects.equals(producer, item.producer) && Objects.equals(groupOfItems, item.groupOfItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, producer, amount, price, groupOfItems);
    }
}

@Setter
@Getter
class GroupOfItems {
    public Item[] items;
    private String name;
    private String description;
//    delete array of items
    GroupOfItems(String name, String description) {
        this.items = new Item[]{};
        this.name = name;
//        this.items= FileData.getAllItemsForGroup(name);
        this.description = description;
    }

    public void setItems(Item[] items) throws IOException {
        this.items = items;
        for (Item item : items) {
            FileData.addItem(item);
        }
    }

    public String toString(){
        return "This group of " + name + " has " + items.length + " items";
    }

    public void setGroupForItems(String groupName) throws IOException {
        for (Item item : items) {
            FileData.changeItemGroup(item.getGroupOfItems(), groupName);
            item.setGroupOfItems(groupName);
        }
    }

    public void removeGroup() throws IOException {
        for (Item item : items) {
            FileData.deleteItem(item.getName());
        }
    }

// принтити в консоль взагалі щось треба?
    public void getInfoAboutItems() {
        if (items.length == 0) {
            System.out.println("No items found");
            return;
        }
        for (Item item : items) {
            System.out.println(item);
        }
    }

    public double priceForItems() {
        double price = 0;
        for (Item item : items) {
            price += item.getTotalPrice();
        }
        return price;
    }

    public void editItem(String itemName, Item item) throws IOException {
        int indexOfItem = findItem(itemName);
        if (indexOfItem == -1) {
            System.out.println("Item " + itemName + " not found");
            return;
        }
        int index = Utils.getIndexOfEntity(items, items[indexOfItem]);
        FileData.editItem(items[index].getName(), item);
        items[index] = item;
    }

    public void removeItem(String itemName) throws IOException {
        int indexOfItem = findItem(itemName);
        if (indexOfItem == -1) {
            System.out.println("Item " + itemName + " not found");
            return;
        }
        int index = Utils.getIndexOfEntity(items, items[indexOfItem]);
        FileData.deleteItem(items[index].getName());
        items = popItem(index).clone();
    }

    public void addItem(Item item) throws IOException {
        items = appendItem(item).clone();
        FileData.addItem(item);
    }

    public int findItem(String name) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private Item [] appendItem(Item item) {
        Item [] newItems = new Item[items.length + 1];
        System.arraycopy(items, 0, newItems, 0, items.length);
        newItems[items.length] = item;
        return newItems;
    }

    private Item [] popItem(int index) {
        Item [] newItems = new Item[items.length - 1];
        System.arraycopy(items, 0, newItems, 0, index);
        System.arraycopy(items, index + 1, newItems, index, items.length - index - 1);
        return newItems;
    }

}

class FileData {
    static File file = new File("data.txt");
    static BufferedWriter bw;

    static {
        try {
            bw = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) throws IOException {
//        addItem(new Item("Grechka", "", "Svoya Liniya", 19, 50, "Grechka@Food"));
//        addItem(new Item("Grechka2", "", "Svoya Liniya", 19, 50, "Grechka@Food"));
//        addItem(new Item("Grechka3", "", "Svoya Liniya", 19, 50, "Grechka@FoodFOOD"));
//        changeItemGroup("Grechka@Food", "GrechkaGrechkaGrechka");
//        changeItemGroup("GrechkaGrechkaGrechka1", "GrechkaGrechkaGrechka2");
//        changeItemGroup("GrechkaGrechkaGrechka", "Grechka");
//        deleteItem("Grechka");
//    }

    public static void addItem(Item item) throws IOException {
        String json = new ObjectMapper().writeValueAsString(item);
        bw.write(json);
        bw.newLine();
        bw.flush();
    }

    public static void editItem(String itemName, Item newItem) throws IOException {
        String fileContent = "";
        Scanner localScanner = new Scanner(file);
        while (localScanner.hasNextLine()) {
            Item item = getItemFromJSON(localScanner.nextLine());
            if (item.getName().equals(itemName)) {
                item = newItem;
            }
            String json = new ObjectMapper().writeValueAsString(item);
            fileContent += json + "\n";
        }
        rewriteFileContent(fileContent);
    }

    public static void deleteItem(String itemName) throws IOException {
        String fileContent = "";
        Scanner localScanner = new Scanner(file);
        while (localScanner.hasNextLine()) {
            Item item = getItemFromJSON(localScanner.nextLine());
            if (item.getName().equals(itemName)) {
                continue;
            }
            String json = new ObjectMapper().writeValueAsString(item);
            fileContent += json + "\n";
        }
        rewriteFileContent(fileContent);
    }

    public static void changeItemGroup(String itemGroup, String newGroupName) throws IOException {
        String fileContent = "";
        Scanner localScanner = new Scanner(file);
        while (localScanner.hasNextLine()) {
            Item item = getItemFromJSON(localScanner.nextLine());
            if (item.getGroupOfItems().equals(itemGroup)) {
                item.setGroupOfItems(newGroupName);
            }
            String json = new ObjectMapper().writeValueAsString(item);
            fileContent += json + "\n";
        }
        rewriteFileContent(fileContent);
    }


    private static Item getItemFromJSON(String itemJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Item item = mapper.readValue(itemJson, Item.class);
        return item;
    }

    private static void rewriteFileContent(String fileContent) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        fileOutputStream.write(fileContent.getBytes());
        fileOutputStream.close();
    }
}
/* class FileData {
   Item [] items;
   private String getAllInfo() -> весь файл
   public Item [] getAllItemsForGroup(String groupName) -> масив айтемів заданої групи ->
   { String allInfo = getAllInfo(), String [] itemsJSON =  allInfo.split(" \n"), -> json["groupName"] == groupName }

   public String [] getAllUniqueGroupName -> повертає масив об єктів груп
}*/
@Getter
@Setter
class Storage {
    public GroupOfItems[] groups;
    private String name;
    // не треба приймати масив груп
    Storage(String name) {

        this.name = name;
        this.groups = new GroupOfItems[]{};
//        this.groups = FileData.getAllItemsForGroup()
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (GroupOfItems group : groups) {
            totalPrice += group.priceForItems();
        }
        return totalPrice;
    }

    public void getAllInfoAboutStorage(){
        if (groups.length == 0) {
            System.out.println("No groups found");
        }
        for (GroupOfItems group : groups) {
            group.getInfoAboutItems();
        }
    }

    public void editGroup(String groupName, String newGroupName) throws IOException {
        int indexOfItem = findGroup(groupName);
        if (indexOfItem == -1) {
            System.out.println("Group " + groupName + " not found");
            return;
        }
        int index = Utils.getIndexOfEntity(groups, groups[indexOfItem]);
        groups[index].setGroupForItems(newGroupName);
        groups[index].setName(newGroupName);

    }

    public void addGroup(GroupOfItems newGroup) {
        groups = appendGroupOfItem(newGroup).clone();
    }

    public void removeGroup(String groupName) throws IOException {
        int indexOfGroup = findGroup(groupName);
        if (indexOfGroup == -1) {
            System.out.println("Group " + groupName + " not found");
            return;
        }
        int index = Utils.getIndexOfEntity(groups, groups[indexOfGroup]);
        groups[index].removeGroup();
        groups = popGroupOfItem(index).clone();
    }

    private GroupOfItems[] popGroupOfItem(int index) {
        GroupOfItems [] newGroupOfItems = new GroupOfItems[groups.length - 1];
        System.arraycopy(groups, 0, newGroupOfItems, 0, index);
        System.arraycopy(groups, index + 1, newGroupOfItems, index, groups.length - index - 1);
        return newGroupOfItems;
    }

    public int findGroup(String groupName) {
        for (int i = 0 ; i < groups.length; i++) {
            if (groups[i].getName().equals(groupName)) {
                return i;
            }
        }
        return -1;
    }

    private GroupOfItems [] appendGroupOfItem(GroupOfItems group) {
        GroupOfItems [] newGroups = new GroupOfItems[groups.length + 1];
        System.arraycopy(groups, 0, newGroups, 0, groups.length);
        newGroups[groups.length] = group;
        return newGroups;
    }
}

public class Lapka2 {
    /*public static void main(String [] args) throws IOException {
        Item[] items = new Item[3];
        items[0] = new Item("Product0", "", "Roshen", 1, 20, "Name");
        items[1] = new Item("Product1", "", "Roshen", 1, 20, "Name");
        items[2] = new Item("Product2", "", "Roshen", 1, 20, "Name");
        GroupOfItems groupOfItems = new GroupOfItems("Name", "Description");
        groupOfItems.addItem(items[0]);
        groupOfItems.addItem(items[1]);
        groupOfItems.addItem(items[2]);
//        FileData.
        Item [] items1 = new Item[2];
        items1[0] = new Item("Grechka", "", "Svoya Liniya", 19, 50, "Grechka@Food");
        items1[1] = new Item("Pomidor\uD83C\uDF45", "", "Ukrainian farmers", 5, 150, "Grechka@Food");
        GroupOfItems groupOfItems1 = new GroupOfItems("Grechka@Food", "Grechka@Food");
        groupOfItems1.addItem(items1[0]);
        groupOfItems1.addItem(items1[1]);
        Storage storage = new Storage("Grechka Storage");
        storage.addGroup(groupOfItems);
        storage.addGroup(groupOfItems1);
        storage.editGroup("Name", "Baklazhan");
        storage.editGroup("Baklazhan", "BaklazhanBaklazhan");
        groupOfItems.editItem("", null);
        groupOfItems1.editItem("Grechka", new Item("GrechkaGrechk1123a", "", "Svoya Liniya", 19, 50, "Grechka@Food"));
        storage.removeGroup("BaklazhanBaklazhan");
//        storage.editGroup("groupOfItems", "Grechka");
//        storage.editGroup("Name", "Pomidor\uD83C\uDF45");
//        storage.getAllInfoAboutStorage();
//        storage.removeGroup("groupOfItems");
//        storage.removeGroup("Pomidor\uD83C\uDF45");
         System.out.println("AFTER DELETE");
//        storage.addGroup(new GroupOfItems(new Item[]{new Item("\uD83C\uDF46", "", "Ukrainian \uD83C\uDF46", 10, 100, "\uD83C\uDF46 group")}, "\uD83C\uDF46 group", ""));
        storage.getAllInfoAboutStorage();
//        storage.removeGroup("groupOfItems");
//        storage.editGroup("Pomidor\uD83C\uDF45", "Ukrainian \uD83C\uDF45");
//        System.out.println("AFTER EDIT");
//        storage.getAllInfoAboutStorage();
//        System.out.println("total price: " + storage.getTotalPrice());
//        groupOfItems.getInfoAboutItems();
//        groupOfItems.removeItem("Product0");
//        groupOfItems.getInfoAboutItems();
//        groupOfItems.getInfoAboutItems();
//        groupOfItems.addItem(new Item("Product0", "", "Roshen", 1, 20, "Name"));
//        groupOfItems.getInfoAboutItems();
//        groupOfItems.removeItem("Product0");
//        groupOfItems.removeItem("Product1");
//        groupOfItems.removeItem("Product2");
//        groupOfItems.removeItem("Product3");
//        groupOfItems.removeItem("Product0");
//        groupOfItems.removeItem("Product2");
//        groupOfItems.removeItem("Product3");
//        groupOfItems.removeItem("Product0");
//
//        System.out.println("AFTER DELETE");
//        groupOfItems.getInfoAboutItems();
//        groupOfItems.removeItem("Product0");
//        groupOfItems.removeItem("Producfsdfd");
//        System.out.println("AFTER DELETEx2");
//        groupOfItems.getInfoAboutItems();
//        groupOfItems.addItem(new Item("Grechka", "For GOATs",
//                "Nasha Liniya", 100, 25, "Crops"));
//        groupOfItems.editItem("Grechka", new Item("Grechka", "For GOATs",
//                "Nasha Liniya", 100, 35, "Crops"));
//        System.out.println("AFTER EDIT");
//        groupOfItems.getInfoAboutItems();
//        groupOfItems.removeItem("Grechka1");
//        groupOfItems.removeItem("Grechka");
//        System.out.println("AFTER DELETE");
//        groupOfItems.getInfoAboutItems();
    }*/

}
