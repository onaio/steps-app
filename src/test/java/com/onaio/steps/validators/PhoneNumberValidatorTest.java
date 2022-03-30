package com.onaio.steps.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import androidx.appcompat.widget.AppCompatEditText;

import com.onaio.steps.StepsTestRunner;

import org.junit.Test;

public class PhoneNumberValidatorTest extends StepsTestRunner {

    @Test
    public void testValidateDefaultAndCustomLength() {

        AppCompatEditText editText = mock(AppCompatEditText.class);

        when(editText.length()).thenReturn(PhoneNumberValidator.DEFAULT_LENGTH);
        assertTrue(new PhoneNumberValidator(editText).validate());

        when(editText.length()).thenReturn(6);
        assertFalse(new PhoneNumberValidator(editText).validate());

        when(editText.length()).thenReturn(8);
        assertFalse(new PhoneNumberValidator(editText).validate());

        when(editText.length()).thenReturn(8);
        assertTrue(new PhoneNumberValidator(editText, 7, 11).validate());

        when(editText.length()).thenReturn(11);
        assertTrue(new PhoneNumberValidator(editText, 7, 11).validate());

        when(editText.length()).thenReturn(12);
        assertFalse(new PhoneNumberValidator(editText, 7, 11).validate());
    }
}
