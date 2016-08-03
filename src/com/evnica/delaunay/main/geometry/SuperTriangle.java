package com.evnica.delaunay.main.geometry;

/**
 * Class: SuperTriangle
 * Version: 0.1
 * Created on 12.07.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class SuperTriangle extends Triangle
{
    Point start, middle, end;

    public SuperTriangle(double minX, double maxX, double minY, double maxY)
    {
        double deltaX = maxX - minX;
        double deltaY = maxY - minY;
        startId = 0;
        middleId = 1;
        endId = 2;
        this.start = new Point (minX - 1.5 * deltaX, minY - deltaY, 0);
        this.middle = new Point( maxX + 1.5 * deltaX, minY - deltaY, 1 );
        this.end = new Point( minX + 0.5 * deltaX, maxY + 2 * deltaY, 2 );
        this.idNeighborStartMid = -1;
        this.idNeighborMidEnd = -1;
        this.idNeighborEndStart = -1;
        this.id = 0;
    }
}
