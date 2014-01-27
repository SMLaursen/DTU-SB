package dk.dtu.sb.GUI.view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

	private BufferedImage image;

	public ImagePanel() {}

	public void setImage(String path){
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException ex) {
			// handle exception...
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		float factor = getWidth() / (float) image.getWidth();
		g.drawImage(image,0,0,(int) (image.getWidth()*factor),(int) (image.getHeight()*factor), null); // see javadoc for more info on the parameters            
		g.dispose();
	}
}
