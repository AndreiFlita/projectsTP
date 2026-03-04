package GUI;

import BusinessLogic.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InputFrame extends JFrame {
    private JTextField timeLimitField = new JTextField(5);
    private JTextField minProcTimeField = new JTextField(5);
    private JTextField maxProcTimeField = new JTextField(5);
    private JTextField numServersField = new JTextField(5);
    private JTextField numClientsField = new JTextField(5);
    private JTextField minArrivalField = new JTextField(5);
    private JTextField maxArrivalField = new JTextField(5);
    private JComboBox<SelectionPolicy> policyBox = new JComboBox<>(SelectionPolicy.values());
    private JButton startButton = new JButton("Start Simulation");

    public InputFrame() {
        setTitle("Simulation Input");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 2, 5, 5));

        add(new JLabel("Time Limit:")); add(timeLimitField);
        add(new JLabel("Min Processing Time:")); add(minProcTimeField);
        add(new JLabel("Max Processing Time:")); add(maxProcTimeField);
        add(new JLabel("Number of Servers:")); add(numServersField);
        add(new JLabel("Number of Clients:")); add(numClientsField);
        add(new JLabel("Min Arrival Time:")); add(minArrivalField);
        add(new JLabel("Max Arrival Time:")); add(maxArrivalField);
        add(new JLabel("Selection Policy:")); add(policyBox);
        add(startButton);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int timeLimit = Integer.parseInt(timeLimitField.getText());
                    int minProc = Integer.parseInt(minProcTimeField.getText());
                    int maxProc = Integer.parseInt(maxProcTimeField.getText());
                    int numServers = Integer.parseInt(numServersField.getText());
                    int numClients = Integer.parseInt(numClientsField.getText());
                    int minArrival = Integer.parseInt(minArrivalField.getText());
                    int maxArrival = Integer.parseInt(maxArrivalField.getText());
                    SelectionPolicy policy = (SelectionPolicy) policyBox.getSelectedItem();

                    SimulationManager sim = new SimulationManager(
                            timeLimit, minProc, maxProc, numServers, numClients,
                            policy, minArrival, maxArrival
                    );
                    Thread t = new Thread(sim);
                    t.start();

                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(InputFrame.this, "Please enter valid numbers.");
                }
            }
        });

        setVisible(true);
    }
}

