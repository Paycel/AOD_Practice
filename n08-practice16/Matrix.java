package com.company;

import java.util.*;

public class Matrix {
    private int size;                       // размерность матрицы
    private int[][] matrix, copy;           // матрица и её первоначальная копия
    private String bestWay;                 // минимальный путь по городам
    private int record = Integer.MAX_VALUE; // минимальный путь (длина)
    private final int infinity = -1;        // константа бесконечности (все элементы >= 0)

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
        this.size = matrix.length;
    }

    public void start() {
        int[][] newMatrix = new int[++size][size];  // крайние элементы будут хранить индексы строк и столбцов
        copy = new int[size - 1][size - 1];
        for (int i = 0; i < size - 1; i++) {
            newMatrix[i][size - 1] = i;
            newMatrix[size - 1][i] = i;
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, size - 1);
            System.arraycopy(matrix[i], 0, copy[i], 0, size - 1);
        }
        this.matrix = newMatrix;
        handle(this.matrix, "", 0);         // запуск рекурсивной функции для рассчёта
        System.out.println("Best way is " + bestWay);
        System.out.println("Total length = " + record);
    }

    private void removeRowColumn(int row, int column) {
        // удаление строки row и столбца column
        int[][] newMatrix = new int[--size][size];
        for (int i = 0; i <= size; i++)
            for (int j = 0; j <= size; j++)
                if (i != row && column != j)
                    newMatrix[i > row ? i - 1 : i][j > column ? j - 1 : j] = matrix[i][j];
        matrix = newMatrix;
    }

    private void addInfinity() {
        // установка бесконечности в ячейку, в строке и столбце которой нет бесконечности
        int size = this.size - 1;
        boolean[] infRow = new boolean[size];
        boolean[] infColumn = new boolean[size];
        Arrays.fill(infRow, false);
        Arrays.fill(infColumn, false);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (matrix[i][j] == infinity) {
                    infRow[i] = true;
                    infColumn[j] = true;
                }
        int notInf = 0;
        for (int i = 0; i < infRow.length; i++)
            if (!infRow[i]) {
                notInf = i;
                break;
            }
        for (int j = 0; j < infColumn.length; j++)
            if (!infColumn[j]) {
                matrix[notInf][j] = infinity;
                break;
            }
    }

    private int subtract() {
        // вычисление констант вычета
        int subtractSum = 0;                // сумма всех вычтенных значений
        int size = this.size - 1;
        int[] minRow = new int[size];       // массивы с минимальными элементами
        int[] minColumn = new int[size];
        Arrays.fill(minRow, Integer.MAX_VALUE);
        Arrays.fill(minColumn, Integer.MAX_VALUE);
        // обход матрицы
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j)
                // поиск минимума в строке
                if (matrix[i][j] < minRow[i] && matrix[i][j] != infinity)
                    minRow[i] = matrix[i][j];
            for (int j = 0; j < size; ++j) {
                // вычитание минимальных элементов из всех элементов строки, кроме бесконечности
                if (matrix[i][j] != infinity)
                    matrix[i][j] -= minRow[i];
                // поиск минимального элемента столбца
                if (matrix[i][j] < minColumn[j] && matrix[i][j] != infinity)
                    minColumn[j] = matrix[i][j];
            }
        }
        for (int j = 0; j < size; ++j)
            for (int i = 0; i < size; ++i)
                // вычитание минимальных элементов из всех элементов столбца, кроме бесконечности
                if (matrix[i][j] != infinity)
                    matrix[i][j] -= minColumn[j];
        // суммирование всех вычтенных значений
        for (int i : minRow)
            subtractSum += i;
        for (int i : minColumn)
            subtractSum += i;
        return subtractSum;
    }

    private void handle(int[][] m, String path, int bottomLimit) {
        // если размерность == 3 (по факту 2, т.к. 1 строку и столбец занимают индексы), то выход из рекурсии
        if (m.length == 3) {
            int i = m[0][0] == infinity ? 1 : 0;
            path += getUnit(m, 0, i) + getUnit(m, 1, 1 - i);
            int count = countWay(path); // длина пути
            String way = getWay(path); // путь
            if (count < record && getCountCities(way) == size - 1){
                record = count;
                bestWay = way + " -> 0";
            }
            return;
        }
        Matrix matrix = new Matrix(m);
        // вычет минимальных элементов, увеличение нижней границы
        bottomLimit += matrix.subtract();

        // узел с самым большим коэфф-том нуля
        Pair edge = matrix.findBestZeros();
        if (edge == null) return;

        // матрица №1
        Matrix newMatrix = new Matrix(matrix.getMatrix());
        // удаление строки и колонки с нулём
        newMatrix.removeRowColumn(edge.row, edge.column);
        String newPath = path;
        newPath += getUnit(matrix.getMatrix(), edge.row, edge.column);
        // добавление бесконечности в ячейку, не содержащей в строке и столбце бесконечности
        // это сделано во избежание преждевременных циклов
        newMatrix.addInfinity();
        // вызов рекурсии с содержащимся ребром удалённого нуля
        handle(newMatrix.getMatrix(), newPath, bottomLimit);

        // матрица №2
        newMatrix = matrix;
        // элемент с самым большим коэфф-том нуля приравнивается к бесконечности
        // тем самым эта матрица соответствует множеству путей, не содержащих данное ребро
        newMatrix.set(edge.row, edge.column, infinity);
        handle(newMatrix.getMatrix(), path, bottomLimit);
    }

    private Pair findBestZeros() {
        // поиск нулей матрицы и запись их коэфф-тов
        ArrayList<Pair> zeros = new ArrayList<>();
        ArrayList<Integer> coeffList = new ArrayList<>();
        int maxCoeff = 0;
        int size = this.size - 1;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (matrix[i][j] == 0) {
                    zeros.add(new Pair(i, j));
                    coeffList.add(getCoefficient(i, j));
                    maxCoeff = Integer.max(maxCoeff, coeffList.get(coeffList.size() - 1));
                }
        if (zeros.size() == 0) return null;
        // возврат нуля с самым большим коэфф-том
        return zeros.get(coeffList.indexOf(maxCoeff));
    }

    private int getCoefficient(int row, int column) {
        // вычисление коэфф-та = сумме минимальных элементов строки и столбца, кроме самого элемента
        int rmin, cmin;
        rmin = cmin = Integer.MAX_VALUE;
        // обход строки и столбца
        for (int i = 0; i < size - 1; i++) {
            if (i != row && matrix[i][column] != infinity)
                rmin = Integer.min(rmin, matrix[i][column]);
            if (i != column && matrix[row][i] != infinity)
                cmin = Integer.min(cmin, matrix[row][i]);
        }
        if (rmin == Integer.MAX_VALUE) rmin = 0;
        if (cmin == Integer.MAX_VALUE) cmin = 0;
        return rmin + cmin;
    }

    public void print() {
        // вывод матрицы
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1; j++)
                System.out.print(matrix[i][j] + "\t");
            System.out.println();
        }
        System.out.println();
    }

    public int[][] getMatrix() {
        return matrix;
    }

    private void set(int i, int j, int value) {
        this.matrix[i][j] = value;
    }

    private String getUnit(int[][] matrix, int i, int j) {
        // возвращает "первоначальные" индексы ячейки в виде (row, column)
        return "(" + matrix[i][matrix.length - 1] + ", " + matrix[matrix.length - 1][j] + ")";
    }

    private String getWay(String path) {
        // преобразование пути от вида (0, 2)(...) к 0 -> 2 -> ...
        StringTokenizer tokens = new StringTokenizer(path, "(), ");
        String result = "";
        ArrayList<Pair> way = new ArrayList<>();
        while (tokens.hasMoreTokens())
            way.add(new Pair(Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken())));
        Pair p = new Pair(-1, -1);
        while (!way.isEmpty()) {
            int size1 = way.size();
            for (Pair pair : way) {
                if (p.row == -1 && pair.row == 0) {
                    p.column = pair.column;
                    p.row = pair.row;
                    way.remove(pair);
                    break;
                } else if (p.column == pair.row){
                    p.column = pair.column;
                    p.row = pair.row;
                    way.remove(pair);
                    break;
                }
            }
            if (size1 == way.size()) break;
            result += p.row + " -> ";
        }
        return result.substring(0, result.length() - 4);
    }

    private int countWay(String path){
        // подсчёт числа городов в пути
        int count = 0;
        StringTokenizer tokens = new StringTokenizer(path, "(), ");
        ArrayList<Pair> way = new ArrayList<>();
        while (tokens.hasMoreTokens())
            way.add(new Pair(Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken())));
        for (Pair pair: way)
            count += copy[pair.row][pair.column];
        return count;
    }

    private int getCountCities(String path){
        return new StringTokenizer(path, "-> ").countTokens();
    }

    private static class Pair {
        private int row;
        private int column;

        public Pair(int aKey, int aValue) {
            row = aKey;
            column = aValue;
        }
    }
}
