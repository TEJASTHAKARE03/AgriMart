package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the CalculationService.
 */
@SpringBootTest
public class CalculationServiceTest {

    @Autowired
    private CalculationService calculationService;

    @Test
    public void testAdd() {
        // Given
        int a = 5;
        int b = 3;
        
        // When
        int result = calculationService.add(a, b);
        
        // Then
        assertEquals(8, result, "5 + 3 should equal 8");
    }

    @Test
    public void testMultiply() {
        // Given
        int a = 4;
        int b = 7;
        
        // When
        int result = calculationService.multiply(a, b);
        
        // Then
        assertEquals(28, result, "4 * 7 should equal 28");
    }
}