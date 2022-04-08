package com.onaio.steps.components;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.util.ReflectionHelpers.getField;
import static org.robolectric.util.ReflectionHelpers.setField;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

public class TogglePasswordEditTextTest extends StepsTestRunner {

    private TogglePasswordEditText togglePasswordEditText;

    @Before
    public void setUp() {
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).get();
        togglePasswordEditText = spy(new TogglePasswordEditText(activity));
    }

    @Test
    public void testOnTouchShouldTogglePasswordWhenActionIsUp() {

        View view = mock(View.class);
        MotionEvent motionEvent = mock(MotionEvent.class);
        Drawable drawable = mock(Drawable.class);
        Rect rect = mock(Rect.class);

        when(motionEvent.getAction()).thenReturn(MotionEvent.ACTION_UP);
        when(motionEvent.getRawX()).thenReturn(100f);
        when(togglePasswordEditText.getRight()).thenReturn(100);
        when(togglePasswordEditText.getCompoundDrawables()).thenReturn(new Drawable[] {null, null, drawable});
        when(drawable.getBounds()).thenReturn(rect);
        when(rect.width()).thenReturn(10);
        when(togglePasswordEditText.getSelectionEnd()).thenReturn(10);

        doNothing().when(togglePasswordEditText).updateDrawable();
        doNothing().when(togglePasswordEditText).updateTransformationMethod();
        doNothing().when(togglePasswordEditText).setSelection(anyInt());

        setIsShowing(false);

        boolean result = togglePasswordEditText.onTouch(view, motionEvent);

        verify(togglePasswordEditText, times(1)).updateDrawable();
        verify(togglePasswordEditText, times(1)).updateTransformationMethod();
        verify(togglePasswordEditText, times(1)).setSelection(eq(10));
        assertTrue(getIsShowing());
        assertTrue(result);
    }

    @Test
    public void testOnTouchDoNothingWhenActionIsNotUp() {

        View view = mock(View.class);
        MotionEvent motionEvent = mock(MotionEvent.class);

        when(motionEvent.getAction()).thenReturn(MotionEvent.ACTION_DOWN);

        setIsShowing(false);

        boolean result = togglePasswordEditText.onTouch(view, motionEvent);

        verify(togglePasswordEditText, times(0)).updateDrawable();
        verify(togglePasswordEditText, times(0)).updateTransformationMethod();
        verify(togglePasswordEditText, times(0)).setSelection(anyInt());
        assertFalse(getIsShowing());
        assertFalse(result);
    }

    @Test
    public void testUpdateDrawableShouldShowFullIconDrawableOnFalseState() {

        setIsShowing(false);
        togglePasswordEditText.updateDrawable();

        verify(togglePasswordEditText, times(1)).setCompoundDrawablesRelativeWithIntrinsicBounds(eq(0), eq(0), eq(R.drawable.ic_baseline_visibility_24), eq(0));
    }

    @Test
    public void testUpdateDrawableShouldShowCrossIconDrawableOnTrueState() {

        setIsShowing(true);
        togglePasswordEditText.updateDrawable();

        verify(togglePasswordEditText, times(1)).setCompoundDrawablesRelativeWithIntrinsicBounds(eq(0), eq(0), eq(R.drawable.ic_baseline_visibility_off_24), eq(0));
    }

    @Test
    public void testUpdateTransformationMethodShouldShowPasswordOnFalseState() {

        setIsShowing(false);
        togglePasswordEditText.updateTransformationMethod();

        verify(togglePasswordEditText, times(1)).setTransformationMethod(any(HideReturnsTransformationMethod.class));
    }

    @Test
    public void testUpdateTransformationMethodShouldHidePasswordOnTrueState() {

        setIsShowing(true);
        togglePasswordEditText.updateTransformationMethod();

        verify(togglePasswordEditText, times(1)).setTransformationMethod(any(PasswordTransformationMethod.class));
    }

    private void setIsShowing(boolean isShowing) {
        setField(togglePasswordEditText, "isShowing", isShowing);
    }

    private boolean getIsShowing() {
        return getField(togglePasswordEditText, "isShowing");
    }
}
