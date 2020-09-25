package com.offcn.sellergoods.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.pojo.TbBrand;
import com.offcn.entity.PageResult;
import com.offcn.entity.Result;
import com.offcn.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand/")
public class BrandController {

    @Reference
    private BrandService service;

    @RequestMapping("findAll")
    public List<TbBrand> findAll(){
        return service.findAll();
    }

    @RequestMapping("findPage")
    public PageResult findPage(int page,int rows){
        return service.findPage(page, rows);
    }

    @RequestMapping("add")
    public Result add(@RequestBody TbBrand brand){

        try {
            service.add(brand);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    @RequestMapping("update")
    public Result update(@RequestBody TbBrand brand){
        try {
            service.update(brand);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    @RequestMapping("findOne")
    public TbBrand findOne(Long id){
        return service.findOne(id);
    }

    @RequestMapping("delete")
    public Result delete(Long[] ids){
        try {
            service.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("search")
    public PageResult search(@RequestBody TbBrand brand,int page,int rows){
        return service.findPage(brand, page, rows);
    }

    @RequestMapping("selectOptionList")
    public List<Map> selectOptionList(){
        return service.selectOptionList();
    }
}
