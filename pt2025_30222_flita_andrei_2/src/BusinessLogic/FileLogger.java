package BusinessLogic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import Model.Server;
import Model.Task;

public class FileLogger {
    private static FileWriter writer;

    public static void initialize() throws IOException {
            writer = new FileWriter("simulation_log.txt", false);
            writer.write("\n===== Simulation " + " =====\n");
    }

    public static synchronized void logEvent(int currentTime, List<Task> waitingClients, List<Server> servers) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Time ").append(currentTime).append("\n");

        sb.append("Waiting clients: ");
        if (waitingClients.isEmpty()) {
            sb.append("none");
        } else {
            for (Task task : waitingClients) {
                sb.append(task).append("; ");
            }
        }
        sb.append("\n");

        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);
            sb.append("Queue ").append(i + 1).append(": ");
            if (server.getTasks().isEmpty()) {
                sb.append("closed");
            } else {
                for (Task task : server.getTasks()) {
                    sb.append(task.toString()).append("; ");
                }
            }
            sb.append("\n");
        }
        sb.append("\n");

        writer.write(sb.toString());
        writer.flush();
    }

    public static synchronized void logResults(double avgWaitingTime, double avgServiceTime, int peakHour) throws IOException {
        StringBuilder sb = new StringBuilder("\nSimulation Results:\n");
        sb.append("Average Waiting Time: ").append(String.format("%.2f", avgWaitingTime)).append("\n");
        sb.append("Average Service Time: ").append(String.format("%.2f", avgServiceTime)).append("\n");
        sb.append("Peak Hour: ").append(peakHour).append("\n");
        sb.append("----------------------------------------\n");

        writer.write(sb.toString());
        writer.close();
    }
}
