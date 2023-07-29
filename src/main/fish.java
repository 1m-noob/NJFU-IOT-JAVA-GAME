package fisher_king.src.main;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
public class fish extends RotatedRectangle {//鱼类
    private final ImageIcon[] live_img = new ImageIcon[10];//活动动画
    private ImageIcon[] caught;//被捕获动画
    private int ID;//鱼的id
    private int caught_FPS;//鱼的被捕动画的帧个数
    private int location_x;//鱼的x坐标
    private int location_y;//鱼的y坐标
    private int V;//鱼的当前速度大小
    private int curimg=0;//当前活动图片的索引
    private int caught_curimg=0;//当前被捕图片的索引
    private boolean isCaught=false;//与鱼是否被抓有关

    public int getLocation_x(){
        return location_x;
    }
    public int getLocation_y(){
        return location_y;
    }
    public int getAngle(){//获取鱼的转向角，并根据鱼的中心坐标调整鱼的转向，保证不会有鱼被浪费
        if(location_x<-400||location_y<-400||location_x>1680||location_y>1160){
            angle+=new Random().nextInt(45)+180;
            location_x=(int)(0.8*location_x);
            location_y=(int)(0.8*location_y);
            centerX=location_x+width/2;
            centerY=location_y+height/2;
        }
        return angle;
    }


    public fish() {//构造一条随机的鱼
        this(new Random().nextInt(11) + 1,//随机鱼的id
                new Dimension(){{
                    do {//确保鱼的初始生成位置几乎不在屏幕内
                        width = new Random().nextInt(2081)-400;
                        height = new Random().nextInt(1561)-400;
                    } while (width>=-100&&height>=-100&&width<=1380&&height<=860);
                }},
                new Random().nextInt(5) +5//随机初始速度
        );
    }

    public fish(int id, Dimension dm, int v) {//构造鱼
        ID = id;
        location_x = dm.width;
        location_y = dm.height;
        V=v;
        caught_FPS = ID > 7 ? 4 : 2;
        caught = new ImageIcon[caught_FPS];
        for (int i = 0; i < 10; i++) {
            String PATH = "./Image/fish/fish" + String.format("%02d", ID) + "_" + String.format("%02d", i + 1) + ".png";
            live_img[i] = new ImageIcon(PATH);
        }
        caught_FPS = ID > 7 ? 4 : 2;
        caught = new ImageIcon[caught_FPS];
        for (int j = 0; j < caught_FPS; j++) {
            String PATH = "./Image/fish/fish" + String.format("%02d", ID) + "_catch_" + String.format("%02d", j + 1) + ".png";
            caught[j] = new ImageIcon(PATH);
        }

        width=live_img[0].getIconWidth();
        height=live_img[0].getIconHeight();
        centerX=location_x+width/2;
        centerY=location_y+height/2;

        int weight1=(dm.width+100)/840/2;//根据鱼的坐标生成的权重，影响鱼的旋转方向
        int weight2=(dm.height+100)/580/2;
        angle=(weight1*(new Random().nextInt(179)+1)+weight2*(new Random().nextInt(179)-179)+new Random().nextInt(89)+180)%360;
        //确保生成的大部分鱼会游过屏幕
    }
    public int get_current_location_x(){//获取鱼移动后更新的x坐标
        location_x-=V*Math.cos(Math.toRadians(angle));
        centerX=location_x+width/2;
        return  location_x;
    }
    public int get_current_location_y(){//获取鱼移动后更新的y坐标
        location_y-=V*Math.sin(Math.toRadians(angle));
        centerY=location_y+height/2;
        return  location_y;
    }

    public ImageIcon getcurrentimg() {//获取鱼当前应该显示的贴图
        if(!isCaught)
            return live_img[curimg++%10];
        else{//如果被抓就显示死亡动画
            return caught[caught_curimg++%caught_FPS];
        }
    }
    public int getPoint(){//获取鱼死亡后的分数，分数为随机值，但是受buff和鱼的长度影响
        return (int)(width*(new Random().nextFloat()*0.25*Buff.score_gain_magnification));
    }

    public void setV(int v) {//设置鱼的速度
        V = v;
    }

    public void setCaught(boolean caught) {//设置鱼的状态，活动/被抓
        isCaught = caught;
    }
}

