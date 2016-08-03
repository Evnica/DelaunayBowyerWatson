package com.evnica.delaunay.main.geometry;

import java.util.ArrayList;

/**
 * Class: Triangulation
 * Version: 0.1
 * Created on 15.07.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class Triangulation
{
    public static ArrayList<Point> vertices = new ArrayList<>();
    private static int[][] edges = new int[50][50];
    public static ArrayList<Triangle> triangles = new ArrayList<>( 1 );
    private static SuperTriangle superTriangle = new SuperTriangle( 0, 726, 0, 582 );

    static
    {
        triangles.add(superTriangle);
        System.out.println("Super triangle added");
        addSuperVertices();
    }

    public static void addVertex( Point point )
    {
        vertices.add( point );
    }

    public static void addVertices( ArrayList<Point> points )
    {
        addSuperVertices();
        vertices.addAll( points );
    }

    public static void addEdge( int startId, int endId, int rightNeighborId )
    {
        edges[ startId ][ endId ] = rightNeighborId;
    }

    public static void deleteEdge( int startId, int endId )
    {
        edges[startId][endId] = -2;
    }

    public static int getRightNeighbor(int startId, int endId)
    {
        return edges[startId][endId];
    }

    private static void addSuperVertices()
    {
        vertices.add( superTriangle.start );
        vertices.add( superTriangle.middle );
        vertices.add( superTriangle.end );
    }
}
