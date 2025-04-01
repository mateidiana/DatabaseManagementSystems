package org.example.model;

public class Review {
    private int id;
    private int userId;
    private int bookId;
    private Float rating;

    public Review(int id, int userId, int bookId, Float rating){
        this.id=id;
        this.userId=userId;
        this.bookId=bookId;
        this.rating=rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Float getRating(){
        return rating;
    }

    public void setRating(Float rating){
        this.rating=rating;
    }
}
