package com.xsxx.mapper;

import java.util.List;

public interface BaseMapper <T, PK extends java.io.Serializable>{

    List<T> findAll();

    void update(T model);

    void insert(T model);

    T load(PK id);

    void delete(PK modelPK);


}
