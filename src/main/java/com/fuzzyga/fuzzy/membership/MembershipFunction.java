package com.fuzzyga.fuzzy.membership;

import java.io.Serializable;

/**
 * A sealed interface representing a fuzzy membership function.
 * It defines a contract for calculating the degree of membership for a given input value.
 * Using a sealed interface allows for a closed set of known membership function types,
 * improving type safety and enabling exhaustive pattern matching.
 */
public sealed interface MembershipFunction extends Serializable {

    /**
     * Calculates the membership degree of an input value.
     *
     * @param x The input value.
     * @return The degree of membership, a value between 0.0 and 1.0.
     */
    double getMembership(double x);

    /**
     * Represents a triangular membership function defined by a center and a width.
     * The triangle's points are at (center - width), (center), and (center + width).
     *
     * @param center The center of the triangle (where membership is 1.0).
     * @param width  The half-width of the triangle's base. The base extends from (center - width) to (center + width).
     */
    record TriangularMembershipFunction(double center, double width) implements MembershipFunction {

        /**
         * Calculates the membership degree for a triangular function.
         * The shape is defined by the points a = center - width, b = center, c = center + width.
         *
         * @param x The input value.
         * @return The degree of membership.
         */
        @Override
        public double getMembership(double x) {
            if (width == 0) {
                return x == center ? 1.0 : 0.0;
            }

            final double left = center - width;
            final double right = center + width;

            if (x <= left || x >= right) {
                return 0.0;
            } else if (x == center) {
                return 1.0;
            } else if (x < center) {
                return (x - left) / width;
            } else {
                return (right - x) / width;
            }
        }
    }
}
