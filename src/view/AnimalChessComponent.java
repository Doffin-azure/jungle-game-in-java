package view;

import model.PlayerColor;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class AnimalChessComponent extends JComponent {
    private final PlayerColor owner;
    private final String name;
    private boolean selected;

    public AnimalChessComponent(PlayerColor owner, int size, String name) {
        this.owner = owner;
        this.name = name;
        setSize(size, size);
        getAccessibleContext().setAccessibleName(owner + " " + name);
    }

    @Override
    public javax.accessibility.AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleJComponent() {};
        }
        return accessibleContext;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D piece = (Graphics2D) graphics.create();
        piece.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        piece.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int inset = Math.max(2, Math.min(width, height) / 12);
        piece.setColor(owner.getColor());
        piece.fillRoundRect(inset, inset, width - 2 * inset, height - 2 * inset, inset * 2, inset * 2);
        piece.setColor(Color.WHITE);
        piece.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(1, Math.min(width, height) / 6)));
        FontMetrics metrics = piece.getFontMetrics();
        piece.drawString(name, (width - metrics.stringWidth(name)) / 2,
                (height - metrics.getHeight()) / 2 + metrics.getAscent());
        piece.dispose();
    }
}
