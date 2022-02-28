package com.cisl.smt.web;

import com.cisl.smt.po.*;
import com.cisl.smt.service.*;
import com.cisl.smt.web.Temp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ExerciseController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private OptionsService optionsService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseEvalService exerciseEvalService;

    @Autowired
    private ProblemEvalService problemEvalService;

    @Autowired
    private ReviewCollecService reviewCollecService;

    private final Util util = new Util();  //工具库变量
    private long USER_ID = 3;   // 暂时的模拟变量
    private CommentTemp commentTemp = new CommentTemp();  // 内部的试卷评估变量

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @GetMapping("/start")
    public ModelAndView start() {
        ModelAndView mav;
        mav = new ModelAndView("start");
        return mav;
    }

    @GetMapping("/analysis")
    public ModelAndView analysis() {
        ModelAndView mav;
        mav = new ModelAndView("analysis");
        return mav;
    }

    @GetMapping("/demo")
    public ModelAndView demo(@RequestParam("src") String src) {
        SettingTemp settingTemp = SettingTemp.getInstance();
        settingTemp.setSrc(src);
        ModelAndView mav;
        mav = new ModelAndView("demo");
        return mav;
    }

    @GetMapping("/demo_marked")
    public ModelAndView demo_marked() {
        ModelAndView mav;
        mav = new ModelAndView("demo_marked");
        return mav;
    }

    @GetMapping("/weekly")
    public ModelAndView weekly() {
        ModelAndView mav;
        mav = new ModelAndView("weekly");
        return mav;
    }

    @GetMapping("/term_mid")
    public ModelAndView term_mid() {
        ModelAndView mav;
        mav = new ModelAndView("term_mid");
        return mav;
    }

    @GetMapping("/term_end")
    public ModelAndView term_end() {
        ModelAndView mav;
        mav = new ModelAndView("term_end");
        return mav;
    }

    @GetMapping(path = "/getUserFromCookies")
    public Long getUserFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String res = "";
        if (cookies != null) {
            for (Cookie cookie : cookies)
                if (cookie.getName().equals("user_id"))
                    res = cookie.getValue();
        }
        return Long.valueOf(res);
    }

    @GetMapping(path = "/getProblem")
    public Problem getProblem(@RequestParam() Long num) {
        return problemService.getProblemByProb_id(num);
    }

    @GetMapping(path = "/getOptions")
    public Options getOptions(@RequestParam() Long id) {
        return optionsService.getOptions(id);
    }

    @GetMapping(path = "/getAnswer")
    public Answer getAnswer(@RequestParam() Long id) {
        return answerService.getAnswer(id);
    }

    @GetMapping(path = "/getMap")
    public SheetMap getMap() {
        return SheetMap.getInstance();
    }

    @GetMapping(path = "/getSheet")
    public SheetTemp getSheet(HttpServletRequest request) {
        Long userId = getUserFromCookies(request);
        HashMap<Long, SheetTemp> map = SheetMap.getInstance().getTempHashMap();
        SheetTemp st = map.get(userId);
        if (st == null) {  //键不存在
            map.put(userId, new SheetTemp());   //一个 user_id 有自己的 sheetTemp
        }
        assert map.get(userId) != null;
        return map.get(userId);
    }

    @PostMapping("/postSheet")
    public SheetTemp postSheet(@RequestParam("idx") Long idx,
                               @RequestParam("finish") Long finish,
                               @RequestParam("choice") String choice,
                               @RequestParam("choice_text") String choice_text,
                               HttpServletRequest request) {

        SheetTemp st = getSheet(request);   //获得对应的 SheetTemp
        ArrayList<ProblemAnsTemp> tmpList = st.getSheet_list();
        for (ProblemAnsTemp pt : tmpList) {
            if (pt.getIdx().equals(idx)) {
                System.out.println(idx.toString() + "choice_text: " + choice_text);
                System.out.println("choice:" + choice);
                System.out.println("type:" + pt.getType());
                pt.setChoice(choice);
                pt.setFinish(finish);
                pt.setChoice_text(choice_text);
            }
        }
        st.setSheet_list(tmpList);

        return st;
    }

    @GetMapping(path = "/getJudgeDetail")
    public SheetTemp getJudgeDetail(HttpServletRequest request) {
        /**
         * @description: 获得数据库中某个 SheetTemp 记录, 属于评估后的记录
         */
        SheetTemp st = getSheet(request);   //获得对应的 SheetTemp
        if (st.isEvaluated()) return st;    //已经评分过，不重复插入

        Long USER_ID = getUserFromCookies(request);  // 获得当前用户
        //TODO 进行分数、掌握知识点等等的评价，写入 CommentTemp 和 t_exer_eval 数据库

        //最新的 SheetTemp 存入 t_exer_eval
        // exercise 的 ID 自增, exer_eval_id 为自增。主键 id 都不要去人为设置
        // t_exer_eval 中保存了 user_id 和 exercise_id
        ExerciseEvaluation ee = new ExerciseEvaluation();

        int evalScore = util.countScore(st.getSheet_list());
        ee.setExer_eval_score((long) evalScore);
        commentTemp.setScore(evalScore);
        commentTemp.setHandle_rate(util.countHandleRate(st.getSheet_list()));

        String curTime = sdf.format(new Date());
        ee.setExer_eval_time(curTime);
        commentTemp.setTime(curTime);

        long inter = 0;
        try {
            inter = sdf.parse(curTime).getTime() - sdf.parse(st.getStart_time()).getTime();
            inter /= 1000;  //秒数
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ee.setConsume_time(inter);  //TODO √ 做题用时

        ee.setUser_id(USER_ID);
        ee.setExer_level(st.getExer_level());  //TODO √ 试卷 level
        exerciseEvalService.insertExerciseEval(ee);

        //每一题都存一个 prob_eval
        for (ProblemAnsTemp pt : st.getSheet_list()) {

            if (pt.getFinish() == 0)   // 只记录做了的题，未做的不计入 eval 里面
                continue;
            ProblemEvaluation pe = new ProblemEvaluation();
            //TODO prob_eval_id 设置：当前系统毫秒数
            pe.setProb_eval_id(System.currentTimeMillis());
            pe.setProb_id(pt.getIdx());
            pe.setUser_id(USER_ID);
            pe.setChoice(pt.getChoice());
            pe.setChoice_text(pt.getChoice_text());
            pe.setProb_text(pt.getProb_text());
            pe.setAnalysis(pt.getAnalysis());
            pe.setAns(pt.getAns());
            pe.setPoint(pt.getPoint());
            pe.setType(pt.getType());
            pe.setProb_eval_res(pt.getEval_res());
            pe.setProb_eval_time(sdf.format(new Date()));
            pe.setConsume_time((long) 20);

            try {
                if (problemEvalService.getProblemEvalById(pe.getProb_eval_id()) == null) {
                    problemEvalService.insertProblemEval(pe);
                    System.out.println("已插入");
                } else {
                    System.out.println(problemEvalService.getProblemEvalById(pe.getProb_eval_id()));
                    System.out.println("已存在");
                }
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                System.out.println(problemEvalService.getProblemEvalById(pe.getProb_eval_id()));
                System.out.println("重复插入,忽略");
            }


            if (pe.getProb_eval_res() == 0) {   // 如果是错题
                String probEvalListStr, probListStr;
                ArrayList<Long> probList = new ArrayList<>();
                ArrayList<Long> probEvalList = new ArrayList<>();
                try {
                    probListStr = reviewCollecService.getCollec(USER_ID).getProb_list();
                    probEvalListStr = reviewCollecService.getCollec(USER_ID).getProb_eval_list();

                    probList = util.stringToList(probListStr);
                    probEvalList = util.stringToList(probEvalListStr);
                } catch (Exception e) {
                    //如果暂时没有此用户的记录，就 new 一个空的
                    try {
                        reviewCollecService.insertCollec(USER_ID, "[]", "[]");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    System.out.println(util.getException(e));
                    //return st;
                }

//                if (probList.size() == 0 || !probList.contains(pt.getIdx())) {
                //TODO √ 重复的题目也加入
                probEvalList.add(pe.getProb_eval_id());   //核心步骤：加入 eval_id
                probList.add(pt.getIdx());   //核心步骤：加入 prob_id
                probEvalListStr = probEvalList.toString();
                probListStr = probList.toString();
                if (probEvalListStr.contains(" "))
                    probEvalListStr = probEvalListStr.replaceAll(" ", "");
                if (probListStr.contains(" "))
                    probListStr = probListStr.replaceAll(" ", "");
                reviewCollecService.updateCollec(USER_ID, probEvalListStr, probListStr);   //更新该用户的错题集

            }
        }

        //TODO 对题存入 t_pass_collec
        st.setEvaluated(true);  //已经评分过，不再重复插入数据库
        return st;
    }


    @GetMapping("/getOneRandom")
    public Long getOneRandom() {
        //没有完全连续
        int max = 10300, min = 10000;
        int res = new Random().nextInt(max - min) + min;
        return (long) res;
    }

    public ArrayList<Long> getFromNaive(Integer totalNum) {
        /**
         * @description: 随机出 totalNum 道题，从数据库的开头开始，为了暂时模拟数据
         * @return: 题目序号列表
         */
        ArrayList<Long> probList = new ArrayList<>();
        if (totalNum == 25) {
            for (int i = 10001; i <= 10017; i++) {
                probList.add((long) i);
            }
            Collections.shuffle(probList);   //打乱顺序
            for (int i = 10259; i <= 10266; i++) {
                probList.add((long) i);
            }
        } else if (totalNum == 20) {
            for (int i = 10001; i <= 10010; i++) {
                probList.add((long) i);
            }
            Collections.shuffle(probList);   //打乱顺序
            for (int i = 10279; i <= 10288; i++) {
                probList.add((long) i);
            }
        } else {
            for (int i = 10001; i <= 10012; i++) {
                probList.add((long) i);
            }
            Collections.shuffle(probList);   //打乱顺序
            for (int i = 10259; i <= 10261; i++) {
                probList.add((long) i);
            }
        }

        return probList;
    }

    public ArrayList<Long> getFromLesson(Long lesson_id, Integer totalNum) {
        /**
         * @description: 从对应的 lesson 中抽取题目，默认文本与选择各占一半
         * @return: 题目序号列表
         */
        ArrayList<Long> probNumList = new ArrayList<>();
        SettingTemp settingTemp = SettingTemp.getInstance();
        List<Problem> probList = problemService.getProblemByLevelAndLesson_id(settingTemp.getLev().toString(), lesson_id);

        //从数据库中取出给定 lesson 的题，交给 customize 定制

        probList = util.customizeProbList(probList, totalNum / 2, totalNum / 2);

        for (Problem pro : probList)
            probNumList.add(pro.getProb_id());

        return probNumList;
    }

    public ArrayList<Long> getFromLessonEasyAndMedium(Long lesson_id, Integer totalNum) {
        /**
         * @description: 从对应的 lesson 中抽取题目[要求是 Easy 12 道(9 选择+3 文本)，和 Mid 3 道]
         * @return: 题目序号列表
         */
        ArrayList<Long> probNumList = new ArrayList<>();
        SettingTemp settingTemp = SettingTemp.getInstance();
        //通过难度和lesson_id取Problem
        System.out.println("level:" + settingTemp.getLev().toString() + ";lesson_id:" + lesson_id);
        List<Problem> tempList = problemService.getProblemByLevelAndLesson_id(settingTemp.getLev().toString(), lesson_id);
//        List<Problem> tempList = problemService.getProblemByLevelAndLesson_id("2", lesson_id);

        List<Problem> easyChoiceProbList = new ArrayList<>(),
                easyTextProbList = new ArrayList<>(),
                mediumProbList = new ArrayList<>(),
                probList = new ArrayList<>();

        System.out.println("");
        for (Problem p : tempList) {
            if (p.getProb_diff().equals("Easy") && p.getProb_type().equals("opt")) {
                easyChoiceProbList.add(p);
            } else if (p.getProb_diff().equals("Easy") && p.getProb_type().equals("txt")) {
                easyTextProbList.add(p);
            } else if (p.getProb_diff().equals("Medium")) {
                mediumProbList.add(p);
            }
        }

        Collections.shuffle(easyChoiceProbList);
        int gap = 9 - easyChoiceProbList.size();
        if (gap < 0) {
            probList.addAll(easyChoiceProbList.subList(0, 9));
        } else {
            probList.addAll(easyChoiceProbList);
        }

        Collections.shuffle(easyTextProbList);
        gap = 3 - easyTextProbList.size();
        if (gap < 0) {
            probList.addAll(easyTextProbList.subList(0, 3));
        } else {
            probList.addAll(easyTextProbList);

        }

        Collections.shuffle(mediumProbList);
        gap = 3 - mediumProbList.size();
        if (gap < 0) {
            probList.addAll(mediumProbList.subList(0, 3));
        } else {
            probList.addAll(mediumProbList);
        }

        for (Problem pro : probList)
            probNumList.add(pro.getProb_id());

        return probNumList;
    }

    @GetMapping("/getFromForgetCurve")
    public ArrayList<Long> getFromForgetCurve(@RequestParam("num") Integer partNum, Long USER_ID) {
        /**
         * @description: 按照做过(不论对错)的题目遗忘曲线方式出题，占比 2道/20
         * @return: 题目序号列表
         */
        //TODO 以两周为分界线，目前是随机
        ArrayList<Long> probList = new ArrayList<>();
        ArrayList<ProblemEvaluation> probEvalList = new ArrayList<>();
        probEvalList = problemEvalService.getProblemEvalByUser(USER_ID);
        Collections.shuffle(probEvalList);
        if (probEvalList.size() >= partNum) {  //修复 bug 遗忘曲线题目量不足
            for (int i = 0; i < partNum; i++) {
                Long prob_id = probEvalList.get(i).getProb_id();
                Problem problem = problemService.getProblemByProb_id(prob_id);
                if (problem != null) {
                    probList.add(prob_id);
                }
            }
        }
//        else if (probEvalList.size() > 0) {
//            int margin = partNum - probEvalList.size();
//            for (ProblemEvaluation pe : probEvalList)
//                probList.add(pe.getProb_id());
//
//        }

        return probList;
    }

    @GetMapping("/getFromSimilar")
    public ArrayList<Long> getFromSimilar(@RequestParam("num") Integer partNum, Long USER_ID) {
        /**
         * @description: 根据该 level 内做错的相似题目出题，占比 5道/20，没有信息则随机
         *               和 recommend 区别在于，只取本 level
         * @return: 题目序号列表
         */
        String probEvalListStr = "[]";
        try {
            probEvalListStr = reviewCollecService.getCollec(USER_ID).getProb_eval_list();
        } catch (NullPointerException ne) {
            reviewCollecService.insertCollec(USER_ID, "[]", "[]");
        }
        ArrayList<Long> probEvalList = util.stringToList(probEvalListStr); //所有错题
        ArrayList<Long> wrongList = new ArrayList<>();
        ArrayList<Long> probList = new ArrayList<>();
        Integer curLevel = SettingTemp.getInstance().getLev();
        for (Long num : probEvalList) {
            Long probId = 0L;
            try {
                probId = problemEvalService.getProblemEvalById(num).getProb_id();
            } catch (NullPointerException e) {

            }
            //只选取level 等于 当前level的题
            //qxy

            Problem problem = problemService.getProblemByProb_id(probId);
            if (problem != null) {
                if (Integer.valueOf(problemService.getProblemByProb_id(probId).getProb_level()).equals(curLevel)) {
                    wrongList.add(probId);
                }
            }
        }

        Collections.shuffle(wrongList);

        try {
            wrongList = new ArrayList<>(wrongList.subList(0, partNum)); //wrongList 表示在当前 level 的错题,选取了 5 道
        } catch (IndexOutOfBoundsException e) {
            //数量不够, 不切割,
            ;
        }
        int curSum = 0;
        for (Long wrongId : wrongList) {
            Long pointId = problemService.getProblemByProb_id(wrongId).getProb_id();  //找到对应的知识点
            List<Problem> pointProblemList = problemService.getProblemByPoint_id(pointId); //找到同知识点的所有题
//            List<Long> tmpList = pointProblemList.stream().map(Problem::getProb_id).collect(Collectors.toList());

            List<Long> tmpList = new ArrayList<>();
            for (Problem problem : pointProblemList) {
                tmpList.add(problem.getProb_id());
            }
            if (curSum + tmpList.size() >= partNum) {
                //数量已足够
                probList.addAll(tmpList.subList(0, partNum - curSum));
                break;
            } else
                probList.addAll(tmpList);
            curSum += tmpList.size();
        }

        while (curSum < partNum) {
            //题目仍然不够
            probList.add(getOneRandom());
            curSum++;
        }


        return probList;
    }

    @GetMapping("/getFromNew")
    public ArrayList<Long> getFromNew(@RequestParam("lesson") Long lesson_id, @RequestParam("num") Integer partNum) {
        /**
         * @description: 根据用户当前的新知识点出题，即课课练中的当堂知识点出题，10道/20
         * @return: 题目序号列表
         */
        ArrayList<Long> probList = getFromLesson(lesson_id, partNum);

        return probList;
    }

    @GetMapping("getFromMedium")
    public ArrayList<Long> getFromMedium(@RequestParam("num") Integer partNum) {
        /**
         * @description: 公测的初期阶段，出一些中等题
         * @return: 题目序号列表
         */
        List<Long> probList = new ArrayList<>();
        List<Problem> allProbList = problemService.getAllProblem();
        for (Problem problem : allProbList) {
            if (problem.getProb_diff().equals("Medium")) {
                probList.add(problem.getProb_id());
            }
        }
        Collections.shuffle(probList);
        probList = probList.subList(0, partNum);
        return new ArrayList<>(probList);
    }

    @GetMapping("/getHighFrequency")
    public ArrayList<Long> getHighFrequency(@RequestParam("num") Integer partNum) {
        /**
         * @description: 按照高频知识点出题，如固定搭配，占比 3道/20
         * @return: 题目序号列表
         */
        ArrayList<Long> probList = new ArrayList<>();
        // 数据库中 0 表示固定搭配，-1 表示未知考点
        ArrayList<Problem> problemList = new ArrayList<>(problemService.getProblemByPoint_id((long) 0));
        for (Problem problem : problemList)
            probList.add(problem.getProb_id());

        Collections.shuffle(probList);
        if (probList.size() >= partNum)
            probList = new ArrayList<>(probList.subList(0, partNum));
        else {
            while (probList.size() < partNum) {
                Long newId = getOneRandom();
                if (!probList.contains(newId) && !problemService.getProblemByProb_id(newId).getProb_text().contains("题目已被删除"))
                    probList.add(newId);
            }
        }

        return probList;
    }

    @GetMapping("/getFromRecommend")
    public ArrayList<Long> getFromRecommend(@RequestParam("num") Integer partNum, Long USER_ID) {
        /**
         * @description: 「推荐练习」模块，根据所有做错的题目，推荐相似的题目
         *                目前按照同一考点来推
         * @return: 题目序号列表
         */
        ArrayList<ProblemEvaluation> wrongList = getWrong(USER_ID);
        ArrayList<Long> pointList = new ArrayList<>();
        ArrayList<Long> outputProblemList = new ArrayList<>();
        for (int i = 0; i < wrongList.size(); i++) {
            //收集所有错题的考点
            ProblemEvaluation pe = wrongList.get(i);
            System.out.println("473");
            System.out.println(pe);
            try {
                Long point_id = problemService.getProblemByProb_id(pe.getProb_id()).getPoint_id();
                if (!pointList.contains(point_id))
                    pointList.add(point_id);
            } catch (NullPointerException e) {
                e.printStackTrace();
                i++;
            }
        }
        Collections.shuffle(pointList);
        System.out.println("考点列表" + pointList.toString());
        for (Long pointId : pointList) {
            List<Problem> allProb = problemService.getProblemByPoint_id(pointId);
            //聚集该考点的所有题目-题号
//            outputProblemList.addAll(allProb.stream().map(Problem::getProb_id).collect(Collectors.toList()));
            for (Problem problem : allProb) {
                outputProblemList.add(problem.getProb_id());
            }
        }
        Collections.shuffle(outputProblemList);
        System.out.println("Line 479: 考点下题目" + outputProblemList.toString());
        outputProblemList = uniqueProbList(outputProblemList);
        Collections.shuffle(outputProblemList);  //一定要打乱，因为 unique 之后会正序
        HashSet<Long> hashSet = new HashSet<>(outputProblemList);
        assert outputProblemList.size() == hashSet.size();  //保证列表去重

        if (outputProblemList.size() > partNum)   //推荐的题目太多则裁剪
            outputProblemList = new ArrayList<>(outputProblemList.subList(0, partNum));
        else {   //题目太少则补充随机
            int margin = partNum - outputProblemList.size();
            if (margin > 0) System.out.println("推荐刷题进入去重，补齐" + margin);
            int loop = 30;
            while (margin > 0 && loop > 0) {
                long newId = getOneRandom();
                loop--;
                if (!outputProblemList.contains(newId) && !problemService.getProblemByProb_id(newId).getProb_text().contains("题目已被删除")) {
                    outputProblemList.add(newId);
                    margin--;
                }
            }
        }
        outputProblemList = reorganizeProbList(outputProblemList);  //保证选择题在文本题前面的顺序

        return outputProblemList;
    }

    @GetMapping("/getFromStrengthen")
    public ArrayList<Long> getFromStrengthen(@RequestParam("num") Integer partNum, HttpServletRequest request) {
        /**
         * @description: 强化练习部分，本次错题+推荐练习合二为一；总题量 20 道
         * @return: 题目序号列表
         */

        ArrayList<Long> outputProblemList = new ArrayList<>();
        SheetTemp st = getSheet(request);
        for (ProblemAnsTemp pat : st.getSheet_list()) {
            if (pat.getFinish() == 1 && pat.getEval_res() == 0)  //已做且做错的题目
                outputProblemList.add(pat.getIdx());
        }
        System.out.println("505");
        System.out.println(outputProblemList);
        Long USER_ID = getUserFromCookies(request);
        ArrayList<Long> rec = getFromRecommend(partNum - outputProblemList.size(), USER_ID);
        System.out.println("509");
        System.out.println(rec);
        outputProblemList.addAll(rec);  //不能保证无重复
        outputProblemList = reorganizeProbList(outputProblemList);
        return outputProblemList;
    }

    public boolean checkDiff(ArrayList<Long> tmpList) {
        int easyNum = 0, midNum = 0;
        for (Long prob_id : tmpList) {
            if (problemService.getProblemByProb_id(prob_id).getProb_attr().equals("Easy")) {
                easyNum++;
            }
            if (problemService.getProblemByProb_id(prob_id).getProb_attr().equals("Medium")) {
                midNum++;
            }
        }
        assert easyNum == 16 && midNum == 4;
        return true;
    }


    @GetMapping(path = "/initPaper")
    public SheetTemp initPaper(HttpServletRequest request) {
        /**
         * @description: 根据 SettingTemp组卷出题，返回题目数组，并且初始化 sheetTemp
         * @return: 返回初始的 sheetTemp
         */
        //TODO 也应该接受一个 id
        //TODO 实现各种出题逻辑,包括设置选择题和文本题各自的题量
        //setting 中 src: lesson/wrong/recommend
        //题量-20，其中新知识点-10，基于相似-5，高频练习-3，基于遗忘曲线-2（新知识点10题数量不可改变）
        // 2021.11.03:厦门海沧新知识点-15（12+3），基于相似-3，基于遗忘曲线-2

        Long USER_ID = getUserFromCookies(request);
        SettingTemp settingTemp = SettingTemp.getInstance();
        ArrayList<Long> probList = new ArrayList<>();
        Integer totalNum = settingTemp.getProbNum();  //lesson 默认 totalNum = 20
        Integer level = settingTemp.getLev();

        if (settingTemp.getSrc().equals("ai")) {
            //进入 AI 刷题温故知新, 依次接收四个来源. 2.0 版本已废弃
        } else if (settingTemp.getSrc().equals("lesson")) {
            //进入课课练做题，V2.0版本中，按照 4 个来源组合抽取, 此处 sys 代表lesson_id

            //1. 一个要是根据要求拿到题目
            //2. 保证不重复
            //3. 符合一定的顺序
            Long lesson_id = Long.valueOf(settingTemp.getSys());
            int num = 15;


            List<Problem> probs = problemService.getByLevelAndLesson(settingTemp.getLev().toString(),lesson_id,num);
            for (Problem prob : probs) {
                probList.add(prob.getProb_id());
            }
            System.out.println("probList:"+probList);

        } else if (settingTemp.getSrc().equals("test")) {
            //进入双周测 or 阶段测做题，此处 sys=7-8 表示7-8周
//            String week = settingTemp.getSys();
//            probList.addAll(getFromLesson(lesson, totalNum));
        } else if (settingTemp.getSrc().equals("recomnd")) {
            //进入推荐练习做题，只按照 lesson 抽取, 此处 sys 代表 redId=8888（参考的试卷）
//            Integer refId = Integer.valueOf(settingTemp.getSys());
            probList = getFromRecommend(totalNum, USER_ID);

        } else if (settingTemp.getSrc().equals("strengthen")) {
            //进入强化练习做题，本次错题 + 推荐练习
            probList = getFromStrengthen(totalNum, request);

        } else if (settingTemp.getSrc().equals("wrong")) {
            //进入错题集做题，此处 sys 无意义
            //注意一定要按顺序，选择在前，文本在后
            for (ProblemEvaluation pe : getWrong(USER_ID)) {
                if (problemService.getProblemByProb_id(pe.getProb_id()).getProb_attr().equals("Choice"))
                    probList.add(pe.getProb_id());
            }
            for (ProblemEvaluation pe : getWrong(USER_ID)) {
                if (!problemService.getProblemByProb_id(pe.getProb_id()).getProb_attr().equals("Choice"))
                    probList.add(pe.getProb_id());
            }
        }

//        probList = getFromNaive(totalNum)

        SheetTemp st = getSheet(request);   //getSheet 如果该用户没有 sheet 就会返回新的空 sheet
        if (st.getSheet_list().size() > 0)  //如果 user 存在，但是当前 sheet 没有清空，则进行一次清空
            st.clearSheet_list();
        st.setEvaluated(false);     //此时试卷是没有评分的状态
        st.setNum_list(probList);   //一个坑，这里是同一个对象，影响了下面的 probList

        st.setId(System.currentTimeMillis());  //试卷 id 的生成: 当前时间的毫秒数

        Date date = new Date();
        st.setStart_time(sdf.format(date));  //试卷开始时间

        int opt_num = 0;
        int txt_num = 0;
        for (Long probNum : probList) {  //将题目填入 sheet_list
            ProblemAnsTemp pt = new ProblemAnsTemp();

            Problem p = problemService.getProblemByProb_id(probNum);
            Options options = optionsService.getOptions(p.getOptions_id());

            pt.setLayoutType(options.getChoice_type());
            pt.setType(p.getProb_attr());
            pt.setChoice_type(options.getChoice_type());

            pt.setIdx(probNum);
            pt.setFinish((long) 0);   //默认未完成状态

            pt.setStem_image(p.getImage_url());
            pt.setStem_audio(p.getAudio_url());
            pt.setPoint(pointService.getPoint(p.getPoint_id()).getPoint_text());
            pt.setProb_text(p.getProb_text());
            pt.setAnalysis(answerService.getAnswer(probNum).getAnalysis_text());
            pt.setAns(answerService.getAnswer(probNum).getAnswer_text());

            switch (options.getChoice_type()) {
                case "text":
                    break;
                case "image":
                    pt.setOption_a_image(options.getA_image_url());
                    pt.setOption_b_image(options.getB_image_url());
                    pt.setOption_c_image(options.getC_image_url());
                    pt.setOption_d_image(options.getD_image_url());
                    break;
                case "audio":
                    pt.setOption_a_audio(options.getA_audio_url());
                    pt.setOption_b_audio(options.getB_audio_url());
                    pt.setOption_c_audio(options.getC_audio_url());
                    pt.setOption_d_audio(options.getD_audio_url());
                    break;
            }
            st.addSheet_list(pt);
        }
//        //确定次序
//        Collections.sort(st.getSheet_list(), new Comparator<ProblemAnsTemp>() {
//            @Override
//            public int compare(ProblemAnsTemp o1, ProblemAnsTemp o2) {
//                if (o1.getShowOrder() < o2.getShowOrder()) return -1;
//                else if (o1.getShowOrder() == o2.getShowOrder()) return 0;
//                else return 1;
//            }
//        });


        st.setExer_level(level.toString());

        st.setOpt_num(opt_num);
        st.setTxt_num(txt_num);
        //TODO 本次出题组卷，插入数据库（目前好像没什么作用）

        Exercise exercise = new Exercise();
        exercise.setExercise_id(st.getId());
        exercise.setGrammar_id((long) -1);
        exercise.setProb_list(probList.toString().replaceAll(" ", ""));
        exercise.setExercise_attr("ai");
        exercise.setUpdate_time(sdf.format(new Date()));

        exerciseService.insertExercise(exercise);
        System.out.println("st:" + st);
        return st;
    }

    @PostMapping(path = "/postSetting")
    public SettingTemp postSetting(@RequestParam("src") String src,
                                   @RequestParam("sys") String sys,
                                   @RequestParam("lev") Integer lev,
                                   @RequestParam("probNum") Integer probNum) {
        /*
        src: lesson ai test recomnd wrong
        lev: 当前等级，num: 题目总量
        refId: 需要参考的组卷 ID，
        sys: 1. 温故知新中：sys=smt 代表出题的体系
            2. 课课练中 sys=8 代表课程 lesson8
            3. 双周测试中，sys=7-8 表示7-8周
            4. 如果有需要参考的 redId=8888，那么 sys 代表 refId，
         */
        SettingTemp settingTemp = SettingTemp.getInstance();
        settingTemp.setSrc(src);
        settingTemp.setLev(lev);
        settingTemp.setProbNum(probNum);
        settingTemp.setSys(sys);
        System.out.println("settingTemp:" + settingTemp);
        return settingTemp;
    }

    @GetMapping(path = "/getCommentAi")
    public CommentTemp getCommentAi(@RequestParam() Long id) {
        /**
         * @description: 根据用户此次做题结果，计算用户做题详情评论，如知识点掌握程度等
         * @return: commentTemp 类
         */
        return commentTemp;
    }

    @GetMapping(path = "/getCommentLesson")
    public CommentTemp getCommentLesson(@RequestParam() Long id) {
        commentTemp.setLesson(Integer.parseInt(SettingTemp.getInstance().getSys()));
        commentTemp.setLevel(SettingTemp.getInstance().getLev());
        return commentTemp;
    }

    @GetMapping(path = "/getCommentExtra")
    public CommentTemp getCommentExtra(@RequestParam() Long id) {
        return commentTemp;
    }

    @GetMapping(path = "/homepageInitial")
    public HashMap<String, Integer> homepageInitial() {
        // 主页初始化操作
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("level", 1);
        map.put("num", 25);
        return map;
    }

    @GetMapping(path = "/getHistory")
    public List<ExerciseEvaluation> getHistory(HttpServletRequest request) {
        // TODO 加上参数用户 id
        /**
         * @description: 获取用户做的历史试卷
         * @return: 试卷序号列表
         */
        Long USER_ID = getUserFromCookies(request);  // 获得当前用户
        return exerciseEvalService.getExerciseEval(USER_ID);
    }

    @GetMapping(path = "/getWrong")
    public ArrayList<ProblemEvaluation> getWrong(Long USER_ID) {
        /**
         * @description: 获取用户做错的所有题 + 题的评判信息
         * @return: 类似 sheetTemp 的列表
         */
        ArrayList<ProblemEvaluation> wrongList = new ArrayList<>();
        String probEvalListStr = "[]";
        try {
            probEvalListStr = reviewCollecService.getCollec(USER_ID).getProb_eval_list();
        } catch (Exception e) {
            System.out.println(util.getException(e));
            reviewCollecService.insertCollec(USER_ID, "[]", "[]");
        }
        ArrayList<Long> probEvalList = util.stringToList(probEvalListStr);
        for (Long probEvalId : probEvalList)
            wrongList.add(problemEvalService.getProblemEvalById(probEvalId));

        return wrongList;
    }


    public ArrayList<Long> reorganizeProbList(ArrayList<Long> tmpList) {
        ArrayList<Long> orderList = new ArrayList<>();
        for (Long tmpNum : tmpList)
            try {
//                System.out.println("problem:"+problemService.getProblemByProb_id(tmpNum));
//                System.out.println("problem_equals:"+problemService.getProblemByProb_id(tmpNum).getProb_attr().equals("Choice"));
                String prob_type = problemService.getProblemByProb_id(tmpNum).getProb_type();
                if ("opt".equals(prob_type))
                    orderList.add(tmpNum);
            } catch (Exception e) {
                System.out.println(tmpNum);
                System.out.println(tmpList);
                e.printStackTrace();
            }

        for (Long tmpNum : tmpList) {
            String prob_type = problemService.getProblemByProb_id(tmpNum).getProb_type();
            if ("txt".equals(prob_type))
                orderList.add(tmpNum);
        }
        return orderList;
    }

    public ArrayList<Long> uniqueProbList(ArrayList<Long> tmpList) {
        /**
         * @description: 列表去重 & 检查被删除的题，然后通过随机抽取题目进行补齐. 注意一点，unique 中 hashset 会自动排序，后序需要再 shuffle
         * @return: Long 的列表
         */
        int origin = tmpList.size();
        HashSet<Long> set = new HashSet<>(tmpList);
        tmpList.clear();
        tmpList.addAll(set);
        int margin = origin - tmpList.size();
        if (margin > 0) System.out.println("uniq进入去重，补齐" + margin);
//        int loop = 30;
//        while (margin > 0 && loop > 0) {  //题目不够会陷入死循环
//            long newId = getOneRandom();
//            loop--;
//            if (!tmpList.contains(newId) && !problemService.getProblemByProb_id(newId).getProb_text().contains("题目已被删除")) {
//                tmpList.add(newId);
//                margin--;
//            }
//        }
        return checkDelete(tmpList);   //去重后还要检查一遍有没有被删除的题
//        return tmpList;   //去重后还要检查一遍有没有被删除的题
    }

    public ArrayList<Long> checkDelete(ArrayList<Long> tmpList) {
        /**
         * @description: 剔除被删除的题目
         * @return: Long 的列表
         */
        int origin = tmpList.size();
        ArrayList<Long> newList = new ArrayList<>();
        System.out.println("tmpList.length:" + tmpList.size());
        System.out.println("tmpList:" + tmpList);
        for (Long prob_id : tmpList) {
            if (!problemService.getProblemByProb_id(prob_id).getProb_text().contains("题目已被删除"))
                newList.add(prob_id);
        }
        int margin = origin - newList.size();
        System.out.println(tmpList);
        System.out.println("new_list:" + newList);
//        if (margin > 0) System.out.println("有被删除的题目，补齐" + margin);
//        int loop = 30;
//        while (margin > 0 && loop > 0) {  //题目不够会陷入死循环
//            long newId = getOneRandom();
//            loop--;
//            if (!newList.contains(newId) && !problemService.getProblemByProb_id(newId).getProb_text().contains("题目已被删除")) {
//                newList.add(newId);
//                margin--;
//            }
//        }
        return newList;
    }

    @GetMapping("/getProblemList")
    public ArrayList<Problem> getProblemList(@RequestParam("list") String listStr) {
        /**
         * @description: 开放 API，给定 problem 的列表字符串参数，返回题目细节
         * @return: Problem 的列表
         */
        listStr = listStr.replaceAll(" ", "");
        listStr = "[" + listStr + "]";
        ArrayList<Long> probList = util.stringToList(listStr);
        ArrayList<Problem> outputList = new ArrayList<>();
        for (Long num : probList)
            outputList.add(problemService.getProblemByProb_id(num));

        return outputList;
    }


}
