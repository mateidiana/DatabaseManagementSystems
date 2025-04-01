package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.example.controller.*;

public class DeleteBookForm extends JDialog{
    private LibraryController controller;
    private JLabel labelDeleteBook;
    private JButton btnDelete;
    private JTextField tfISBN;
    private JButton btnCancel;
    private JLabel labelIsbn;
    private JPanel deleteBookPanel;

    public DeleteBookForm(JFrame parent, LibraryController controller) throws Exception{
        super(parent);
        this.controller=controller;

        setTitle("Create a new book");
        setContentPane(deleteBookPanel);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteBook();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }
    public void deleteBook() throws Exception{
        String isbn = tfISBN.getText();
        if (isbn.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(controller.deleteBook(isbn))
            dispose();
        else{
            JOptionPane.showMessageDialog(this, "Failed insertion!", "Try again",JOptionPane.ERROR_MESSAGE);
        }
    }
}
