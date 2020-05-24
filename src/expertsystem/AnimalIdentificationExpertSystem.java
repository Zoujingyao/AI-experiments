package expertsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AnimalIdentificationExpertSystem extends JFrame {
    String[] features = {"有毛", "产奶", "有羽毛", "会飞", "会下蛋", "吃肉", "有犬齿", "有爪", "眼睛盯前方", "有蹄",
            "反刍", "黄褐色", "有斑点", "有黑色条纹", "长脖", "长腿", "不会飞", "会游泳", "黑白两色", "善飞"};
    private JCheckBox[] checkBoxes = new JCheckBox[features.length];
    private String input = "";
    private int[] conditions;
    private RuleRepository ruleRepository;

    private AnimalIdentificationExpertSystem() {
        super("动物识别专家系统");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        dimension.width = (int) (dimension.width * 0.6);
        dimension.height = (int) (dimension.height * 0.6);

        this.setLayout(new FlowLayout());

        JPanel option = new JPanel();
        option.setLayout(new GridLayout(2, 10));
        option.setPreferredSize(new Dimension((int)(dimension.width * 0.95), (int) (dimension.height * 0.2)));
        for (int i = 0; i < features.length; i++) {
            checkBoxes[i] = new JCheckBox(features[i]);
            option.add(checkBoxes[i]);
        }
        this.add(option);

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

        box.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(dimension.width, (int) (dimension.height * 0.38)));
//        rightPanel.setBackground(Color.red);
        JTextArea showResult = new JTextArea("我叫邹菁瑶，这是我制作的动物识别专家系统\n");
        showResult.setPreferredSize(new Dimension(dimension.width, (int) (dimension.height * 0.38)));
        rightPanel.add(showResult);
        box.add(rightPanel);

        JButton confirm = new JButton("开始");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // 清空上一次的结果
                input = "";
                showResult.setText("");
                for(int i = 0; i < features.length; i++) {
                    if(checkBoxes[i].isSelected()) {
                        input += i;
                        input += " ";
                    }
                }
                for(int i = 0; i < 7; i++) {
                    pictures[i].setBorder(BorderFactory.createLineBorder(Color.white, 0));
                }
                // 转化为数组，按空格分割
                String[] inputs = input.split("\\s+");
                conditions = new int[inputs.length];
                for(int i = 0; i < inputs.length; i++) {
                    conditions[i] = Integer.parseInt(inputs[i]);
                }
                ruleRepository = new RuleRepository(conditions);
                String res = ruleRepository.reasoning();
                showResult.setText(res);
                String animalString = ruleRepository.getAnimals();
                String[] animals = animalString.split("\\s+");
                for(int i = 0; i < animals.length; i++){
                    if(animals[i].contentEquals("")) continue;
                    int pic = Integer.parseInt(animals[i]) - 24;
                    pictures[pic].setBorder(BorderFactory.createLineBorder(Color.red, 3));
                }
            }
        });
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
