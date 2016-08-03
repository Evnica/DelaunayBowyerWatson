package com.evnica.delaunay.main.geometry;

/**
 * Class: Triangle
 * Version: 0.1
 * Created on 12.07.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */

public class Triangle
{
    int startId, endId, middleId;
    int idNeighborStartMid, idNeighborMidEnd, idNeighborEndStart;
    int id;
    private static int triangleCounter = 1;

    Triangle(){}

    public int getId()
    {
        return id;
    }

    public Triangle( int id1, int id2, int id3 )
    {
        id = triangleCounter;
        triangleCounter++;
        int location = Triangulation.vertices.get( id3 ).determineRelativeLocation( Triangulation.vertices.get( id1 ), Triangulation.vertices.get( id2 ) );
        if (location == 0)
        {
            throw new FlatTriangleException( "Can't create a triangle with collinear vertices" );
        }
        if (location == 1)
        {
            startId = id1;
            middleId = id2;
            endId = id3;
        }
        else if (location == -1)
        {
            startId = id1;
            middleId = id3;
            endId = id2;
        }
        idNeighborStartMid = -3; // -3=neighbor not set, -2 = edge doesn't exist, -1 = edge belongs to the super triangle

        idNeighborMidEnd = -3;
        idNeighborEndStart = -3;
    }

    public int getIdNeighborStartMid()
    {
        return idNeighborStartMid;
    }

    public void setIdNeighborStartMid( int idNeighborStartMid )
    {
        this.idNeighborStartMid = idNeighborStartMid;
    }

    public int getIdNeighborMidEnd()
    {
        return idNeighborMidEnd;
    }

    public void setIdNeighborMidEnd( int idNeighborMidEnd )
    {
        this.idNeighborMidEnd = idNeighborMidEnd;
    }

    public int getIdNeighborEndStart()
    {
        return idNeighborEndStart;
    }

    public void setIdNeighborEndStart( int idNeighborEndStart )
    {
        this.idNeighborEndStart = idNeighborEndStart;
    }

    public int getStartId()
    {
        return startId;
    }

    public int getEndId()
    {
        return endId;
    }

    public int getMiddleId()
    {
        return middleId;
    }
}
