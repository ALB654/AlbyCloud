package de.alb654.AlbySpigotPlugin.utils;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 13.7.2020
 */

public class Utils {

    public static long timeStringToMillis(String timeString) {
        String[] params = timeString.split(" ");
        long time = 0;

        try {

            for (String param : params) {
                if (param.equalsIgnoreCase("p") || param.equalsIgnoreCase("permanent"))
                    return -1;
                else if (param.endsWith("d")) {
                    long days = Long.parseLong(param.replace("d", ""));
                    time += days * 1000 * 60 * 60 * 24;
                } else if (param.endsWith("h")) {
                    long hours = Long.parseLong(param.replace("h", ""));
                    time += hours * 1000 * 60 * 60;
                } else if (param.endsWith("m")) {
                    long minutes = Long.parseLong(param.replace("m", ""));
                    time += minutes * 1000 * 60;
                } else if (param.endsWith("s")) {
                    long seconds = Long.parseLong(param.replace("s", ""));
                    time += seconds * 1000;
                } else
                    return -2;
            }

        } catch (NumberFormatException e) {
            return -2;
        }
        return time;
    }

    public static String toTimeString(long millis) {
        return toTimeString(millis, "d", "h", "m", "s");
    }

    public static String toTimeString(long millis, String daySuffix, String hourSuffix, String minuteSuffix, String secondSuffix) {

        if(millis == -1)
            return "PERMANENT";

        int days = 0, hours = 0, minutes = 0, seconds = 0;

        final int dayMillis = 1000 * 60 * 60 * 24;
        final int hourMillis = dayMillis / 24;


        while (millis >= dayMillis) {
            days++;
            millis -= dayMillis;
        }
        while (millis >= hourMillis) {
            hours++;
            millis -= hourMillis;
        }
        while (millis >= hourMillis / 60) {
            minutes++;
            millis -= hourMillis / 60;
        }
        while (millis >= hourMillis / 60 / 60) {
            seconds++;
            millis -= hourMillis / 60 / 60;
        }

        String timeString = "";
        if(days == 0) {
            timeString = timeString.concat(seconds + secondSuffix);
        }

        timeString = hours + hourSuffix + " " + minutes + minuteSuffix + " " + timeString;
        if(days != 0){
            timeString = days + daySuffix + " " + timeString;
        }

        return timeString;
    }

}
