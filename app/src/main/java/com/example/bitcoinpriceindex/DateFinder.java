package com.example.bitcoinpriceindex;

public class DateFinder {

    private static int[] daysOfMonth = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static int[] result = new int[3];

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    public static int[] findDateBefore(int year, int month, int day, int daysBefore) {
        daysOfMonth[1] = isLeapYear(year) ? 29 : 28;
        for (int i = 0; i < daysBefore; ++i) {
            --day;
            if (day == 0) {
                --month;
                if (month == 0) {
                    --year;
                    daysOfMonth[1] = isLeapYear(year) ? 29 : 28;
                    month = 12;
                }
                day = daysOfMonth[month - 1];
            }
        }
        result[0] = year;
        result[1] = month;
        result[2] = day;
        return result;
    }

    public static String findNextDay(String currentDate) {//yyyy-mm-dd
        int year = Integer.parseInt(currentDate.substring(0, 4));
        int month = Integer.parseInt(currentDate.substring(5, 7));
        int day = Integer.parseInt(currentDate.substring(8, 10));
        daysOfMonth[1] = isLeapYear(year) ? 29 : 28;
        ++day;
        if (day > daysOfMonth[month - 1]) {
            day = 1;
            ++month;
            if (month > 12) {
                month = 1;
                ++year;
            }
        }
        currentDate = year + "-"
                + (month > 9 ? month : "0" + month) + "-"
                + (day > 9 ? day : "0" + day);
        return currentDate;
    }

}
