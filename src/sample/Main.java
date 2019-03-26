package sample;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        var root = new Group();

        Media media = new Media("file:/home/kevin/Desktop/JG/Side-Projects/VideoPlayer/videos/paleBlueDot.mp4");
        MediaPlayer player = new MediaPlayer(media);
        MediaView view = new MediaView(player);

        root.getChildren().add(view);
        Scene scene = new Scene(root, 400, 400, Color.BLACK);
        stage.setScene(scene);
        stage.show();
    }
}