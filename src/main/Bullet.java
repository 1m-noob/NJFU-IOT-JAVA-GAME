package fisher_king.src.main;

import javax.swing.*;
import java.awt.*;

public class Bullet extends RotatedRectangle{//子弹类
    private static final Image bullet=new ImageIcon("./Image/bullet/bullet.png").getImage();//子弹贴图路径
    private int v= (int) (-15*Buff.projectile_v_magnification);//子弹速度
    public Bullet(int _x,int _y,int _angle){
        super(_x,_y,24,26,_angle);
    }

    public Image getBullet() {
        return bullet;
    }

    public int getAngle() {
        return super.angle;
    }

    public int get_current_x(){//更新子弹当前中心x坐标并返回
        centerX+=(int)v*Math.sin(Math.toRadians(-angle));
        return centerX-width/2;
    }

    public int get_current_y(){//更新子弹当前中心y坐标并返回
        centerY+=(int)v*Math.cos(Math.toRadians(-angle));
        return centerY-height/2;
    }

    public int getCenX() {
        return centerX;
    }

    public int getCenY() {
        return centerY;
    }
}
