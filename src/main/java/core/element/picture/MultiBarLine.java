package core.element.picture;

import org.apache.poi.util.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Усольцев Иван
 */
public class MultiBarLine implements PictureElement {

    private int leftPosition;
    private int topPosition;
    private int rightPosition;
    private int bottomPosition;
    private int height = 1;
    private int width = 0;
    private boolean autoSize = true;
    private List<Bar> bars = new ArrayList<>();

    /**
     * CONSTRUCTORS
     */
    public MultiBarLine() {
    }

    public MultiBarLine(int leftPosition, int topPosition, int rightPosition, int bottomPosition, int height) {
        this.leftPosition = leftPosition;
        this.topPosition = topPosition;
        this.rightPosition = rightPosition;
        this.bottomPosition = bottomPosition;
        this.height = height;
    }

    /**
     * METHODS
     */
    public void addBar(Bar bar){
        this.addBar(bar.getWidth(), bar.getColorRGB(), bar.getTitle());
    }

    public void addBar(int width, String colorRGB){
        this.addBar(width, colorRGB, "");
    }

    public void addBar(int width, String colorRGB, String title){
        Bar bar = new Bar(0, 0,0,0, colorRGB);
        bar.setWidth(width);
        bar.setHeight(this.height);
        bar.setTitle(title);
        this.bars.add(bar);
        this.width += width;
    }

    public List<Bar> getBars(){
        return Collections.unmodifiableList(this.bars);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        final int WIDTH = this.width;
        final int HEIGHT = this.height;

        int i = 0;
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (Bar bar : this.bars) {
            Integer rgb = Integer.decode(bar.getColorRGB());
            int chunkSize = bar.getWidth();
            for (int j = 0; j < chunkSize; j++) {
                for (int k = 0; k < bufferedImage.getHeight(); k++) {
                    bufferedImage.setRGB(i, k, rgb);
                }
                i++;
            }
        }
        ByteArrayOutputStream byteArrayOps = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            ImageIO.write(bufferedImage, "png", byteArrayOps);
            is = new ByteArrayInputStream(byteArrayOps.toByteArray());
            byte[] bytes = IOUtils.toByteArray(is);
            return bytes;
        }finally {
            if(is != null) is.close();
            byteArrayOps.close();
        }
    }

    @Override
    public void resize(int width, int height) {
        final int EX_WIDTH = this.width;
        final int EX_HEIGHT = this.height;
        float kW = (float) width / EX_WIDTH;
        float kH = (float) height / EX_HEIGHT;

        this.height = height;
        this.width = width;

        int bufferWidth = 0;
        for(Bar b : bars){
            int w = Math.round(b.getWidth() * kW);
            if(bufferWidth + w > width) w = w -1;
            b.setWidth(w);
            bufferWidth += w;
            int h = Math.round(b.getHeight() * kH);
            b.setHeight(h);
        }
        if(bufferWidth < width){
            Bar bar = bars.get(0);
            int w = Math.round(width - bufferWidth + bar.getWidth());
            bar.setWidth(w);
        }
    }

    @Override
    public int getLeftPosition() {
        return leftPosition;
    }

    @Override
    public void setLeftPosition(int leftPosition) {
        this.leftPosition = leftPosition;
    }

    @Override
    public int getTopPosition() {
        return topPosition;
    }

    @Override
    public void setTopPosition(int topPosition) {
        this.topPosition = topPosition;
    }

    @Override
    public int getRightPosition() {
        return rightPosition;
    }

    @Override
    public void setRightPosition(int rightPosition) {
        this.rightPosition = rightPosition;
    }

    @Override
    public int getBottomPosition() {
        return bottomPosition;
    }

    @Override
    public void setBottomPosition(int bottomPosition) {
        this.bottomPosition = bottomPosition;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
    }

    @Override
    public boolean getAutoSize() {
        return autoSize;
    }
}