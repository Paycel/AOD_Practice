package com.company;


public class Main {
    public static void main(String[] args) {
        int[] p = {5, 10, 3, 12, 5, 50, 6};
        Solution solve = new Solution(p);
        solve.printFor(Value.MAX);
        solve.printFor(Value.MIN);
    }

    enum Value {
        MIN, MAX
    }

    static class Solution {
        int n;
        int[] p;
        int[][] max, min, // хранение стоимостей: m[i,j] хранит число произведений в A[i..j]
                maxK, minK; // индексы K для оптимальной стоимости
        char symbol = 'A';

        /*
         Произведения матриц: A[i]A[i+1]..A[j] = A[i..j] (для сокращения)
         Мы исходим из того, что в m[i,j] на промежутке от i до j существует К такое, что его можно разбить на
         матрицы A[k] и A[k+1], тогда нужно найти A[i..k]*A[k+1..j], число скалярных произведений которого = p[i-1] * p[k] * p[j]
         Тогда стоимость m[i,j] = m[i,k] + m[k+1,j] + p[i-1] * p[k] * p[j]
         */
        public Solution(int[] p) {
            this.p = p;
            this.n = p.length - 1;
            max = new int[n][n];
            min = new int[n][n];
            maxK = new int[n][n];
            minK = new int[n][n];
            solve();
        }

        private void solve() {
            for (int i = 1; i < n; i++) {
                max[i][i] = 0;
                min[i][i] = 0;
            }
            for (int length = 2; length < n; length++)
                for (int i = 1; i < n - length + 1; i++) {
                    int j = i + length - 1;
                    max[i][j] = Integer.MIN_VALUE;
                    min[i][j] = Integer.MAX_VALUE;
                    for (int k = i; k <= j - 1; k++) {
                        int q_min = min[i][k] + min[k + 1][j] + p[i - 1] * p[k] * p[j];
                        int q_max = max[i][k] + max[k + 1][j] + p[i - 1] * p[k] * p[j];
                        if (q_min < min[i][j]) {
                            min[i][j] = q_min;
                            minK[i][j] = k;
                        }
                        if (q_max > max[i][j]) {
                            max[i][j] = q_max;
                            maxK[i][j] = k;
                        }
                    }
                }
        }

        public void printFor(Value value) {
            print(1, n - 1, value.equals(Value.MAX) ? maxK : minK);
            System.out.println();
            symbol = 'A';
        }

        private void print(int i, int j, int[][] m) {
            if (i == j) {
                System.out.print(symbol++);
                return;
            }
            System.out.print("(");
            print(i, m[i][j], m);
            print(m[i][j] + 1, j, m);
            System.out.print(")");
        }
    }
}

