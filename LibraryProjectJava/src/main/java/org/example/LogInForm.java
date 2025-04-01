package org.example;

import javax.swing.*;

import org.example.controller.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogInForm extends JDialog{

    private LibraryController controller;
    private JLabel LogInLabel;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JLabel usernameLabel;
    private JPanel logInPanel;
    private JLabel passwordLabel;
    private JButton cancelBtn;
    private JButton logInButton;
    private JButton btnLogUser;

    public LogInForm(JFrame parent, LibraryController controller) throws Exception{
        super(parent);
        this.controller=controller;

        setTitle("Log into your account");
        setContentPane(logInPanel);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    logInUser();
                    AdminCrudForm adminCrudForm=new AdminCrudForm(null, controller);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnLogUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    logInUser2();
                    UserCrudForm userCrudForm=new UserCrudForm(null,controller,tfUsername.getText());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }

    public void logInUser() throws Exception{
        String username = tfUsername.getText();
        String password = String.valueOf(tfPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(controller.logIn(username,password)&&controller.getRole(username).equals("admin"))
            dispose();
        else{
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Try again",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void logInUser2() throws Exception{
        String username = tfUsername.getText();
        String password = String.valueOf(tfPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(controller.logIn(username,password)&&controller.getRole(username).equals("user"))
            dispose();
        else{
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Try again",JOptionPane.ERROR_MESSAGE);
        }
    }
}
