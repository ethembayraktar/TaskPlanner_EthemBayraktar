import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static javax.swing.JOptionPane.showMessageDialog;

public class TaskDetail extends JFrame {
    private JLabel taskId;
    private JLabel value1;
    private JLabel value2;
    private JLabel value3;
    private JLabel value4;
    private JLabel value5;
    private JLabel value6;
    private JLabel value7;
    private JPanel panel;

    TaskDetail(String taskName) {
        setTitle("Task Detail - " + taskName);
        setSize(650, 280);
        setContentPane(panel);
        setResizable(false);
        setVisible(true);

        value1.setFont(new Font("Arial", Font.PLAIN, 13));
        value2.setFont(new Font("Arial", Font.PLAIN, 13));
        value3.setFont(new Font("Arial", Font.PLAIN, 13));
        value4.setFont(new Font("Arial", Font.PLAIN, 13));
        value5.setFont(new Font("Arial", Font.PLAIN, 13));
        value6.setFont(new Font("Arial", Font.PLAIN, 13));
        value7.setFont(new Font("Arial", Font.PLAIN, 13));

        try (Connection connection = DriverManager.getConnection(Main.sqlData[0], Main.sqlData[1], Main.sqlData[2])) {
            String nameSQLQuery = "SELECT * FROM tasks WHERE TaskName = '"+taskName+"'";
            Statement nameQuery = connection.createStatement();
            ResultSet resultSet = nameQuery.executeQuery(nameSQLQuery);
            DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            while (resultSet.next()) {
                value1.setText(String.valueOf(resultSet.getInt("Id")));
                java.sql.Date sqlEntryDate = resultSet.getDate("EntryDate");
                String entryDateAsStr = outputDateFormat.format(sqlEntryDate);
                value2.setText(entryDateAsStr);
                value3.setText(resultSet.getString("TaskName"));
                value4.setText(resultSet.getString("ShortDesc"));
                java.sql.Date sqlDate = resultSet.getDate("Deadline");
                String deadlineAsStr = outputDateFormat.format(sqlDate);
                value5.setText(deadlineAsStr);
                int priorityValue = resultSet.getInt("Priority");
                value6.setText(priorityValue != 0 ? String.valueOf(priorityValue) : "No Priority");
                int reminder = resultSet.getInt("HasReminderImage");
                value7.setText(reminder != 0 ? "Yes" : "No");
            }
        }
        catch (SQLException sqlEx) {
            showMessageDialog(null, sqlEx);
        }
    }
}
