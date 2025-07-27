package org.example;

public abstract class ProcessManager
{
    public static Process[] processes;

    public static  void start(int number_processes)
    {
        System.out.println("Started simulating with" + number_processes + "interconnected processes");
        processes = new Process[number_processes];

        for(int i = 0; i < number_processes; i++)
        {
            processes[i] = new Process(i, number_processes);
        }

        for(Process p : processes)
        {
            p.consumerThread.start();
            p.producerThread.start();
            p.localEventThread.start();
        }
    }
}
