package com.offcn.page.service;

public interface ItemPageService {

    public boolean genItemHtml(Long goodsId);

    /**
     * 删除商品详细页
     * @param
     * @return
     */
    public boolean deleteItemHtml(Long[] goodsIds);
}
