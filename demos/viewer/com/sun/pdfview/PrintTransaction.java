package com.sun.pdfview;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;


public class PrintTransaction {

    public Boolean printingPayment(int cost){
            Object[] options1 = { "Help", "Print as Guest",
                "Print with my Account" };
         
           // int result = JOptionPane.showOptionDialog(null, "Enter your username","", JOptionPane.PLAIN_MESSAGE,
             //   inputs, options1, null);
             
             
             int result = JOptionPane.showOptionDialog(null, "Do you have an account? Or Print as Guest?", "Select Print Mode",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options1, null);
            if (result == JOptionPane.YES_OPTION){
                //display Help
                JPanel infoPanel = new JPanel();
                JLabel title = new JLabel("Help Information:");
                title.setFont(new Font("Arial", Font.BOLD, 21));
                String msg="Note: This machine does not give change to is customers."
                        + " Change will be stored in your account if you have one.\n\n"
                        + "Login with your account using your username and "
                        + "6 Digit Pin so the machine can use your available "
                        + "credits, and store your changes.\nLoging in as GUEST "
                        + "will not keep your credits or change. Please enter EXACT AMOUNT"
                        + " if you print as GUEST.\n\nFor any further consent "
                        + "please visit our website uPrintHub.com\n"
                        + "Thank You For Printing With Us!";
                JTextArea textArea = new JTextArea(msg);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setOpaque(false);
                textArea.setEditable(false);
                textArea.setFocusable(false);
                
                textArea.setFont(UIManager.getFont("Label.font"));
                textArea.setBorder(UIManager.getBorder("Label.border"));

                //infoProper.setFont(new Font("Arial", Font.PLAIN, 14));
                GridLayout layout1 = new GridLayout(0,1);
                layout1.setHgap(0);
                layout1.setVgap(0);
                infoPanel.setLayout(layout1);
                infoPanel.add(title);
                infoPanel.add(textArea);
                infoPanel.setMaximumSize(new Dimension(800,600));
                infoPanel.setPreferredSize(new Dimension(450,300));
                Object[] options3 = { "Close","Okay" };
                int result2 = JOptionPane.showConfirmDialog(null, infoPanel,"Print Help Information",JOptionPane.PLAIN_MESSAGE);
                if(result2 == JOptionPane.OK_OPTION){
                    printingPayment(cost);
                }
            }else if(result == JOptionPane.NO_OPTION){
                //Print as Guest
                
            }else if(result == JOptionPane.CANCEL_OPTION){
                //check account
                   JTextField userName = new JTextField();
            JTextField userPin = new JTextField();
             final JComponent[] inputs = new JComponent[] {
                     new JLabel("Username"),
                     userName,
                     new JLabel("Pin"),
                     userPin
             };
             Object[] options2 = { "cancel", "Login" };
             //try catch for input validation?
             result = JOptionPane.showConfirmDialog(null,inputs, "User Login",
                 JOptionPane.PLAIN_MESSAGE);
             if(result==JOptionPane.OK_OPTION){
              int pin = Integer.parseInt(userPin.getText());
              if(acctVerify(userName.getText(),pin)>=0){
                int bal = balanceAcct(userName.getText());
                cost = cost - bal;
                JOptionPane.showMessageDialog(null, "You have "+ bal+ " credits in your account.");
                askCoins(bal,cost);
                }else if(acctVerify(userName.getText(),pin)== -19){
                    JOptionPane.showMessageDialog(null, "Login error");
                    //reloop
                    printingPayment(cost);
                }   
             }
               
            }
    
        return false;
    }
    int acctVerify(String user, int pin){
        //verify username and pin
        int result = 0;
        try{
        Class.forName("com.mysql.jdbc.Driver");
        
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/porta_print",
                "root",
                "");
        PreparedStatement getFileStmt = con.prepareStatement("SELECT *"
                + " FROM clientcredit "
                + " WHERE clientUsername= '"+user+"'");
        ResultSet res = getFileStmt.executeQuery();
        if(res.next()){
            //userfound check pin
            if(res.getInt("clientPin")== pin){
                //verified
                                System.out.println(res.getInt("clientPin"));
                                result = res.getInt("credit");
            }else{
                //incorrect PIN!
                System.out.println(res.getInt("clientPin"));
                System.out.print(res);

                String newPin = JOptionPane.showInputDialog(null, "User pin incorrect. Please check your pin");
                if(newPin.equals(pin+"")){
                result = res.getInt("credit");   
                }else{
                result = -19;
                }
                
            }
        }else{
            //no username found
            JOptionPane.showMessageDialog(null,"Username not found. Please check your username.");
            result = -19;
        }
        
        }catch(Exception e){
            
        }
        return result;
    }
    
    int balanceAcct(String username){
        //Check if user exists
        
        //ask again if not
        
        return 1;
    }
    
    void askCoins(int credit, int cost){
        Label warning = new Label("This machine does not give change. We will store the change as credits in your account.");
        Label creditMsg = new Label("Your credit: "+credit+"Php");
        Panel cPanel = new Panel();
        JButton printBtn = new JButton("PRINT");
        printBtn.setEnabled(false);
        printBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //do order to print
            }
        });
        printBtn.setSize(new Dimension(150,75));
        cPanel.add(warning);
        cPanel.add(creditMsg);
        cPanel.add(printBtn);
        JOptionPane.showConfirmDialog(cPanel,"Insert your coins");
        
    }
    
    void coinAcceptor(){
        
    }
}
