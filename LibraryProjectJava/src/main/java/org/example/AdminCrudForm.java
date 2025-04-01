package org.example;

import javax.swing.*;
import java.util.*;

import org.example.controller.*;
import org.example.model.Book;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminCrudForm extends JDialog{
    private LibraryController controller;
    private JButton btnUpdateBook;
    private JLabel textLabel;
    private JButton btnViewBooks;
    private JButton btnCreateBook;
    private JButton btnDeleteBook;
    private JButton btnCancel;
    private JPanel AdminCrudPanel;

    public AdminCrudForm(JFrame parent, LibraryController controller) throws Exception{
        super(parent);
        this.controller=controller;
        setTitle("Admin functionalities");
        setContentPane(AdminCrudPanel);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);


        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnViewBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showBooks();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        btnCreateBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CreateBookForm bookForm=new CreateBookForm(null,controller);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnUpdateBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UpdateBookForm updateBookForm=new UpdateBookForm(null,controller);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnDeleteBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DeleteBookForm deleteBookForm=new DeleteBookForm(null,controller);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }

    public void showBooks() throws Exception {
        List<Book> books = controller.getAllBooks();
        // Define column names
        String[] columnNames = {"Title", "Author", "ISBN", "Availability"};

        // Create data array to hold book details
        String[][] data = new String[books.size()][4];
        for (int i = 0; i < books.size(); i++) {
            data[i][0] = books.get(i).getTitle();
            data[i][1] = books.get(i).getAuthor();
            data[i][2] = books.get(i).getIsbn();
            data[i][3] = books.get(i).isAvailability() ? "Available" : "Not Available";  // Convert boolean to readable text
        }

        // Create JTable and set it inside a scroll pane
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create and configure dialog
        JDialog dialog = new JDialog(this, "Book List", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.add(scrollPane);

        dialog.setVisible(true);

    }
}
