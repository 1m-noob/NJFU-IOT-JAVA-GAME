package fisher_king.src.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome extends JFrame {//开始窗体
    public Welcome(){//构造方法
        setTitle("捕鱼达人");//设置窗体名称
        setSize(643,433);//设置大小
        setLocationRelativeTo(null);//设置居中
        setResizable(false);//禁止用户改变大小
        add(new JPanel(){//通过匿名内部类的方式添加一个新的Jpanel
            @Override
            public void paintComponent(Graphics g) {//重写绘制方法，放置背景图片
                super.paintComponents(g);
                g.drawImage(new ImageIcon("./Image/welcome/welcome.png").getImage(),0,0,this.getWidth(),this.getHeight(),this);
            }
            {
                setLayout(null);//设置绝对布局
                add(new JButton("开始游戏"){{//匿名内部类，添加按钮
                    addActionListener(new ActionListener() {//匿名内部类，添加按钮监听
                        @Override
                        public void actionPerformed(ActionEvent e) {//按下按钮触发下面的事件
                            new Game();//生成游戏窗体
                            dispose();//关闭当前窗体
                        }
                    });
                    setBounds(50,250,100,35);//设置按钮的大小及位置
                }});
                add(new JButton("退出游戏"){{//匿名内部类，添加按钮
                    addActionListener(new ActionListener() {//匿名内部类，添加按钮监听
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    });
                    setBounds(50,290,100,35);//设置大小位置
                }});

        }});
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置窗体关闭方式
        setVisible(true);//设置可见
    }
}
