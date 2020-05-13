package eightpuzzle;

import java.util.*;

public class EightPuzzle {
    private static List<Solution> solutions = new ArrayList<Solution>();
    //0~10的阶乘值
    private static int[] factorial={1,1,2,6,24,120,720,5040,40320,362880,3628800};
    private static int n = 9;
    //初始状态序列
    private static int[] origin;
    //目标状态序列
    private static int[] goal;
    private static int startId, goalId;
    private static int max = 400000;
    private static int[] parent = new int[max]; //每个结点的前结点
    private static int[] direction = new int[max]; //到这个结点的方向
    private static boolean[] checked = new boolean[max];
    private static String result = "";


    /*
     * 康托展开式，实现全排列到自然数的映射
     */
    private static int cantor(int[] numbers){
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
    private static boolean isSovable(){
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
//        System.out.println("reverseValue:" + originReverseValue + ", " + goalReverseValue);
        if(originReverseValue % 2 == goalReverseValue % 2){
            return true;
        }
        else
            return false;
    }

    public static void IO(){
        origin = new int[n];
        goal = new int[n];
        Scanner in = new Scanner(System.in);
        System.out.println("请输入初始状态（空格用0表示）：");
        for(int i = 0; i < n; i++){
            origin[i] = in.nextInt();
        }
        System.out.println("请输入终止状态（空格用0表示）：");
        for(int i = 0; i < n; i++){
            goal[i] = in.nextInt();
        }
        startId = cantor(origin);
        goalId = cantor(goal);
        System.out.println("startId, goalId:" + startId + ", " + goalId);
    }

    /*
     * 按照解的启发式函数值从大到小排序，返回最好的解
     */
    public static Solution getFittest() {
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

    public static void printResult(int id){
        if(id == startId) {
            return;
        }
        printResult(parent[id]);
        if(direction[id] == 1) {
            result += "u";
            System.out.println("up");
        }
        else if(direction[id] == 2){
            result += "d";
            System.out.println("down");
        }
        else if(direction[id] == 3){
            result += "l";
            System.out.println("left");
        }
        else{
            result += "r";
            System.out.println("right");
        }
    }

    public static boolean walk(int[] next, Solution solution, int direct){
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


    public static void heuristicSearch(){
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
        System.out.println("result:" + result);
    }

    public static void main(String[] args){
        System.out.println("Hello world!");

        // 八数码问题
        IO();

        if(!isSovable()){
            System.out.println("这两个状态之间不可达。");
        }
        else{
            Solution start = new Solution(startId, n);
            solutions.add(start);

            heuristicSearch();
        }
    }
}
