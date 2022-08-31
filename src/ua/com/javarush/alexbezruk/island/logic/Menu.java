package ua.com.javarush.alexbezruk.island.logic;

import ua.com.javarush.alexbezruk.island.statistics.Statistics;
import ua.com.javarush.alexbezruk.island.terrain.Island;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public record Menu(Island island) {
    private static final String NUMBER_FORMAT_INFORMATION = "Введена информация неверного формата, повторите ввод";
    private static final String IO_INFORMATION = "Ошибка при чтении данных с консоли: ";
    private static final String CHOOSING_ACTION = "\nВыбери необходимое действие:";
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void output() {
        int number = 0;
        System.out.println(CHOOSING_ACTION);
        System.out.println("1 - Симуляция еще одного дня жизни острова");
        System.out.println("2 - Вывод статистики по острову");
        System.out.println("3 - Окончание симуляции жизни острова (выход из программы)");
        try {
            number = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            System.out.println(NUMBER_FORMAT_INFORMATION);
            output();
        } catch (IOException e) {
            System.err.println(IO_INFORMATION + e.getMessage());
            output();
        }
        switch (number) {
            case 1 -> {
            }
            case 2 -> statisticsOutput();
            case 3 -> System.exit(0);
            default -> {
                System.out.println(NUMBER_FORMAT_INFORMATION);
                output();
            }
        }
    }

    public void statisticsOutput() {
        int number = 0;
        Statistics statistics = new Statistics(island);
        System.out.println(CHOOSING_ACTION);
        System.out.println("1 - Вывод общей статистики по острову");
        System.out.println("2 - Вывод количества умерших и рожденных животных за сутки");
        System.out.println("3 - Вывод прироста животных со дня основания острова");
        System.out.println("4 - Возвращение в основное меню");
        try {
            number = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            System.out.println(NUMBER_FORMAT_INFORMATION);
            statisticsOutput();
        } catch (IOException e) {
            System.err.println(IO_INFORMATION + e.getMessage());
            statisticsOutput();
        }
        switch (number) {
            case 1 -> statistics.outputGeneral();
            case 2 -> statistics.outputNumberOfBornAndDeadAnimals();
            case 3 -> statistics.outputAnimalsGrowth();
            case 4 -> output();
            default -> {
                System.out.println(NUMBER_FORMAT_INFORMATION);
                statisticsOutput();
            }
        }
        output();
    }
}
