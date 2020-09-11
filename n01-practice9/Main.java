package com.company;


public class Main {

    public static void main(String[] args) {
        PriorityQueue queue = new PriorityQueue();
        Request request1 = new Request('P', 10, 1);
        Request request2 = new Request('M', 15, 2);
        Request request3 = new Request('K', 12, 3);
        Request request4 = new Request('P', 11, 4);
        queue.insert(request1);
        queue.insert(request2);
        queue.insert(request3);
        queue.insert(request4);
        queue.showRequests();
        queue.countTime();
    }
}
