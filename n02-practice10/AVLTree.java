package com.company;

public class AVLTree {
    private Node root;

    public void insert(int key) {
        root = insert(root, key);
    }

    private Node insert(Node node, int key) {
        if (node == null) return new Node(key);
        else if (node.key > key) node.left = insert(node.left, key);
        else if (node.key < key) node.right = insert(node.right, key);
        else throw new RuntimeException("duplicate Key!");
        return rebalance(node);
    }

    private Node rebalance(Node z) {
        updateHeight(z);
        int balance = getBalance(z);
        if (balance > 1)
            if (height(z.right.right) > height(z.right.left)) z = rotateLeft(z);
            else {
                z.right = rotateRight(z.right);
                z = rotateLeft(z);
            }
        else if (balance < -1)
            if (height(z.left.left) > height(z.left.right)) z = rotateRight(z);
            else {
                z.left = rotateLeft(z.left);
                z = rotateRight(z);
            }
        return z;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node z = x.right;
        x.right = y;
        y.left = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node y) {
        Node x = y.right;
        Node z = x.left;
        x.left = y;
        y.right = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int height(Node n) {
        return n == null ? -1 : n.height;
    }

    public int getBalance(Node n) {
        return (n == null) ? 0 : height(n.right) - height(n.left);
    }

    private void printBinaryAVLTree(Node node, int level) {
        if (node != null) {
            printBinaryAVLTree(node.right, level + 1);
            for (int i = 0; i < level; i++) System.out.print("            ");
            System.out.println(node.key);
            printBinaryAVLTree(node.left, level + 1);
        }
    }

    public void printAVLTree(){
        printBinaryAVLTree(root,1);
    }
}
