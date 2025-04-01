package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.example.controller.*;
import org.example.repo.*;
import org.example.model.*;

public class RegistrationForm extends JDialog{

    private LibraryController controller;
    private JTextField tfUsername;
    private JTextField tfEmail;
    private JTextField tfRole;
    private JPasswordField tfPassword;
    private JPasswordField tfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public RegistrationForm(JFrame parent, LibraryController controller) throws Exception {
        super(parent);
        this.controller=controller;

        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerUser();
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

    public void registerUser() throws Exception {
        String username = tfUsername.getText();
        String email = tfEmail.getText();
        String role = tfRole.getText();
        String password = String.valueOf(tfPassword.getPassword());
        String confirmPassword = String.valueOf(tfConfirmPassword.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!role.equals("admin") && !role.equals("user")){
            JOptionPane.showMessageDialog(this, "The role can be either user or admin!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this, "Confirm password does not match!", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(controller.createUser(username,email,password,role))
            dispose();
        else{
            JOptionPane.showMessageDialog(this, "Failed registration!", "Try again",JOptionPane.ERROR_MESSAGE);
        }

    }

//    public static void main(String []args) throws Exception {
//        RegistrationForm myForm = new RegistrationForm(null);
//
//    }
}
