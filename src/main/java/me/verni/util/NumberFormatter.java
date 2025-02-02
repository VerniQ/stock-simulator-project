package me.verni.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {

    public static String formatNumber(double number) {
        DecimalFormat df = new DecimalFormat("#.##");
        if (number < 1000) {
            return df.format(number);
        }

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        df = (DecimalFormat) nf;
        df.applyPattern("#.##");

        if (number < 1_000_000) {
            return df.format(number / 1_000) + "K";
        }
        if (number < 1_000_000_000){
            return df.format(number / 1_000_000) + "M";
        }
        return df.format(number / 1_000_000_000) + "B";
    }

    public static String formatNumber(int number) {
        DecimalFormat df = new DecimalFormat("#.##");
        if (number < 1000) {
            return df.format(number);
        }

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        df = (DecimalFormat) nf;
        df.applyPattern("#.##");

        if (number < 1_000_000) {
            return df.format(number / 1_000) + "K";
        }
        if (number < 1_000_000_000){
            return df.format(number / 1_000_000) + "M";
        }
        return df.format(number / 1_000_000_000) + "B";
    }
}