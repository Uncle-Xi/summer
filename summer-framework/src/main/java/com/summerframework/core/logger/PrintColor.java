package com.summerframework.core.logger;

/**
 * @description: print color
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class PrintColor {

    private static boolean IS_JAR = false;
    private static int lineNum = 0;
    private static int innerNum = 0;

    public static void setIsJar(boolean isJar){
        IS_JAR = isJar;
    }

    public static String[][] COLOR_ARR = {
            {"\033[30;1m", "\033[0m"},
//            {"\033[31;1m", "\033[0m"},
//            {"\033[32;1m", "\033[0m"},
//            {"\033[33;1m", "\033[0m"},
//            {"\033[34;1m", "\033[0m"},
//            {"\033[35;1m", "\033[0m"},
//            {"\033[36;1m", "\033[0m"},
            {"\033[37;1m", "\033[0m"},
//            {"\033[90;1m", "\033[0m"},
//            {"\033[31;42;1m", "\033[0m"},
//            {"\033[32;43;1m", "\033[0m"},
//            {"\033[33;44;1m", "\033[0m"},
//            {"\033[94;45;1m", "\033[0m"},
//            {"\033[95;36;1m", "\033[0m"},
//            {"\033[96;37;1m", "\033[0m"},
//            {"\033[97;30;1m", "\033[0m"},
//            {"\033[30;91;1m", "\033[0m"},
//            {"\033[31;92;1m", "\033[0m"},
//            {"\033[32;93;1m", "\033[0m"},
//            {"\033[33;94;1m", "\033[0m"},
//            {"\033[34;95;1m", "\033[0m"},
//            {"\033[35;96;1m", "\033[0m"},
//            {"\033[36;97;1m", "\033[0m"},
            {"\033[37;1m", "\033[0m"}
    };

    public static String getRed(){
        return IS_JAR? "" : "\033[31;1m";
    }

    public static String getGreen(){
        return IS_JAR? "" : "\033[32;1m";
    }

    public static String getViolet(){
        return IS_JAR? "" : "\033[35;1m";
    }

    public static String getBlue(){
        return IS_JAR? "" : "\033[36;1m";
    }

    public static String getNone(){
        return IS_JAR? "" : "\033[0m";
    }

    public static String getColor(int ... args){
        synchronized (PrintColor.class){
            if (lineNum >= COLOR_ARR.length){
                lineNum = 0;
            }
            String color = COLOR_ARR[lineNum][innerNum % 2];
            int add = 2;
            if (args != null && args.length > 0) {
                add = args[0];
            }
            if (add > COLOR_ARR.length) {
                add = 4;
            }
            lineNum += add;
            innerNum ++;
            if (innerNum > Integer.MAX_VALUE) {
                innerNum = 0;
            }
            return IS_JAR? "" : color;
        }
    }
}
