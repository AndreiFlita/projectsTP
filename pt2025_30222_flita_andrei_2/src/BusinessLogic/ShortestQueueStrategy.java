package BusinessLogic;

import Model.Server;
import Model.Task;
import java.util.*;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        Server bestServer = servers.get(0);
        for (Server server : servers) {
            if (server.getTasks().size() < bestServer.getTasks().size()) {
                bestServer = server;
            }
        }
        bestServer.addTask(t);
    }
}
