package BusinessLogic;


import Model.Server;
import Model.Task;

import java.util.*;

public interface Strategy {
    void addTask(List<Server> servers, Task t);
}
