package RandomGenerator2;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static RandomGenerator2.RT2.transparency;

public class BackgroundPanel extends JPanel {
    private Image image;

    public boolean setBackgroundImage(String path) {
        //异步处理图片
        image = Toolkit.getDefaultToolkit().createImage(path);
        //自动触发paintComponent
        repaint();
        File pf = new File(path);
        if (pf.exists() && pf.isFile()) {
            return pf.getName().contains("_light");
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            //SRC_OVER 表示最常用的“最上面图层盖在下面图层上”的混色规则。
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
            //自适应
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            //释放资源
            g2.dispose();
        }
    }
}
