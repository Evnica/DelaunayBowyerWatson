package com.evnica.delaunay.main.logic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader( getClass().getResource( "../resources/main.fxml" ) );
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setMain( this );
        primaryStage.setTitle("Delaunay Triangulation");
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().addAll( this.getClass().getResource( "../resources/style.css" ).toExternalForm() );

        primaryStage.setScene(scene);
        primaryStage.setResizable( false );
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
