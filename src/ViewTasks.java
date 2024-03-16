import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

public class ViewTasks extends JFrame {
    private JList<String> list1;
    private JPanel panel;
    private JButton detailButton;
    private JButton editUpdateButton;
    private JButton deleteTaskButton;
    private JLabel noTasks;
    private JLabel explanation;
    private JButton a️ViewImageButton;
    private JButton a️CreateNewTaskButton;

    private static ViewTasks instance; // Singleton

    public static ViewTasks getInstance() {
        if (instance == null) {
            instance = new ViewTasks();
        }
        return instance;
    }

    public void loadDataFromDatabase()
    {
        noTasks.setVisible(false);
        noTasks.setFont(new Font("Arial", Font.PLAIN, 13));
        try (Connection connection = DriverManager.getConnection(Main.sqlData[0], Main.sqlData[1], Main.sqlData[2])) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TaskName FROM tasks");
            ArrayList<String> taskList = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("TaskName");
                taskList.add(name);
            }
            DefaultListModel<String> model = new DefaultListModel<>();
            for (String s : taskList) {
                model.addElement(s);
            }
            list1.setModel(model);
            if (list1.getModel().getSize() == 0) {
                noTasks.setVisible(true);
            }
        }
        catch (SQLException sqlEx) {
            throw new IllegalStateException("A problem occured while connecting database.", sqlEx);
        }
    }

    public void setVisible(boolean visible) {
        if (visible) {
            loadDataFromDatabase();
        }
        super.setVisible(visible);
    }

    public ViewTasks()
    {
        explanation.setFont(new Font("Arial", Font.PLAIN, 13));
        noTasks.setForeground(Color.RED);
        setTitle("My Tasks");
        setSize(850,450);
        setContentPane(panel);
        setResizable(false);
        setVisible(true);
        deleteTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = list1.getSelectedIndex();
                if (selectedIndex != -1) {
                    int choice = JOptionPane.showConfirmDialog(null, "Are you sure to delete this task?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        try (Connection connection = DriverManager.getConnection(Main.sqlData[0], Main.sqlData[1], Main.sqlData[2])) {
                            Object selectedObject = list1.getModel().getElementAt(selectedIndex);
                            String selectedTaskName = selectedObject.toString();
                            String sql = "DELETE FROM tasks WHERE TaskName = ?";
                            PreparedStatement statement = connection.prepareStatement(sql);
                            statement.setString(1, selectedTaskName);
                            int rowsDeleted = statement.executeUpdate();
                            if (rowsDeleted > 0) {
                                showMessageDialog(null, "This task has successfully deleted.");
                                setVisible(false);
                                dispose();
                            }
                        } catch (SQLException sqlEx) {
                            showMessageDialog(null, sqlEx);
                        }
                    }
                }
                else {
                    showMessageDialog(null, "Please select a task.");
                }
            }
            });
        editUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = list1.getSelectedIndex();
                if (selectedIndex != -1) {
                    Object selectedObject = list1.getModel().getElementAt(selectedIndex);
                    String selectedTaskName = selectedObject.toString();
                    try (Connection connection = DriverManager.getConnection(Main.sqlData[0], Main.sqlData[1], Main.sqlData[2])) {
                        String nameSQLQuery = "SELECT * FROM tasks WHERE TaskName = '"+selectedTaskName+"'";
                        Statement nameQuery = connection.createStatement();
                        ResultSet resultSet = nameQuery.executeQuery(nameSQLQuery);
                        while (resultSet.next()) {
                            int taskId = resultSet.getInt("Id");
                            new UpdateTask(selectedTaskName , taskId);
                        }
                    }
                    catch (SQLException sqlEx) {
                        throw new IllegalStateException("A problem occured while connecting database.", sqlEx);
                    }

                }
                else {
                    showMessageDialog(null, "Please select a task.");
                }
            }
        });
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = list1.getSelectedIndex();
                if (selectedIndex != -1) {
                    Object selectedObject = list1.getModel().getElementAt(selectedIndex);
                    String selectedTaskName = selectedObject.toString();
                    try (Connection connection = DriverManager.getConnection(Main.sqlData[0], Main.sqlData[1], Main.sqlData[2])) {
                        String nameSQLQuery = "SELECT * FROM tasks WHERE TaskName = '"+selectedTaskName+"'";
                        Statement nameQuery = connection.createStatement();
                        ResultSet resultSet = nameQuery.executeQuery(nameSQLQuery);
                        while (resultSet.next()) {
                            new TaskDetail(selectedTaskName);
                        }
                    }
                    catch (SQLException sqlEx) {
                        throw new IllegalStateException("A problem occured while connecting database.", sqlEx);
                    }
                }
                else {
                    showMessageDialog(null, "Please select a task.");
                }
            }
        });
        a️ViewImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = list1.getSelectedIndex();
                if (selectedIndex != -1) {
                    Object selectedObject = list1.getModel().getElementAt(selectedIndex);
                    String selectedTaskName = selectedObject.toString();
                    try (Connection connection = DriverManager.getConnection(Main.sqlData[0], Main.sqlData[1], Main.sqlData[2])) {
                        String nameSQLQuery = "SELECT * FROM tasks WHERE TaskName = '"+selectedTaskName+"'";
                        Statement nameQuery = connection.createStatement();
                        ResultSet resultSet = nameQuery.executeQuery(nameSQLQuery);
                        while (resultSet.next()) {
                            boolean hasReminderImg = resultSet.getBoolean("HasReminderImage");
                            if(hasReminderImg)
                            {
                                int taskId = resultSet.getInt("Id");
                                new TaskImage(taskId);
                            }
                            else
                                showMessageDialog(null, "This task hasn't got any image.");
                        }
                    }
                    catch (SQLException sqlEx) {
                        throw new IllegalStateException("A problem occured while connecting database.", sqlEx);
                    }
                }
                else {
                    showMessageDialog(null, "Please select a task.");
                }
            }
        });
        a️CreateNewTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddTask.getInstance().setVisible(true);
                // Applying singleton - this form should not appear more than once
            }
        });
    }
    }
