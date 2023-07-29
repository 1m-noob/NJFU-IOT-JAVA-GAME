package fisher_king.src.main;

import javax.swing.*;
import java.awt.*;

public class Net extends RotatedRectangle{//网类
    private Image bimg= new ImageIcon("./Image/net/net09.png").getImage();//网的图片
    private int times=5;//网显示的次数，50ms刷新一次，也就是网只会显示0.25s

    public Net(int x,int y){
        width=160;
        height=160;
        angle=0;
        centerX=x;
        centerY=y;
    }
    public int getTimes(){//获取这个网还有多少次刷新会消失，为0就消失
        if(times<=0)
            return 0;
        else
            return times--;
    }
    public Image getBimg(){//给其他类获取网图片的方法
        return bimg;
    }
}
