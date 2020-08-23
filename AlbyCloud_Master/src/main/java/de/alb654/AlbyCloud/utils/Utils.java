package de.alb654.AlbyCloud.utils;


import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.screens.ScreenManager;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 15.10.2019.
 */
public class Utils {

    public static void printStartBanner() {
        System.out.println(" ");
        System.out.println(ConsoleColor.ANSI_CYAN.Id() +" ____  _     ____ ___  _" + ConsoleColor.ANSI_RESET.Id() + " ____  _     ____  _     ____ ");
        System.out.println(ConsoleColor.ANSI_CYAN.Id() +"/  _ \\/ \\   /  _ \\\\  \\//" + ConsoleColor.ANSI_RESET.Id() + "/   _\\/ \\   /  _ \\/ \\ /\\/  _ \\");
        System.out.println(ConsoleColor.ANSI_CYAN.Id() +"| / \\|| |   | | // \\  / " + ConsoleColor.ANSI_RESET.Id() + "|  /  | |   | / \\|| | ||| | \\|");
        System.out.println(ConsoleColor.ANSI_CYAN.Id() +"| |-||| |_/\\| |_\\\\ / /  " + ConsoleColor.ANSI_RESET.Id() + "|  \\__| |_/\\| \\_/|| \\_/|| |_/|");
        System.out.println(ConsoleColor.ANSI_CYAN.Id() +"\\_/ \\|\\____/\\____//_/   " + ConsoleColor.ANSI_RESET.Id() + "\\____/\\____/\\____/\\____/\\____/");
        System.out.println(" ");
        System.out.println(ConsoleColor.ANSI_RESET.Id() + "Developed by" + ConsoleColor.ANSI_CYAN.Id() + " ALB654" + ConsoleColor.ANSI_RESET.Id());
        System.out.println(" ");
    }

    public static void writeToFile(File f, String line, boolean override) {
        try (PrintWriter fw = new PrintWriter(new BufferedWriter(new FileWriter(f, !override)))) {

            fw.write(line);
            fw.flush();
        } catch (IOException e) {
            Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN).addError(e, "Schreiben von '" + line + "' in ein File");
        }
    }

    public static String toTimeString(long millis){

        long seconds = 0;

        while(millis >= 1000) {
            seconds++;
            millis -= 1000;
        }

        long minutes = 0;

        while(seconds >= 60) {
            minutes++;
            seconds -= 60;
        }

        int hours = 0;

        while(minutes >= 60) {
            hours++;
            minutes-= 60;
        }

        int days = 0;

        while(hours >= 24) {
            days++;
            hours -= 24;
        }

        // 1 Tag 2 Stunden 55 Minuten 36 Sekunden

        if(days == 0) {
            if(hours == 0) {
                if(minutes == 0) {
                    return seconds + " Sekunden";
                }
                return minutes + " Minuten " + seconds + " Sekunden";
            }
            return hours + " Stunden " + minutes + " Minuten " + seconds + " Sekunden";
        }
        return days + " Tage " + hours + " Stunden " + minutes + " Minuten " + seconds + " Sekunden";
    }

}
