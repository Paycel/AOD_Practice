package com.company;

public class HashTable extends Realization implements BaseFunctions{
    private Node[] trie;

    public HashTable(Node[] trie) {
        this.trie = trie;
    }

    public HashTable() {
        trie = new Node[26];
    }

    @Override
    public void insert(Node node) {
        if (node.getCode() == null) node.setCode(generateCode(node.getName()));
        if (trie[hash(node.getCode())] == null) trie[hash(node.getCode())] = node;
        else trie[hash(node.getCode())].insert(node);
    }

    @Override
    public void insert(String name, String code){
        insert(new Node(name, code));
    }

    @Override
    public void insert(String name){
        insert(new Node(name, generateCode(name)));
    }

    @Override
    public void delete(String name, String code) {
        Node node = trie[hash(code)];
        if (node.getCode().equals(code) && node.getName().equals(name)) node = node.getNext();
        else node.delete(name, code);
    }

    @Override
    public Node find(String name, String code) {
        Node link = trie[hash(code)];
        if (link.getCode().equals(code) && link.getName().equals(name)) return link;
        return link.find(name, code);
    }

    protected Node[] getTrie() {
        return trie;
    }
}
