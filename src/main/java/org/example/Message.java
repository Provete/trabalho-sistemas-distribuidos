package org.example;

public class Message
{
    public VectorClock clock;

    public Message(final VectorClock clock)
    {
        try{
            this.clock = clock.clone();
        } catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

    }
}
