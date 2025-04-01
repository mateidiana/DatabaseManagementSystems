package org.example;

import javax.swing.*;

import org.example.controller.*;
import org.example.model.Book;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserCrudForm extends JDialog{
    private LibraryController controller;

    private String username;
    private JButton btnViewYourBorrowed;
    private JButton btnViewAllBooks;
    private JButton btnBorrow;
    private JButton btnCancel;
    private JLabel labelLogIn;
    private JPanel userCrudPanel;
    private JButton returnBookBtn;

    public UserCrudForm(JFrame parent, LibraryController controller, String username) throws Exception{
        super(parent);
        this.controller=controller;
        this.username=username;
        setTitle("User functionalities");
        setContentPane(userCrudPanel);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);


        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnViewAllBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showBooks();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnBorrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BorrowBookForm bookForm=new BorrowBookForm(null,controller,username);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnViewYourBorrowed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showBorrowedBooks();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        returnBookBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ReturnBookForm returnBookForm=new ReturnBookForm(null,controller,username);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }

    public void showBooks() throws Exception {
        List<Book> books = controller.filterByAvailability();

        String[] columnNames = {"Title", "Author", "ISBN", "Availability"};

        String[][] data = new String[books.size()][4];
        for (int i = 0; i < books.size(); i++) {
            data[i][0] = books.get(i).getTitle();
            data[i][1] = books.get(i).getAuthor();
            data[i][2] = books.get(i).getIsbn();
            data[i][3] = books.get(i).isAvailability() ? "Available" : "Not Available";
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JDialog dialog = new JDialog(this, "Book List", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.add(scrollPane);

        dialog.setVisible(true);

    }

    public void showBorrowedBooks() throws Exception{
        List<Book> books = controller.viewCurrentBorrowings(username);

        String[] columnNames = {"Title", "Author", "ISBN", "Availability"};


        String[][] data = new String[books.size()][4];
        for (int i = 0; i < books.size(); i++) {
            data[i][0] = books.get(i).getTitle();
            data[i][1] = books.get(i).getAuthor();
            data[i][2] = books.get(i).getIsbn();
            data[i][3] = books.get(i).isAvailability() ? "Available" : "Not Available";
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JDialog dialog = new JDialog(this, "Book List", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.add(scrollPane);

        dialog.setVisible(true);
    }
}
