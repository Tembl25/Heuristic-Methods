import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class ScheduleAlgorithm {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите количество строк: ");
        int numRows = scanner.nextInt();
        System.out.print("Введите количество столбцов: ");
        int numCols = scanner.nextInt();

        System.out.print("Введите минимальное значение случайного числа: ");
        int minRandomValue = scanner.nextInt();
        System.out.print("Введите максимальное значение случайного числа: ");
        int maxRandomValue = scanner.nextInt();

        int[][] tasks = generateRandomMatrix(numRows, numCols, minRandomValue, maxRandomValue);

        System.out.println("Матрица задач:");
        printMatrix(tasks);
        System.out.println();

        System.out.println("Матрица задач (оригинальная):");
        printMatrix(tasks);
        System.out.println();

        int[] originalSchedule = buildSchedule(tasks);
        System.out.println("Расписание выполнения задач (оригинальная):");
        System.out.println(Arrays.toString(originalSchedule));

        // Находим максимальную загрузку процессора
        int maxLoadOriginal = findMax(originalSchedule);
        System.out.println("Максимальная загрузка процессора (оригинальная): " + maxLoadOriginal);
        System.out.println();

        // Сортируем матрицу по возрастанию
        int[][] tasksAscending = Arrays.copyOf(tasks, tasks.length);
        sortMatrixAscending(tasksAscending);
        System.out.println("Матрица задач (по возрастанию):");
        printMatrix(tasksAscending);
        System.out.println();

        // Строим расписание для матрицы, отсортированной по возрастанию
        int[] scheduleAscending = buildSchedule(tasksAscending);
        System.out.println("Расписание выполнения задач (по возрастанию):");
        System.out.println(Arrays.toString(scheduleAscending));

        // Находим максимальную загрузку процессора
        int maxLoadAscending = findMax(scheduleAscending);
        System.out.println("Максимальная загрузка процессора (по возрастанию): " + maxLoadAscending);
        System.out.println();

        // Сортируем матрицу по убыванию
        int[][] tasksDescending = Arrays.copyOf(tasks, tasks.length);
        sortMatrixDescending(tasksDescending);
        System.out.println("Матрица задач (по убыванию):");
        printMatrix(tasksDescending);
        System.out.println();

        // Строим расписание для матрицы, отсортированной по убыванию
        int[] scheduleDescending = buildSchedule(tasksDescending);
        System.out.println("Расписание выполнения задач (по убыванию):");
        System.out.println(Arrays.toString(scheduleDescending));

        // Находим максимальную загрузку процессора
        int maxLoadDescending = findMax(scheduleDescending);
        System.out.println("Максимальная загрузка процессора (по убыванию): " + maxLoadDescending);

        scanner.close();
    }

    public static int[][] generateRandomMatrix(int rows, int cols, int min, int max) {
        Random random = new Random();
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(max - min + 1) + min;
            }
        }
        return matrix;
    }

    public static void sortMatrixAscending(int[][] matrix) {
        Arrays.sort(matrix, Comparator.comparingInt((int[] row) -> sumRow(row)));
    }

    public static void sortMatrixDescending(int[][] matrix) {
        Arrays.sort(matrix, Comparator.comparingInt((int[] row) -> sumRow(row)).reversed());
    }

    public static int[] buildSchedule(int[][] tasks) {
        int numRows = tasks.length;
        int numCols = tasks[0].length;

        int[] processorLoad = new int[numCols];

        for (int i = 0; i < numRows; i++) {
            int minIndex = findMinIndex(tasks[i]);
            processorLoad[minIndex] += tasks[i][minIndex];
        }

        return processorLoad;
    }

    private static int sumRow(int[] row) {
        int sum = 0;
        for (int num : row) {
            sum += num;
        }
        return sum;
    }

    private static int findMinIndex(int[] array) {
        int minIndex = 0;
        int minValue = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    private static int findMax(int[] array) {
        int max = array[0];
        for (int num : array) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }
}
