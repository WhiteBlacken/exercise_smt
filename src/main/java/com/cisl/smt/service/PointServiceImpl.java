package com.cisl.smt.service;

import com.cisl.smt.dao.PointRepository;
import com.cisl.smt.po.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointServiceImpl implements PointService{
    /**
     * @description:获取考点
     * @author: Hopenx
     * @date: 2020-11-14 17:29
     */

    @Autowired
    private PointRepository pointRepository;

    @Override
    public Point getPoint(Long id){
        return pointRepository.getPoint(id);
    }
}
