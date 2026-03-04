package GUI;

import Model.Server;
import Model.Task;

import javax.swing.*;
import java.util.*;

public class SimulationFrame extends JFrame {
    private JTextArea textArea;

    public SimulationFrame() {
        setTitle("Queue Simulation");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea();
        add(new JScrollPane(textArea));
        setVisible(true);
    }

    public void updateUI(List<Server> servers, int currentTime) {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("Time: " + currentTime + "\n");
            for (int i = 0; i < servers.size(); i++) {
                textArea.append("Queue " + (i + 1) + ": ");
                if (servers.get(i).getTasks().isEmpty()) {
                    textArea.append("closed\n");
                } else {
                    for (Task task : servers.get(i).getTasks()) {
                        textArea.append(task.toString() + "; ");
                    }
                    textArea.append("\n");
                }
            }
        });
    }
}
