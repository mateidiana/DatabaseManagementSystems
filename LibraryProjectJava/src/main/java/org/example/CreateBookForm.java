package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.example.controller.*;

public class CreateBookForm extends JDialog{
    private LibraryController controller;
    private JLabel textLabel;
    private JTextField tfTitle;
    private JTextField tfAuthor;
    private JTextField tfISBN;
    private JTextField tfAvailability;
    private JButton btnCancel;
    private JLabel labelTitle;
    private JLabel labelAuthor;
    private JLabel Labelisbn;
    private JLabel labelAvailability;
    private JPanel createBookPanel;
    private JButton addbtn;

    public CreateBookForm(JFrame parent, LibraryController controller) throws Exception{
        super(parent);
        this.controller=controller;

        setTitle("Create a new book");
        setContentPane(createBookPanel);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        addbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createBook();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }

    public void createBook() throws Exception{
        String title = tfTitle.getText();
        String author = tfAuthor.getText();
        String isbn = tfISBN.getText();
        String available = tfAvailability.getText();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || available.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!available.equals("true") && !available.equals("false")){
            JOptionPane.showMessageDialog(this, "The availability can either be true or false!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean availability;
        availability= available.equals("true");

        if(controller.createBook(title,author,"",isbn,availability))
            dispose();
        else{
            JOptionPane.showMessageDialog(this, "Failed insertion!", "Try again",JOptionPane.ERROR_MESSAGE);
        }
    }
}
