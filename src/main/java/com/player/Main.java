package com.player;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Movie Player");
        var root = new Group();
        var media = new Media("file:/home/kevin/Desktop/JG/Side-Projects/MoviePlayer/src/videos/droneClip.mp4");
        var player = new MediaPlayer(media);
        var view = new MediaView(player);
        Button play = new Button("Play");
        play.setOnAction(e -> {
            player.play();
        });

        Button pause = new Button("Pause");
        pause.setOnAction(e -> {
            player.pause();
        });

        final Timeline slideIn = new Timeline();
        final Timeline slideOut = new Timeline();
        root.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                slideIn.play();
            }
        });
        root.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                slideOut.play();
            }
        });

        final VBox vBox = new VBox();
        var slider = new Slider();
        vBox.getChildren().add(slider);

        final HBox hBox = new HBox(2);
        final int bands = player.getAudioSpectrumNumBands();
        final Rectangle[] rects = new Rectangle[bands];
        for(int i=0; i < rects.length; i++) {
            rects[i] = new Rectangle();
            rects[i].setFill(Color.GREENYELLOW);
            hBox.getChildren().add(rects[i]);
        }
        vBox.getChildren().add(hBox);

        root.getChildren().add(view);
        root.getChildren().add(vBox);

        var scene = new Scene(root, 400, 400, Color.BLACK);
        stage.setScene(scene);
        stage.show();

        player.play();
        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                int w = player.getMedia().getWidth();
                int h = player.getMedia().getHeight();

                hBox.setMinWidth(w);
                int bandwidth = w/rects.length;
                for(Rectangle r: rects) {
                    r.setWidth(bandwidth);
                    r.setHeight(2);
                }

                stage.setMinWidth(w);
                stage.setMinHeight(h);

                vBox.setMinSize(w-100, 100);
                vBox.setTranslateY(h-100);

                slider.setMin(0.0);
                slider.setValue(0.0);
                slider.setMax(player.getTotalDuration().toSeconds());

                slideOut.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(vBox.translateYProperty(), h-100),
                                new KeyValue(vBox.opacityProperty(), 0.9)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(vBox.translateYProperty(), h),
                                new KeyValue(vBox.opacityProperty(), 0.0)
                        ));
                slideIn.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(vBox.translateYProperty(), h),
                                new KeyValue(vBox.opacityProperty(), 0.0)
                ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(vBox.translateYProperty(), h-100),
                                new KeyValue(vBox.opacityProperty(), 0.9)
                ));
            }
        });

        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration duration, Duration current) {
                slider.setValue(current.toSeconds());
            }
        });
        slider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                player.seek(Duration.seconds(slider.getValue()));
            }
        });

        player.setAudioSpectrumListener(new AudioSpectrumListener() {
            @Override
            public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                for(int i = 0; i<rects.length; i++) {
                    double h = magnitudes[i]+60;
                    if(h > 2) {
                        rects[i].setHeight(h);
                    }
                }
            }
        });
    }
}