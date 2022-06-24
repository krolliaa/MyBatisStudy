package com.kk.mapper;

import com.kk.pojo.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptMapper {
    public abstract Dept selectStepTwoByDId(@Param(value = "did") Integer did);

    public abstract List<Dept> selectCollection();

    public abstract List<Dept> selectCollectionStepOne();
}
