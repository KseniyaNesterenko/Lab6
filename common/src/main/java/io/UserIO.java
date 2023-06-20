package io;

import java.util.Scanner;

public class UserIO {

    public static Scanner scanner;

    public UserIO() {
        scanner = new Scanner(System.in, "UTF-8");
    }


    public UserIO(Scanner scanner) {
        this.scanner = scanner;
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scanner) {
        UserIO.scanner = scanner;
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public void printCommandText(String str) {
        System.out.print(str);
    }

    public void printPreamble() {
        System.out.print(">");
    }

    public void printCommandError(String s) {
    }
}
