package org.example.controller;
import java.util.*;
import org.example.model.*;
import org.example.repo.IRepository;

public class LibraryController {
    private final IRepository<Book> bookRepo;
    private final IRepository<User> userRepo;
    private final IRepository<Borrowed> borrowedRepo;
    private final IRepository<Review> reviewRepo;

    public LibraryController(IRepository<Book> bookRepo, IRepository<User> userRepo, IRepository<Borrowed> borrowedRepo, IRepository<Review> reviewRepo){
        this.bookRepo=bookRepo;
        this.userRepo=userRepo;
        this.borrowedRepo=borrowedRepo;
        this.reviewRepo=reviewRepo;
    }

    public boolean createUser(String username, String email, String password, String role) throws Exception {
        for (User user: userRepo.getAll()){
            if (user.getEmail().equals(email) || user.getUsername().equals(username))
                return false;
        }
        User user = new User(userRepo.getAll().size()+1, username, email, password, role);
        userRepo.create(user);
        return true;
    }

    public boolean logIn(String username, String password) throws Exception {
        for (User user: userRepo.getAll()){
            if (user.getUsername().equals(username) && user.getPassword().equals(password))
                return true;
        }
        return false;
    }

    public User getByUsername(String username) throws Exception {
        for (User user: userRepo.getAll()){
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public User getById(int id) throws Exception {
        for (User user: userRepo.getAll()){
            if (user.getId() == id)
                return user;
        }
        return null;
    }

    public boolean deleteUser(String username, String password) throws Exception {
        if (logIn(username,password)){
            int deleted=getByUsername(username).getId();
            userRepo.delete(deleted);

            for (User user:userRepo.getAll()){
                if(user.getId()>deleted){
                    user.setId(user.getId()-1);
                    userRepo.update(user);
                }
            }

            return true;
        }
        return false;
    }

    public boolean updateUser(String username, String password, String newEmail, String newUsername, String newPassword) throws Exception {
        if (logIn(username,password)){
            User user = getByUsername(username);
            user.setUsername(newUsername);
            user.setPassword(newPassword);
            user.setEmail(newEmail);
            userRepo.update(user);
            return true;
        }
        return false;
    }

    public boolean createBook(String title, String author, String summary, String isbn, boolean availability) throws Exception{
        for (Book book: bookRepo.getAll())
            if (book.getIsbn().equals(isbn))
                return false;
        Book book = new Book(bookRepo.getAll().size()+1,title,author,summary,isbn,availability);
        bookRepo.create(book);
        return true;
    }

    public Book getByIsbn(String isbn) throws Exception{
        for (Book book:bookRepo.getAll())
            if (book.getIsbn().equals(isbn))
                return book;
        return null;
    }

    public List<Book> getBookByTitleAndAuthor(String title, String author) throws Exception{
        List<Book> books = new ArrayList<>();
        for (Book book: bookRepo.getAll())
            if (book.getAuthor().equals(author) && book.getTitle().equals(title))
                books.add(book);
        return books;
    }

    public boolean deleteBook(String isbn) throws Exception {
        if (getByIsbn(isbn)!=null){
            int deleted=getByIsbn(isbn).getId();
            bookRepo.delete(deleted);

            for (Book book:bookRepo.getAll()){
                if(book.getId()>deleted){
                    book.setId(book.getId()-1);
                    bookRepo.update(book);
                }
            }

            return true;
        }
        return false;
    }

    public boolean updateBook(String isbn, String newAuthor, String newTitle, String newIsbn, String newSummary, boolean newAvailability) throws Exception {
        if (getByIsbn(isbn)!=null){
            Book book = getByIsbn(isbn);
            book.setAuthor(newAuthor);
            book.setTitle(newTitle);
            if(getByIsbn(newIsbn)==null)
                book.setIsbn(newIsbn);
            book.setSummary(newSummary);
            book.setAvailability(newAvailability);
            bookRepo.update(book);
            return true;
        }
        return false;
    }

    public List<Book> filterByAuthor(String author) throws Exception{
        List<Book> filtered=new ArrayList<>();
        for (Book book: bookRepo.getAll())
            if (book.getAuthor().equals(author))
                filtered.add(book);
        return filtered;
    }

    public String getRole(String username) throws Exception {
        for (User user:userRepo.getAll())
            if (user.getUsername().equals(username))
                return user.getRole();
        return "";
    }

    public List<Book> filterByAvailability() throws Exception{
        List<Book> filtered=new ArrayList<>();
        for (Book book: bookRepo.getAll())
            if (book.isAvailability())
                filtered.add(book);
        return filtered;
    }

    public List<Book> getAllBooks() throws Exception{
        return bookRepo.getAll();
    }

    public boolean borrowBook(String username, String isbn) throws Exception {
        Book book = getByIsbn(isbn);
        User user = getByUsername(username);
        if (!book.isAvailability() || user == null)
            return false;
        Borrowed borrowed = new Borrowed(borrowedRepo.getAll().size()+1, book.getId(), user.getId());
        borrowedRepo.create(borrowed);
        book.setAvailability(false);
        bookRepo.update(book);
        return true;
    }

    public boolean returnBook(String username, String isbn) throws Exception {
        int deleted = 0;
        Book book = getByIsbn(isbn);
        book.setAvailability(true);
        bookRepo.update(book);
        User user = getByUsername(username);
        for (Borrowed borrowed: borrowedRepo.getAll())
            if (borrowed.getUserId()==user.getId() && borrowed.getBookId()==book.getId())
            {
                deleted=borrowed.getId();
                borrowedRepo.delete(deleted);
            }
        if (deleted!=0){
            for (Borrowed borrowed: borrowedRepo.getAll())
                if (borrowed.getId()>deleted){
                    borrowed.setId(borrowed.getId()-1);
                    borrowedRepo.update(borrowed);
                }
            return true;
        }

        return false;
    }

    public void leaveReview(String username, String isbn, Float rating) throws Exception {
        Book book = getByIsbn(isbn);
        User user = getByUsername(username);
        Review leftReview = new Review(reviewRepo.getAll().size()+1, user.getId(), book.getId(), rating);
        reviewRepo.create(leftReview);
    }

    public Book getBookById(int id) throws Exception {
        for (Book book:bookRepo.getAll())
            if(book.getId()==id)
                return book;
        return null;

    }

    public List<Book> viewCurrentBorrowings(String username) throws Exception {
        List<Book> borrowings = new ArrayList<>();
        User user = getByUsername(username);
        for (Borrowed borrowed:borrowedRepo.getAll())
            if (borrowed.getUserId() == user.getId())
                borrowings.add(getBookById(borrowed.getBookId()));
        return borrowings;
    }

    public Map<User,Float> reviewsForBook(String title, String author) throws Exception {
        Map<User,Float> reviews = new HashMap<>();
        List<Book> books = getBookByTitleAndAuthor(title,author);
        for (Book book:books)
            for (Review review:reviewRepo.getAll())
                if (book.getId() == review.getBookId())
                    reviews.put(getById(review.getUserId()),review.getRating());
        return reviews;

    }

    public Map<Book,Float> avgRatingsForBooks() throws Exception{
        Map<Book,Float> ratings = new HashMap<>();
        for (Book book: bookRepo.getAll()){
            Float rating = 0.0F;
            int numReviews = 0;
            for (Review review:reviewRepo.getAll()){
                if (book.getId() == review.getBookId()){
                    rating+=review.getRating();
                    numReviews+=1;
                }

            }
            Float avg = rating/numReviews;
            ratings.put(book,avg);
        }

        List<Map.Entry<Book, Float>> entryList = new ArrayList<>(ratings.entrySet());
        entryList.sort(Map.Entry.comparingByValue());

        LinkedHashMap<Book, Float> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Book, Float> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }


}
