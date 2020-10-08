package com.company;

public class Node extends Realization implements BaseFunctions{
    private String name;
    private String code;
    private Node next;

    public Node(String name) {
        this.name = name;
        this.next = null;
    }

    public Node(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public Node find(String name, String code){
        Node link = null;
        Node sol = this;
        if (sol.getCode().equals(code) && sol.getName().equals(name)) return sol;
        while (sol.getNext() != null) {
            sol = sol.getNext();
            if (sol.getCode().equals(code) && sol.getName().equals(name)) {
                link = sol;
                break;
            }
        }
        return link;
    }

    @Override
    public void insert(Node node){
        if (node.getCode() == null) node.setCode(generateCode(node.getName()));
        Node link = this;
        while (link.getNext() != null) link = link.getNext();
        link.setNext(node);
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
    public void delete(String name, String code){
        Node link = this;
        while (link.getNext() != null) {
            Node prev = link;
            link = link.getNext();
            if (link.getCode().equals(code) && link.getName().equals(name)) {
                prev.setNext(link.getNext());
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", next=" + next +
                '}';
    }
}
