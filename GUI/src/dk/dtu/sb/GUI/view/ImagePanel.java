package dk.dtu.sb.GUI.view;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

    private String imagePath;

    public ImagePanel() {
    }

    public void setImage(String path) {
        imagePath = path;

        (new SwingWorker<Boolean, Boolean>() {
            Image scaledImage;
            int height, width;            

            @Override
            protected Boolean doInBackground() throws Exception {
                BufferedImage image = ImageIO.read(new File(imagePath));
                float factor = (getWidth() - UIManager.getDefaults().getInt("ScrollBar.width")) / (float) image.getWidth();
                height = (int) (image.getHeight() * factor);
                width = getWidth() - UIManager.getDefaults().getInt("ScrollBar.width");
                scaledImage = image.getScaledInstance(
                        width, height,
                        Image.SCALE_SMOOTH);
                return true;
            }
            
            @Override
            protected void done() {
                JLabel img = new JLabel(new ImageIcon(scaledImage));
                setPreferredSize(new Dimension(width, height));
                removeAll();
                add(img);
                invalidate();
                validate();
                repaint();
                updateUI();
            }
        }).execute();
    }
}
