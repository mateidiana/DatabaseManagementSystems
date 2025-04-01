package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.example.controller.*;

public class ReturnBookForm extends JDialog{
    private LibraryController controller;
    private String username;
    private JButton btnReturn;
    private JButton cancelBtn;
    private JTextField tfISBN;
    private JLabel labelReturn;
    private JLabel labelISBN;
    private JPanel returnPanel;

    public ReturnBookForm(JFrame parent, LibraryController controller, String username)  throws Exception{
        super(parent);
        this.controller=controller;
        this.username=username;
        setTitle("Return a book");
        setContentPane(returnPanel);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    returnBook();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }

    public void returnBook()throws Exception{
        String isbn = tfISBN.getText();
        if (isbn.isEmpty()||username.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(controller.returnBook(username,isbn))
            dispose();
        else{
            JOptionPane.showMessageDialog(this, "Failed insertion!", "Try again",JOptionPane.ERROR_MESSAGE);
        }
    }
}
