package com.cisl.smt.web;

import com.cisl.smt.dao.ProblemRepository;
import com.cisl.smt.po.Problem;
import com.cisl.smt.service.ProblemService;
import com.cisl.smt.web.Temp.ProblemAnsTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
public class Util {
    /**
     * @description: 项目中的一些工具函数
     * @author: Hopenx
     * @date: 2021/1/6 7:41 上午
     */

    @Autowired
    private ProblemService problemService;

    public ArrayList<Long> stringToList(String str) {
        //TODO: 要学会 lambda 表达式
        ArrayList<Long> resList = new ArrayList<>();
        if(str.equals("[]"))
            return resList;
        str = str.substring(1, str.length() - 1);
        if (!str.contains(",")) {
            resList.add(Long.valueOf(str));
        } else {
            String[] strSplit = str.split(",");
            for (String item : strSplit)
                resList.add(Long.valueOf(item));
        }
        return resList;
    }

    public int countScore(ArrayList<ProblemAnsTemp> probList) {
        int sum = 0;
        for (ProblemAnsTemp pt : probList) sum += pt.getEval_res();
        return sum * 100 / probList.size();
    }

    public int countHandleRate(ArrayList<ProblemAnsTemp> probList) {
        /**
         * @description: 计算该次测试的考点掌握程度，共 N 个考点，掌握 M 个，掌握程度 M/N
         * 掌握的定义：该考点下做对比例超过一个 threshold, 暂定 70%
         */
        int threshold = 70;
        int totalPointNum = 0;
        int handlePointNum = 0;
        HashMap<String, ArrayList<ProblemAnsTemp>> pointProbMap = new HashMap<>();
        for (ProblemAnsTemp pt : probList) {
            String pointId = pt.getPoint();
            if (!pointProbMap.containsKey(pointId))
                pointProbMap.put(pointId, new ArrayList<ProblemAnsTemp>());
            pointProbMap.get(pointId).add(pt);
        }
        for (String pointId : pointProbMap.keySet()) {
            totalPointNum += 1;
//            int rightNum = (int) pointProbMap.get(pointId).stream().filter(pt -> pt.getEval_res() == 1).count();
            int rightNum = 0;
            for(ProblemAnsTemp pat : pointProbMap.get(pointId)) {
                if(pat.getEval_res() == 1)
                    rightNum++;
            }
            if (rightNum / pointProbMap.get(pointId).size() *100 > threshold)
                handlePointNum += 1;
        }
        return handlePointNum *100 / totalPointNum;
    }

    public String getException(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        ex.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception e) {
        }
        return ret;
    }

    public List<Problem> customizeProbList(List<Problem> probList, Integer choiceNum, Integer txtNum) {
        /**
         * @description: 根据参数，定制题目列表
         *      随机打乱一套题目的顺序，保证选择题排在文本题前面，给定题目类型和难度参数
         * @return: 题目序号列表
         */
        List<Problem> choiceProblemList = new ArrayList<>();
        List<Problem> txtProblemList = new ArrayList<>();
        for (Problem pro : probList) {
            if (pro.getProb_attr().equals("Choice"))
                choiceProblemList.add(pro);
            else txtProblemList.add(pro);
        }

        List<Problem> resList = new ArrayList<>();
        try {
            Collections.shuffle(choiceProblemList);
            resList = choiceProblemList.subList(0, choiceNum);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Util 108: 当前 level 的选择题数量不够，题库有问题，返回一个空列表");
            return resList;
        }
        try {
            Collections.shuffle(txtProblemList);
            resList.addAll(txtProblemList.subList(0, txtNum));
        } catch (IndexOutOfBoundsException e) {
            //当前 level 的文本题数量不够，选择题凑
            resList.addAll(choiceProblemList.subList(choiceNum, choiceNum + txtNum - txtProblemList.size()));
            resList.addAll(txtProblemList);
        }

        return resList;
    }

    public static String getMd5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

}
