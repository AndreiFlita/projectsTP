# PT2025_30222_Flita_Andrei_2



## Getting started

To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Already a pro? Just edit this README.md and make it your own. Want to make it easy? [Use the template at the bottom](#editing-this-readme)!

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/topics/git/add_files/#add-files-to-a-git-repository) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://gitlab.com/pt2025_30222_flita_andrei/pt2025_30222_flita_andrei_2.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](https://gitlab.com/pt2025_30222_flita_andrei/pt2025_30222_flita_andrei_2/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Set auto-merge](https://docs.gitlab.com/user/project/merge_requests/auto_merge/)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing (SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***
## PT2025_30222_Flita_Andrei_2 - Java Queue Simulator

## General Structure
Packages:

BusinessLogic – Application logic (simulation manager, strategies, logging)

Model – Core classes (Task, Server)

GUI – Graphical interface (InputFrame, SimulationFrame)
## How It Works
1.Input Data – The user enters the simulation parameters in the InputFrame window.

2.Client Generation – Clients (Task) are randomly generated with arrival and service times within the selected intervals.

3.Simulation – Clients are distributed to queues (Server) based on a selected policy:

  -> SHORTEST_QUEUE – the queue with the fewest clients

  -> SHORTEST_TIME – the queue with the lowest estimated waiting time

4.Visualization & Logging – The system evolution is displayed in a window and saved to the file simulation_log.txt.
## Input Parameters
Time Limit – Total simulation time (in seconds)

Min/Max Processing – Time interval for processing a client

Number of Servers – Number of active queues

Number of Clients – Total number of clients to be generated

Min/Max Arrival – Interval for client arrival time

Selection Policy – Allocation policy: SHORTEST_QUEUE / SHORTEST_TIME
## Output & Metrics
All events and results are written to simulation_log.txt, including:

Clients waiting and queue states at each second

Average waiting time

Average processing time

Peak hour (moment with the highest number of tasks in the system)
## Key Files
Main.java – Entry point

SimulationManager.java – Controls the entire simulation

Server.java – Individual queue processing clients

Task.java – A client

Scheduler.java – Distributes tasks to queues

FileLogger.java – Saves events to the log file

InputFrame.java – Interface for entering data

SimulationFrame.java – Interface displaying queues in real time
## How to Run the Application
1.Compile all .java files.

2.Run the Main class.

3.Fill in the fields in the GUI and press “Start Simulation.”

4.Observe the evolution of queues in the simulation window.

5.Check simulation_log.txt for the complete log.
##  Requirements
Java 8 or later

IDE like IntelliJ IDEA, Eclipse, or command-line execution
## Possible Improvements
Graphical display of waiting/service times

Introducing client priorities