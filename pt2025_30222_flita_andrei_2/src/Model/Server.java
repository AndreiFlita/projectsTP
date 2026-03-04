package Model;

import BusinessLogic.SimulationManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private SimulationManager simulationManager;
    private int currentTime = 0;

    public Server(SimulationManager simulationManager) {
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.simulationManager = simulationManager;
    }

    public void addTask(Task newTask) {
        int calculatedWaitingTime = waitingPeriod.get();
        newTask.setWaitingTime(calculatedWaitingTime);
        simulationManager.addWaitingTime(calculatedWaitingTime);

        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Task currentTask = tasks.peek();
                if (currentTask != null) {
                    if (currentTask.getStartTime() == -1) {
                        currentTask.setStartTime(currentTime);
                    }
                    int remainingTime = currentTask.getServiceTime() - (currentTime - currentTask.getStartTime());
                    if (remainingTime <= 0) {
                        tasks.poll();
                        waitingPeriod.addAndGet(-currentTask.getServiceTime());
                        simulationManager.addServiceTime(currentTask.getServiceTime());
                        simulationManager.incrementCompletedTask();
                    }
                }
                currentTime++;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }
}
