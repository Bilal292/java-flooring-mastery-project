package com.sg.flooringmastery.view;

public interface UserIO {
    void print(String msg);
    String readString(String prompt);
    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

}
