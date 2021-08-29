package Controllers;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ControllerWrapper {
	

	protected void replacePaneWithFxml(String fxmlPath, Pane toReplace) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		AnchorPane pane = loader.load();
		toReplace.getChildren().removeAll(toReplace.getChildren());
		toReplace.getChildren().add(pane);
	}
	
	public void moveToScene(String fxmlName, Stage primaryStage, int width, int height) {
		width = width <= 0? Consts.defaultWidth : width;
		height = height <= 0? Consts.defaultHeight : height;
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
			primaryStage.setScene(new Scene(root, width, height));
	        primaryStage.centerOnScreen();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void replacePane(AnchorPane toReplacePane, String path) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			AnchorPane pane = loader.load();
			toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
			toReplacePane.getChildren().add(pane);
		}catch (IOException e1) {
				e1.printStackTrace();
			}
	}
	
	public void moveToScene(String fxmlName, Stage primaryStage) {
		moveToScene(fxmlName, primaryStage, Consts.defaultWidth, Consts.defaultHeight);
	}
	
	public void soundOfButton(String path) {
		String musicFile = path;  
//		Media sound = new Media(new File(musicFile).toURI().toString());
//		MediaPlayer mediaPlayer = new MediaPlayer(sound);
//		mediaPlayer.play();
		AudioClip soundMyNoise = new AudioClip(new File(musicFile).toURI().toString());
		soundMyNoise.play();
	}
	
	public static void autoResizeColumns( TableView<?> table )
	{
	    //Set the right policy
	    table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
	    table.getColumns().stream().forEach( (column) ->
	    {
	        //Minimal width = columnheader
	        Text t = new Text( column.getText() );
	        double max = t.getLayoutBounds().getWidth();
	        for ( int i = 0; i < table.getItems().size(); i++ )
	        {
	            //cell must not be empty
	            if ( column.getCellData( i ) != null )
	            {
	                t = new Text( column.getCellData( i ).toString() );
	                double calcwidth = t.getLayoutBounds().getWidth();
	                //remember new max-width
	                if ( calcwidth > max )
	                {
	                    max = calcwidth;
	                }
	            }
	        }
	        //set the new max-widht with some extra space
	        column.setPrefWidth( max + 10.0d );
	    } );
	}
	
	
	public void fitColumnWidthToContent (String colId, TableView<?> tableView) {
	    // find column matching id
	    TableColumn column = null;

	    for (TableColumn tempCol : tableView.getColumns()) {
	        if (tempCol.getId().equals(colId)) {
	            column = tempCol;
	            break;
	        }
	    }

	    if (column == null) {
	        throw new IllegalStateException("Column ID doesn't match any actual column");
	    }

	    // set default width to column header width
	    Text text = new Text(column.getText());
	    double max = text.getLayoutBounds().getWidth();

	    for (int i = 0; i < tableView.getItems().size(); i++ ) {
	        if (column.getCellData(i) == null) continue;

	        text = new Text(column.getCellData(i).toString());
	        double textWidth = text.getLayoutBounds().getWidth();

	        if (textWidth > max) {
	            max = textWidth;
	        }
	    }

	    column.setPrefWidth(max + 12);
	}
	
	
}
