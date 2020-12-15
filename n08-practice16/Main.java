package com.company;

public class Main {
    public static void main(String[] args) {
        // -1 соответствует бесконечности или М на диагонале матрицы
        Matrix matrix = new Matrix(new int[][]{
                {-1, 20, 18, 12, 8},
                {5, -1, 14, 7, 11},
                {12, 18, -1, 6, 11},
                {11, 17, 11, -1, 12},
                {5, 5, 5, 5, -1}
        });
        matrix.start();
    }
}

