package se.miun.android_app.EmployeeUnit;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jony1 on 2017-12-09.
 */
public class RollingAverageTest {

    @Test
    public void setRssiStorage() throws Exception {
        RollingAverage rollingAverageTest = new RollingAverage("00:11:FF:FE:FE:FE", 10);

        int[] expected = new int[]{
                0, -5, -10, -15, -20, -25, -30, -35, -40, -45
        };

        for (int i = 0; i < 10; i++) {
            rollingAverageTest.setRssiStorage(-5*i);
        }

        assertArrayEquals(expected, rollingAverageTest.getRssiStorage());
    }

    @Test
    public void getAverageRssi() throws Exception {
    }

}