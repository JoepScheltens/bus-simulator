package bussimulator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Joep Scheltens on 02-01-2021.
 */
class RunnerTest {
    @Test
    public void whenInitBussenIsCalledThenNewBusInList(){
        Runner runner = new Runner();
        assertNotEquals(0, Runner.initBussen());
    }
}