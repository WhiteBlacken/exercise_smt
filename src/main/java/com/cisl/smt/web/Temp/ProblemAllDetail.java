package com.cisl.smt.web.Temp;

import com.cisl.smt.po.Answer;
import com.cisl.smt.po.Options;
import com.cisl.smt.po.Point;
import com.cisl.smt.po.Problem;

/**
 * 编辑需要的所有problem信息
 * @Author qxy
 * @Date 2022/2/28 14:31
 * @Version 1.0
 */

public class ProblemAllDetail {

    private Problem problem;
    private Options options;
    private Answer answer;
    private Point point;

    public ProblemAllDetail() {
    }

    public ProblemAllDetail(Problem problem, Options options, Answer answer, Point point) {
        this.problem = problem;
        this.options = options;
        this.answer = answer;
        this.point = point;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
