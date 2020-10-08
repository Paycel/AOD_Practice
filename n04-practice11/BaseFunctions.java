package com.company;

interface BaseFunctions{
    void insert(Node node);
    void insert(String name, String code);
    void insert(String name);
    void delete(String name, String code);
    Node find(String name, String code);
}
