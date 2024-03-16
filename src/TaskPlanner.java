import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

public class TaskPlanner extends JFrame{
    private JPanel panel;
    private JButton showTasksButton;
    private JButton viewTaskImage;
    private JButton orderTasks;
    private JLabel welcomeLabel;
    private JLabel welcome2;

    TaskPlanner(String username) {
        ArrayList<String> deadlineApproachingTasks = new ArrayList<>();
        welcomeLabel.setText("Welcome, " + username);
        setTitle("TaskPlanner - by Ethem Bayraktar");
        setSize(530,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel);
        setResizable(false);
        setVisible(true);
        welcome2.setFont(new Font("Arial", Font.PLAIN, 13));

        try (Connection connection = DriverManager.getConnection(Main.sqlData[0], Main.sqlData[1], Main.sqlData[2])) {
            String nameSQLQuery = "SELECT * FROM TASKS WHERE Deadline BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 1 DAY)";
            Statement nameQuery = connection.createStatement();
            ResultSet resultSet = nameQuery.executeQuery(nameSQLQuery);
            while (resultSet.next()) {
                String taskName = resultSet.getString("TaskName");
                String taskDeadline = resultSet.getDate("Deadline").toString();
                deadlineApproachingTasks.add(taskName + " | Deadline: " + taskDeadline + "]");
            }
            if(!deadlineApproachingTasks.isEmpty())
            {
                showMessageDialog(null, "Dear "+ username +", some of your tasks' deadlines are approaching: \n\n " +deadlineApproachingTasks, "Your deadlines are approaching!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (SQLException sqlEx) {
            throw new IllegalStateException("A problem occured while connecting database.", sqlEx);
        }
        showTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewTasks.getInstance().setVisible(true);
                // Applying singleton - this form should not appear more than once
            }
        });
        viewTaskImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisplayTasksGivenDates.getInstance().setVisible(true);
                // Applying singleton - this form should not appear more than once
            }
        });
        orderTasks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OrderTasksByPriority.getInstance().setVisible(true);
                // Applying singleton - this form should not appear more than once
            }
        });
    }
}
