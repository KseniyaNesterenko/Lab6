package file;

import collection.*;
import exceptions.ValidValuesRangeException;
import io.UserIO;

import java.time.Instant;
import java.time.ZonedDateTime;

public class DragonFieldsReader {

    private UserIO userIO;

    public DragonFieldsReader(UserIO userIO) {
        this.userIO = userIO;
    }

    public Dragon read(Integer id) {

        String i = Instant.now().toString();
        return new Dragon(id, readName(), readCoordinates(), readColor(), readAge(), readType(), readCharacter(), readHead(), ZonedDateTime.parse(i));
    }

    public String readName() {
        String str;

        while (true) {
            userIO.printCommandText("name (not null): ");
            str = userIO.readLine().trim();
            if (str.equals("")) userIO.printCommandError("\nЗначение поля не может быть null или пустой строкой \n");
            else return str;
        }
    }

    public Coordinates readCoordinates() {
        return new Coordinates(readCoordinateX(), readCoordinateY());
    }

    public Double readCoordinateX() {
        Double x;
        while (true) {
            try {
                userIO.printCommandText("coordinate_x (double & not null & x > -459): ");
                x = Double.parseDouble(userIO.readLine().trim());
                if (x <= -459) throw new ValidValuesRangeException();
                else return x;
            } catch (ValidValuesRangeException ex) {
                System.out.println("Координата x должна быть больше -459");
            } catch (NumberFormatException ex) {
                System.err.println("Число должно быть типа Double и не null");
            }
        }
    }

    public int readCoordinateY() {
        int y;
        while (true) {
            try {
                userIO.printCommandText("coordinate_y (int & can be null & y < 675): ");
                String str = userIO.readLine().trim();
                if (str.equals("")) y = 0;
                else {
                    y = Integer.parseInt(str);
                    if (y >= 675) throw new ValidValuesRangeException();
                }
                return y;
            } catch (ValidValuesRangeException ex) {
                System.out.println("Координата y должна быть меньше 675");
            } catch (NumberFormatException ex) {
                System.err.println("Число должно быть типа int");
            }
        }
    }

    public Long readAge() {
        Long age;
        while (true) {
            try {
                userIO.printCommandText("age (long & can be null & age > 0): ");
                String str = userIO.readLine().trim();
                if (str.equals("")) age = null;
                else {
                    age = Long.parseLong(str);
                    if (age <= 0) throw new ValidValuesRangeException();
                }
                return age;
            } catch (ValidValuesRangeException ex) {
                System.err.println("Значение age должно быть больше 0");
            } catch (NumberFormatException ex) {
                System.err.println("Число должно быть типа Long");
            }
        }
    }

    public Color readColor() {
        Color color;
        while (true) {
            try {
                userIO.printCommandText("Допустимые значения color :\n");
                for (Color val : Color.values()) {
                    userIO.printCommandText(val.name() + "\n");
                }
                userIO.printCommandText("color: ");
                color = Color.valueOf(userIO.readLine().toUpperCase().trim());
                return color;
            } catch (IllegalArgumentException ex) {
                System.err.println("Значение введенной константы не представлено в перечислении Color");
            }
        }
    }

    public DragonType readType() {
        DragonType dragonType;
        while (true) {
            try {
                userIO.printCommandText("Допустимые значения type :\n");
                for (DragonType val : DragonType.values()) {
                    userIO.printCommandText(val.name() + "\n");
                }
                userIO.printCommandText("type: ");
                dragonType = DragonType.valueOf(userIO.readLine().toUpperCase().trim());
                return dragonType;
            } catch (IllegalArgumentException ex) {
                System.err.println("Значение введенной константы не представлено в перечислении DragonType");
            }
        }
    }

    public DragonCharacter readCharacter() {
        DragonCharacter dragonCharacter;
        while (true) {
            try {
                userIO.printCommandText("Допустимые значения character :\n");
                for (DragonCharacter val : DragonCharacter.values()) {
                    userIO.printCommandText(val.name() + "\n");
                }
                userIO.printCommandText("character: ");
                dragonCharacter = DragonCharacter.valueOf(userIO.readLine().toUpperCase().trim());
                return dragonCharacter;
            } catch (IllegalArgumentException ex) {
                System.err.println("Значение введенной константы не представлено в перечислении DragonCharacter");
            }
        }
    }

    public DragonHead readHead() {
        Double size;
        while (true) {
            try {
                userIO.printCommandText("head_size (double & can be null): ");
                String str = userIO.readLine().trim();
                if (str.equals("")) size = null;
                else {
                    size = Double.parseDouble(str);
                }
                return new DragonHead(size);
            } catch (NumberFormatException ex) {
                System.err.println("Число должно быть типа double");
            }
        }
    }
}
