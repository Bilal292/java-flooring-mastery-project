package com.sg.flooringmastery.view;

import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO{
    private Scanner input;

    public UserIOConsoleImpl () {
        input = new Scanner(System.in);
    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        this.print(prompt);
        String userString = this.input.nextLine();
        return userString;
    }

    @Override
    public int readInt(String prompt) {
        this.print(prompt);
        int userInt = Integer.parseInt(this.input.nextLine());
        return userInt;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        this.print(prompt);

        while (true) {
            int userInt = Integer.parseInt(this.input.nextLine());

            if (userInt <= max && userInt >= min) {
                return userInt;
            } else {
                this.print(prompt);
            }
        }
    }

}
