package com.cisl.smt.web.Temp;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProblemAnsTemp {
    /**
     * @description: 做题页面中某题的作答
     * @author: Hopenx
     * @date: 2020.10.19
     */

    //TODO 把判断正误的逻辑写在后台，而非前端

    private Long idx;
    private Long finish;
    private String choice;
    private String choice_text;
    private String prob_text;
    private String analysis;
    private String ans;
    private String point;
    private String type;   // 新增了一个题型变量
    private int eval_res; // 本题做对与做错

    //以下为新增布局相关
    private int showOrder;
    private String layoutType;
    private String choice_type;
    private String stem_audio;
    private String stem_image;
    private String option_a_audio;
    private String option_b_audio;
    private String option_c_audio;
    private String option_d_audio;
    private String option_a_image;
    private String option_b_image;
    private String option_c_image;
    private String option_d_image;



    public void setChoice(String choice) {  // 设置选项的时候就判断正误
        this.choice = choice;
        if (!(this.type.equals("txt"))) {
            if (this.choice.equalsIgnoreCase(this.ans))
                this.setEval_res(1);
            else this.setEval_res(0);
        }
    }


    public static String purify(String raw) {
        //筛选出所有字母
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (Character.isLowerCase(c) || Character.isUpperCase(c))
                sb.append(c);
        }
        return sb.toString().toLowerCase();
    }

    public static ArrayList<String> extractMultiAns(String raw) {
        ArrayList<String> ansList = new ArrayList<>();
        raw = raw.replace('(', '（');
        raw = raw.replace(')', '）');
        String regex = "（(.*?)）";   //匹配中文括号内的内容
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(raw);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
            ansList.add(matcher.group(1));
        }
        return ansList;
    }

    public Boolean judgeTxt(String choice_text, String ans) {
        if (ans.contains("（")) {   //如果有多个答案
            ArrayList<String> ansList = new ArrayList<>();
            ansList.add(ans.substring(0, ans.indexOf("（")));
            ansList.addAll(extractMultiAns(ans));

            for (String txt : ansList) {
                if (purify(txt).equals(purify(choice_text))) {
                    return true;
                }
            }
        } else {   //如果只有一个答案
            return purify(ans).equals(purify(choice_text));
        }
        return false;
    }

    public void setChoice_text(String choice_text) {  // 设置选项的时候就判断正误
        this.choice_text = choice_text;
        System.out.println("答案:" + this.ans.trim());
        if (this.type.equals("txt")) {   //文本题的判断需要考虑：1.多个答案满足  2.多行匹配   3.标点符号
            String ans = this.ans.trim();
            ans = ans.replace("(", "（");
            ans = ans.replace(")", "）");

            if (ans.contains("\n")) {   //如果是问答题，分行
                String[] ansSplit = ans.split("\n");
                if (choice_text.contains("$")) {
                    String[] choiceSplit = choice_text.split("\\$");
                    System.out.println("116" + Arrays.toString(ansSplit));
                    System.out.println("117" + Arrays.toString(choiceSplit));
                    int corr = 0;
                    for (int i = 0; i < choiceSplit.length; i++) {
                        if(judgeTxt(choiceSplit[i], ansSplit[i]))
                            corr += 1;
                    }
                    if (corr == ansSplit.length)  //每一个分行都是对的
                        this.setEval_res(1);
                    else this.setEval_res(0);
                } else this.setEval_res(0);  //没有分行直接判错
            } else {
                if (judgeTxt(choice_text, ans))
                    this.setEval_res(1);
                else this.setEval_res(0);
            }


        }
    }

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public Long getFinish() {
        return finish;
    }

    public void setFinish(Long finish) {
        this.finish = finish;
    }

    public String getChoice() {
        return choice;
    }

    public String getChoice_text() {
        return choice_text;
    }

    public String getProb_text() {
        return prob_text;
    }

    public void setProb_text(String prob_text) {
        this.prob_text = prob_text;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEval_res() {
        return eval_res;
    }

    public void setEval_res(int eval_res) {
        this.eval_res = eval_res;
    }

    public int getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(int showOrder) {
        this.showOrder = showOrder;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public String getStem_audio() {
        return stem_audio;
    }

    public void setStem_audio(String stem_audio) {
        this.stem_audio = stem_audio;
    }

    public String getStem_image() {
        return stem_image;
    }

    public void setStem_image(String stem_image) {
        this.stem_image = stem_image;
    }

    public String getOption_a_audio() {
        return option_a_audio;
    }

    public void setOption_a_audio(String option_a_audio) {
        this.option_a_audio = option_a_audio;
    }

    public String getOption_b_audio() {
        return option_b_audio;
    }

    public void setOption_b_audio(String option_b_audio) {
        this.option_b_audio = option_b_audio;
    }

    public String getOption_c_audio() {
        return option_c_audio;
    }

    public void setOption_c_audio(String option_c_audio) {
        this.option_c_audio = option_c_audio;
    }

    public String getOption_d_audio() {
        return option_d_audio;
    }

    public void setOption_d_audio(String option_d_audio) {
        this.option_d_audio = option_d_audio;
    }

    public String getOption_a_image() {
        return option_a_image;
    }

    public void setOption_a_image(String option_a_image) {
        this.option_a_image = option_a_image;
    }

    public String getOption_b_image() {
        return option_b_image;
    }

    public void setOption_b_image(String option_b_image) {
        this.option_b_image = option_b_image;
    }

    public String getOption_c_image() {
        return option_c_image;
    }

    public void setOption_c_image(String option_c_image) {
        this.option_c_image = option_c_image;
    }

    public String getOption_d_image() {
        return option_d_image;
    }

    public void setOption_d_image(String option_d_image) {
        this.option_d_image = option_d_image;
    }

    public String getChoice_type() {
        return choice_type;
    }

    public void setChoice_type(String choice_type) {
        this.choice_type = choice_type;
    }

    @Override
    public String toString() {
        return "ProblemAnsTemp{" +
                "idx=" + idx +
                ", finish=" + finish +
                ", choice='" + choice + '\'' +
                ", choice_text='" + choice_text + '\'' +
                ", prob_text='" + prob_text + '\'' +
                ", analysis='" + analysis + '\'' +
                ", ans='" + ans + '\'' +
                ", point='" + point + '\'' +
                ", type='" + type + '\'' +
                ", eval_res=" + eval_res +
                ", showOrder=" + showOrder +
                ", layoutType='" + layoutType + '\'' +
                ", stem_audio='" + stem_audio + '\'' +
                ", stem_image='" + stem_image + '\'' +
                ", option_a_audio='" + option_a_audio + '\'' +
                ", option_b_audio='" + option_b_audio + '\'' +
                ", option_c_audio='" + option_c_audio + '\'' +
                ", option_d_audio='" + option_d_audio + '\'' +
                ", option_a_image='" + option_a_image + '\'' +
                ", option_b_image='" + option_b_image + '\'' +
                ", option_c_image='" + option_c_image + '\'' +
                ", option_d_image='" + option_d_image + '\'' +
                '}';
    }
}
