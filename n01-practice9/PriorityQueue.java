package com.company;

import java.util.TreeSet;

class Request implements Comparable<Request>{
    private int time, number, priority;

    public Request(char category, int time, int number) {
        this.time = time;
        this.number = number;
        switch (category){
            case 'M': this.priority = 1; break;
            case 'K': this.priority = 2; break;
            case 'P': this.priority = 3; break;
        }
    }

    @Override
    public int compareTo(Request o) {
        if (priority == o.priority) return 1;
        return priority - o.priority;
    }

    @Override
    public String toString() {
        char category = ' ';
        switch (priority){
            case 1: category = 'M'; break;
            case 2: category = 'K'; break;
            case 3: category = 'P'; break;
        }
        return "Запрос {" +
                "Время выполнения - " + time +
                ", номер - " + number +
                ", категория - " + category + "}";
    }

    public int getPriority() {
        return priority;
    }

    public int getTime() {
        return time;
    }
}

public class PriorityQueue {

    private TreeSet<Request> list;

    public PriorityQueue() {
        list = new TreeSet<>();
    }

    public void insert(Request request){
        list.add(request);
    }

    public void showRequests(){
        for (Request request: list) System.out.println(request);
    }

    public void countTime(){
        int timeM = 0, timeK = 0, timeP = 0;
        for (Request request: list){
            if (request.getPriority() == 1) timeM += request.getTime();
            else if (request.getPriority() == 2) timeK += request.getTime();
            else timeP += request.getTime();
        }
        System.out.println("Менеджер - " + timeM + "\nКонтролер - " + timeK + "\nРабочий - " + timeP);
    }

}
