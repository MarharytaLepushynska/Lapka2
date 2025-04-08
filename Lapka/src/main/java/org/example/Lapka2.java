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

// Item.setGroup
// Item.getPrice -> double
// Item.toEquals -> boolean
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
    GroupOfItems(Item [] items, String name, String description) {
        this.items = items;
        this.name = name;
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
//    public void getInfoAboutItems() {
//        for (Item item : items) {
//            System.out.println(item);
//        }
//    }

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
            return;
        }
        int index = Utils.getIndexOfEntity(items, items[indexOfItem]);
        items[index] = item;
    }

    public void removeItem(String itemName) {
        int indexOfItem = findItem(itemName);
        if (indexOfItem == -1) {
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

@Getter
@Setter
class Storage {
    private GroupOfItems[] groups;
    private String name;
    Storage(String name, GroupOfItems[] groups) {
        this.groups = groups;
        this.name = name;
    }

    public void editGroup(String groupName, String newGroupName) {
        int indexOfItem = findGroup(groupName);
        if (indexOfItem == -1) {
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
    //comment try
}
