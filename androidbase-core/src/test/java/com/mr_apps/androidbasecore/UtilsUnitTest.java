package com.mr_apps.androidbasecore;

import com.mr_apps.androidbase.utils.Utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilsUnitTest {

    @Test
    public void checkIfStringIsNullOrEmpty() throws Exception {
        assertTrue(Utils.isNullOrEmpty(""));
        assertTrue(Utils.isNullOrEmpty(null));
        assertTrue(Utils.isNullOrEmpty("   "));
    }

    @Test
    public void checkIfStringIsNotNullNorEmpty() throws Exception {
        assertFalse(Utils.isNullOrEmpty("Test"));
        assertFalse(Utils.isNullOrEmpty(" T "));
        assertFalse(Utils.isNullOrEmpty("  Test"));
        assertFalse(Utils.isNullOrEmpty("Test test"));
    }

    @Test
    public void checkStringContainsAnotherString() throws Exception {
        assertTrue(Utils.containsIgnoreCase("Test", "Test"));
        assertTrue(Utils.containsIgnoreCase("Test", "Te"));
    }

    @Test
    public void checkStringContainsAnotherStringIgnoreCase() throws Exception {
        assertTrue(Utils.containsIgnoreCase("Test", "test"));
        assertTrue(Utils.containsIgnoreCase("Test", "te"));
    }

    @Test
    public void checkStringNotContainsAnotherString() throws Exception {
        assertFalse(Utils.containsIgnoreCase("Test", "Test2"));
        assertFalse(Utils.containsIgnoreCase("Test", "Te "));
    }

    @Test
    public void checkSubstringIsCorrect() throws Exception {
        String testString = "This is a test";
        assertEquals("Thi", Utils.subString(testString, 3));
        assertEquals("", Utils.subString(testString, 0));
        assertEquals(testString, Utils.subString(testString, testString.length()));
    }

    @Test
    public void checkSameStringIfSubstringIndexOverflow() throws Exception {
        String testString = "This is a test";
        assertEquals(testString, Utils.subString(testString, 30));
    }
}