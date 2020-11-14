package com.example.centrummedyczne;

import java.util.Date;

public class FormatData {
    public static String reformatDate(Date date){

        String startDate = date.toString();
        String correctDate = "";
        String day = startDate.substring(8,10);
        String mm = startDate.substring(4,7);
        String month = "";
        switch (mm){
            case "Sep":
                month = "9";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mm);
        }
        String year = startDate.substring(startDate.length()-4);
        correctDate += day + "." + month + "." + year;

        return correctDate;
    }

    public static String reformatTime(Date date){
        String startTime = date.toString();
        String correctTime = startTime.substring(11,16);
        return correctTime;
    }

    public static String reformatDateTime(Date date){
        return reformatDate(date) + " " +reformatTime(date);
    }
}
