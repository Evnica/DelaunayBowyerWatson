package com.evnica.delaunay.main.logic;

import com.evnica.delaunay.main.geometry.*;
import com.evnica.delaunay.main.gui.PlayField;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;


public class Controller {
    @FXML
    private Label pointNumLabel;
    @FXML
    private Slider pointNumSlider;
    @FXML
    private Button genNewButton;
    @FXML
    private RadioButton buttonGenRandom;
    @FXML
    private RadioButton buttonGenClick;
    @FXML
    private RadioButton buttonModeAuto;
    @FXML
    private RadioButton buttonModeStep;
    @FXML
    private Button nextButton;
    @FXML
    private Button calcButton;
    @FXML
    private Canvas playField;
    private boolean onClickMode = false;

    public static GraphicsContext graphicContext;
    private static int numberOfSites = 10;
    private final ToggleGroup modeGroup = new ToggleGroup();
    private final ToggleGroup genTypeGroup = new ToggleGroup();

    private Main main;
    private Step currentStep = Step.SELECT_A_POINT;
    private ArrayList<Triangle> invalidTriangles = new ArrayList<>( 1 );
    private ArrayList<Edge> cavity = new ArrayList<>(  );
    private int currentPointIndex = 3;

    public void setMain(Main main)
    {
        this.main = main;
    }

    @FXML
    public void initialize()
    {
        buttonGenRandom.setToggleGroup( genTypeGroup );
        buttonGenRandom.setSelected( true );
        buttonGenClick.setToggleGroup( genTypeGroup );

        buttonModeAuto.setToggleGroup( modeGroup );
        buttonModeAuto.setSelected( true );
        buttonModeStep.setToggleGroup( modeGroup );

        genTypeGroup.selectedToggleProperty().addListener( ( observable, oldValue, newValue ) -> {
            if (genTypeGroup.getSelectedToggle().equals( buttonGenClick ))
            {
                disableRandom();
                onClickMode = true;
                nextButton.setDisable( true );
            }
            else
            {
                enableRandom();
                onClickMode = false;
                nextButton.setDisable( false );
            }
        } );

        graphicContext = playField.getGraphicsContext2D();
        calcButton.defaultButtonProperty().setValue( true );

        pointNumSlider.valueProperty().addListener( ( observable, oldValue, newValue ) -> {
            numberOfSites = newValue.intValue();
            pointNumLabel.setText( numberOfSites + "" );
        } );

        genNewButton.setOnAction( event -> generateRandomSites() );

        nextButton.setOnAction( event -> {
            if ( Triangulation.vertices.size() > 3 )
            {
                calculateTriangulation();
            }
        } );

        playField.addEventHandler( MouseEvent.MOUSE_CLICKED, event -> {

            System.out.println("-------------");
            for ( Point p: Triangulation.vertices )
            {
                System.out.println(p.id + " " + p.x + " " + p.y + " ");
            }

            if ( onClickMode && Triangulation.vertices.size() < 53 )
            {
                double x = event.getSceneX() - 170;
                double y = event.getSceneY()-5;

                Point pointOnClick = new Point( x, y, Triangulation.vertices.size() );

                if ( x >= 8 && x <= 715 && y >=8 && y < 580 )
                {
                    Triangulation.addVertex( pointOnClick );
                    PlayField.drawAPoint( pointOnClick );
                    System.out.println("Added " + pointOnClick.id + " " + pointOnClick.x + " " + pointOnClick.y);
                }
            }
        } );
    }

    private static void generateRandomSites()
    {
        deleteOldSites();
        Triangulation.addVertices(Point.generateRandomPoints( numberOfSites ));
        Triangulation.vertices.forEach( PlayField::drawAPoint );
    }

    private static void deleteOldSites()
    {
        Triangulation.vertices = new ArrayList<>();
        graphicContext.clearRect( 0, 0, 730, 590 );
    }

    private void disableRandom()
    {
        buttonModeAuto.setDisable( true );
        buttonModeStep.setDisable( true );
        genNewButton.setDisable( true );
    }

    private void enableRandom()
    {
        buttonModeAuto.setDisable( false );
        buttonModeStep.setDisable( false );
        genNewButton.setDisable( false );
    }

    private void calculateTriangulation()
    {
            Point point = Triangulation.vertices.get( currentPointIndex );
            switch ( currentStep )
            {
                case SELECT_A_POINT:
                    PlayField.selectAPoint( point );
                    System.out.println("Point selected");
                    currentStep = Step.FIND_INVALID_TRIANGLES;
                    break;
                case FIND_INVALID_TRIANGLES:
                    System.out.println("Looking for invalid triangles among " + Triangulation.triangles.size() + " triangles");
                    for ( int i = 0; i < Triangulation.triangles.size(); i++ )
                    {
                        System.out.println("Current point (id=" +  point.id + ") is in circumcircle of the triangle "
                        + Triangulation.triangles.get( i ).getId() + ": " + point.isInCircumcircle( Triangulation.triangles.get( i ) ));

                        if (point.isInCircumcircle( Triangulation.triangles.get( i ) ))
                        {
                           // indicesOfInvalidTriangles.add( i );
                            invalidTriangles.add( Triangulation.triangles.get( i ) );
                            System.out.println("Triangle " + Triangulation.triangles.get( i ).getId() + " added to the invalid triangles");
                            PlayField.colorInvalidTriangle( Triangulation.triangles.get( i ) );
                            Triangulation.vertices.forEach( PlayField::drawAPoint );
                            PlayField.selectAPoint( point );
                        }
                    }
                    currentStep = Step.CREATE_CAVITY;
                    break;
                case CREATE_CAVITY:
                    int rightNeighborId;
                    boolean invalidEdge = false;
                    System.out.println("There are " + invalidTriangles.size() + " invalid triangles");
                    for (Triangle badTriangle: invalidTriangles)
                    {
                        //id of the triangle adjacent to the start-middle edge
                        rightNeighborId = badTriangle.getIdNeighborStartMid();

                        for ( Triangle anotherBadTriangle: invalidTriangles )
                        {
                            //if the adjacent triangle belongs to invalid triangles
                            if (rightNeighborId == anotherBadTriangle.getId())
                            {
                                invalidEdge = true;
                                break;
                            }
                        }
                        if (invalidEdge)
                        {
                            invalidEdge = false;
                            //delete start-middle edge from edges
                            Triangulation.deleteEdge( badTriangle.getStartId(), badTriangle.getMiddleId() );
                            //color start-middle edge transparent
                            PlayField.deleteEdge( Triangulation.vertices.get(badTriangle.getStartId()), Triangulation.vertices.get( badTriangle.getMiddleId() ) );
                        }
                        else
                        {
                            //add a valid start-middle edge to cavity
                            cavity.add( new Edge(badTriangle.getStartId(), badTriangle.getMiddleId(), rightNeighborId) );
                        }

                        //id of the triangle adjacent to the middle-end edge
                        rightNeighborId = badTriangle.getIdNeighborMidEnd();
                        for ( Triangle anotherBadTriangle: invalidTriangles )
                        {
                            //if the adjacent triangle belongs to invalid triangles
                            if (rightNeighborId == anotherBadTriangle.getId())
                            {
                                invalidEdge = true;
                                break;
                            }
                        }
                        if (invalidEdge)
                        {
                            invalidEdge = false;
                            //delete middle-end edge from edges
                            Triangulation.deleteEdge( badTriangle.getMiddleId(), badTriangle.getEndId() );
                            //color middle-end edge transparent
                            PlayField.deleteEdge( Triangulation.vertices.get(badTriangle.getMiddleId()), Triangulation.vertices.get(badTriangle.getEndId()) );
                        }
                        else
                        {
                            //add a valid middle-end edge to cavity
                            cavity.add( new Edge(badTriangle.getMiddleId(), badTriangle.getEndId(), rightNeighborId) );
                        }

                        //id of the triangle adjacent to the end-start edge
                        rightNeighborId = badTriangle.getIdNeighborEndStart();
                        for ( Triangle anotherBadTriangle: invalidTriangles )
                        {
                            //if the adjacent triangle belongs to invalid triangles
                            if (rightNeighborId == anotherBadTriangle.getId())
                            {
                                invalidEdge = true;
                                break;
                            }
                        }
                        if (invalidEdge)
                        {
                            invalidEdge = false;
                            //delete middle-end edge from edges
                            Triangulation.deleteEdge( badTriangle.getEndId(), badTriangle.getStartId() );
                            //color middle-end edge transparent
                            PlayField.deleteEdge( Triangulation.vertices.get(badTriangle.getEndId()), Triangulation.vertices.get(badTriangle.getStartId()) );
                        }
                        else
                        {
                            //add a valid middle-end edge to cavity
                            cavity.add( new Edge(badTriangle.getEndId(), badTriangle.getStartId(), rightNeighborId) );
                        }
                    }
                    System.out.println(cavity.size() + " cavity size");
                    for ( Triangle triangle: invalidTriangles )
                    {
                        Triangulation.triangles.remove( triangle );
                    }
                    invalidTriangles.clear();
                    currentStep = Step.TRIANGULATE_CAVITY;
                    break;
                case TRIANGULATE_CAVITY:
                    ArrayList<Triangle> newTriangles = new ArrayList<>( cavity.size() );
                    int currentEdgeIndex = -1, endId;
                    endId = cavity.get( 0 ).endId;
                    Triangle triangle = new Triangle( cavity.get( 0 ).startId, endId, point.id );
                    triangle.setIdNeighborStartMid( cavity.get( 0 ).rightNeighborId );
                    newTriangles.add( triangle );
                    int indexOfTheLastTriangleAdded = 0;
                    cavity.remove( 0 );
                    while ( !cavity.isEmpty() )
                    {
                        for (int i = 0; i < cavity.size(); i++)
                        {
                            if (cavity.get( i ).startId == endId)
                            {
                                endId = cavity.get( i ).endId;
                                currentEdgeIndex = i;
                                break;
                            }
                        }
                        if ( currentEdgeIndex != -1 )
                        {
                            //create a new triangle, connecting the inner point as the end-vertex to the cavity edge
                            Triangle newTriangle = new Triangle( cavity.get( currentEdgeIndex ).startId, cavity.get( currentEdgeIndex ).endId, point.id );
                            //set the right neighbor of the cavity edge as the start-middle neighbor of the new triangle
                            newTriangle.setIdNeighborStartMid( cavity.get( currentEdgeIndex ).rightNeighborId );
                            //set the previous triangle as the end-start neighbor of the new triangle
                            newTriangle.setIdNeighborEndStart( newTriangles.get(indexOfTheLastTriangleAdded ).getId());
                            //set the new triangle as the middle-end neighbor of the previous triangle
                            newTriangles.get( indexOfTheLastTriangleAdded ).setIdNeighborMidEnd( newTriangle.getId() );
                            newTriangles.add( newTriangle );
                            indexOfTheLastTriangleAdded++;
                            cavity.remove( currentEdgeIndex );
                        }
                    }
                    newTriangles.get( indexOfTheLastTriangleAdded ).setIdNeighborMidEnd( newTriangles.get( 0 ).getId() );
                    newTriangles.get( 0 ).setIdNeighborEndStart( newTriangles.get( indexOfTheLastTriangleAdded ).getId() );

                    for (Triangle t: newTriangles)
                    {
                        Triangulation.addEdge( t.getStartId(), t.getMiddleId(), t.getIdNeighborStartMid() );
                        PlayField.drawEdge( Triangulation.vertices.get( t.getStartId()), Triangulation.vertices.get( t.getMiddleId() ) );
                        Triangulation.addEdge( t.getMiddleId(), t.getEndId(), t.getIdNeighborMidEnd() );
                        PlayField.drawEdge( Triangulation.vertices.get( t.getMiddleId()), Triangulation.vertices.get( t.getEndId() ) );
                        Triangulation.addEdge( t.getEndId(), t.getStartId(), t.getIdNeighborEndStart() );
                        PlayField.drawEdge( Triangulation.vertices.get( t.getEndId()), Triangulation.vertices.get( t.getStartId() ) );
                        Triangulation.triangles.add( t );
                    }
                    currentStep = Step.SELECT_A_POINT;
                    if (currentPointIndex < Triangulation.vertices.size() - 1)
                    {
                        currentPointIndex++;
                    }
                    else
                    {
                        finalizeTriangulation();
                    }
                    break;
            }

    }

    private void finalizeTriangulation(){}

}
