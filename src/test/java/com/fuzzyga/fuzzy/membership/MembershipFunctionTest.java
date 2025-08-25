package com.fuzzyga.fuzzy.membership;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MembershipFunctionTest {

    @Test
    @DisplayName("TriangularMembershipFunction should calculate correct membership values")
    void testTriangularMembershipFunction() {
        // A triangle centered at 10 with a width of 5.
        // The points are: left=5, center=10, right=15.
        MembershipFunction mf = new MembershipFunction.TriangularMembershipFunction(10, 5);
        double delta = 1e-6; // Delta for double comparison

        // Test points outside the triangle
        assertEquals(0.0, mf.getMembership(4.999), delta, "Value to the left of the triangle should be 0");
        assertEquals(0.0, mf.getMembership(15.001), delta, "Value to the right of the triangle should be 0");

        // Test boundaries
        assertEquals(0.0, mf.getMembership(5.0), delta, "Value at the left boundary should be 0");
        assertEquals(0.0, mf.getMembership(15.0), delta, "Value at the right boundary should be 0");


        // Test the peak
        assertEquals(1.0, mf.getMembership(10.0), delta, "Value at the center should be 1");

        // Test the left slope (between 5 and 10)
        assertEquals(0.5, mf.getMembership(7.5), delta, "Value halfway up the left slope should be 0.5");
        assertEquals(0.2, mf.getMembership(6.0), delta, "Value on left slope");

        // Test the right slope (between 10 and 15)
        assertEquals(0.5, mf.getMembership(12.5), delta, "Value halfway down the right slope should be 0.5");
        assertEquals(0.8, mf.getMembership(11.0), delta, "Value on right slope");
    }

    @Test
    @DisplayName("TriangularMembershipFunction should handle zero width correctly")
    void testZeroWidthTriangularMembershipFunction() {
        // A triangle with zero width is a crisp singleton set.
        MembershipFunction mf = new MembershipFunction.TriangularMembershipFunction(10, 0);
        double delta = 1e-6;

        assertEquals(1.0, mf.getMembership(10.0), delta, "Value at the center of a zero-width triangle should be 1");
        assertEquals(0.0, mf.getMembership(10.001), delta, "Value just off the center of a zero-width triangle should be 0");
        assertEquals(0.0, mf.getMembership(9.999), delta, "Value just off the center of a zero-width triangle should be 0");
    }
}
