package expertsystem;

import java.util.*;

public class RuleRepository {
    List<Rule> rules = new ArrayList<>();
    Set<Integer> conditions = new TreeSet<>();

    // 构造函数，传入用户输入的条件编号
    public RuleRepository(int[] conditions){
        for(int i = 0; i < conditions.length; i++) {
            this.conditions.add(conditions[i]);
        }
    }

    // 初始化规则库，初始有15条规则，总共能识别7种动物
    private void init(){
        // 15条规则
        int[][][] expr = new int[][][]{
                {{0}, {20}},
                {{1}, {20}},
                {{2}, {21}},
                {{3, 4}, {21}},
                {{20, 5}, {22}},
                {{6, 7, 8}, {22}},
                {{20, 8}, {23}},
                {{20, 9}, {23}},
                {{22, 11, 12}, {30}},
                {{22, 11, 13}, {29}},
                {{23, 14, 15, 12}, {28}},
                {{23, 13}, {27}},
                {{21, 14, 15, 16}, {26}},
                {{21, 19}, {25}},
                {{21, 17, 18, 16}, {24}}};
        for(int i = 0; i < expr.length; i++){
            Rule rule = new Rule();
            for(int j = 0; j < expr[i][0].length; j++){
                rule.addCondition(expr[i][0][j]);
            }
            rule.setConclusion(expr[i][1][0]);
            rules.add(rule);
        }
    }

    // 正向推理
    public String reasoning(){
        String result = "";
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            if(rule.isSatisfied(conditions.toArray())){
                result += rule.toString();
                // 注意：满足的话要把结论加到已知条件中，且要维持有序（条件序列满足拓扑结构）
                conditions.add(rule.getConclusion());
            }
        }
        return result;
    }
}
