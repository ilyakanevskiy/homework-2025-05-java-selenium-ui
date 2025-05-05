package dev.ilyakanevskiy.utils;

public class PlaceholderHelper {
    public static String adaptPlaceholder(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\{\\d+}", "{}");
    }
}
