package org.example.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String summary;
    private String isbn;
    private boolean availability;

    public Book(int id, String title, String author, String summary, String isbn, boolean availability)
    {
        this.id=id;
        this.title=title;
        this.author=author;
        this.summary=summary;
        this.isbn=isbn;
        this.availability=availability;
    }

    public int getId(){return this.id;}
    public void setId(int id){this.id=id;}

    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}

    public String getAuthor(){return author;}
    public void setAuthor(String author){this.author=author;}

    public String getSummary(){return summary;}
    public void setSummary(String summary){this.summary=summary;}

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String toString(){
        return "\"" + title + "\" by " + author;
    }
}
