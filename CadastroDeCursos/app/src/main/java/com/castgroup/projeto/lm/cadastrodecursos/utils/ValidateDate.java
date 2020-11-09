package com.castgroup.projeto.lm.cadastrodecursos.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateDate {

    public static boolean compararDatas(String d1, String d2)
        throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date dInicio = sdf.parse(d1);
        Date dTermino = sdf.parse(d2);

        return (dTermino.after(dInicio));
    }

    public static boolean validateDate(String date) {
        String regex = "^(1[0-9]|0[1-9]|3[0-1]|2[0-9])\\/(0[1-9]|1[0-2])\\/[0-9]{4}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(date);

        if (date == null || date.isEmpty() || !m.matches()) {
            return false;
        }

        m.reset();

        if (m.find()) {
            String dateDetails[] = date.split("/");
            String day = dateDetails[1];
            String month = dateDetails[0];
            String year = dateDetails[2];
            if (validateMonthWithMaxDate(day, month)) {
                return false;
            } else if (isFebruaryMonth(month)) {
                if (isLeapYear(year)) {
                    return leapYearWith29Date(day);
                } else {
                    return notLeapYearFebruary(day);
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private static Boolean validateMonthWithMaxDate(String day, String month){
        return (day.equals("31") && (month.equals("4") || month.equals("6") || month.equals("9") || month.equals("11") || month.equals("04") || month.equals("06") || month.equals("09")));
    }

    private static Boolean isFebruaryMonth(String month){
        return (month.equals("02") || month.equals("2"));
    }

    private static Boolean isLeapYear(String year){
        return Integer.parseInt(year) % 4 == 0;
    }

    private static Boolean leapYearWith29Date(String day)
    {
        return !(day.equals("30") || day.equals("31"));
    }

    private static Boolean notLeapYearFebruary(String day) {
        return !(day.equals("29") || day.equals("30") || day.equals("31"));
    }

}

