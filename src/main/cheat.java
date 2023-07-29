package fisher_king.src.main;

import javax.swing.*;
import java.awt.*;

public class cheat extends JFrame {//作弊窗体
    public cheat(){//构造方法
        setTitle("开发者模式");
        add(new cheatPanel());
        setSize(400,400);
        setVisible(true);
        setResizable(false);
    }
}
class cheatPanel extends JPanel{//定义作弊面板
    public cheatPanel(){
        setLayout(new GridLayout(5,1));
        add(new JButton("一键清屏"){{
            addActionListener(e -> {
                for(fish f:Game.p.getFishmanager()){
                    Game.p.addScore(f.getPoint());
                }
                Game.p.getFishmanager().clear();
                Game.p.initpool();
            });
        }});
        add(new JButton("增加分数"){{
            addActionListener(e -> Game.p.addScore(1000));
        }});
        add(new JButton("获取Buff"){{
            addActionListener(e -> {
                Buff.getBuffs();
            });
        }});
        add(new JButton("暂停/继续计时"){{
            addActionListener(e -> {
                pool.pause= !pool.pause;
            });
        }});
        add(new JButton("结束游戏"){{
            addActionListener(e -> {
                pool.count=0;
            });
        }});
    }
}