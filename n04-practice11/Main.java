package com.company;

public class Main extends Realization{
    public static void main(String[] args) {
        HashTable table = new HashTable();
        table.insert("Carrot");
        table.insert("Test", "key1");
        table.insert("Apple", "key2");
        table.insert("Apple2", "key3");
        printTrie(table);
        table.delete("Apple", "key2");
        printTrie(table);
        System.out.println(table.find("Test", "key1"));
    }
}

