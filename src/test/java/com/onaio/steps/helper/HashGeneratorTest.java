package com.onaio.steps.helper;

import static org.junit.Assert.assertEquals;

import com.onaio.steps.StepsTestRunner;

import org.junit.Test;

public class HashGeneratorTest extends StepsTestRunner {

    @Test
    public void testGenerateShouldReturnMD5Hash() {
        String text = "test";
        String md5Hash = "098f6bcd4621d373cade4e832627b4f6";
        assertEquals(md5Hash, HashGenerator.generate(text, HashGenerator.HashStrategy.MD5));
    }

    @Test
    public void testGenerateShouldReturnSHA256Hash() {
        String text = "test";
        String md5Hash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        assertEquals(md5Hash, HashGenerator.generate(text, HashGenerator.HashStrategy.SHA_256));
    }
}
