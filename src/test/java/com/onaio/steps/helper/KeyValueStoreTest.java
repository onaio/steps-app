package com.onaio.steps.helper;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.MainActivityOrchestrator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class KeyValueStoreTest extends StepsTestRunner {

    private KeyValueStore keyValueStore;

    @Mock private Context context;
    @Mock private SharedPreferences sharedPreferences;
    @Mock private SharedPreferences.Editor editor;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(context.getSharedPreferences(anyString(), eq(MODE_PRIVATE))).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.clear()).thenReturn(editor);

        keyValueStore = new KeyValueStore(context);
    }

    @Test
    public void clearTestShouldClearAppSharedPreferences() {
        keyValueStore.clear(context);
        verify(editor, times(1)).clear();
        verify(editor, times(1)).apply();
    }

    @Test
    public void testGetPreferenceFileNameShouldReturnMainActivityOrchestratorFileName() {
        assertEquals("activities." + MainActivityOrchestrator.class.getSimpleName(), keyValueStore.getPreferenceFileName());
    }
}
