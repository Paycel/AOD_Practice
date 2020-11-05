package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private LinkedList<Integer[]> tab;
    private int node_amount;
    private LinkedList<LinkedList<Integer>> connects;
    private LinkedList<LinkedList<Integer>> temp_connects;
    private Scanner scanner = new Scanner(System.in);
    private Integer[] sol;

    public Graph() {
        System.out.println("Узлы вводите неповторяющиеся (1-2 либо 2-1), вводить (2-2) не обязательно\n" +
                "Формат ввода 'Узел-узел-вес'");
        System.out.print("Введите количество узлов: ");
        this.node_amount = scanner.nextInt();
        this.sol = new Integer[node_amount];
        init();
    }

    private void init() {
        tab = new LinkedList<>();
        for (int i = 0; i < node_amount; i++)
            tab.add(new Integer[node_amount]);
        for (int i = 0; i < (node_amount * (node_amount - 1)) / 2; i++) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            int weight = scanner.nextInt();
            tab.get(--from)[--to] = weight;
            tab.get(to)[from] = weight;
            tab.get(from)[from] = 0;
        }
        tab.get(node_amount - 1)[node_amount - 1] = 0;
    }

    // to == NULL => вывод кратчайшей цепочки от from до to
    public void dijkstra(Integer from, Integer to) {
        // connects хранит связи между узлами
        connects = new LinkedList<>();
        temp_connects = new LinkedList<>();
        fill_nodes(connects);
        for (LinkedList list : connects) temp_connects.add((LinkedList<Integer>) list.clone());
        // sol хранит кратчайшие пути до i-го (они же "метки" W)
        Arrays.fill(sol, Integer.MAX_VALUE);
        sol[--from] = 0;
        solve_node(from);
        System.out.print("Кратчайшие пути от " + (from + 1) + " элемента: ");
        for (Integer integer : sol) System.out.print(integer + "\t");
        System.out.println();
        if (to != null) print_way(from, --to);
    }

    private void print_way(Integer from, Integer to) {
        ArrayList<Integer> way = get_way(from, to, sol[to]);
        way.add(++from);
        Collections.reverse(way);
        way.add(++to);
        System.out.println("Кратчайший путь: " + way.stream().map(String::valueOf)
                .collect(Collectors.joining(" -> ")));
    }

    private ArrayList<Integer> get_way(Integer from, Integer to, int weight) {
        ArrayList<Integer> way = new ArrayList<>();
        for (int cur_node : connects.get(to))
            if (weight - get_weight(cur_node, to) == sol[cur_node] && sol[cur_node] != 0) {
                way.add(cur_node + 1);
                way.addAll(get_way(from, cur_node, sol[cur_node]));
            }
        return way;
    }

    private void solve_node(int node) {
        for (int cur_node : temp_connects.get(node))
            if (get_weight(node, cur_node) + sol[node] < sol[cur_node])
                sol[cur_node] = get_weight(node, cur_node) + sol[node];
        del_node(node);
        for (int cur_node : sort(temp_connects.get(node)))
            solve_node(cur_node);
    }

    private void del_node(int node) {
        for (LinkedList<Integer> list : temp_connects)
            for (int cur_node : list)
                if (cur_node == node) {
                    list.remove(Integer.valueOf(cur_node));
                    break;
                }
    }

    private void fill_nodes(LinkedList<LinkedList<Integer>> connects) {
        for (int i = 0; i < node_amount; i++) {
            connects.add(new LinkedList<>());
            for (int j = 0; j < node_amount; j++)
                if (get_weight(i, j) != 0)
                    connects.get(i).add(j);
        }
    }

    private LinkedList<Integer> sort(LinkedList<Integer> arr) {
        arr.sort((integer, t1) -> {
            if (sol[integer].equals(sol[t1])) return 1;
            return sol[integer] - sol[t1];
        });
        return arr;
    }

    private int get_weight(int node1, int node2) {
        return tab.get(node1)[node2];
    }

    public void print_tab() {
        System.out.print("\t");
        for (int i = 0; i < node_amount; i++) System.out.print((i + 1) + "\t");
        System.out.println();
        int i = 0;
        for (Integer[] arr : tab) {
            System.out.print(++i + "\t");
            for (Integer integer : arr) System.out.print(integer + "\t");
            System.out.println();
        }
    }
}
