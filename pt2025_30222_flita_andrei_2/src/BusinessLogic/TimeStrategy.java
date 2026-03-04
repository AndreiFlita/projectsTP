package BusinessLogic;

import Model.Server;
import Model.Task;
import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        Server bestServer = servers.get(0);
        for (Server server : servers) {
            if (server.getWaitingPeriod().get() < bestServer.getWaitingPeriod().get()) {
                bestServer = server;
            }
        }
        bestServer.addTask(t);
    }
}
