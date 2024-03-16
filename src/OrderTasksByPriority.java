import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

public class OrderTasksByPriority extends JFrame {

    private static OrderTasksByPriority instance; // Singleton
    private JPanel panel;
    private JButton sortByPriorityButton;
    private JTextField textField1;
    private JTextField textField2;
    private JLabel priorityLabel;

    public static OrderTasksByPriority getInstance() {
        if (instance == null) {
            instance = new OrderTasksByPriority();
        }
        return instance;
    }

    OrderTasksByPriority()
    {
        ArrayList<String> orderedTasks = new ArrayList<>();
        setTitle("Order Tasks by Deadline & Priority");
        setSize(380,230);
        setContentPane(panel);
        setResizable(false);
        setVisible(true);
        sortByPriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DriverManager.getConnection(Main.sqlData[0], Main.sqlData[1], Main.sqlData[2])) {
                    orderedTasks.clear();
                    String deadline = textField1.getText();
                    int priorityNum = !textField2.getText().isBlank() ? Integer.parseInt(textField2.getText()) : -1;

                    // Check cases

                    if (!textField2.getText().isBlank() && (priorityNum < 1 || priorityNum > 5)) {
                        showMessageDialog(null, "Priority should be in range of 1-5.");
                        return;
                    }
                    /** Check deadline is a date **/
                    java.sql.Date deadlineDateCheck = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(deadline).getTime());
                    /*** If not, program throws exception from here, as a messagebox. (ParseException below) **/


                    LocalDate deadlineAsDate = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String deadlineSql = deadlineAsDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    String nameSQLQuery = "SELECT * FROM taskplannerdb.tasks WHERE Deadline = '"+ deadlineSql + "' AND Priority = "+ priorityNum;
                    Statement nameQuery = connection.createStatement();
                    ResultSet resultSet = nameQuery.executeQuery(nameSQLQuery);
                    while (resultSet.next()) {
                        String taskName = resultSet.getString("TaskName");
                        int priority = resultSet.getInt("Priority");
                        String taskDeadline = resultSet.getDate("Deadline").toString();
                        orderedTasks.add(taskName + " | Priority: " + priority + " | Deadline: " + taskDeadline + "]");
                    }
                    if(!orderedTasks.isEmpty())
                    {
                        showMessageDialog(null, "Your tasks with this deadline of " + textField1.getText() + " and priority of " + textField2.getText() + ": \n\n " +orderedTasks, "Your tasks",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        showMessageDialog(null, "Not any task found with this deadline and priority.", "No tasks found",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException sqlEx) {
                    throw new IllegalStateException("A problem occured while connecting database.", sqlEx);
                }
                catch (ParseException ex) {
                    showMessageDialog(null, "Invalid date type. Please type a date with dd/mm/yy structure. Example: 25/05/2023");
                }
                catch (NumberFormatException nfe) {
                    showMessageDialog(null, "Priority should be in range of 1-5.");
                }
            }
        });
    }

}
