package view;
//CellComponent 改名为 CellColorView

import model.ChessboardPoint;
import model.SharedData;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * This is the equivalent of the Cell class,
 * but this class only cares how to draw Cells on ChessboardComponent
 */

public class CellColorView extends JPanel {
    private Color background;
    int size;
    public boolean canStep;
    public boolean mouseSuspendedAt;

    public boolean isGrass1 = false;
    public boolean isGrass2 = false;
    public boolean isRiver1 = false;
    public boolean isRiver2 = false;
    public boolean isTrap1 = false;
    public boolean isTrap2 = false;
    public boolean isDen1 = false;
    public boolean isDen2 = false;

    public CellColorView(Color background, Point location, int size) {
        this.size = size;
        setLayout(new GridLayout(1, 1));
        setLocation(location);
        setSize(size, size);
        this.background = background;
    }

    @Override
    protected void paintComponent(Graphics g) {


        super.paintComponents(g);
        g.setColor(background);
        g.fillRect(1, 1, this.getWidth() - 1, this.getHeight() - 1);

//鼠标点击IsValidMove和划过显示


        if (mouseSuspendedAt) {//Highlights the cell if the mouse is suspended on it
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(255, 172, 155, 120));//颜色与上面不同
            //普通矩形美化成为圆角矩形
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(1, 1, this.getWidth() - 1, this.getHeight() - 1, size / 5000, size / 5000);
            g2d.fill(roundedRectangle);
        }

        //用于设置草地图片
        if (isGrass1) {//jungle模式
            ImageIcon gifImage = new ImageIcon("resource/Animal Supporter Asset Pack/Green Grass/R1.png");
            g.fillOval(0, 0, getWidth(), getWidth());
            g.drawImage(gifImage.getImage(), 1, 1, getWidth() - 1, getHeight() - 1, this);
        }
        if (isGrass2) {//jungle模式
            ImageIcon gifImage = new ImageIcon("resource/Animal Supporter Asset Pack/Green Grass/R5.jpg");
            g.fillOval(0, 0, getWidth(), getWidth());
            g.drawImage(gifImage.getImage(), 1, 1, getWidth() - 1, getHeight() - 1, this);
        }


        //用于设置河流gif
        if (isRiver1) {//jungle模式
            ImageIcon gifImage = new ImageIcon("resource/Animal Supporter Asset Pack/Flowing River/R.gif");
            g.fillOval(0, 0, getWidth(), getWidth());
            g.drawImage(gifImage.getImage(), 1, 1, getWidth() - 1, getHeight() - 1, this);
        }
        if (isRiver2) {//jungle模式
            ImageIcon gifImage = new ImageIcon("resource/Animal Supporter Asset Pack/Flowing River/R1.jpg");
            g.fillOval(0, 0, getWidth(), getWidth());
            g.drawImage(gifImage.getImage(), 1, 1, getWidth() - 1, getHeight() - 1, this);
        }


        //用于设置陷阱gif
        if (isTrap1) {//Trap1对应space模式
            ImageIcon gifImage = new ImageIcon("resource/Animal Supporter Asset Pack/Amazing BlackHole/R.gif");
            g.fillOval(0, 0, getWidth(), getWidth());
            g.drawImage(gifImage.getImage(), 1, 1, getWidth() - 1, getHeight() - 1, this);
        }

        if (isTrap2) {//Trap2对应jungle模式
            ImageIcon gifImage = new ImageIcon("resource/Animal Supporter Asset Pack/Amazing BlackHole/R2.png");
            g.fillOval(0, 0, getWidth(), getWidth());
            g.drawImage(gifImage.getImage(), 1, 1, getWidth() - 1, getHeight() - 1, this);
        }

        if (isDen1) {//Den对应space模式
            ImageIcon gifImage = new ImageIcon("resource/Animal Supporter Asset Pack/Amazing Cave/R1.gif");
            g.fillOval(0, 0, getWidth(), getWidth());
            g.drawImage(gifImage.getImage(), 1, 1, getWidth() - 1, getHeight() - 1, this);
        }


        if (isDen2) {//Den对应jungle模式
            ImageIcon gifImage = new ImageIcon("resource/Animal Supporter Asset Pack/Amazing Cave/R2.gif");
            g.fillOval(0, 0, getWidth(), getWidth());
            g.drawImage(gifImage.getImage(), 1, 1, getWidth() - 1, getHeight() - 1, this);
        }


        if (canStep) {
            // Highlights the possible move model if selected.
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(239, 37, 222, 150));
            Rectangle2D rectangle = new Rectangle2D.Double(1, 1, this.getWidth() - 1, this.getHeight() - 1);
            g2d.fill(rectangle);

        }

    }
}
