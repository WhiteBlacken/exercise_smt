package com.cisl.smt.service;

import com.cisl.smt.dao.ProblemRepository;
import com.cisl.smt.po.Problem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {
    /**
     * @description: 实现对题目的获取等操作
     * @author: Hopenx
     * @date: 2020-10-19 20:02
     */

    @Autowired
    private ProblemRepository problemRepository;

    @Override
    public Problem getProblemByProb_id(Long num) {
        return problemRepository.getProblemByProb_id(num);
    }

    @Override
    public List<Problem> getProblemByLevelAndLesson_id(String prob_level, Long lesson_id) {
        return problemRepository.getProblemByLevelAndLesson_id(prob_level, lesson_id);
    }

    @Override
    public List<Problem> getProblemByPoint_id(Long point_id) {
        return problemRepository.getProblemByPoint_id(point_id);
    }

    @Override
    public List<Problem> getAllProblem() {
        return problemRepository.getAllProblem();
    }

    @Override
    public Long getLastProb_id() {
        return problemRepository.getLastProb_id();
    }

    @Override
    public void updateProblemByPoint_id(Long prob_id, String prob_text, String prob_attr, String prob_diff, String prob_level, Long lesson_id, Long point_id, Long blank_num) {
        problemRepository.updateProblemByPoint_id(prob_id, prob_text, prob_attr, prob_diff, prob_level, lesson_id, point_id, blank_num);
    }

    @Override
    public void insertProblem(Long prob_id, Long grammar_id, Long point_id, String prob_attr, String prob_diff, String prob_level, String prob_text, Long options_id, Long answer_id, Long lesson_id, Long blank_num) {
        problemRepository.insertProblem(prob_id, grammar_id, point_id, prob_attr, prob_diff, prob_level, prob_text, options_id, answer_id, lesson_id, blank_num);
    }

    @Override
    public List<Problem> getByLevelAndLesson(String level, Long lesson_id, int num) {
        return problemRepository.getByLevelAndLesson(level,lesson_id,num);
    }

    /**
     * 根据level、lesson对挑战关卡出题
     * @param level
     * @param lesson_id
     * @param num
     * @return
     */
    @Override
    public List<Problem> getByLevelAndLessonInChallenge(String level, Long lesson_id, int num) {
        /**
         * 挑战关卡的要求
         * 1. 错、中、困难占比随机
         * 2. 包含前是哪个lesson的内容
         */
        List<Problem> probs = problemRepository.getByLevelAndLessonDomain(level,lesson_id-3,lesson_id-1,num);
        return probs;
    }
    /**
     * 根据level、lesson对boss关卡出题
     * @param level
     * @param lesson_id
     * @param num
     * @return
     */
    @Override
    public List<Problem> getByLevelAndLessonInBoss(String level, Long lesson_id, int num) {
        /**
         * boss关卡的要求
         * 1. 困难为主
         * 2. 包含前是哪个lesson的内容
         */
        int capacity = 1000;
        List<Problem> probs = problemRepository.getByLevelAndLessonDomain(level,lesson_id-4,lesson_id-1,capacity);
//       尝试改为java8，看在服务器上是否可以运行
//        List<Problem> hard_probs = probs.stream().filter(p->p.getProb_attr()=="Hard");
        List<Problem> hard_probs = new ArrayList<>();
        int cnt = 0;
        for(Problem p:probs){
            if("Hard".equals(p.getProb_attr())){
                cnt++;
                hard_probs.add(p);
            }
            if(cnt>=num){
                //如果困难数量足够，直接返回
                System.out.println("困难题目抽出"+cnt+"道;");
                return hard_probs;
            }
        }
        //困难数量不足继续补全
        for(Problem p:probs){
            if(!("Hard".equals(p.getProb_attr()))){
                cnt++;
                hard_probs.add(p);
            }
            if(cnt>=num){
                System.out.println("简单、中等题已经补齐");
                break;
            }
        }
        return hard_probs;
    }
    /**
     * 根据level、lesson对big boss关卡出题
     * @param level
     * @param lesson_id
     * @param num
     * @return
     */
    @Override
    public List<Problem> getByLevelAndLessonInBigBoss(String level, Long lesson_id, int num) {
        /**
         * 大boss关卡的要求
         * 1. 困难为主
         * 2. 包含前是哪个lesson的内容 (除了lesson涵盖的范围，其他未发现与boss关卡的不同)
         */
        int capacity = 1000;
        List<Problem> probs = new ArrayList<>();
        if(lesson_id==25){
            probs = problemRepository.getByLevelAndLessonDomain(level,1L,24L,capacity);
        }else if(lesson_id==42){
            probs = problemRepository.getByLevelAndLessonDomain(level,26L,41L,capacity);
        }

//       尝试改为java8，看在服务器上是否可以运行
//        List<Problem> hard_probs = probs.stream().filter(p->p.getProb_attr()=="Hard");
        List<Problem> hard_probs = new ArrayList<>();
        int cnt = 0;
        for(Problem p:probs){
            if("Hard".equals(p.getProb_attr())){
                cnt++;
                hard_probs.add(p);
            }
            if(cnt>=num){
                //如果困难数量足够，直接返回
                System.out.println("困难题目抽出"+cnt+"道;");
                return hard_probs;
            }
        }
        //困难数量不足继续补全
        for(Problem p:probs){
            if(!("Hard".equals(p.getProb_attr()))){
                cnt++;
                hard_probs.add(p);
            }
            if(cnt>=num){
                System.out.println("简单、中等题已经补齐");
                break;
            }
        }
        return hard_probs;
    }

    /**
     * 根据level、lesson在普通模式下进行出题
     * @param level
     * @param lesson_id
     * @param num
     * @return
     */
    @Override
    public List<Problem> getByLevelAndLessonInNormal(String level, Long lesson_id, int num) {
        /**
         * 普通模式下的出题要求
         * 1. Easy难度占到4-6题
         * 2. Medium难度占到剩余的70%
         * 3. Hard难度占到剩余的30%
         */
        int capacity = 1000;
        List<Problem> probs = problemRepository.getByLevelAndLesson(level,lesson_id,capacity);
        List<Problem> exer_probs = new ArrayList<>();
        int cnt = 0;
        //选取easy题目
        for(Problem p:probs){
            if("Easy".equals(p.getProb_attr())){
                cnt++;
                exer_probs.add(p);
            }
            if(cnt>=6)break;
        }
        //选取中等难度题目
        int medium_num = (int)((num-cnt)*0.7);
        for(Problem p:probs){
            if("Medium".equals(p.getProb_attr())){
                cnt++;
                medium_num--;
                exer_probs.add(p);
            }
            if(medium_num<=0)break;
        }
        //选取困难难度题目
        for(Problem p:probs){
            if("Hard".equals(p.getProb_attr())){
                cnt++;
                exer_probs.add(p);
            }
            if(cnt>=num)break;
        }
        return exer_probs;
    }


}
