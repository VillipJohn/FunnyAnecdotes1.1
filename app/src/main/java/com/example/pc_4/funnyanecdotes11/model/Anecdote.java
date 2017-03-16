package com.example.pc_4.funnyanecdotes11.model;

/**
 * Created by PC-4 on 16.03.2017.
 */

public class Anecdote {

    //Variables that are in our json
    private int anecdoteId;
    private String text;
    private String date;

    public int getAnecdoteId() {
        return anecdoteId;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public void setAnecdoteId(int anecdoteId) {
        this.anecdoteId = anecdoteId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

