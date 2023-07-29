package fisher_king.src.main;

import javax.swing.*;
import java.awt.*;

public class Barrel {//大炮类
    private final Image brl=new ImageIcon("./Image/barrel/barrel.png").getImage();//大炮贴图路径
    private static final int width=42;//大炮贴图长度
    private static final int height=62;//大炮贴图宽度
    private static final int cenX=619;//大炮中心坐标
    private static final int cenY=698;
    private int angle=0;//大炮默认旋转角度

    public int getWidth() {
        return width;
    }

    public Image getBrl() {
        return brl;
    }

    public int getHeight() {
        return height;
    }

    public int getCenX() {
        return cenX;
    }

    public int getCenY() {
        return cenY;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
