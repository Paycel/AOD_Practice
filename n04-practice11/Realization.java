package com.company;

import java.util.Random;

public abstract class Realization {
    protected static final String[] nameList = {
            "Milk", "Eggs", "Carrot", "Onion", "Cabbage", "Tea", "Coffee", "Bread", "Chocolate"
    };

    protected static String generateCode(String name) {
        StringBuilder code = new StringBuilder(Character.toLowerCase(name.charAt(0)) + "");
        for (int i = 0; i < 15; i++)
            code.append((char) (new Random().nextInt(26) + 'a'));
        return code.toString();
    }

    protected static int hash(String code) {
        return Character.toLowerCase(code.getBytes()[0]) - 'a';
    }

    protected static Node[] generateList() {
        Node[] n = new Node[26];
        for (int i = 0; i < 100; i++) {
            Node node = new Node(nameList[(int) (Math.random() * nameList.length)]);
            node.setCode(generateCode(node.getName()));
            if (n[hash(node.getCode())] == null) n[hash(node.getCode())] = node;
            else n[hash(node.getCode())].insert(node);
        }
        return n;
    }

    protected static void printTrie(HashTable table) {
        Node[] n = table.getTrie();
        for (Node node : n) System.out.print((node == null ? "null" : node.getName()) + "\t");
        System.out.println();
        for (Node node : n)
            if (node != null) {
                System.out.print(node.getName());
                Node next = node.getNext();
                while (next != null) {
                    System.out.print(" - " + next.getName());
                    next = next.getNext();
                }
                System.out.println();
            }
        System.out.println();
        for (Node node : n) System.out.print((node == null ? "null" : node.getCode()) + "\t");
        System.out.println();
        for (Node node : n)
            if (node != null) {
                System.out.print(node.getCode());
                Node next = node.getNext();
                while (next != null) {
                    System.out.print(" - " + next.getCode());
                    next = next.getNext();
                }
                System.out.println();
            }
        System.out.println("---------------------------------------");
    }
}
