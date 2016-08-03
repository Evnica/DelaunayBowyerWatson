package com.evnica.delaunay.main.geometry;

import com.evnica.delaunay.main.gui.PlayField;

import java.util.ArrayList;

/**
 * Class: Point
 * Version: 0.1
 * Created on 12.07.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class Point implements Comparable<Point>
{
    public double x, y;
    public int id;

    /**
     * for this app, an precision higher than 9 decimal digits is unnecessary
     * lower precision can result in a too high tolerance by determinant computation
     * */
    public static double tolerance = 0.0000001;

    public Point( double x, double y, int id )
    {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    /**
     * compareTo() is not consistent with equalsWithinTolerance(Object anotherObject). As we deal with double values, tolerance is used
     * to compare point coordinates. The precision of 6 decimal digits is satisfactory for the purposes of this app.
     * Non-consistency of compareTo() and equalsWithinTolerance() can result in a 'strange' behaviour of Points in sorted sets/maps.
     * These data structures are not used in the app.
     * */
    @Override
    public int compareTo( Point point )
    {
        double deltaX = this.x - point.x;
        double deltaY = this.y - point.y;

        return  (this.equalsWithinTolerance( point ) ? 0 :
                 (Math.abs( deltaX ) < tolerance ) ? (deltaY  > 0 ) ? 1 : -1 :
                         (deltaX > 0 ? 1 : -1));
    }

    public boolean equalsWithinTolerance( Point point)
    {
        return hasXEqualWithinTolerance( point ) && hasYEqualWithinTolerance( point );
    }

    private boolean hasXEqualWithinTolerance( Point point)
    {
        return Math.abs( this.x - point.x ) < tolerance;
    }

    private boolean hasYEqualWithinTolerance( Point point)
    {
        return Math.abs( this.y - point.y ) < tolerance;
    }

    /**
     * Determines, if {@code this} is to the left, to the right or on the directed line defined by {@code start} and
     * {@code end}.
     * Returns {@code 1} if {@code this} is located to the left, {@code -1} if {@code this} is located to the right and
     * {@code 0} if {@code this} is collinear with {@code start} and {@code end}.
     *          @param start - a starting Point defining the line
     *          @param end - an endpoint (Point) defining the line
     * */
    public int determineRelativeLocation(Point start, Point end)
    {
        double determinant = ( (end.x - start.x)*(this.y - start.y) ) - ( (end.y - start.y) * (this.x - start.x) ) ;
        return (determinant > tolerance) ? 1 : (determinant < - tolerance) ? -1 : 0;
    }

    /**
     * Determines if {@code this} is located within the circumcircle of the passed triangle by evaluating the determinant
     * | Ax    Ay    (Ax)^2 +(Ay)^2    1 |
     * | Bx    By    (Bx)^2 +(By)^2    1 |
     * | Cx    Cy    (Cx)^2 +(Cy)^2    1 |
     * | Dx    Dy    (Dx)^2 +(Dy)^2    1 |
     * where A, B, C - vertices of the triangle in CCW order, and D is {@code this}.
     * If D is within the ABC circumcircle, the determinant above is positive.
     * */
    public boolean isInCircumcircle( Triangle triangle)
    {
        final Point A = Triangulation.vertices.get( triangle.startId ),
                    B = Triangulation.vertices.get( triangle.middleId ),
                    C = Triangulation.vertices.get( triangle.endId ),
                    D = this;

        double determinant =    (A.x - D.x) * (B.y - D.y) * ((C.x*C.x - D.x*D.x) + (C.y*C.y - D.y*D.y)) +
                                (A.y - D.y) * (C.x - D.x) * ((B.x*B.x - D.x*D.x) + (B.y*B.y - D.y*D.y)) +
                                (B.x - D.x) * (C.y - D.y) * ((A.x*A.x - D.x*D.x) + (A.y*A.y - D.y*D.y)) -
                                (C.x - D.x) * (B.y - D.y) * ((A.x*A.x - D.x*D.x) + (A.y*A.y - D.y*D.y)) -
                                (B.x - D.x) * (A.y - D.y) * ((C.x*C.x - D.x*D.x) + (C.y*C.y - D.y*D.y)) -
                                (C.y - D.y) * (A.x - D.x) * ((B.x*B.x - D.x*D.x) + (B.y*B.y - D.y*D.y));
        return (determinant > 0);
    }

    public boolean determineIfInTriangle(Triangle triangle)
    {
        Point   start = Triangulation.vertices.get( triangle.startId ),
                middle = Triangulation.vertices.get( triangle.middleId ),
                end = Triangulation.vertices.get( triangle.endId );
        double area = 0.5 * (-middle.y * end.x + start.y * (-middle.x + end.x) + start.x * (middle.y - end.y) + middle.x * end.y);
        int sign = (area < 0) ? -1 : 1;
        double s = (start.y * end.x - start.x * end.y + (end.y - start.y) * this.x + (start.x - end.x) * this.y) * sign;
        double t = (start.x * middle.y - start.y * middle.x + (start.y - middle.y) * this.x + (middle.x - start.x) * this.y) * sign;

        return s > 0 && t > 0 && (s + t) < 2 * area * sign;
    }

    public static ArrayList<Point> generateRandomPoints( int numberOfPointsToGenerate )
    {
        int i = 3;
        ArrayList<Point> randomPoints = new ArrayList<>( numberOfPointsToGenerate - 1 );
        while (i < numberOfPointsToGenerate + 3)
        {
            double x = (double) Math.round( PlayField.getRandomX() * 1000000 ) / 1000000;
            double y = (double) Math.round( PlayField.getRandomY() * 1000000 ) / 1000000;
           randomPoints.add( new Point( x, y, i ) );
           i++;
        }
        return randomPoints;
    }

    public boolean isInCircumcircleDetail( Triangle triangle)
    {
        final Point A = Triangulation.vertices.get( triangle.startId ),
                B = Triangulation.vertices.get( triangle.middleId ),
                C = Triangulation.vertices.get( triangle.endId ),
                D = this;

        System.out.println("(" + A.x + " - " + D.x + ") * (" + B.y + " - "  + D.y + ") * ((" + C.x*C.x + " - " + D.x*D.x  +") + " + "(" + C.y*C.y + " - " + D.y*D.y +"))");
        System.out.println((A.x - D.x) * (B.y - D.y) * ((C.x*C.x - D.x*D.x) + (C.y*C.y - D.y*D.y)));
        System.out.println((A.y - D.y) * (C.x - D.x) * ((B.x*B.x - D.x*D.x) + (B.y*B.y - D.y*D.y)));
        System.out.println((B.x - D.x) * (C.y - D.y) * ((A.x*A.x - D.x*D.x) + (A.y*A.y - D.y*D.y)));
        System.out.println(-(C.x - D.x) * (B.y - D.y) * ((A.x*A.x - D.x*D.x) + (A.y*A.y - D.y*D.y)));
        System.out.println(-(B.x - D.x) * (A.y - D.y) * ((C.x*C.x - D.x*D.x) + (C.y*C.y - D.y*D.y)));
        System.out.println(-(C.y - D.y) * (A.x - D.x) * ((B.x*B.x - D.x*D.x) + (B.y*B.y - D.y*D.y)));


        double determinant =    (A.x - D.x) * (B.y - D.y) * ((C.x*C.x - D.x*D.x) + (C.y*C.y - D.y*D.y)) +
                (A.y - D.y) * (C.x - D.x) * ((B.x*B.x - D.x*D.x) + (B.y*B.y - D.y*D.y)) +
                (B.x - D.x) * (C.y - D.y) * ((A.x*A.x - D.x*D.x) + (A.y*A.y - D.y*D.y)) -
                (C.x - D.x) * (B.y - D.y) * ((A.x*A.x - D.x*D.x) + (A.y*A.y - D.y*D.y)) -
                (B.x - D.x) * (A.y - D.y) * ((C.x*C.x - D.x*D.x) + (C.y*C.y - D.y*D.y)) -
                (C.y - D.y) * (A.x - D.x) * ((B.x*B.x - D.x*D.x) + (B.y*B.y - D.y*D.y));

        System.out.println("Determinant: " + determinant);
        return (determinant > 0);
    }

    @Override
    public String toString()
    {
       return this.x + " " + this.y + " [" + this.id + "]";
    }
}


