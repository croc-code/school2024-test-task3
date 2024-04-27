package DTO;

import java.util.ArrayList;

// DTO класс для представления результата в формате JSON
public class Result {
    private ArrayList<String> sites;

    public Result(ArrayList<String> sites) {
        this.sites = sites;
    }
}
