package eightpuzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class EightPuzzle extends JFrame{
    private List<Solution> solutions = new ArrayList<Solution>();
    //0~10的阶乘值
    private int[] factorial={1,1,2,6,24,120,720,5040,40320,362880,3628800};
    private int n = 9;
    //初始状态序列
    private int[] origin;
    //目标状态序列
    private int[] goal;
    private int startId, goalId;
    private int max = 400000;
    private int[] parent = new int[max]; //每个结点的前结点
    private int[] direction = new int[max]; //到这个结点的方向
    private boolean[] checked = new boolean[max];
    private String result = "";
    private int zeroIdx;


    // 于界面有关的
    private JTextField[] blocks = new JTextField[9];
    private JTextField originField;
    private JTextField goalField;
    private JTextField resultField;

    private EightPuzzle(){
        super("八数码求解器");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        dimension.width = (int) (dimension.width * 0.2);
        dimension.height = (int) (dimension.height * 0.42);

        Box box = Box.createVerticalBox();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));
        for(int i = 0;  i < 9; i++) {
            blocks[i] = new JTextField();
            blocks[i].setHorizontalAlignment(JTextField.CENTER);
            blocks[i].setFont(new Font("宋体", Font.PLAIN, 30));
            panel.add(blocks[i]);
        }
        box.add(panel);

        Box tip = Box.createVerticalBox();
        JLabel label = new JLabel("欢迎！" + "\n" + "空格用0表示。");
        tip.add(label);
        JLabel originLabel = new JLabel("初始序列");
        tip.add(originLabel);
        originField = new JTextField();
        tip.add(originField);
        JLabel goalLabel = new JLabel("目标序列");
        tip.add(goalLabel);
        goalField = new JTextField();
        tip.add(goalField);
        JButton confirm = new JButton("开始");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(originField.getText().isEmpty() || goalField.getText().isEmpty() ){
                    JOptionPane.showMessageDialog(null, "请输入初始序列和目标序列", "输入为空！", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    IO();
                    if(!isSovable()){
                        JOptionPane.showMessageDialog(null, "这两个状态之间不可达。", "不可达状态！", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        Solution start = new Solution(startId, n);
                        solutions.add(start);
                        heuristicSearch();
                        drawRoute();
                    }
                }

            }
        });

        tip.add(confirm);
        resultField = new JTextField();
        tip.add(resultField);
        box.add(tip);

        this.add(box);
        this.setSize(dimension);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                System.exit(0);
            }
        });
    }
    /*
     * 康托展开式，实现全排列到自然数的映射
     */
    private int cantor(int[] numbers){
        int ans = 0, sum = 0;
        for(int i = 0; i < n-1; i++){
            for(int j = i + 1; j < n; j++)
                if(numbers[j] < numbers[i])
                    sum++;
            ans += sum * factorial[n-i-1];
            sum = 0;
        }
        return ans;
    }

    /*
     * 利用初始状态和目标状态的数列逆序值的奇偶性判断两个状态是否可达
     * 二者奇偶性一致则可达; 反之，不可达
     * 逆序值：位于这个数之前，比这个数大的数的个数
     * 数列逆序值：数列中每个数的逆序值之和
     *
     * 注意算逆序值时忽略空格0
     */
    private boolean isSovable(){
        int originReverseValue = 0;
        int goalReverseValue = 0;
        for(int i = 1; i < n; i++){
            if(origin[i] != 0) {
                for (int j = 0; j < i; j++) {
                    if (origin[j] > origin[i]) originReverseValue++;
                }
            }
        }
        for(int i = 1; i < n; i++){
            if(goal[i] != 0){
                for(int j = 0; j < i; j++){
                    if(goal[j] > goal[i]) goalReverseValue++;
                }
            }
        }
        if(originReverseValue % 2 == goalReverseValue % 2){
            return true;
        }
        else
            return false;
    }

    public void IO(){
        origin = new int[n];
        goal = new int[n];
        String originString = originField.getText();
        for(int i = 0; i < n; i++){
            origin[i] = Integer.parseInt(originString.substring(i, i+1));
            if(origin[i] == 0)
                zeroIdx = i;
            blocks[i].setText(String.valueOf(origin[i]));
        }
        String goalString = goalField.getText();
        for(int i = 0; i < n; i++){
            goal[i] = Integer.parseInt(goalString.substring(i, i+1));
        }
        startId = cantor(origin);
        goalId = cantor(goal);
        System.out.println("startId, goalId:" + startId + ", " + goalId);
    }

    /*
     * 按照解的启发式函数值从大到小排序，返回最好的解
     */
    public Solution getFittest() {
        solutions.sort(new Comparator<Solution>() {
            @Override
            public int compare(Solution solution, Solution t1) {
                //按照启发函数值从大到小排序
                if(solution.getFitness(goal) < t1.getFitness(goal))
                    return 1;
                else return -1;
            }
        });
        // Return the fittest solution
        return solutions.get(0);
    }

    public void printResult(int id){
        if(id == startId) {
            return;
        }
        printResult(parent[id]);
        if(direction[id] == 1) {
            result += "u";
            resultField.setText(resultField.getText() + "up ");
        }
        else if(direction[id] == 2){
            result += "d";
            resultField.setText(resultField.getText() + "down ");
        }
        else if(direction[id] == 3){
            result += "l";
            resultField.setText(resultField.getText() + "left ");
        }
        else{
            result += "r";
            resultField.setText(resultField.getText() + "right ");
        }
    }

    public boolean walk(int[] next, Solution solution, int direct){
        int id = cantor(next);
        if(checked[id]){
            return id == goalId;
        }
//        System.out.println("id；" + id);
        checked[id] = true;
        parent[id] = solution.getId();
        direction[id] = direct;
        Solution newSolution = new Solution(id, n);
        solutions.add(newSolution);
        return id == goalId;
    }


    public void heuristicSearch(){
        boolean find = false;
        while (solutions.size() != 0){
            Solution solution = getFittest();
            int[] sequence = solution.decantor();
            //solution.printRoute();
            int zeroIdx = -1;
            for(int i=0; i<sequence.length; i++){
                if(sequence[i] == 0){
                    zeroIdx = i;
                    break;
                }
            }
            if(zeroIdx == -1){
                System.out.println("Some error occur!");
                return;
            }

            int[] next = new int[sequence.length];
            if(zeroIdx > 2){
                //up
//                System.out.println("up");
                for(int i=0; i<sequence.length; i++){
                    next[i] = sequence[i];
                }
                int temp = next[zeroIdx];
                next[zeroIdx]= next[zeroIdx - 3];
                next[zeroIdx - 3] = temp;
                if(walk(next, solution, 1)) find = true;
            }
            if(zeroIdx < 6){
                //down
//                System.out.println("down");
                for(int i=0; i<sequence.length; i++){
                    next[i] = sequence[i];
                }
                int temp = next[zeroIdx];
                next[zeroIdx]= next[zeroIdx + 3];
                next[zeroIdx + 3] = temp;
                if(walk(next, solution, 2)) find = true;
            }
            if((zeroIdx+1) % 3 != 1){
                //left
//                System.out.println("left");
                for(int i=0; i<sequence.length; i++){
                    next[i] = sequence[i];
                }
                int temp = next[zeroIdx];
                next[zeroIdx]= next[zeroIdx - 1];
                next[zeroIdx - 1] = temp;
                if(walk(next, solution, 3)) find = true;
            }
            if((zeroIdx+1) % 3 != 0){
                //right
//                System.out.println("right");
                for(int i=0; i<sequence.length; i++){
                    next[i] = sequence[i];
                }
                int temp = next[zeroIdx];
                next[zeroIdx]= next[zeroIdx + 1];
                next[zeroIdx + 1] = temp;
                if(walk(next, solution, 4)) find = true;
            }
            if(find)
                break;
        }
        printResult(goalId);
//        resultField.setText(result);
        System.out.println("result:" + result);
    }

    private void swap(int i, int j){
        String temp = blocks[i].getText();
        blocks[i].setText(blocks[j].getText());
        blocks[j].setText(temp);
    }

    private void drawRoute(){
        for(int i = 0; i < result.length(); i++){
            String d = result.substring(i, i+1);
            if(d.contentEquals("u")){
                swap(zeroIdx, zeroIdx-3);
                zeroIdx -=3;
            }
            else if(d.contentEquals("d")){
                swap(zeroIdx, zeroIdx+3);
                zeroIdx += 3;
            }
            else if(d.contentEquals("l")){
                swap(zeroIdx, zeroIdx-1);
                zeroIdx -= 1;
            }
            else{
                swap(zeroIdx, zeroIdx+1);
                zeroIdx += 1;
            }
        }
    }

    public static void main(String[] args){
        System.out.println("Hello world!");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        EightPuzzle eightPuzzle = new EightPuzzle();
        eightPuzzle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
