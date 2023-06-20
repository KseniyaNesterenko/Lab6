package collection;

import java.util.Locale;
public class DragonFieldValidation {
    public static boolean validate(String field, String value) {
        try {
            switch (field) {
                case "id" -> {
                    if (value == null || value.equals("")) throw new NullPointerException();
                    if (Integer.parseInt(value) > 0) return true;
                }
                case "name" -> {
                    if (value == null || value.equals("")) throw new NullPointerException();
                    return true;
                }
                case "coordinate_x" -> {
                    if (value == null || value.equals("")) throw new NullPointerException();

                    if (Double.parseDouble(value) > -648) {
                        return true;
                    }
                }
                case "coordinate_y" -> {
                    if (value == null || value.equals("")) throw new NullPointerException();

                    if (Integer.parseInt(value) < 804) {
                        return true;
                    }
                }
                case "age" -> {
                    if (value == null || value.equals("")) return true;

                    if (Long.parseLong(value) > 0) return true;
                }
                case "color" -> {
                    Color.valueOf(value.toUpperCase(Locale.ROOT));
                    return true;
                }
                case "type" -> {
                    DragonType.valueOf(value.toUpperCase(Locale.ROOT));
                    return true;
                }
                case "character" -> {
                    DragonCharacter.valueOf(value.toUpperCase(Locale.ROOT));
                    return true;
                }
                case "head_size" -> {
                    if (value == null || value.equals("")) return true;

                    Double.parseDouble(value);
                    return true;
                }
                case "" -> {
                    return false;
                }
            }
        } catch (ClassCastException | IllegalArgumentException | NullPointerException ignored) {
        }
        return false;
    }
}