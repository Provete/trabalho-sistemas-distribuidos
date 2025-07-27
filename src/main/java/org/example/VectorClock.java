package org.example;

import java.lang.Math;
import java.util.Arrays;

public final class VectorClock implements Cloneable {
    public int[] clock;

    public VectorClock(int amount_processes)
    {
        clock = new int[amount_processes];
    }

    // Both internal and other.clock must be the same size
    public void update(final VectorClock other)
    {
        for(int i = 0; i < clock.length; i++)
        {
            clock[i] = Math.max(clock[i], other.clock[i]);
        }
    }

    @Override
    public VectorClock clone() throws CloneNotSupportedException
    {
        VectorClock vectorClockClone = (VectorClock) super.clone();
        vectorClockClone.clock = clock.clone();

        return vectorClockClone;
    }

    @Override
    public String toString()
    {
        return Arrays.toString(clock);
    }
}
