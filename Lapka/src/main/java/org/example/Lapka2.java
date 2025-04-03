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
//            item.setGroup();
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
//            price += item.getPrice();
        }
        return price;
    }

    public void editItem(Item item) {
        int index = Utils.getIndexOfEntity(items, item);
        if (index == -1) {
            return;
        }
        items[index] = item;
    }

    public void removeItem(Item item) {
        int index = Utils.getIndexOfEntity(items, item);
        if (index == -1) {
            return;
        }

    }

    public void addItem(Item item) {

    }
//    private

}


public class Lapka2 {
    //comment try
}
