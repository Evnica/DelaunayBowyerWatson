package com.evnica.delaunay.main.gui;

import com.evnica.delaunay.main.geometry.Point;
import com.evnica.delaunay.main.geometry.Triangle;
import com.evnica.delaunay.main.geometry.Triangulation;
import com.evnica.delaunay.main.logic.Controller;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class: PlayField
 * Version: 0.1
 * Created on 01.08.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class PlayField
{
    private static final int CANVAS_WIDTH  = 710;
    private static final int CANVAS_HEIGHT = 580;
    private static final int WIDTH = 7;
    private static final GraphicsContext GRAPHIC_CONTEXT = Controller.graphicContext;

    private static double getRandomNumberWithRange( int border1, int border2)
    {
        return (Math.random() * Math.abs(border2 - border1 ) + ( border1 <= border2 ? border1 : border2));
    }

    public static double getRandomY()
    {
        return getRandomNumberWithRange( 20, CANVAS_HEIGHT - 20 );
    }

    public static double getRandomX()
    {
        return getRandomNumberWithRange( 20, CANVAS_WIDTH - 20 );
    }

    public static void drawAPoint( Point point )
    {
        basicDrawPoint( point, Color.CHARTREUSE, Color.DARKGREEN );
    }

    public static void selectAPoint( Point point )
    {
        basicDrawPoint( point, Color.RED, Color.DARKMAGENTA );
    }

    private static void basicDrawPoint( Point point, Color fill, Color border )
    {
        GRAPHIC_CONTEXT.setFill( fill );
        GRAPHIC_CONTEXT.setLineWidth( WIDTH );
        GRAPHIC_CONTEXT.fillOval( point.x - WIDTH, point.y - WIDTH, WIDTH, WIDTH );
        GRAPHIC_CONTEXT.setFill( border );
        GRAPHIC_CONTEXT.setLineWidth( 2.0 );
        GRAPHIC_CONTEXT.strokeOval( point.x - WIDTH, point.y - WIDTH, WIDTH, WIDTH );
    }

    public static void drawEdge(Point start, Point end)
    {
        basicDrawLine( start, end, Color.GREEN );
    }

    public static void deleteEdge(Point start, Point end)
    {
        basicDrawLine( start, end, Color.TRANSPARENT );
        drawAPoint( start );
        drawAPoint( end );
    }

    private static void basicDrawLine (Point start, Point end, Color stroke)
    {
        GRAPHIC_CONTEXT.setStroke( stroke );
        GRAPHIC_CONTEXT.setLineWidth( 2.0 );
        GRAPHIC_CONTEXT.strokeLine( start.x, start.y, end.x, end.y );
    }

    public static void colorInvalidTriangle( Triangle triangle)
    {
        basicColorTriangle( Triangulation.vertices.get(triangle.getStartId()),
                            Triangulation.vertices.get(triangle.getMiddleId()),
                            Triangulation.vertices.get(triangle.getEndId()),
                            Color.rgb(255, 80, 80, 0.5));
    }

    public static void colorTriangleTransparent( Triangle triangle)
    {
        basicColorTriangle( Triangulation.vertices.get(triangle.getStartId()),
                Triangulation.vertices.get(triangle.getMiddleId()),
                Triangulation.vertices.get(triangle.getEndId()),
                Color.TRANSPARENT);
    }

    public static void basicColorTriangle( Point point1, Point point2, Point point3, Color color)
    {
        GRAPHIC_CONTEXT.setFill( color );
        GRAPHIC_CONTEXT.fillPolygon(    new double[] {point1.x, point2.x, point3.x},
                                        new double[] {point1.y, point2.y, point3.y}, 3 );
    }
}
