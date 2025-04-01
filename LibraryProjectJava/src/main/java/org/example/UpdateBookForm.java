package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.example.controller.*;

public class UpdateBookForm extends JDialog{
    private LibraryController controller;
    private JLabel labelUpdate;
    private JTextField tfOldISBN;
    private JTextField tfNewTitle;
    private JTextField tfNewAuthor;
    private JTextField tfNewAvailable;
    private JTextField tfNewISBN;
    private JLabel labelOldIsbn;
    private JLabel labelTitle;
    private JLabel labelNewAuthor;
    private JLabel labelAvailable;
    private JLabel labelNewIsbn;
    private JButton btnCancel;
    private JButton btnUpdateBook;
    private JPanel updateBookPanel;


    public UpdateBookForm(JFrame parent, LibraryController controller) throws Exception{
        super(parent);
        this.controller=controller;

        setTitle("Update book");
        setContentPane(updateBookPanel);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnUpdateBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateBook();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }

    public void updateBook() throws Exception{
        String oldISBN = tfOldISBN.getText();
        String title = tfNewTitle.getText();
        String author = tfNewAuthor.getText();
        String isbn = tfNewISBN.getText();
        String available = tfNewAvailable.getText();

        if (oldISBN.isEmpty()||title.isEmpty() || author.isEmpty() || isbn.isEmpty() || available.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!available.equals("true") && !available.equals("false")){
            JOptionPane.showMessageDialog(this, "The availability can either be true or false!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean availability;
        availability= available.equals("true");

        if(controller.updateBook(oldISBN,author,title,isbn,"",availability))
            dispose();
        else{
            JOptionPane.showMessageDialog(this, "Failed insertion!", "Try again",JOptionPane.ERROR_MESSAGE);
        }
    }

}
