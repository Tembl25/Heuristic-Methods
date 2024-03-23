import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

public class FlexibleLoad {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите количество строк: ");
        int numRows = scanner.nextInt();
        System.out.print("Введите количество столбцов: ");
        int numCols = scanner.nextInt();

        // Получаем от пользователя диапазон случайных чисел
        System.out.print("Введите минимальное значение случайного числа: ");
        int minRandomValue = scanner.nextInt();
        System.out.print("Введите максимальное значение случайного числа: ");
        int maxRandomValue = scanner.nextInt();

        // Генерируем случайную матрицу
        int[][] tasks = generateRandomMatrix(numRows, numCols, minRandomValue, maxRandomValue);
        sortMatrixDescending(tasks);

        // Выводим матрицу задач
        System.out.println("Матрица задач случайная:");
        printMatrix(tasks);
        System.out.println();

        // Сортируем матрицу по возрастанию
        int[][] tasksAscending = Arrays.copyOf(tasks, tasks.length);
        sortMatrixAscending(tasksAscending);
        System.out.println("Матрица задач (по возрастанию):");
        printMatrix(tasksAscending);
        System.out.println();

        // Сортируем матрицу по убыванию
        int[][] tasksDescending = Arrays.copyOf(tasks, tasks.length);
        sortMatrixDescending(tasksDescending);
        System.out.println("Матрица задач (по убыванию):");
        printMatrix(tasksDescending);
        System.out.println();


        System.out.println("Решение. Оригинальная матрица");
        Result result1 = flexibleLoad(tasks);
        System.out.println("CPUs: " + Arrays.toString(result1.CPUs));
        System.out.println("Max: " + result1.max);
        System.out.println();
        System.out.println("==========");
        System.out.println("Алгоритм минимальных элементов");
        printMatrix(tasks);
        int[] scheduleOriginal = buildSchedule(tasks);
        System.out.println("Расписание выполнения задач (по убыванию):");
        System.out.println(Arrays.toString(scheduleOriginal));
        int maxLoadOriginal = findMax(scheduleOriginal);
        System.out.println("Максимальная загрузка процессора (по убыванию): " + maxLoadOriginal);
        System.out.println();

        System.out.println("Матрица по возрастанию");
        Result result2 = flexibleLoad(tasksAscending);
        System.out.println("CPUs: " + Arrays.toString(result2.CPUs));
        System.out.println("Max: " + result2.max);
        System.out.println();
        System.out.println("==========");
        System.out.println("Алгоритм минимальных элементов");
        printMatrix(tasksAscending);
        int[] scheduleAscending = buildSchedule(tasksAscending);
        System.out.println("Расписание выполнения задач (по возрастанию):");
        System.out.println(Arrays.toString(scheduleAscending));
        int maxLoadAscending = findMax(scheduleAscending);
        System.out.println("Максимальная загрузка процессора (по возрастанию): " + maxLoadAscending);
        System.out.println();

        System.out.println("Матрица по убыванию");
        Result result3 = flexibleLoad(tasksDescending);
        System.out.println("CPUs: " + Arrays.toString(result3.CPUs));
        System.out.println("Max: " + result3.max);
        System.out.println();
        System.out.println("==========");
        System.out.println("Алгоритм минимальных элементов");
        printMatrix(tasksDescending);
        int[] scheduleDescending = buildSchedule(tasksDescending);
        System.out.println("Расписание выполнения задач (по убыванию):");
        System.out.println(Arrays.toString(scheduleDescending));
        int maxLoadDescending = findMax(scheduleDescending);
        System.out.println("Максимальная загрузка процессора (по убыванию): " + maxLoadDescending);
        System.out.println();

        scanner.close();
    }

    static class Result {
        int[] CPUs;
        int max;

        Result(int[] CPUs, int max) {
            this.CPUs = CPUs;
            this.max = max;
        }
    }

    public static void sortMatrixAscending(int[][] matrix) {
        Arrays.sort(matrix, Comparator.comparingInt((int[] row) -> sumRow(row)));
    }

    public static void sortMatrixDescending(int[][] matrix) {
        Arrays.sort(matrix, Comparator.comparingInt((int[] row) -> sumRow(row)).reversed());
    }

    private static int sumRow(int[] row) {
        int sum = 0;
        for (int num : row) {
            sum += num;
        }
        return sum;
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

    public static Result flexibleLoad(int[][] matrix) {
        int[] sums = new int[matrix[0].length];
        int[] CPUs = new int[matrix[0].length];

        for (int[] str : matrix) {
            int[] step = new int[sums.length];
            for (int j = 0; j < sums.length; j++) {
                step[j] = str[j] + sums[j];
            }
            int indexOfMin = getIndexOfMin(step);

            System.out.println("Строка: " + Arrays.toString(str));
            System.out.println("Сумма до: " + Arrays.toString(sums));
            CPUs[indexOfMin] += str[indexOfMin];
            sums[indexOfMin] += str[indexOfMin];
            System.out.println("Сумма после: " + Arrays.toString(sums) + "\n");
        }

        int max = Arrays.stream(CPUs).max().getAsInt();
        return new Result(CPUs, max);
    }

    public static int getIndexOfMin(int[] arr) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
                index = i;
            }
        }
        return index;
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
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
}