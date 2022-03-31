package com.onaio.steps.components;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.onaio.steps.R;

public class TogglePasswordEditText extends AppCompatEditText implements View.OnTouchListener {

    private boolean isShowing = false;

    public TogglePasswordEditText(@NonNull Context context) {
        super(context);
    }

    public TogglePasswordEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TogglePasswordEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int right = 2;

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (motionEvent.getRawX() >= (getRight() - getCompoundDrawables()[right].getBounds().width())) {
                int selection = getSelectionEnd();
                toggleDrawable();
                toggleTransformationMethod();
                setSelection(selection);
                isShowing = !isShowing;
                return true;
            }
        }

        return false;
    }

    public void toggleDrawable() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, isShowing ? R.drawable.ic_baseline_visibility_off_24 : R.drawable.ic_baseline_visibility_24, 0);
    }

    public void toggleTransformationMethod() {
        setTransformationMethod(isShowing ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
    }
}
