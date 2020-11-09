package com.cast.test.cadastro.curso;

import com.cast.test.cadastro.curso.utils.ValidateDate;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(true, ValidateDate.validateDate("20/10/2020"));
        assertEquals(false, ValidateDate.validateDate("10/32/2020"));
    }
}