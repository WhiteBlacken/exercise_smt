package com.cisl.smt.web;

import com.cisl.smt.dao.AnswerRepository;
import com.cisl.smt.dao.OptionsRepository;
import com.cisl.smt.dao.PointRepository;
import com.cisl.smt.dao.ProblemRepository;
import com.cisl.smt.po.Answer;
import com.cisl.smt.po.Options;
import com.cisl.smt.po.Point;
import com.cisl.smt.po.Problem;
import com.cisl.smt.service.OptionsService;


import com.cisl.smt.web.Temp.FileInfo;
import com.cisl.smt.web.Temp.ProblemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UploadController {
    /**
     * @description: 项目的题库上传对应逻辑
     * @author: Hopenx
     * @date: 2020/12/24 4:50 下午
     */

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private OptionsRepository optionsRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private OptionsService optionsService;

    public Long calGrammar_id(Long lesson_id, String prob_level) {
        if (lesson_id < 10)
            prob_level += "0";
        return Long.valueOf(prob_level + lesson_id.toString());
    }

    @GetMapping("/preview")
    public ModelAndView preview() {
        ModelAndView mav;
        mav = new ModelAndView("preview");
        return mav;
    }

    @GetMapping("/upload")
    public ModelAndView upload() {
        return new ModelAndView("upload");
    }

    @GetMapping("/upload_list")
    public ModelAndView upload_list() {
        return new ModelAndView("upload_list");
    }

    @GetMapping("/listAllProb")
    public ArrayList<ProblemDetail> listAllProb(@Param("level") String level,
                                                @Param("lesson_id") Long lesson_id) {
        ArrayList<ProblemDetail> probDetList = new ArrayList<>();
        List<Problem> problemList = problemRepository.getProblemByLevelAndLesson_id(level, lesson_id);
        for (Problem problem : problemList) {
            try {
                ProblemDetail problemDetail = getProblemDetail(problem.getProb_id());
                probDetList.add(problemDetail);
            } catch (NullPointerException e) {
                System.out.println(problem);
            }
        }
        return probDetList;
    }

    @GetMapping("/listAllPoint")
    public ArrayList<Point> listAllPoint() {
        return new ArrayList<>(pointRepository.getAllPoint());
    }

    Boolean checkUnique(String point_text) {
        ArrayList<Point> pointList = listAllPoint();
        for (Point point : pointList) {
            if (point.getPoint_text().equals(point_text))
                return false;
        }
        return true;
    }

    @PostMapping("/editPoint")
    public String editPoint(@RequestParam("point_id") Long point_id,
                            @RequestParam("point_text") String point_text) {
        try {
            if (!checkUnique(point_text))
                return "已有重复考点";
            pointRepository.updatePoint(point_id, point_text);
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
        return "OK";
    }

    @PostMapping("/addPoint")
    public String addPoint(@RequestParam("grammar_id") Long grammar_id,
                           @RequestParam("point_text") String point_text) {
        try {
            if (!checkUnique(point_text))
                return "已有重复考点";
            Long point_id = pointRepository.getLastPoint_id() + 1;
            pointRepository.insertPoint(point_id, grammar_id, point_text);
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
        return "OK";
    }

    @GetMapping("/searchProb")
    public ArrayList<ProblemDetail> searchProb(@RequestParam("text") String input_text, @RequestParam("mode") String mode) {  //要用 RequestParam
        ArrayList<ProblemDetail> probDetList = new ArrayList<>();
        if (input_text == null || input_text.length() == 0) {
            return probDetList;
        }
        List<Problem> probList = problemRepository.getAllProblem();
        try {
            if (mode.equals("题干")) {
                for (Problem prob : probList) {  //检查是否有匹配的题干
                    String probTxt = prob.getProb_text();
                    if (purify(probTxt).contains(purify(input_text)))
                        probDetList.add(getProblemDetail(prob.getProb_id()));

                }
            } else if (mode.equals("考点")) {
                for (Problem prob : probList) {  //检查是否有匹配的考点
                    try {
                        Long pointId = prob.getPoint_id();
                        String pointTxt = pointRepository.getPoint(pointId).getPoint_text();
                        if (pointTxt.contains(input_text))
                            probDetList.add(getProblemDetail(prob.getProb_id()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Collections.sort(probDetList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return probDetList;
    }

    @GetMapping("/getProblemDetail")
    public ProblemDetail getProblemDetail(@RequestParam("prob_id") Long prob_id) {
        Problem problem = problemRepository.getProblemByProb_id(prob_id);
        ProblemDetail problemDetail = new ProblemDetail();
        try {
            problemDetail.setProb_id(problem.getProb_id());
            Options options = optionsRepository.getOptions(problem.getOptions_id());
            String options_text = "";
            if (options.getOption_a() != null && options.getOption_a().length() != 0) {
                options_text = "【A】" + options.getOption_a() + "【B】" + options.getOption_b() + "【C】" + options.getOption_c();
                if (options.getOption_d() != null && options.getOption_d().length() > 0)
                    options_text += "【D】" + options.getOption_d();
            } else {
                if (problem.getBlank_num() == null)
                    problem.setBlank_num((long) 0);
                options_text = "空行数量：" + problem.getBlank_num().toString();
            }
            problemDetail.setOptions(options_text);
            problemDetail.setProb_text(problem.getProb_text());
            problemDetail.setProb_attr(problem.getProb_attr());
            problemDetail.setProb_level(problem.getProb_level());
            problemDetail.setProb_diff(problem.getProb_diff());
            problemDetail.setLesson_id(problem.getLesson_id());
            problemDetail.setPoint_text(pointRepository.getPoint(problem.getPoint_id()).getPoint_text());
            problemDetail.setPoint_id(problem.getPoint_id());
            problemDetail.setBlank_num(problem.getBlank_num());
            problemDetail.setAnswer_text(answerRepository.getAnswer(problem.getProb_id()).getAnswer_text());
            problemDetail.setAnalysis(answerRepository.getAnswer(problem.getProb_id()).getAnalysis_text());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return problemDetail;
    }

    public String purify(String raw) {
        //筛选出所有字母
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (Character.isLowerCase(c) || Character.isUpperCase(c))
                sb.append(c);
        }
        return sb.toString();
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /*
     2021.01.26 需求更新
     上传的函数里增加几个文件的地址字段即可
     显示的时候直接根据这个去拿

     */

    /***
     * 题目上传 -需求更改 qxy 220126
     * @param prob_id
     * @param optionA
     * @param optionB
     * @param optionC
     * @param optionD
     * @param answer
     * @param analysis
     * @param prob_text
     * @param prob_attr
     * @param prob_diff
     * @param prob_level
     * @param lesson_id
     * @param point_id
     * @param blank_num
     * @param mode
     * @return
     */
    @PostMapping("/postProblem")
    public String postProblem(@RequestParam("prob_id") Long prob_id,
                              @RequestParam("optionA") String optionA,
                              @RequestParam("optionB") String optionB,
                              @RequestParam("optionC") String optionC,
                              @RequestParam("optionD") String optionD,
                              @RequestParam("answer") String answer,
                              @RequestParam("analysis") String analysis,
                              @RequestParam("prob_text") String prob_text,
                              @RequestParam("prob_attr") String prob_attr,
                              @RequestParam("prob_diff") String prob_diff,
                              @RequestParam("prob_level") String prob_level,
                              @RequestParam("lesson_id") Long lesson_id,
                              @RequestParam("point_id") Long point_id,
                              @RequestParam("blank_num") Long blank_num,
                              @RequestParam("mode") Long mode) {
        try {
            if (mode == 0) {   //修改模式
                optionsRepository.updateOptions(prob_id, optionA, optionB, optionC, optionD);
                answerRepository.updateAnswer(prob_id, analysis, answer);
                problemRepository.updateProblemByPoint_id(prob_id, prob_text, prob_attr, prob_diff, prob_level, lesson_id, point_id, blank_num);
            } else if (mode == 1) {   //新增模式
                if (!isContainChinese(prob_text)) {  //检查是否有重复题目, 有中文就不检测
                    List<Problem> probList = problemRepository.getAllProblem();
                    for (Problem prob : probList) {
                        String probTxt = prob.getProb_text();
                        if (purify(probTxt).equals(purify(prob_text)))
                            return "已有重复题目: " + prob.getProb_id().toString();
                    }
                }
                //难点：确保这三条语句原子性操作
                prob_id = problemRepository.getLastProb_id() + 1;  //ID 顺次加一
                optionsRepository.insertOptions(prob_id, optionA, optionB, optionC, optionD);
                answerRepository.insertAnswer(prob_id, analysis, answer);

                Long grammar_id = calGrammar_id(lesson_id, prob_level);
                problemRepository.insertProblem(prob_id, grammar_id, point_id, prob_attr, prob_diff, prob_level, prob_text, prob_id, prob_id, lesson_id, blank_num);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail";
        }
        return "OK";
    }

    @PostMapping("/postProblem1")
    public String postProblem1(@RequestParam("prob_id") Long prob_id,
                               @RequestParam(value = "optionA") String optionA,
                               @RequestParam(value = "optionB") String optionB,
                               @RequestParam(value = "optionC") String optionC,
                               @RequestParam(value = "optionD") String optionD,
                               @RequestParam("option_a_image") String option_a_image, //add
                               @RequestParam("option_b_image") String option_b_image, //add
                               @RequestParam("option_c_image") String option_c_image, //add
                               @RequestParam("option_d_image") String option_d_image, //add
                               @RequestParam("option_a_audio") String option_a_audio, //add
                               @RequestParam("option_b_audio") String option_b_audio, //add
                               @RequestParam("option_c_audio") String option_c_audio, //add
                               @RequestParam("option_d_audio") String option_d_audio, //add
                               @RequestParam("option_flag") int option_flag, //add
                               @RequestParam("answer") String answer_text,
                               @RequestParam("analysis") String analysis,
                               @RequestParam("prob_text") String prob_text,
                               @RequestParam("stem_image") String stem_image, //add
                               @RequestParam("stem_audio") String stem_audio, //add
                               @RequestParam("stem_flag") int stem_flag, //add
                               @RequestParam("prob_attr") String prob_attr,
                               @RequestParam("prob_diff") String prob_diff,
                               @RequestParam("prob_level") String prob_level,
                               @RequestParam("lesson_id") Long lesson_id,
                               @RequestParam("point_id") Long point_id,
                               @RequestParam("blank_num") Long blank_num,
                               @RequestParam("mode") Long mode) {
        try {
            if (mode == 0) {   //修改模式 //修改暂不进行更改
                optionsRepository.updateOptions(prob_id, optionA, optionB, optionC, optionD);
                answerRepository.updateAnswer(prob_id, analysis, answer_text);
                problemRepository.updateProblemByPoint_id(prob_id, prob_text, prob_attr, prob_diff, prob_level, lesson_id, point_id, blank_num);
            } else if (mode == 1) {   //新增模式
                if (!isContainChinese(prob_text)) {  //检查是否有重复题目, 有中文就不检测
                    List<Problem> probList = problemRepository.getAllProblem();
                    for (Problem prob : probList) {
                        String probTxt = prob.getProb_text();
                        if (purify(probTxt).equals(purify(prob_text)))
                            return "已有重复题目: " + prob.getProb_id().toString();
                    }
                }
                /*
                需要做三件事
                1. 处理option *
                2. 处理answer
                3. 处理problem *
                 */

                //1. 处理option
                //如果传入为空会怎么样？-> 依然可以执行   mysql-utf-8 一个中文占3个字节
                Options options = new Options(optionA,optionB,optionC,optionD);
                switch (option_flag){
                    case 1:
                        //text only->do nothing
                        break;
                    case 2:
                        //pic only->save pic url
                        options.setA_image_url(option_a_image);
                        options.setB_image_url(option_b_image);
                        options.setC_image_url(option_c_image);
                        options.setD_image_url(option_d_image);
                        break;
                    case 3:
                        //audio only->save audio url
                        options.setA_audio_url(option_a_audio);
                        options.setB_audio_url(option_b_audio);
                        options.setC_audio_url(option_c_audio);
                        options.setD_audio_url(option_d_audio);
                        break;
                    case 4:
                        //audio+pic->save both url
                        options.setA_audio_url(option_a_audio);
                        options.setB_audio_url(option_b_audio);
                        options.setC_audio_url(option_c_audio);
                        options.setD_audio_url(option_d_audio);

                        options.setA_image_url(option_a_image);
                        options.setB_image_url(option_b_image);
                        options.setC_image_url(option_c_image);
                        options.setD_image_url(option_d_image);
                        break;
                }
                //要把flag存下来，作为展示时的信息
                options.setResource_flag(option_flag);
                Options now_options = optionsRepository.save(options);

                //2. 处理answer
                Answer answer = new Answer(answer_text,analysis);
                Answer now_answer = answerRepository.save(answer);

                //3. 处理problem

                Long grammar_id = calGrammar_id(lesson_id, prob_level);
                Problem problem = new Problem(prob_text,prob_attr,prob_level,prob_diff,lesson_id,grammar_id,point_id,blank_num);
                problem.setOptions_id(now_options.getOptions_id());
                problem.setAnswer_id(now_answer.getAnswer_id());
                //根据flag来确定
                switch (stem_flag){
                    case 1:
                        //text only->do nothing
                        break;
                    case 2:
                        //text+pic->save pic url
                        problem.setImage_url(stem_image);
                        break;
                    case 3:
                        //text+audio->save audio url
                        problem.setAudio_url(stem_audio);
                        break;
                    case 4:
                        //text+audio+pic->save both url
                        problem.setImage_url(stem_image);
                        problem.setAudio_url(stem_audio);
                        break;
                }
                //将flag记录下来，作为展示的依据
                problem.setResource_flag(stem_flag);
                problemRepository.save(problem);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Fail";
        }
        return "OK";
    }

    @PostMapping("/deleteProb")
    public String deleteProb(@RequestParam("prob_id") Long prob_id) {
        try {
            String prob_text = problemRepository.getProblemByProb_id(prob_id).getProb_text();
            problemRepository.markDeleteProblem(prob_id, "【题目已被删除】" + prob_text);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail";
        }
        return "OK";
    }

    @GetMapping("/getNextProb")
    public Long getNextProb() {
        return problemRepository.getLastProb_id() + 1;
    }

    @PostMapping("/fileUpload")
    public String singleImage(@RequestParam("file") MultipartFile file, @RequestParam(value = "prob_id") String prob_id, HttpServletRequest request) throws FileNotFoundException {  //参数名需与前端文件标签名一样
        // 获取项目classes/static的地址
        String path = ClassUtils.getDefaultClassLoader().getResource("static").getPath();  //static
        String fileName = file.getOriginalFilename();  //获取文件名  xxx.jpg -> prob_id.jpg
//        fileName = prob_id + "." + fileName.split("\\.")[1];
        fileName = prob_id + ".png";
        // 图片访问URI(即除了协议、地址和端口号的URL)
        String url_path = "image/_prob" + File.separator + fileName;
        String savePath = path + File.separator + url_path;  //图片保存路径
        System.out.println(("图片保存地址：" + savePath));
        File saveFile = new File(savePath);
        // 这个语句只会产生文件夹
//        if (!saveFile.exists()){
//            saveFile.mkdirs();
//        }
        try {
            file.transferTo(saveFile);  //将临时存储的文件移动到真实存储路径下
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回图片访问地址
        return url_path;
    }

    /*
    2021.12.24 bug 修复
    transferTo 提示 Permission Denied 修改一下权限即可
    sudo chmod 777 /home/smartree/www/smt-exercise/WEB-INF/classes/static/image/_prob
     */

    /*
    2021.01.26 需求更新
    修改的原则:
        1. 尽量不对原有代码进行改动
        2. 尽量与原有代码的逻辑和风格保持统一
    上传的需求:
        1. 针对题目和选项均要有三种格式的上传
    主要思路：
        1. 上传均采用共同的函数
        2. 命名上均采用prob_id作为名称，audio和image文件夹下均有_prob、_option文件夹
            分别来记录题干和选项
     */

    /**
     * 获得秒级的时间戳
     *
     * @param date 当前时间
     * @return
     */
    public static int getSecondTimestampTwo(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Integer.valueOf(timestamp);
    }

    /**
     * 文件上传（pic、audio）
     *
     * @param file     文件本身
     * @param fileType 文件的类型（主要是image和audio）
     * @param info     是题干还是选项
     * @param option   如果上传的是选型素材，需要提供是哪个选项
     * @return 文件的地址
     * @throws FileNotFoundException
     */
    @PostMapping("/fileUpload1")
    public FileInfo fileUpload(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "fileType") String fileType,
                               @RequestParam(value = "info") String info,
                               @RequestParam(value = "option", defaultValue = "A") String option)
            throws FileNotFoundException {  //参数名需与前端文件标签名一样
        // 获取项目classes/static的地址
        String path = ClassUtils.getDefaultClassLoader().getResource("static").getPath();  //static
        //获取文件的后缀
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //获取当前时间并作为时间戳给文件夹命名
        String timeStamp = simpleDateFormat.format(new Date());
        fileName = timeStamp + suffixName;
        // 文件访问URI(即除了协议、地址和端口号的URL)
        String url_path = "";

        if ("image".equals(fileType) && "stem".equals(info)) {
            url_path = "image/_prob" + "/" + fileName;
        } else if ("image".equals(fileType) && "option".equals(info)) {
            url_path = "image/_option/_" + option + "/" + fileName;
        } else if ("audio".equals(fileType) && "stem".equals(info)) {
            url_path = "audio/_prob" + "/" + fileName;
        } else if ("audio".equals(fileType) && "option".equals(info)) {
            url_path = "audio/_option/_" + option + "/" + fileName;
        }
        String savePath = path + "/" + url_path;  //图片保存路径
        System.out.println(("图片保存地址：" + savePath));
        File saveFile = new File(savePath);

        try {
            file.transferTo(saveFile);  //将临时存储的文件移动到真实存储路径下
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInfo fileInfo = new FileInfo(info,option,url_path);
        //返回文件访问地址
        return fileInfo;
    }

}
