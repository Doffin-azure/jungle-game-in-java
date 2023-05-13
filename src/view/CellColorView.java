package view;
//CellComponent 改名为 CellColorView
import model.ChessboardPoint;
import model.SharedData;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * This is the equivalent of the Cell class,
 * but this class only cares how to draw Cells on ChessboardComponent
 */

public class CellColorView extends JPanel {
    private Color background;
    int size;
    Point location;
    public boolean canStep;
    public boolean mouseSuspendedAt;

    public boolean isRiver=false;

    public CellColorView(Color background, Point location, int size) {
        this.size=size;
        setLayout(new GridLayout(1,1));
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

        if (canStep) { // Highlights the possible move model if selected.
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(87, 135, 255, 150));
            //普通矩形美化成为圆角矩形
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(1, 1, this.getWidth() - 1, this.getHeight() - 1, size/2 , size/2 );
            g2d.fill(roundedRectangle);
        }

        if(mouseSuspendedAt){//Highlights the cell if the mouse is suspended on it
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(255, 172, 155, 120));//颜色与上面不同
            //普通矩形美化成为圆角矩形
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(1, 1, this.getWidth() - 1, this.getHeight() - 1, size/2 , size/2 );
            g2d.fill(roundedRectangle);
        }

       //用于设置河流gif
        if(isRiver){
            ImageIcon gifImage = new ImageIcon("resource/Animal Supporter Asset Pack/Flowing River/R.gif");
            g.fillOval(0,0 ,getWidth(), getWidth());
            g.drawImage(gifImage.getImage(),1,1,getWidth()-1,getHeight()-1, this);


        }

//        点击
//        if (mouseAt) {
//            Graphics2D g2d = (Graphics2D) g;
//            g2d.setColor(new Color(255, 172, 155, 120));
//            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(1, 1,
//                    this.getWidth() - 1, this.getHeight() - 1, size / 4, size / 4);


    }




}