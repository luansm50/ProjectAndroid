package com.castgroup.projeto.lm.cadastrodecursos;

import com.castgroup.projeto.lm.cadastrodecursos.utils.ValidateDate;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(true, ValidateDate.validateDate("20/09/2020"));

        assertEquals(true, ValidateDate.validateDate("12/01/2012"));
        assertEquals(true, ValidateDate.validateDate("01/12/2012"));
        assertEquals(true, ValidateDate.validateDate("13/12/2012"));
        assertEquals(true, ValidateDate.validateDate("13/01/2012"));
        assertEquals(false, ValidateDate.validateDate("12/32/2012"));
        assertEquals(true, ValidateDate.validateDate("14/01/2012"));
        assertEquals(false, ValidateDate.validateDate("15/43/2012"));
        assertEquals(false, ValidateDate.validateDate("02/28/2012"));
        assertEquals(false, ValidateDate.validateDate("02/29/2012"));
        assertEquals(false, ValidateDate.validateDate("02/29/2013"));
        assertEquals(false, ValidateDate.validateDate("02/29/2015"));
        assertEquals(false, ValidateDate.validateDate("02/29/2016"));
        assertEquals(false, ValidateDate.validateDate("02/30/2012"));
        assertEquals(false, ValidateDate.validateDate("02/29/2004"));
        assertEquals(false, ValidateDate.validateDate("02/31/2004"));
        assertEquals(false, ValidateDate.validateDate("14/30/2012"));
        assertEquals(false, ValidateDate.validateDate("01/32/2012"));
        assertEquals(true, ValidateDate.validateDate("13/03/2012"));
        assertEquals(true, ValidateDate.validateDate("10/12/2000"));
        assertEquals(false, ValidateDate.validateDate("43/35/2000"));
    }
}