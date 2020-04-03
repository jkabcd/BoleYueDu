package com.example.yuedulib;

import android.graphics.Bitmap;

import java.util.List;

public class Book {
    enum BOOKTYPE{
        PRE,CUR,NEXT,COVERCUR
    }
    String title;
    List<String> strings;
    BOOKTYPE booktype;
    Bitmap bitmap;
    int currentpage;
    public Book() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Book(String title, List<String> strings, BOOKTYPE booktype, Bitmap bitmap, int currentpage) {
        this.title = title;
        this.strings = strings;
        this.booktype = booktype;
        this.bitmap = bitmap;
        this.currentpage = currentpage;
    }

    public Book(String title, List<String> strings, BOOKTYPE booktype) {
        this.title = title;
        this.strings = strings;
        this.booktype = booktype;
    }

    public Book(String title, List<String> strings, BOOKTYPE booktype, int currentpage) {
        this.title = title;
        this.strings = strings;
        this.booktype = booktype;
        this.currentpage = currentpage;
    }

    public BOOKTYPE getBooktype() {
        return booktype;
    }

    public void setBooktype(BOOKTYPE booktype) {
        this.booktype = booktype;
    }

    public Book(String title, List<String> strings) {
        this.title = title;
        this.strings = strings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }
}
