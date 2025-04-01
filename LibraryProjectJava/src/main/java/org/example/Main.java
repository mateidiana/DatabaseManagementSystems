package org.example;
import org.example.controller.LibraryController;
import org.example.model.*;
import org.example.repo.*;

public class Main {
    public static void main(String[] args) throws Exception {

        IRepository<Book> bookRepo = new BookDBRepository("jdbc:mysql://127.0.0.1:3306/library","root","Bill4761");
        IRepository<User> userRepo = new UserDBRepository("jdbc:mysql://127.0.0.1:3306/library","root","Bill4761");
        IRepository<Borrowed> borrowedRepo = new BorrowedDBRepository("jdbc:mysql://127.0.0.1:3306/library","root","Bill4761");
        IRepository<Review> reviewRepo = new ReviewDBRepository("jdbc:mysql://127.0.0.1:3306/library","root","Bill4761");
        LibraryController controller=new LibraryController(bookRepo,userRepo,borrowedRepo,reviewRepo);

        WelcomeForm welcomeForm = new WelcomeForm(null,controller);

    }
}
