package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.example.controller.*;

public class BorrowBookForm extends JDialog{
    private LibraryController controller;
    private String username;
    private JLabel labelBorrow;
    private JButton btnCancel;
    private JTextField tfBook;
    private JButton btnBorrow;
    private JLabel labelIsbn;
    private JPanel borrowBookPanel;
    public BorrowBookForm(JFrame parent, LibraryController controller, String username) throws Exception{
        super(parent);
        this.controller=controller;
        this.username=username;
        setTitle("Borrow a new book");
        setContentPane(borrowBookPanel);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);


        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnBorrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    borrowBook();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }

    public void borrowBook() throws Exception{

        String isbn = tfBook.getText();
        if (isbn.isEmpty()||username.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(controller.borrowBook(username,isbn))
            dispose();
        else{
            JOptionPane.showMessageDialog(this, "Failed insertion!", "Try again",JOptionPane.ERROR_MESSAGE);
        }
    }
}
