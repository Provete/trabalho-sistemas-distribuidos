package org.example;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import org.example.Message;

public class Process
{
    static Random rand = new Random();

    protected final int internalId;
    private final LinkedBlockingQueue<Message> messageQueue;
    public Thread consumerThread;
    public Thread producerThread;
    public Thread localEventThread;
    private final VectorClock internalClock;


    public Process(int id, int numProcesses)
    {
        internalId = id;
        messageQueue = new LinkedBlockingQueue<>();
        internalClock = new VectorClock(numProcesses);

        consumerThread = new Thread(this::consume);
        producerThread = new Thread(this::produce);
        localEventThread = new Thread(this::localEvent);
    }

    private void consume()
    {
        while(true)
        {

            if(!messageQueue.isEmpty())
            {
                Message m;
                try
                {
                    int randomReceivedMessageProcessingTime = rand.nextInt(70);
                    log("Executing message in queue");
                    m = messageQueue.take();
                    internalClock.update(m.clock);
                    internalClock.clock[internalId]++;

                    Thread.sleep(10 + randomReceivedMessageProcessingTime);
                } catch (InterruptedException e) { e.printStackTrace(); }
            } else {
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {e.printStackTrace();}
            }

        }
    }

    private void produce()
    {
        while(true)
        {
            int randomProcessId = rand.nextInt(ProcessManager.processes.length);
            if (randomProcessId == internalId) continue;
            sendMessageTo(ProcessManager.processes[randomProcessId]);

            int randomTimeBetweenMessages = rand.nextInt(3000);
            try {
                Thread.sleep(50 + randomTimeBetweenMessages);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    protected void receiveMessage(final Message m)
    {
        try {
            messageQueue.put(m);
        } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void sendMessageTo(Process other)
    {
        log("Sending message to [p " + other.internalId + "]");
        internalClock.clock[internalId]++;
        Message m = new Message(internalClock);
        other.receiveMessage(m);
    }

    private void localEvent(){
        while(true)
        {
            int randomProcessTime = rand.nextInt(1000);
            log("Local Event");
            internalClock.clock[internalId]++;
            try {
                Thread.sleep(500 + randomProcessTime);
            } catch (InterruptedException e) {e.printStackTrace();}
        }
    }

    private void log(String status)
    {
        System.out.println("[P " + internalId + "]: " + status);
        System.out.println(internalClock.toString());
        System.out.println();
    }
}