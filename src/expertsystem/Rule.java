package expertsystem;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    List<Integer> conditions = new ArrayList<Integer>();
    int conclusion;
    String[] features = {"有毛", "产奶", "有羽毛", "会飞", "会下蛋", "吃肉", "有犬齿", "有爪", "眼睛盯前方", "有蹄",
            "反刍", "黄褐色", "有斑点", "有黑色条纹", "长脖", "长腿", "不会飞", "会游泳", "黑白两色", "善飞",
            "哺乳类", "鸟类", "肉食类", "蹄类", "企鹅", "海燕", "鸵鸟", "斑马", "长颈鹿", "虎", "金钱豹"};

    // 添加一个条件
    public void addCondition(int condition) {
        conditions.add(condition);
    }

    // 设置结论
    public void setConclusion(int conclusion) {
        this.conclusion = conclusion;
    }

    public int getConclusion() {
        return conclusion;
    }

    // 这条规则是否满足
    public boolean isSatisfied(Object[] conditions) {
        boolean satisfied = true;
        for (int i = 0; i < this.conditions.size(); i++) {
            int rule = this.conditions.get(i);
            boolean ok = false;
            for(int j = 0; j < conditions.length; j++) {
                if(Integer.parseInt(conditions[j].toString()) == rule){
                    ok = true;
                    break;
                }
            }
            if(!ok){
                // 有一个条件不满足都不满足
                satisfied = false;
                break;
            }
        }
        return satisfied;
    }

    public String toString() {
        String expr = "因为";
        for (int i = 0; i < conditions.size(); i++) {
            expr += features[conditions.get(i)];
            if (i != conditions.size() - 1) {
                expr += "、";
            }
        }
        expr += ", 所以是";
        expr += features[conclusion];
        expr += "。\n";
        return expr;
    }
}
