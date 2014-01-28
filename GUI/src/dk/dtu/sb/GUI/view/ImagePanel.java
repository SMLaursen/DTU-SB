package dk.dtu.sb.GUI.view;

import java.awt.Graphics;
import java.awt.Image;
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
		if(image!=null){
			float factor = getWidth() / (float) image.getWidth();
			g.drawImage(image.getScaledInstance(getWidth(), (int) (image.getHeight()*factor), Image.SCALE_SMOOTH),0,0, null); // see javadoc for more info on the parameters            
			g.dispose();
		}
	}
}
