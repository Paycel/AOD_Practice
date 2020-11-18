package com.company;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Huffman {
    private String uncompressed;
    private TreeSet<Node> alphabet;
    private static HashMap<Character, String> map = new HashMap<>();

    private static class Node implements Comparable<Node> {
        char ch;
        int count;
        double percent;
        Node right, left;
        int code;

        public Node(Character ch, int count, double percent, Node right, Node left) {
            this.ch = ch;
            this.count = count;
            this.percent = percent;
            this.right = right;
            this.left = left;
        }

        @Override
        public int compareTo(Node node) {
            return node.count - this.count == 0 ? 1 : node.count - this.count;
        }

        @Override
        public String toString() {
            //return String.format("%c %d %.2f %s %s %d", ch, count, percent, right, left, code);
            return String.format("%c %d", ch, code);
        }

        public void setCodes() {
            if (left != null) {
                left.code = 0;
                left.setCodes();
            }
            if (right != null) {
                right.code = 1;
                right.setCodes();
            }
        }

        public void createCodes(String code) {
            if (left != null)
                left.createCodes(code + this.code);
            if (right != null)
                right.createCodes(code + this.code);
            if (left == null && right == null)
                map.put(this.ch, code.substring(1) + this.code);
        }

        public Node find(String code) {
            if (code.length() == 0) return this;
            if (code.charAt(0) == '0')
                return left.find(code.substring(1));
            else
                return right.find(code.substring(1));
        }
    }

    public Huffman(String uncompressed) {
        this.alphabet = new TreeSet<>();
        this.uncompressed = uncompressed;
        init();
    }

    private void init() {
        // формирование алфавита
        for (String set : new HashSet<>(Arrays.asList(uncompressed.split("")))) {
            double count = 0;
            for (char ch : uncompressed.toCharArray()) if (set.charAt(0) == ch) count++;
            alphabet.add(new Node(set.charAt(0), (int) count, round((count / (double) uncompressed.length()), 2), null, null));
        }
        // формирование дерева
        while (alphabet.size() != 1) {
            Node node1 = alphabet.pollLast();
            Node node2 = alphabet.pollLast();
            Node newNode = new Node('\0', node1.count + node2.count, node1.percent + node2.percent, node1, node2);
            alphabet.add(newNode);
        }
        // установка кодов и их получение + вывод
        alphabet.first().setCodes();
        alphabet.first().createCodes("");
        System.out.println("Таблица ключей:");
        map.forEach((k, v) -> System.out.println("Символ: " + k + "\tКод: " + v));
        System.out.println("Закодированная строка: ");
        for (char ch : uncompressed.toCharArray()) System.out.printf("%s ", map.get(ch));
        System.out.println();
        double[] coeffs = getCoeffs();
        System.out.println("Кф. сжатия отн. ASCII = " + coeffs[0]);
        System.out.println("Кф. сжатия отн. равномерного кода = " + coeffs[1]);
        System.out.println("Средняя длина кода = " + coeffs[2] + " бит/символ");
        System.out.println("Дисперсия = " + coeffs[3]);
    }

    private double[] getCoeffs() {
        double[] coeffs = new double[4];
        // coeffs[0] - кф. сжатия относительно ASCII (8 бит/символ)
        // coeffs[1] - кф. сжатия относительно равномерного кода (5 бит/символ)
        // coeffs[2] - средняя длина кода (coeffs[2] бит/символ)
        // coeffs[3] - дисперсия (отклонение кодов)
        int length_ascii = 8 * uncompressed.length();
        int length_evenly = 5 * uncompressed.length();
        AtomicInteger length_huff = new AtomicInteger();
        // сумма: число символов * длину кода
        map.forEach((k, v) -> length_huff.addAndGet(alphabet.first().find(v).count * v.length()));
        // L_ASCII / L_HUFF
        coeffs[0] = round((double) length_ascii / (double) length_huff.get(), 3);
        // L_EVENLY / L_HUFF
        coeffs[1] = round((double) length_evenly / (double) length_huff.get(), 3);
        AtomicReference<Double> length_average = new AtomicReference<>((double) 0);
        // сумма: вероятность * длину кода
        map.forEach((k, v) -> length_average.updateAndGet(v1 -> (v1 + alphabet.first().find(v).percent * v.length())));
        coeffs[2] = round(length_average.get(), 3);
        AtomicReference<Double> dispersion = new AtomicReference<>((double) 0);
        // сумма: вероятность * (разность длин кодов)^2
        map.forEach((k, v) -> dispersion.updateAndGet(v1 -> (v1 + alphabet.first().find(v).percent * Math.pow(length_average.get() - v.length(), 2))));
        coeffs[3] = round(dispersion.get(), 4);
        return coeffs;
    }

    private static double round(double value, int newScale) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(newScale, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
