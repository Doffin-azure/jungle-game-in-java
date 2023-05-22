package view;


import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.jar.Attributes.Name;


/**
 * This is the equivalent of the ChessPiece class,
 * but this class only cares how to draw Chess on ChessboardComponent
 */
public class AnimalChessComponent extends JComponent {
    private PlayerColor owner;
    private boolean selected;
    private String name;
    private String address;
    private ImageIcon gifImage;
    public AnimalChessComponent(PlayerColor owner, int size, String name,String address) {
        this.owner = owner;
        this.selected = false;
        this.name = name;
        this.address = address;
        gifImage = new ImageIcon(getClass().getResource(address));
        setSize(size/2, size/2);
        setLocation(0,0);
        setVisible(true);
    }



    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//这是以前版本，我认为比较丑
//        // Draw the GIF image
//        if (gifImage != null) {
//            if(this.owner.getColor() == Color.BLUE) g.setColor(new Color(41, 6, 199, 255));
//            else g.setColor(new Color(255, 0, 0, 255));
//            g.fillOval(0,0 ,getWidth(), getWidth());
//            g.drawImage(gifImage.getImage(),11,11,getWidth()-22,getHeight()-22, this);
//
//        }
//
//        super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the rounded rectangle
        if (gifImage != null) {
            int arcSize = 50; // 控制圆角的大小
            int x = 0;
            int y = 0;
            int width = getWidth();
            int height = getHeight();
            RoundRectangle2D roundRect = new RoundRectangle2D.Double(x, y, width, height, arcSize, arcSize);

            if (owner.getColor() == Color.BLUE) {
                g2.setColor(new Color(17, 84, 255, 255));
            } else {
                g2.setColor(new Color(248, 19, 19, 255));
            }
            g2.fill(roundRect);
            g2.drawImage(gifImage.getImage(), x + 11, y + 11, width - 22, height - 22, this);
        }
}
}
