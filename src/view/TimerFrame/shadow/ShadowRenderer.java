package view.TimerFrame.shadow;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.awt.Color;
import java.awt.image.BufferedImage;

public class ShadowRenderer {
    private int size;
    private float opacity;
    private Color color;

    public ShadowRenderer() {
        this(5, 0.5F, Color.BLACK);
    }

    public ShadowRenderer(int size, float opacity, Color color) {
        this.size = 5;
        this.opacity = 0.5F;
        this.color = Color.BLACK;
        this.size = size;
        this.opacity = opacity;
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public int getSize() {
        return this.size;
    }

    public BufferedImage createShadow(BufferedImage image) {
        int shadowSize = this.size * 2;
        int srcWidth = image.getWidth();
        int srcHeight = image.getHeight();
        int dstWidth = srcWidth + shadowSize;
        int dstHeight = srcHeight + shadowSize;
        int left = this.size;
        int right = shadowSize - left;
        int yStop = dstHeight - right;
        int shadowRgb = this.color.getRGB() & 16777215;
        int[] aHistory = new int[shadowSize];
        BufferedImage dst = new BufferedImage(dstWidth, dstHeight, 2);
        int[] dstBuffer = new int[dstWidth * dstHeight];
        int[] srcBuffer = new int[srcWidth * srcHeight];
        GraphicsUtilities.getPixels(image, 0, 0, srcWidth, srcHeight, srcBuffer);
        int lastPixelOffset = right * dstWidth;
        float hSumDivider = 1.0F / (float)shadowSize;
        float vSumDivider = this.opacity / (float)shadowSize;
        int[] hSumLookup = new int[256 * shadowSize];

        for(int i = 0; i < hSumLookup.length; ++i) {
            hSumLookup[i] = (int)((float)i * hSumDivider);
        }

        int[] vSumLookup = new int[256 * shadowSize];

        int srcOffset;
        for(srcOffset = 0; srcOffset < vSumLookup.length; ++srcOffset) {
            vSumLookup[srcOffset] = (int)((float)srcOffset * vSumDivider);
        }

        int x = 0;

        int historyIdx;
        int aSum;
        int bufferOffset;
        int y;
        int a;
        for(bufferOffset = left * dstWidth; x < srcHeight; ++x) {
            for(historyIdx = 0; historyIdx < shadowSize; aHistory[historyIdx++] = 0) {
            }

            aSum = 0;
            historyIdx = 0;
            srcOffset = x * srcWidth;

            for(y = 0; y < srcWidth; ++y) {
                a = hSumLookup[aSum];
                dstBuffer[bufferOffset++] = a << 24;
                aSum -= aHistory[historyIdx];
                a = srcBuffer[srcOffset + y] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;
                ++historyIdx;
                if (historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }

            for(y = 0; y < shadowSize; ++y) {
                a = hSumLookup[aSum];
                dstBuffer[bufferOffset++] = a << 24;
                aSum -= aHistory[historyIdx];
                ++historyIdx;
                if (historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }
        }

        x = 0;

        for(bufferOffset = 0; x < dstWidth; bufferOffset = x) {
            aSum = 0;

            for(historyIdx = 0; historyIdx < left; aHistory[historyIdx++] = 0) {
            }

            for(y = 0; y < right; bufferOffset += dstWidth) {
                a = dstBuffer[bufferOffset] >>> 24;
                aHistory[historyIdx++] = a;
                aSum += a;
                ++y;
            }

            bufferOffset = x;
            historyIdx = 0;

            for(y = 0; y < yStop; bufferOffset += dstWidth) {
                a = vSumLookup[aSum];
                dstBuffer[bufferOffset] = a << 24 | shadowRgb;
                aSum -= aHistory[historyIdx];
                a = dstBuffer[bufferOffset + lastPixelOffset] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;
                ++historyIdx;
                if (historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }

                ++y;
            }

            for(y = yStop; y < dstHeight; bufferOffset += dstWidth) {
                a = vSumLookup[aSum];
                dstBuffer[bufferOffset] = a << 24 | shadowRgb;
                aSum -= aHistory[historyIdx];
                ++historyIdx;
                if (historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }

                ++y;
            }

            ++x;
        }

        GraphicsUtilities.setPixels(dst, 0, 0, dstWidth, dstHeight, dstBuffer);
        return dst;
    }
}
