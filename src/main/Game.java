package fisher_king.src.main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
public class Game extends JFrame {//游戏窗体
    public static pool p;//静态鱼池变量
    private Clip clip;//与播放背景音乐有关
    private Thread music=new Thread(new Runnable() {//播放背景音乐的线程
        public void run() {
            try {
                String musicLocation = "./music/bgmusic.wav";
                File musicPath = new File(musicLocation);
                if(musicPath.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                    clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    });
    public Game(){//构造方法
        setTitle("捕鱼达人");//设置标题
        setSize(1280,760);//设置窗体大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置可通关右上角按钮关闭
        music.start();//启动音乐播放的线程
        p=new pool();//给静态鱼池变量赋值，即生成一个新鱼池
        add(p);//把鱼池添加到窗体内
        addKeyListener(new KeyListener() {//添加键盘监听
            private final int[] cheatCode = {KeyEvent.VK_UP, KeyEvent.VK_UP, KeyEvent.VK_DOWN,
                    KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
                    KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT,
                    KeyEvent.VK_RIGHT, KeyEvent.VK_B,
                    KeyEvent.VK_A};//设置键盘要监听的指定输入的数组，这里为监听上上下下左右左右ba，即经典的魂斗罗作弊秘籍
            private int currentKeyIndex = 0;//数组索引

            @Override
            public void keyPressed(KeyEvent e) {
                // 检查按下的键是否与按键序列中的下一个按键匹配
                if (e.getKeyCode() == cheatCode[currentKeyIndex]) {
                    currentKeyIndex++;
                    // 如果匹配了整个按键序列，则触发事件
                    if (currentKeyIndex == cheatCode.length) {
                        new cheat();//生成一个作弊子窗体
                        currentKeyIndex = 0;//重置索引
                    }
                } else {
                    // 如果不匹配，则重置到按键序列的开头
                    currentKeyIndex = 0;//重置索引
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}//这里不需要重写，用不上

            @Override
            public void keyTyped(KeyEvent e) {}//同上
        });
        setResizable(false);//禁止玩家改变窗体大小
        setLocationRelativeTo(null);//设置居中显示
        setVisible(true);//设置窗体可见
    }
    public void stopMusic() {//用来关闭窗体的背景音乐
        if (clip != null) {
            clip.stop();
        }
    }
}
