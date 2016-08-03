package com.evnica.delaunay.test;

import com.evnica.delaunay.main.geometry.Point;
import com.evnica.delaunay.main.geometry.Triangle;
import com.evnica.delaunay.main.geometry.Triangulation;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class: PointTest
 * Version: 0.1
 * Created on 03.08.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class PointTest
{
    private static Point a = new Point( 670.794868, 447.938697, 3 );
    private static Point b = new Point( 322.694294, 435.133081, 4 );
    private static Point c = new Point( 420.425015, 122.269736, 5 );
    private static Point d = new Point( 478.103359, 461.463632, 6 );
    private static Point e = new Point( 288.832657, 518.620228, 7 );
    private static Triangle triangle;

    @BeforeClass
    public static void init()
    {
        Triangulation.vertices.add( a );
        Triangulation.vertices.add( b );
        Triangulation.vertices.add( c );
        Triangulation.vertices.add( d );
        Triangulation.vertices.add( e );
        triangle = new Triangle( 0, 1, 2 );
    }


    @Test
    public void determineRelativeLocation() throws Exception
    {

    }

    @Test
    public void isInCircumcircle() throws Exception
    {
        assertTrue( a.isInCircumcircle( triangle ) );
        assertTrue( b.isInCircumcircle( triangle ) );
        assertTrue( c.isInCircumcircle( triangle ) );
        assertTrue( d.isInCircumcircle( triangle ) );
        assertTrue( e.isInCircumcircle( triangle ) );
    }

    @Test
    public void determineIfInTriangle() throws Exception
    {

    }



}