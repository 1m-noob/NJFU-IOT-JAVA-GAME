package fisher_king.src.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static fisher_king.src.main.Buff.*;
import static fisher_king.src.main.Buffchoose.exist;
import static fisher_king.src.main.pool.addcount;

public class Buff {//游戏加成，初值均为1即无任何加成
    public static double projectile_v_magnification=1;//影响投射物速度，即炮弹
    public static int projectile_buffs=0;//投射物buff加成次数，初始为0
    public static double catch_probability_magnification=1;//影响抓捕概率
    public static int catch_probability_buffs=0;//抓捕概率buff加成次数，初始为0
    public static double score_gain_magnification=1;//影响分数获取
    public static int score_gain_buffs=0;//分数获取提高buff加成次数，初始为0
    public static int add_time_buffs=0;//时间增加buff获取次数
    public static int dont_need_buffs=0;//不需要buff的次数
    private static double function(double x){//新倍率计算函数，会收敛于2
        return 1+x/(x+2);
    }//用来计算增益的函数，收敛于2
    public static void new_pj_v_mag(){//更新投射物加速buff提供的倍率
        projectile_v_magnification=function(++projectile_buffs/2.0);
    }
    public static double see_new_pj(){
        return function((projectile_buffs+1)/2.0);
    }//用于给玩家选择buff时查看buff效果
    public static void new_catch_p_mag(){//更新抓捕概率提高buff的倍率
        catch_probability_magnification=function(++catch_probability_buffs/2.0);
    }
    public static double see_new_catchp(){
        return function((catch_probability_buffs+1)/2.0);
    }//用于给玩家选择buff时查看buff效果
    public static void new_score_g_mag(){//更新分数获取提高buff的倍率
        score_gain_magnification=function(++score_gain_buffs/2.0);
    }
    public static double see_new_score_g(){
        return function((score_gain_buffs+1)/2.0);
    }//用于给玩家选择buff时查看buff效果
    public static void getBuffs(){//用于给其他类调用生成buff选择窗体，已经存在时将不会有新窗体生成
        if(!exist){
        Buffchoose bc=new Buffchoose();
        exist=true;
        }
    }
}

class Buffchoose extends JFrame{//buff选择窗体的相关设置
    public static boolean exist=false;//这个变量用于控制窗体是否允许生成
    public Buffchoose(){//构造方法
        setLocation(400,0);//窗体位置，故意不居中，防止误触
        addcount(5);//每次触发buff选择时都延时5秒作为奖励
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("选择你的Buff");
        setSize(800,400);
        add(new Panel(){{
            setLayout(new GridLayout(1,5));//设置网格自适应布局，1行4列
            //下面的html语言只是用来控制按钮上的文本换行和空格
            add(new BuffButton(String.format("<html>&nbsp&nbsp&nbsp&nbsp极速动量<br>&nbsp&nbsp投射物速度<br>提高至%.2f%%</html>",see_new_pj()*100),new ImageIcon("./Image/Buff/momentum.png")){{
                addActionListener(e -> {
                    new_pj_v_mag();
                    dispose();
                    exist=false;
                });
            }});
            add(new BuffButton(String.format("<html>&nbsp&nbsp&nbsp&nbsp老练捕手<br>&nbsp&nbsp&nbsp&nbsp抓捕概率<br>提高至%.2f%%</html>",see_new_catchp()*100),new ImageIcon("./Image/Buff/oldfisher.png")){{
                addActionListener(e -> {
                    new_catch_p_mag();
                    dispose();
                    exist=false;
                });
            }});
            add(new BuffButton(String.format("<html>&nbsp&nbsp&nbsp&nbsp多多益善<br>&nbsp&nbsp&nbsp&nbsp分数获取<br>提高至%.2f%%</html>",see_new_score_g()*100),new ImageIcon("./Image/Buff/ineedmore.png")){{
                addActionListener(e -> {
                    new_score_g_mag();
                    dispose();
                    exist=false;
                });
            }});
            add(new BuffButton("<html>&nbsp&nbsp&nbsp&nbsp&nbsp紧急延迟<br>倒计时延迟20秒</html>",new ImageIcon("./Image/Buff/time.png")){{
                addActionListener(e -> {
                    addcount(20);
                    add_time_buffs++;
                    dispose();
                    exist=false;
                });
            }});
            add(new BuffButton("<html>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp自信强者<br>我不需要任何Buff</html>",new ImageIcon("./Image/Buff/king.png")){{
                addActionListener(e -> {
                    dont_need_buffs++;
                    dispose();
                    exist=false;});
            }});
        }});
        setResizable(false);
        setVisible(true);
    }
}
class BuffButton extends JButton {//buff按钮设置，主要用来美化，涉及背景图片，文本大小，颜色等
    private final ImageIcon originalIcon;//按钮上的图片
    private final ImageIcon scaledIcon;//鼠标移动到按钮上时的图片，原图放大30%

    public BuffButton(String s, ImageIcon img) {
        super(s, img);//父类的构造方法
        originalIcon = img;
        scaledIcon = new ImageIcon(getScaledImage(img.getImage(), (int) (img.getIconWidth() * 1.30), (int) (img.getIconHeight() * 1.30)));
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        //设置文本在图片下方
        setForeground(Color.RED);//设置红字
        addMouseListener(new MouseAdapter() {//添加鼠标监听
            @Override
            public void mouseEntered(MouseEvent e) {//鼠标放置在按钮上触发
                setForeground(Color.ORANGE);//设置橘色字体
                setIcon(scaledIcon);//设置放大的图标
                setContentAreaFilled(true);//允许填充
                setBackground(Color.DARK_GRAY);//填充灰色
            }

            @Override
            public void mouseExited(MouseEvent e) {//鼠标从按钮上移开触发

                setForeground(Color.RED);
                setIcon(originalIcon);
                setContentAreaFilled(false);//禁止填充
            }
        });
    }

    private Image getScaledImage(Image srcImg, int w, int h) {//用来获取放大后图片的方法
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
}
