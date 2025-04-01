package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.example.controller.*;
import org.example.repo.*;
import org.example.model.*;

public class WelcomeForm extends JDialog{
    private LibraryController controller;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel WelcomePanel;

    public WelcomeForm(JFrame parent, LibraryController controller){
        super(parent);
        this.controller=controller;

        setTitle("Welcome!");
        setContentPane(WelcomePanel);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LogInForm logInForm = new LogInForm(null, controller);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RegistrationForm myForm = new RegistrationForm(null, controller);
                    //dispose();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }
}
