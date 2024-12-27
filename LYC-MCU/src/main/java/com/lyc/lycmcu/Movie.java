package com.lyc.lycmcu;

public class Movie {
    private String name;
    private String year;
    private String phase;
    private String posterPath;

    public Movie(String name, String year, String phase, String posterPath) {
        this.name = name;
        this.year = year;
        this.phase = phase;
        this.posterPath = posterPath;
    }

    public String getName() { return name; }
    public String getYear() { return year; }
    public String getPhase() { return phase; }
    public String getPosterPath() { return posterPath; }
}
