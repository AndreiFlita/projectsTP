package BusinessLogic;

import Model.Server;
import Model.Task;
import GUI.SimulationFrame;

import java.io.IOException;
import java.util.*;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int minProcessingTime;
    private int maxProcessingTime;
    private int numberOfServers;
    private int numberOfClients;
    private SelectionPolicy selectionPolicy;

    private double totalWaitingTime = 0;
    private double totalServiceTime = 0;
    private int taskCount = 0;
    private int peakHour = -1;
    private int maxTasksInSystem = 0;
    private Map<Integer, Integer> tasksPerHour = new HashMap<>();
    private int minArrivalTime;
    private int maxArrivalTime;
    private static int globalCurrentTime = 0;

    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Task> generatedTasks;

    public SimulationManager(int timeLimit, int minProcessingTime, int maxProcessingTime,
                             int numberOfServers, int numberOfClients, SelectionPolicy selectionPolicy,
                             int minArrivalTime, int maxArrivalTime) {
        this.timeLimit = timeLimit;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.selectionPolicy = selectionPolicy;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;

        System.out.println("Simulation initialized with " + numberOfServers + " servers.");
        System.out.println("Selection policy: " + selectionPolicy);

        this.scheduler = new Scheduler(numberOfServers, numberOfClients, this);
        scheduler.changeStrategy(selectionPolicy);
        this.frame = new SimulationFrame();
        this.generatedTasks = generateNRandomTasks();
        try {
            FileLogger.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Task> generateNRandomTasks() {
        List<Task> tasks = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < numberOfClients; i++) {
            int arrivalTime = minArrivalTime + rand.nextInt(maxArrivalTime - minArrivalTime + 1);
            int serviceTime = minProcessingTime + rand.nextInt(maxProcessingTime - minProcessingTime + 1);
            tasks.add(new Task(i + 1, arrivalTime, serviceTime));
        }
        tasks.sort(Comparator.comparingInt(Task::getArrivalTime));
        return tasks;
    }

    @Override
    public void run() {
        int currentTime = 0;
        while (currentTime < timeLimit) {
            System.out.println("Current time: " + currentTime);
            Iterator<Task> iterator = generatedTasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (task.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(task);
                    System.out.println("Dispatched task " + task.getId() + " at time " + currentTime);
                    iterator.remove();
                }
            }

            int tasksInSystem = 0;
            for (Server server : scheduler.getServers()) {
                tasksInSystem += server.getTasks().size();
            }

            if (tasksInSystem > maxTasksInSystem) {
                maxTasksInSystem = tasksInSystem;
                peakHour = currentTime;
            }

            tasksPerHour.put(currentTime, tasksInSystem);

            try {
                FileLogger.logEvent(currentTime, new ArrayList<>(generatedTasks), scheduler.getServers());
            } catch (IOException e) {
                e.printStackTrace();
            }

            frame.updateUI(scheduler.getServers(), currentTime);
            currentTime++;
            globalCurrentTime=currentTime;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        printMetrics();
    }

    public synchronized void addServiceTime(int serviceTime) {
        totalServiceTime += serviceTime;
    }

    public synchronized void addWaitingTime(int waitingTime) {
        totalWaitingTime += waitingTime;
    }

    public synchronized void incrementCompletedTask() {
        taskCount++;
    }

    public static int getCurrentTime() {
            return globalCurrentTime;
    }


    private void printMetrics() {
        double avgServiceTime = (taskCount > 0) ? totalServiceTime / taskCount : 0;
        double avgWaitingTime = totalWaitingTime / numberOfClients;

        System.out.println("Average Service Time: " + avgServiceTime);
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Peak Hour: " + peakHour);

        try {
            FileLogger.logResults(avgWaitingTime, avgServiceTime, peakHour);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

