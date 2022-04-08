package com.onaio.steps.decorators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class StepsFileDecoratorTest {

    private StepsFileDecorator stepsFileDecorator;

    @Before
    public void setUp() {
        File file = mock(File.class);
        when(file.getAbsolutePath()).thenReturn("dummy_path");

        stepsFileDecorator = new StepsFileDecorator(file);
        stepsFileDecorator.setFormTitle("A");
    }

    @Test
    public void testGetFileShouldReturnValidFile() {
        File file = stepsFileDecorator.getFile();

        assertNotNull(file);
        assertEquals("dummy_path", file.getAbsolutePath());
    }

    @Test
    public void testGetFormTitleShouldReturnValidFormTitle() {
        assertEquals("A", stepsFileDecorator.getFormTitle());
    }
}
