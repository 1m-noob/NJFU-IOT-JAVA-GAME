package fisher_king.src.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.*;

import static fisher_king.src.main.Buff.*;

public class pool extends JPanel implements MouseMotionListener {//鱼池类
    private final Vector<fish> Fishmanager =new Vector<>();//管理所有的鱼，vector是线程安全的
    private final Vector<Bullet> Bulletmanager=new Vector<>();//管理所有的子弹
    private final Vector<Net> Netmanager=new Vector<>();//管理所有的网
    private Image[] backgroundimg=new Image[7];//鱼池背景图，每1000分刷新一次，仅防止审美疲劳
    private final Barrel B=new Barrel();//生成大炮
    private java.util.Timer timer = new Timer();//新建一个定时器，用来规划任务（鱼被抓到后固定延迟后被销毁）

    private JLabel score_label=new JLabel("分数:");//分数面板初始化
    private JLabel time_left=new JLabel("剩余时间:");//倒计时面板初始化
    private int score =0;
    public static int count =120;//倒计时初值
    private boolean running=true;//控制是否重绘，停止重绘游戏就会暂停，默认为true
    public static boolean pause=false;//控制是否暂停时间
    private Thread update=new Thread(() -> {//控制重绘的线程，50ms刷新一次，即20帧
        while (running) {
            repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    });
    private ActionListener task = new ActionListener() {//倒计时事件
        public void actionPerformed(ActionEvent evt) {
            if (count > 0&&!pause) {//不暂停的情况下正常倒计时
                count--;
            } else if(count<=0){//倒计时为0触发
                ((javax.swing.Timer) evt.getSource()).stop();//停止倒计时
                running=false;//停止重绘
                if (JOptionPane.showConfirmDialog(null, "你的分数是:"+score, "游戏结束！", JOptionPane.DEFAULT_OPTION) == JOptionPane.YES_OPTION) {
                    //抛出消息框，并在玩家确认时触发下列事件
                    Game parentFrame = (Game) SwingUtilities.getWindowAncestor(pool.this);//获取游戏窗体，并声明为parentFrame方便后续操作
                    parentFrame.dispose();//关闭游戏窗体
                    parentFrame.stopMusic();//关闭背景音乐
                    new Welcome();//生成新的欢迎窗体
                }

            }
        }
    };
    public void initpool(){
        for(int i=0;i<50;i++){
            Fishmanager.addElement(new fish());
        }//初始化鱼塘，加50条鱼
    }
    public void load_bgimg(){//初始化背景图数组
        for(int i=0;i<7;i++){
            String path="./Image/background/fishlightbg_"+String.format("%d",i)+".jpg";
            backgroundimg[i]=new ImageIcon(path).getImage();
        }
    }
    public pool(){//构造鱼池
        count =120;//重置倒计时
        running=true;//允许重绘
        initpool();//加鱼
        addMouseMotionListener(this);//添加鼠标监听
        new javax.swing.Timer(1000,task).start();//开始倒计时
        update.start();//定时重绘
        load_bgimg();//加载背景图
        setLayout(null);//设置绝对布局
        add(score_label);//添加分数面板
        add(time_left);//添加倒计时面板
        score_label.setBounds(0,0,500,25);//设置分数面板大小位置
        time_left.setBounds(0,25,200,25);
        time_left.setForeground(Color.ORANGE);
        time_left.setFont(new Font(null,Font.PLAIN,25));
        score_label.setForeground(Color.RED);//设置分数面板的字体颜色
        score_label.setFont(new Font(null, Font.PLAIN,25));//设置分数面板的字体大小
        //添加鼠标点击事件监听
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {//点击鼠标左键效果
                    Bulletmanager.addElement(new Bullet(B.getCenX(), B.getCenY(), B.getAngle()));//添加子弹
                }
            }
        });
    }

    public void check() {//检测鱼和子弹的碰撞，发生碰撞会销毁子弹生成网动画，并根据抓捕概率销毁鱼，抓捕概率被设计成与鱼的width有关，width越大越难抓
        Iterator<Bullet> bullet_iter = Bulletmanager.iterator();//迭代所有子弹进行判断
        while (bullet_iter.hasNext()) {
            Bullet b = bullet_iter.next();
            for (int i = Fishmanager.size() - 1; i >= 0; i--) {//倒序遍历所有鱼，删除鱼时可避免报错
                fish f = Fishmanager.get(i);
                if (b.isIntersecting(f)) {//判断鱼和子弹是否相交
                    double iscaught = new Random().nextFloat();
                    if(iscaught<=(1.0/f.width*24.0*Buff.catch_probability_magnification))//对本次是否抓到鱼进行判断（概率捕捉）
                    {//如果判断抓捕成功则触发下列事件
                        f.setV(0);//设置鱼速度为0
                        f.setCaught(true);//设置鱼为被捕状态
                        TimerTask task = new TimerTask() {
                            @Override
                            public void  run() {
                                Fishmanager.remove(f);
                                Fishmanager.addElement(new fish());
                                addScore(f.getPoint());
                            }
                        };
                        // 安排销毁鱼任务，不立刻删除是为了使用鱼死亡动画
                        timer.schedule(task, 1000);//一秒后销毁鱼
                    }
                    bullet_iter.remove();//删除当前子弹（无论是否捕获成功），迭代器可安全删除元素不报错
                    Netmanager.addElement(new Net(b.centerX,b.centerY));//在原地生成一个网，提升视觉效果
                    break;//子弹已销毁，需立刻结束当前遍历（防止报错），进入一个新的遍历
                }
            }
        }
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        int a=e.getX()-B.getCenX();
        int b=e.getY()-B.getCenY();
        double Angle=Math.atan((double) b /a);
        int angle=(int)Math.toDegrees(Angle);
        B.setAngle(angle<0?90+angle:angle-90);
    }//获取鼠标坐标，旋转炮台

    @Override
    public void mouseDragged(MouseEvent e) {//用不上，不需要重写

    }
    @Override
    public void paintComponent(Graphics g) {//重写绘制方法
        super.paintComponent(g);//调用父类绘制方法
        g.drawImage(backgroundimg[score/1000%7],0, 0,this.getWidth(), this.getHeight(), this);//绘制背景图，根据分数切换背景
        for(int i=Bulletmanager.size()-1;i>=0;i--){//绘制子弹，倒序遍历，删除元素时可避免报错
            Bullet b =Bulletmanager.get(i);//获取当前遍历的元素
            if(b.getCenX()<=-50||b.getCenY()<=-50||b.getCenX()>1330||b.getCenY()>830){
                Bulletmanager.remove(b);//如果子弹超过屏幕范围立刻销毁
                continue;//直接遍历下一个子弹
            }
            Graphics2D g_2d=(Graphics2D) g.create();//构建一个新的Graphics2D对象，以初始的g为基础
            int ANGLE =b.getAngle();
            int current_x=ANGLE<0?b.get_current_x()-b.width/8:b.get_current_x()+b.width/8;
            int current_y=ANGLE<0?b.get_current_y()-b.height/8:b.get_current_y()+b.height/8;
            g_2d.rotate(Math.toRadians(ANGLE), b.getCenX(), b.getCenY());//旋转当前绘制坐标系，与炮台旋转方向一致
            g_2d.drawImage(b.getBullet(),current_x,current_y, b.width, b.height,this);//在旋转的坐标系中绘制子弹
        }//绘制子弹
        check();//检测碰撞
        synchronized (Fishmanager){for(fish f:Fishmanager){//绘制鱼
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.rotate(Math.toRadians(f.getAngle()), f.getLocation_x(), f.getLocation_y());
            g2d.drawImage(f.getcurrentimg().getImage(), f.get_current_location_x(), f.get_current_location_y(), f.width, f.height, this);
        }}//绘制鱼
        for(int j=Netmanager.size()-1;j>=0;j--){//绘制网，提升视觉效果
            Net n=Netmanager.get(j);
            int t=n.getTimes();
            if(t>0){
                g.drawImage(n.getBimg(),n.centerX-n.width/2,n.centerY-n.height/2,n.width,n.height,this);
            }
            else{
                Netmanager.remove(n);//不需要这个网时将其销毁
            }
        }//绘制网
        Graphics2D G2d=(Graphics2D)g.create();//绘制大炮
        G2d.rotate(Math.toRadians(B.getAngle()),B.getCenX(),B.getCenY());
        G2d.drawImage(B.getBrl(),B.getCenX()-B.getWidth()/2,B.getCenY()-B.getHeight()/2,B.getWidth(),B.getHeight(),this);
        score_label.setText("分数:"+ score);//更新分数
        time_left.setText("剩余时间:"+count+"秒");//更新倒计时
        if((score+1)/200>projectile_buffs+catch_probability_buffs+score_gain_buffs+add_time_buffs+dont_need_buffs)
            getBuffs();//每获得200分触发buff选择
    }//绘制每一帧的画面

    public Vector<fish> getFishmanager() {
        return Fishmanager;
    }

    public void addScore(int score) {//用来增加分数
        this.score += score;
    }
    public static void addcount(int x){//用来延长倒计时
        count+=x;
    }
}
