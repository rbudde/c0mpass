package com.gmail.rwbudde.app_c0mpass;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DegreeTest {
    @Test
    public void testDegreeForView() {
        assertEquals(0, MainActivity.degree2view(0));
        assertEquals(10, MainActivity.degree2view(10));
        assertEquals(160, MainActivity.degree2view(160));
        assertEquals(180, MainActivity.degree2view(180));
        assertEquals(-160, MainActivity.degree2view(200));
        assertEquals(-20, MainActivity.degree2view(340));
    }

    @Test
    public void testDegreeOfOpposite() {
        assertEquals(180, MainActivity.degreeOfOpposite(0));
        assertEquals(190, MainActivity.degreeOfOpposite(10));
        assertEquals(340, MainActivity.degreeOfOpposite(160));
        assertEquals(0, MainActivity.degreeOfOpposite(180));
        assertEquals(20, MainActivity.degreeOfOpposite(200));
        assertEquals(160, MainActivity.degreeOfOpposite(340));
    }

    @Test
    public void testDeltaWithDirection() {
        // from < opposite from from
        assertEquals(20, MainActivity.deltaWithDirection(20,40));
        assertEquals(140, MainActivity.deltaWithDirection(20,160));
        assertEquals(180, MainActivity.deltaWithDirection(20,200));
        assertEquals(-70, MainActivity.deltaWithDirection(20,310));
        assertEquals(-30, MainActivity.deltaWithDirection(70,40));
        assertEquals(0, MainActivity.deltaWithDirection(70,70));
        assertEquals(90, MainActivity.deltaWithDirection(70,160));
        assertEquals(140, MainActivity.deltaWithDirection(70,210));
        assertEquals(-90, MainActivity.deltaWithDirection(70,340));

        // from > opposite from from
        assertEquals(-160, MainActivity.deltaWithDirection(200,40));
        assertEquals(-40, MainActivity.deltaWithDirection(200,160));
        assertEquals(0, MainActivity.deltaWithDirection(200,200));
        assertEquals(110, MainActivity.deltaWithDirection(200,310));
        assertEquals(60, MainActivity.deltaWithDirection(340,40));
        assertEquals(180, MainActivity.deltaWithDirection(340,160));
        assertEquals(-130, MainActivity.deltaWithDirection(340,210));

        assertEquals(45, MainActivity.deltaWithDirection(0,45));
    }
}