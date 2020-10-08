package com.company;

public class Main {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(7000);
        tree.insert(5120);
        tree.insert(2);
        tree.insert(7777);
        tree.insert(1234);
        tree.printAVLTree();
    }
}

