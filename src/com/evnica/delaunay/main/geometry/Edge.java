package com.evnica.delaunay.main.geometry;

/**
 * Class: Edge
 * Version: 0.1
 * Created on 12.07.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class Edge
{
   // Point start, end;
    public int startId, endId;
    public int rightNeighborId;

    public Edge (int startId, int endId, int rightNeighborId)
    {
        if (Triangulation.vertices.get(startId).compareTo(Triangulation.vertices.get(endId)) == 0)
        {
            throw new IllegalArgumentException( "Can't create an edge between two equal points" );
        }
        this.startId = startId;
        this.endId = endId;
        this.rightNeighborId = rightNeighborId;
    }

    /*public int determineRelativePosition(Point newPoint)
    {
        double determinant = ( (end.x - start.x)*(newPoint.y - start.y) ) - ( (end.y - start.y) * (newPoint.x - start.x) ) ;
        return (determinant > Point.tolerance) ? 1 : (determinant < -Point.tolerance) ? -1 : 0;
    }*/


}
