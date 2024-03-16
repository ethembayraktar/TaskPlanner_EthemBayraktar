import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class TaskImage { // extends JFrame

    private JPanel panel;
    private JFrame frame;
    private JLabel labelForImage;
    private ImageIcon imageIcon;

    TaskImage(int taskId)
    {
        frame = new JFrame();
        panel = new JPanel();
        frame.add(panel);
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/task_images/task_" + taskId + ".png")));
        labelForImage = new JLabel(imageIcon);
        panel.add(labelForImage);
        frame.setTitle("Task " + taskId);
        frame.setContentPane(panel);
        frame.setResizable(false);
        frame.setSize(220, 220);
        frame.setVisible(true);
    }
}
