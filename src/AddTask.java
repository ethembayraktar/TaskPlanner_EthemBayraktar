import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static javax.swing.JOptionPane.showMessageDialog;

public class AddTask extends JFrame {

    private static AddTask instance; // Singleton
    private JTextField taskName;
    private JPanel panel1;
    private JTextField priority;
    private JTextField reminderImage;
    private JTextField taskDesc;
    private JTextField deadline;
    private JButton addTaskButton;

    public static AddTask getInstance() {
        if (instance == null) {
            instance = new AddTask();
        }
        return instance;
    }

    AddTask()
    {
        setTitle("Add Task");
        setSize(350,230);
        setContentPane(panel1);
        setResizable(false);
        setVisible(true);
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DriverManager.getConnection(Main.sqlData[0], Main.sqlData[1], Main.sqlData[2])) {
                    String taskNameStr = taskName.getText();
                    String taskDescStr = taskDesc.getText();
                    String deadlineStr = deadline.getText();
                    LocalDate entryDate = LocalDate.now();
                    String reminderImageAsStr = reminderImage.getText().toLowerCase();
                    boolean hasReminderImage = reminderImageAsStr.equals("true");
                    int priorityNum = !priority.getText().isBlank() ? Integer.parseInt(priority.getText()) : -1;

                    // First of all, stop adding task if any case is missing: (Priority can be null)
                    if (taskNameStr.isBlank()) {
                        showMessageDialog(null, "Task name should not be empty.");
                        return;
                    }
                    if (taskDescStr.isBlank()) {
                        showMessageDialog(null, "Task description should not be empty.");
                        return;
                    }
                    if (deadlineStr.isBlank()) {
                        showMessageDialog(null, "Task deadline should not be empty.");
                        return;
                    }
                    if (reminderImageAsStr.isBlank() || !reminderImageAsStr.equals("true") && !reminderImageAsStr.equals("false")) {
                        showMessageDialog(null, "Please select your choice for reminder image.");
                        return;
                    }
                    if (taskNameStr.length() > 45) {
                        showMessageDialog(null, "Task name is too long. Allowed maximum length: 45");
                        return;
                    }
                    if (taskDescStr.length() > 200) {
                        showMessageDialog(null, "Task description is too long. Allowed maximum length: 200");
                        return;
                    }
                    if (!priority.getText().isBlank() && (priorityNum < 1 || priorityNum > 5)) {
                        showMessageDialog(null, "Priority should be in range of 1-5.");
                        return;
                    }

                    //If all if statements above would not return the listener, check the task name (unique key) exists:
                    String nameSQLQuery = "SELECT * FROM tasks WHERE TaskName = '"+taskNameStr+"'";
                    Statement nameQuery = connection.createStatement();
                    ResultSet resultSet = nameQuery.executeQuery(nameSQLQuery);

                    if (!resultSet.next()) {
                        // If no task with that name exists, we can finally insert:
                        if (!priority.getText().isBlank()) {
                            String sql = "INSERT INTO tasks (EntryDate, TaskName, ShortDesc, Deadline, Priority, HasReminderImage) VALUES (?, ?, ?, ?, ?, ?)";
                            PreparedStatement statement = connection.prepareStatement(sql);
                            statement.setDate(1, Date.valueOf(entryDate));
                            statement.setString(2, taskNameStr);
                            statement.setString(3, taskDescStr);
                            java.sql.Date sqlDeadline = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(deadlineStr).getTime());
                            statement.setDate(4, sqlDeadline);
                            statement.setInt(5, priorityNum);
                            statement.setInt(6, hasReminderImage ? 1 : 0);
                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                showMessageDialog(null, "Your task has created successfully.");
                                setVisible(false);
                                dispose();
                                ViewTasks.getInstance().dispose();
                            }
                        } else {
                            String sql = "INSERT INTO tasks (EntryDate, TaskName, ShortDesc, Deadline, HasReminderImage) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement statement = connection.prepareStatement(sql);
                            statement.setDate(1, Date.valueOf(entryDate));
                            statement.setString(2, taskNameStr);
                            statement.setString(3, taskDescStr);
                            java.sql.Date sqlDeadline = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(deadlineStr).getTime());
                            statement.setDate(4, sqlDeadline);
                            statement.setInt(5, hasReminderImage ? 1 : 0);
                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                showMessageDialog(null, "Your task has created successfully.");
                                setVisible(false);
                                dispose();
                                ViewTasks.getInstance().dispose();
                            }
                        }
                    }
                    else{
                        showMessageDialog(null, "There is already a task with this name. Please try another name.");
                    }
                }
                catch (SQLException sqlEx) {
                    throw new IllegalStateException("A problem occured while connecting database.", sqlEx);
                } catch (ParseException ex) {
                    showMessageDialog(null, "Invalid date type. Please type a date with dd/mm/yy structure. Example: 25/05/2023");
                }
                catch (NumberFormatException nfe) {
                    showMessageDialog(null, "Priority should be in range of 1-5.");
                }
            }
        });
    }

    }