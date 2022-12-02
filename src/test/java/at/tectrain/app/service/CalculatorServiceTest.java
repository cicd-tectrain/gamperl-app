package at.tectrain.app.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorServiceTest {

    CalculatorService service = new CalculatorService();

    @Test
    void testAdd() {
        int result = service.add(1, 2);
        assertEquals(1, result);
    }

    @Test
    void testSubtract() {
        int result = service.subtract(1, 2);
        assertEquals(-1, result);
    }

    @Test
    void testMultiply() {
        int result = service.multiply(1, 2);
        assertEquals(2, result);
    }

    @Test
    void testDivide() {
        int result = service.divide(1, 2);
        assertEquals(0, result);
    }

}
