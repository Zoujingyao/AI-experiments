package eightpuzzle;

import java.io.Serializable;

class Solution implements Serializable {
    private int id;
    private int[] numbers;
    private int n;
    private int[] factorial= {1,1,2,6,24,120,720,5040,40320,362880,3628800};

    Solution(int id, int n){
        this.id = id;
        this.n = n;
        numbers = new int[n];
    }

    int getId() {
        return id;
    }

    /*
     * 康托展开式逆运算：求该解id对应的数字序列
     */
    public int[] decantor() {
        int cantor = this.id;
        boolean[] used = new boolean[n+1];
        int quotient, remainder;//商，余数
        int idx = 0, cnt = 0;
        for (int i = n - 1; i >= 0; i--) {
            quotient = cantor / factorial[i];
            remainder = cantor % factorial[i];
//            System.out.println("q, r:" + quotient + ", " + remainder);
            for (int j = 0; j < n ; j++) { //这不是1~n的排列，而是0~n-1的
                if (!used[j]) idx++;
                if (idx == quotient + 1) {
                    this.numbers[cnt++] = j;
//                    System.out.println("j:" + j);
                    idx = 0;
                    cantor = remainder;
                    used[j] = true;
                    break;
                }
            }
        }
        return this.numbers;
    }

    /*
     * 启发式函数：取目标状态与当前状态相同的节点个数
     * 另一种：当前状态每个结点到目标状态相应结点所需步数的总和
     * 这里使用第一种
     *
     * @param goal: 目标状态数组
     */
    public int getFitness(int[] goal){
        decantor();
        int fitness = 0;
        for(int i = 0; i < n; i++){
//            System.out.println("a, b:" + this.numbers[i] + ", " + goal[i]);
            if(this.numbers[i] == goal[i]) fitness++;
        }
        return fitness;
    }

    public void printRoute(){
        decantor();
        for(int i = 0; i < n;  i++){
            System.out.print(this.numbers[i] + " ");
        }
        System.out.println();
    }
}
