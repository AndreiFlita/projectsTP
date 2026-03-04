package Model;

import BusinessLogic.SimulationManager;

public class Task {
    private int id;
    private int arrivalTime;
    private int serviceTime;
    private int startTime = -1;
    private int waitingTime;
    private transient SimulationManager simulationManager;


    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.waitingTime = 0;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }


    @Override
    public String toString() {
        if (startTime != -1) {
            int remainingTime = serviceTime - (SimulationManager.getCurrentTime() - startTime);
            return "[" + id + "," + arrivalTime + "," + remainingTime + "/" + serviceTime + "]";
        }
        return "(" + id + "," + arrivalTime + "," + serviceTime + ")";
    }

    public int getStartTime() {
        return startTime;
    }
}
