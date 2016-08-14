package com.ozcaan11.noteapp.Class;

import com.ozcaan11.noteapp.R;

/**
 * Author : l50 - Özcan YARIMDÜNYA (@ozcaan11)
 * Date   : 10.07.2016 - 02:10
 */

public class Convertion {

    public static int colorNameToPath(String colorName) {

        if (String.valueOf(colorName).equalsIgnoreCase("blue"))
            return R.color.colorBlue;
        else if (String.valueOf(colorName).equalsIgnoreCase("green"))
            return R.color.colorGreen;
        else if (String.valueOf(colorName).equalsIgnoreCase("red"))
            return R.color.colorRed;
        else if (String.valueOf(colorName).equalsIgnoreCase("orange"))
            return R.color.colorOrange;
        else if (String.valueOf(colorName).equalsIgnoreCase("black"))
            return R.color.colorBlack;
        else
            return R.color.colorBlue;
    }

    public static String hexToColorName(String hexValue) {

        if (String.valueOf(hexValue).equalsIgnoreCase("FF0000FF"))
            return "blue";
        else if (String.valueOf(hexValue).equalsIgnoreCase("FF008000"))
            return "green";
        else if (String.valueOf(hexValue).equalsIgnoreCase("FFFF0000"))
            return "red";
        else if (String.valueOf(hexValue).equalsIgnoreCase("FFFFA500"))
            return "orange";
        else if (String.valueOf(hexValue).equalsIgnoreCase("FF000000"))
            return "black";
        else
            return "blue";
    }

    public static String colorNameToHex(String colorName,boolean alpha) {

        if (String.valueOf(colorName).equalsIgnoreCase("blue")) {
            if (alpha)
                return "#FF0000B2";
            return "#FF0000FF";
        } else if (String.valueOf(colorName).equalsIgnoreCase("green")) {
            if (alpha)
                return "#FF005900";
            return "#FF008000";
        } else if (String.valueOf(colorName).equalsIgnoreCase("red")) {
            if (alpha)
                return "#FFCC0000";
            return "#FFFF0000";
        } else if (String.valueOf(colorName).equalsIgnoreCase("orange")) {
            if (alpha)
                return "#FFCC8400";
            return "#FFFFA500";
        } else if (String.valueOf(colorName).equalsIgnoreCase("black")) {
            if (alpha)
                return "#FF191919";
            return "#FF000000";
        } else {
            if (alpha)
                return "#FF000099";
            return "#FF0000FF";
        }
    }
}
