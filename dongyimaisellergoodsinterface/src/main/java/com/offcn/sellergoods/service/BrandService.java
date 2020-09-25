package com.offcn.sellergoods.service;

import com.offcn.pojo.TbBrand;
import com.offcn.entity.PageResult;

import java.util.List;
import java.util.Map;

public interface BrandService {

    List<TbBrand> findAll();

    PageResult findPage(int pageNum, int pageSize);

    void add(TbBrand brand);

    void update(TbBrand brand);

    TbBrand findOne(Long id);

    void delete(Long[] ids);

    PageResult findPage(TbBrand brand,int pageNum,int pageSize);

    /**
     * 品牌下拉框数据
     */
    List<Map> selectOptionList();
}
