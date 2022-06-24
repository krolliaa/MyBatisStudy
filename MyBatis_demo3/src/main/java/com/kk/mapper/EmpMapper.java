package com.kk.mapper;

import com.kk.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmpMapper {
    public abstract List<Emp> selectAllEmp();

    public abstract List<Emp> selectStepOne();

    public abstract List<Emp> selectCollectionStepTwo(Integer did);

    public abstract List<Emp> selectEmpByCondition(@Param(value = "empName") String empName, @Param(value = "age") Integer age, @Param(value = "sex") String sex, @Param(value = "email") String email);

    public abstract int deleteEmpByForeach(@Param(value = "eIds") Integer[] eIds);
}
