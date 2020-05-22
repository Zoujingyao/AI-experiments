package expertsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AnimalIdentificationExpertSystem extends JFrame {
    String[] features = {"有毛", "产奶", "有羽毛", "会飞", "会下蛋", "吃肉", "有犬齿", "有爪", "眼睛盯前方", "有蹄",
            "反刍", "黄褐色", "有斑点", "有黑色条纹", "长脖", "长腿", "不会飞", "会游泳", "黑白两色", "善飞",
            "哺乳类", "鸟类", "肉食类", "蹄类", "企鹅", "海燕", "鸵鸟", "斑马", "长颈鹿", "虎", "金钱豹"};
    private JCheckBox[] checkBoxes = new JCheckBox[features.length];

    private AnimalIdentificationExpertSystem() {
        super("动物识别专家系统");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        dimension.width = (int) (dimension.width * 0.6);
        dimension.height = (int) (dimension.height * 0.6);

        this.setLayout(new FlowLayout());
        for (int i = 0; i < features.length; i++) {
            checkBoxes[i] = new JCheckBox(features[i]);
            this.add(checkBoxes[i]);
        }

        Box box = Box.createVerticalBox();

        JPanel leftPanel = new JPanel();
        JLabel[] pictures = new JLabel[7];
        Icon[] icons = new ImageIcon[7];
        for (int i = 1; i <= 7; i++) {
            pictures[i - 1] = new JLabel();
            icons[i - 1] = new ImageIcon("src/images/" + i + ".png");
            pictures[i - 1].setIcon(icons[i - 1]);
            leftPanel.add(pictures[i - 1]);
        }

        // 这样选中某动物图片
//        pictures[2].setBorder(BorderFactory.createLineBorder(Color.red, 3));
        box.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(dimension.width, (int) (dimension.height * 0.5)));
//        rightPanel.setBackground(Color.red);
        JTextArea showResult = new JTextArea("我叫邹菁瑶，这是我正在做的动物系统");
        showResult.setPreferredSize(new Dimension(dimension.width, (int) (dimension.height * 0.5)));
        rightPanel.add(showResult);
        box.add(rightPanel);

        JButton confirm = new JButton("开始");
        box.add(confirm);

        this.add(box);

        this.setSize(dimension);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                AnimalIdentificationExpertSystem.this.dispose();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        AnimalIdentificationExpertSystem animalIdentificationExpertSystem = new AnimalIdentificationExpertSystem();
    }

}
