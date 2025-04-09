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

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

// add amount and delete items
@Setter
@Getter
class Item{
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
    private Item[] items;
    private String name;
    private String description;
//    delete array of items
    GroupOfItems(Item [] items, String name, String description) {
        this.items = items;
        this.name = name;
//        this.items= FileData.getAllItemsForGroup(name);
        this.description = description;
    }

    public String toString(){
        return "This group of " + name + " has " + items.length + " items";
    }

    public void setGroupForItems(String groupName) {
        for (Item item : items) {
            item.setGroupOfItems(groupName);
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

    public void editItem(String itemName, Item item) {
        int indexOfItem = findItem(itemName);
        if (indexOfItem == -1) {
            System.out.println("Item " + itemName + " not found");
            return;
        }
        int index = Utils.getIndexOfEntity(items, items[indexOfItem]);
        items[index] = item;
    }

    public void removeItem(String itemName) {
        int indexOfItem = findItem(itemName);
        if (indexOfItem == -1) {
            System.out.println("Item " + itemName + " not found");
            return;
        }
        int index = Utils.getIndexOfEntity(items, items[indexOfItem]);
        items = popItem(index).clone();
    }

    public void addItem(Item item) {
        items = appendItem(item).clone();
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
    private GroupOfItems[] groups;
    private String name;
    // не треба приймати масив груп
    Storage(String name, GroupOfItems[] groups) {

        this.name = name;
        this.groups = groups;
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

    public void editGroup(String groupName, String newGroupName) {
        int indexOfItem = findGroup(groupName);
        if (indexOfItem == -1) {
            System.out.println("Group " + groupName + " not found");
            return;
        }
        int index = Utils.getIndexOfEntity(groups, groups[indexOfItem]);
        groups[index].setName(newGroupName);
        groups[index].setGroupForItems(newGroupName);
    }

    public void addGroup(GroupOfItems newGroup) {
        groups = appendGroupOfItem(newGroup).clone();
    }

    public void removeGroup(String groupName) {
        int indexOfGroup = findGroup(groupName);
        if (indexOfGroup == -1) {
            System.out.println("Group " + groupName + " not found");
            return;
        }
        int index = Utils.getIndexOfEntity(groups, groups[indexOfGroup]);
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
    public static void main(String [] args) {
//        Item[] items = new Item[3];
//        items[0] = new Item("Product0", "", "Roshen", 1, 20, "Name");
//        items[1] = new Item("Product1", "", "Roshen", 1, 20, "Name");
//        items[2] = new Item("Product2", "", "Roshen", 1, 20, "Name");
//        GroupOfItems groupOfItems = new GroupOfItems(items, "Name", "Description");
//        Item [] items1 = new Item[2];
//        items1[0] = new Item("Grechka", "", "Svoya Liniya", 19, 50, "Grechka@Food");
//        items1[1] = new Item("Pomidor\uD83C\uDF45", "", "Ukrainian farmers", 5, 150, "Grechka@Food");
//        GroupOfItems groupOfItems1 = new GroupOfItems(items1, "Grechka@Food", "Grechka@Food@Description");
//        Storage storage = new Storage("Grechka Storage", new GroupOfItems[]{groupOfItems, groupOfItems1});
//        storage.editGroup("groupOfItems", "Grechka");
//        storage.editGroup("Name", "Pomidor\uD83C\uDF45");
//        storage.getAllInfoAboutStorage();
//        storage.removeGroup("groupOfItems");
////        storage.removeGroup("Pomidor\uD83C\uDF45");
//        System.out.println("AFTER DELETE");
//        storage.addGroup(new GroupOfItems(new Item[]{new Item("\uD83C\uDF46", "", "Ukrainian \uD83C\uDF46", 10, 100, "\uD83C\uDF46 group")}, "\uD83C\uDF46 group", ""));
//        storage.getAllInfoAboutStorage();
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
    }

}
