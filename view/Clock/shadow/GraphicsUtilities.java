package view.Clock.shadow;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import javax.imageio.ImageIO;

public class GraphicsUtilities {
    private GraphicsUtilities() {
    }

    private static GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    public static BufferedImage createColorModelCompatibleImage(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        return new BufferedImage(cm, cm.createCompatibleWritableRaster(image.getWidth(), image.getHeight()), cm.isAlphaPremultiplied(), (Hashtable)null);
    }

    public static BufferedImage createCompatibleImage(BufferedImage image) {
        return createCompatibleImage(image, image.getWidth(), image.getHeight());
    }

    public static BufferedImage createCompatibleImage(BufferedImage image, int width, int height) {
        return getGraphicsConfiguration().createCompatibleImage(width, height, image.getTransparency());
    }

    public static BufferedImage createCompatibleImage(int width, int height) {
        return getGraphicsConfiguration().createCompatibleImage(width, height);
    }

    public static BufferedImage createCompatibleTranslucentImage(int width, int height) {
        return getGraphicsConfiguration().createCompatibleImage(width, height, 3);
    }

    public static BufferedImage loadCompatibleImage(URL resource) throws IOException {
        BufferedImage image = ImageIO.read(resource);
        return toCompatibleImage(image);
    }

    public static BufferedImage toCompatibleImage(BufferedImage image) {
        if (image.getColorModel().equals(getGraphicsConfiguration().getColorModel())) {
            return image;
        } else {
            BufferedImage compatibleImage = getGraphicsConfiguration().createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
            Graphics g = compatibleImage.getGraphics();
            g.drawImage(image, 0, 0, (ImageObserver)null);
            g.dispose();
            return compatibleImage;
        }
    }

    public static BufferedImage createThumbnailFast(BufferedImage image, int newSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio;
        if (width > height) {
            if (newSize >= width) {
                throw new IllegalArgumentException("newSize must be lower than the image width");
            }

            if (newSize <= 0) {
                throw new IllegalArgumentException("newSize must be greater than 0");
            }

            ratio = (float)width / (float)height;
            width = newSize;
            height = (int)((float)newSize / ratio);
        } else {
            if (newSize >= height) {
                throw new IllegalArgumentException("newSize must be lower than the image height");
            }

            if (newSize <= 0) {
                throw new IllegalArgumentException("newSize must be greater than 0");
            }

            ratio = (float)height / (float)width;
            height = newSize;
            width = (int)((float)newSize / ratio);
        }

        BufferedImage temp = createCompatibleImage(image, width, height);
        Graphics2D g2 = temp.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), (ImageObserver)null);
        g2.dispose();
        return temp;
    }

    public static BufferedImage createThumbnailFast(BufferedImage image, int newWidth, int newHeight) {
        if (newWidth < image.getWidth() && newHeight < image.getHeight()) {
            if (newWidth > 0 && newHeight > 0) {
                BufferedImage temp = createCompatibleImage(image, newWidth, newHeight);
                Graphics2D g2 = temp.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), (ImageObserver)null);
                g2.dispose();
                return temp;
            } else {
                throw new IllegalArgumentException("newWidth and newHeight must be greater than 0");
            }
        } else {
            throw new IllegalArgumentException("newWidth and newHeight cannot be greater than the image dimensions");
        }
    }

    public static BufferedImage createThumbnail(BufferedImage image, int newSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        boolean isWidthGreater = width > height;
        if (isWidthGreater) {
            if (newSize >= width) {
                throw new IllegalArgumentException("newSize must be lower than the image width");
            }
        } else if (newSize >= height) {
            throw new IllegalArgumentException("newSize must be lower than the image height");
        }

        if (newSize <= 0) {
            throw new IllegalArgumentException("newSize must be greater than 0");
        } else {
            float ratioWH = (float)width / (float)height;
            float ratioHW = (float)height / (float)width;
            BufferedImage thumb = image;

            BufferedImage temp;
            do {
                if (isWidthGreater) {
                    width /= 2;
                    if (width < newSize) {
                        width = newSize;
                    }

                    height = (int)((float)width / ratioWH);
                } else {
                    height /= 2;
                    if (height < newSize) {
                        height = newSize;
                    }

                    width = (int)((float)height / ratioHW);
                }

                temp = createCompatibleImage(image, width, height);
                Graphics2D g2 = temp.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(thumb, 0, 0, temp.getWidth(), temp.getHeight(), (ImageObserver)null);
                g2.dispose();
                thumb = temp;
            } while(newSize != (isWidthGreater ? width : height));

            return temp;
        }
    }

    public static BufferedImage createThumbnail(BufferedImage image, int newWidth, int newHeight) {
        int width = image.getWidth();
        int height = image.getHeight();
        if (newWidth < width && newHeight < height) {
            if (newWidth > 0 && newHeight > 0) {
                BufferedImage thumb = image;

                BufferedImage temp;
                do {
                    if (width > newWidth) {
                        width /= 2;
                        if (width < newWidth) {
                            width = newWidth;
                        }
                    }

                    if (height > newHeight) {
                        height /= 2;
                        if (height < newHeight) {
                            height = newHeight;
                        }
                    }

                    temp = createCompatibleImage(image, width, height);
                    Graphics2D g2 = temp.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(thumb, 0, 0, temp.getWidth(), temp.getHeight(), (ImageObserver)null);
                    g2.dispose();
                    thumb = temp;
                } while(width != newWidth || height != newHeight);

                return temp;
            } else {
                throw new IllegalArgumentException("newWidth and newHeight must be greater than 0");
            }
        } else {
            throw new IllegalArgumentException("newWidth and newHeight cannot be greater than the image dimensions");
        }
    }

    public static int[] getPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels) {
        if (w != 0 && h != 0) {
            if (pixels == null) {
                pixels = new int[w * h];
            } else if (pixels.length < w * h) {
                throw new IllegalArgumentException("pixels array must have a length >= w*h");
            }

            int imageType = img.getType();
            if (imageType != 2 && imageType != 1) {
                return img.getRGB(x, y, w, h, pixels, 0, w);
            } else {
                Raster raster = img.getRaster();
                return (int[])((int[])raster.getDataElements(x, y, w, h, pixels));
            }
        } else {
            return new int[0];
        }
    }

    public static void setPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels) {
        if (pixels != null && w != 0 && h != 0) {
            if (pixels.length < w * h) {
                throw new IllegalArgumentException("pixels array must have a length >= w*h");
            } else {
                int imageType = img.getType();
                if (imageType != 2 && imageType != 1) {
                    img.setRGB(x, y, w, h, pixels, 0, w);
                } else {
                    WritableRaster raster = img.getRaster();
                    raster.setDataElements(x, y, w, h, pixels);
                }

            }
        }
    }
}
