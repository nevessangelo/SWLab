/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getfeatures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



/**
 *
 * @author angelo
 */


    public class Teste extends Application {

        File fileSrc;
        private static final int bufferSize = 8192;

        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Hello World!");

            final Label labelFile = new Label();

            Button btn = new Button();
            btn.setText("Open FileChooser'");
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    FileChooser fileChooser = new FileChooser();

                    //Set extension filter
                    FileChooser.ExtensionFilter extFilter
                            = new FileChooser.ExtensionFilter("gz files (*.gz)", "*.gz");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show open file dialog
                    fileSrc = fileChooser.showOpenDialog(null);

                    String source = fileSrc.getPath();
                    //remove ".gz" from the path
                    String targetUnGzFile = source.substring(0, source.length() - 3);

                    try {
                    //UnCompress file

                        GZIPInputStream gZIPInputStream;

                        FileInputStream fileInputStream
                                = new FileInputStream(source);
                        gZIPInputStream = new GZIPInputStream(fileInputStream);

                        byte[] buffer = new byte[bufferSize];
                        try (FileOutputStream fileOutputStream = new FileOutputStream(targetUnGzFile)) {
                            int numberOfByte;

                            while ((numberOfByte = gZIPInputStream.read(buffer, 0, bufferSize))
                                    != -1) {
                                fileOutputStream.write(buffer, 0, numberOfByte);
                            }

                            fileOutputStream.close();
                        }

                        labelFile.setText("UnCompressed file saved as: " + targetUnGzFile);

                    } catch (IOException ex) {
                        Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            VBox vBox = new VBox();
            vBox.getChildren().addAll(labelFile, btn);

            StackPane root = new StackPane();
            root.getChildren().add(vBox);
            primaryStage.setScene(new Scene(root, 300, 250));
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }

    }
