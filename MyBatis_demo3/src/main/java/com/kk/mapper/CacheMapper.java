package com.kk.mapper;

import com.kk.pojo.Emp;

public interface CacheMapper {
    public abstract Emp selectEmpById(Integer eid);
}
