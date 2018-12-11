package com.example.cs121.flarevisualizer;

import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class FlareDatabaseAbstractUnitTest {

    FlareDatabaseAbstract flare;

    @Test
    public void addFlareDatabaseAbstract() {
        flare = new FlareDatabaseAbstract();

        // before flare was added
        String output = flare.getStartTime();
        assertEquals(output, "");
        output = flare.getEndTime();
        assertEquals(output, "");
        int avgOutput = flare.getAvg_pain();
        assertEquals(avgOutput, 0);
        output = flare.getDbId();
        assertEquals(output, "");

        int time = (int) (System.currentTimeMillis());
        Timestamp ts = new Timestamp(time);
        flare.addFlareDatabaseAbstract(ts, ts, 10, "alpha");

        // after flare was added
        output = flare.getStartTime();
        assertEquals(output, ts.toString());
        output = flare.getEndTime();
        assertEquals(output, ts.toString());
        avgOutput = flare.getAvg_pain();
        assertEquals(avgOutput, 10);
        output = flare.getDbId();
        assertEquals(output, "alpha");

    }

    @Test
    public void setAndGetStartTime() {
        flare = new FlareDatabaseAbstract();
        String output = flare.getStartTime();
        assertEquals(output, "");
        flare.setStartTime("foo");
        output = flare.getStartTime();
        assertEquals(output, "foo");
    }

    @Test
    public void setAndGetEndTime() {
        flare = new FlareDatabaseAbstract();
        String output = flare.getEndTime();
        assertEquals(output, "");
        flare.setEndTime("bar");
        output = flare.getEndTime();
        assertEquals(output, "bar");
    }

    @Test
    public void setAndAvg_pain() {
        flare = new FlareDatabaseAbstract();
        int output = flare.getAvg_pain();
        assertEquals(output, 0);
        flare.setAvg_pain(10);
        output = flare.getAvg_pain();
        assertEquals(output, 10);
    }

    @Test
    public void setAndGetDbId() {
        flare = new FlareDatabaseAbstract();
        String output = flare.getDbId();
        assertEquals(output, "");
        flare.setDbId("baz");
        output = flare.getDbId();
        assertEquals(output, "baz");
    }
}