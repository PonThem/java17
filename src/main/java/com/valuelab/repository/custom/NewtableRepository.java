package com.valuelab.repository.custom;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.valuelab.entity.Newtable;

@Mapper
public interface NewtableRepository {
    // 全件取得
    public List<Newtable> selectAll();

    // 1件追加
    public int insert(Newtable newtable);

}
