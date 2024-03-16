import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import static javax.swing.JOptionPane.showMessageDialog;

public class TaskPlannerLogin extends JFrame {

    private boolean invalidInfo = true;
    private JButton loginToTaskPlannerButton;
    private JPanel loginPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;

    // All users stored in this array below.
    // You can add or remove users by here.
    // All users in this array are accepted by program and can login.
    private final String[][] users = {
            {"deniz.ozsoyeller", "deniz123"},
            {"ethem.bayraktar", "123321"}};

    public TaskPlannerLogin()
    {
        setTitle("Login to TaskPlanner - by Ethem Bayraktar");
        setSize(450,200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(loginPanel);
        setResizable(false);
        setVisible(true);
        loginToTaskPlannerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invalidInfo = true;
                String username = textField1.getText();
                String pass = new String(passwordField1.getPassword());
                for (String[] user : users) {
                    if (username.equals(user[0])) {
                        if (pass.equals(user[1])) {
                            invalidInfo = false;
                            new TaskPlanner(username);
                            setVisible(false);
                            dispose();
                        }
                    }
                }
                if(invalidInfo)
                {
                    showMessageDialog(null, "Username or password is invalid.");
                }
            }
        });
    }

}
