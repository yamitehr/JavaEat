package Controllers;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoController {
	private File file;
	private MediaPlayer mediaPlayer;
	private Media media;
	@FXML
	private MediaView restaurantVideo;
	
	@FXML
	public void initialize() {
		file = new File("restaurantVideoHD.mp4");
		media = new Media(file.toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		restaurantVideo.setMediaPlayer(mediaPlayer);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.setMute(true);
	}
	
	
	

}
