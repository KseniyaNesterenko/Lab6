public class Main {

    //private static final Logger rootLogger = LogManager.getRootLogger();

    public static void main(String[] args) {

        if (args.length != 0) {
            App app = new App(Integer.parseInt(args[0]));
            try {
                app.start();
            } catch (NumberFormatException ex) {
                System.out.println("Значение порта должно быть целочисленным.\n Введенное значение: " + args[0]);
            }
        }
    }
}