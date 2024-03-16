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

public class DisplayTasksGivenDates extends JFrame {
    private JPanel panel;
    private static DisplayTasksGivenDates instance; // Singleton
    private JTextField textField1;
    private JTextField textField2;
    private JButton filterButton;
    private JLabel startLabel;
    private JLabel endLabel;

    public static DisplayTasksGivenDates getInstance() {
        if (instance == null) {
            instance = new DisplayTasksGivenDates();
        }
        return instance;
    }

    DisplayTasksGivenDates()
    {
        ArrayList<String> tasksBetweenDates = new ArrayList<>();
        setTitle("Display tasks within date range");
        setSize(380,230);
        setContentPane(panel);
        setResizable(false);
        setVisible(true);
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DriverManager.getConnection(Main.sqlData[0], Main.sqlData[1], Main.sqlData[2])) {
                    tasksBetweenDates.clear();
                    String startDt = textField1.getText();
                    String endDt = textField2.getText();

                    /*** Check if inputs are a date **/
                    java.sql.Date entryDateCheck = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(startDt).getTime());
                    java.sql.Date deadlineDateCheck = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(endDt).getTime());
                    /*** If not, program throws exception from here, as a messagebox. (ParseException below) **/

                    LocalDate sdate = LocalDate.parse(startDt, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String startDateSql = sdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    LocalDate edate = LocalDate.parse(endDt, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String endDateSql = edate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    String nameSQLQuery = "SELECT * FROM taskplannerdb.tasks WHERE EntryDate BETWEEN '"+ startDateSql + "' AND'"+ endDateSql +"' OR Deadline BETWEEN '"+ startDateSql + "' AND'"+ endDateSql + "'";
                    Statement nameQuery = connection.createStatement();
                    ResultSet resultSet = nameQuery.executeQuery(nameSQLQuery);
                    while (resultSet.next()) {
                        String taskName = resultSet.getString("TaskName");
                        String taskEntry = resultSet.getDate("EntryDate").toString();
                        String taskDeadline = resultSet.getDate("Deadline").toString();
                        tasksBetweenDates.add(taskName + " | Creation Date: "+ taskEntry +" | Deadline: " + taskDeadline + "]");
                    }
                    if(!tasksBetweenDates.isEmpty())
                    {
                        showMessageDialog(null, "You have some tasks in this range: \n\n " +tasksBetweenDates, "Your tasks",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        showMessageDialog(null, "Not any task found in corresponding range.", "No tasks found",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException sqlEx) {
                    throw new IllegalStateException("A problem occured while connecting database.", sqlEx);
                }
                catch (ParseException ex) {
                    showMessageDialog(null, "Invalid date type. Please type a date with dd/mm/yy structure. Example: 25/05/2023");
                }
            }
        });
    }
}

