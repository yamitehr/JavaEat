package Controllers;

import java.awt.Dimension;

public class Consts {
	
	/*
	public static final int defaultWidth = 1430;
	public static final int defaultHeight = 770; 
*/
	
	
	static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	
	public static int defaultWidth = (int) (screenSize.getWidth());
	public static int defaultHeight = (int) (screenSize.getHeight());
}
