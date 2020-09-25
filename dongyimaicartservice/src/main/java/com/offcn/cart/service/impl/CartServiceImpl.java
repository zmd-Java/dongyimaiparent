package com.offcn.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.offcn.cart.service.CartService;
import com.offcn.mapper.TbItemMapper;
import com.offcn.pojo.Cart;
import com.offcn.pojo.TbItem;
import com.offcn.pojo.TbOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
//1.根据商品SKU ID查询SKU商品信息
//2.获取商家ID
//3.根据商家ID判断购物车列表中是否存在该商家的购物车
//4.如果购物车列表中不存在该商家的购物车
//4.1 新建购物车对象
//4.2 将新建的购物车对象添加到购物车列表
//5.如果购物车列表中存在该商家的购物车
// 查询购物车明细列表中是否存在该商品
//5.1. 如果没有，新增购物车明细
//5.2. 如果有，在原购物车明细上添加数量，更改金额
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {

        //1.根据商品SKU ID查询SKU商品信息
        TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
        if (item==null){
            throw new RuntimeException("商品不存在");
        }
        if (!item.getStatus().equals("1")){
            throw new RuntimeException("商品状态无效");
        }

        //2.获取商家ID
        String sellerId = item.getSellerId();
        Cart cart = searchCartBySellerId(cartList, sellerId);
        //4.如果购物车列表中不存在该商家的购物车
        if (cart==null){
            //4.1 新建购物车对象
            cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            TbOrderItem orderItem = createOrderItem(item, num);
            List orderItemList = new ArrayList();
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            //4.2将购物车对象添加到购物车列表
            cartList.add(cart);
        }else {
            //5.如果购物车列表中存在该商家的购物车
            // 判断购物车明细列表中是否存在该商品
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            TbOrderItem tbOrderItem = searchOrderItemByItemId(orderItemList, itemId);
            if (tbOrderItem==null){
                //5.1. 如果没有，新增购物车明细
                tbOrderItem = createOrderItem(item, num);
                orderItemList.add(tbOrderItem);
            }else {
                //5.2. 如果有，在原购物车明细上添加数量，更改金额
               tbOrderItem.setNum(tbOrderItem.getNum()+num);
               tbOrderItem.setTotalFee(new BigDecimal(tbOrderItem.getNum()*tbOrderItem.getPrice().doubleValue()));
               //如果数量操作后小于等于0，则移除
                if (tbOrderItem.getNum()<=0){
                    cart.getOrderItemList().remove(tbOrderItem);//移除购物车明细
                }
                //如果移除后cart的明细数量为0，则将cart移除
                if (cart.getOrderItemList().size()==0){
                    cartList.remove(cart);
                }
            }
        }
        return cartList;
    }

    @Override
    public List<Cart> findCartListFromRedis(String username) {

        System.out.println("从redis中提取购物车数据....."+username);
       List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
       if (cartList==null){
           cartList = new ArrayList<>();
       }
        return cartList;
    }

    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        System.out.println("向redis存入购物车数据....."+username);
        redisTemplate.boundHashOps("cartList").put(username, cartList);
    }

    @Override
    public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {

        System.out.println("合并购物车");
        for (Cart cart : cartList2) {
            for (TbOrderItem tbOrderItem : cart.getOrderItemList()) {
                cartList1 = addGoodsToCartList(cartList1, tbOrderItem.getItemId(), tbOrderItem.getNum());
            }
        }
        return cartList1;
    }

    /**
     * 根据商家id查询购物车对象
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart searchCartBySellerId(List<Cart> cartList,String sellerId){
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }

    /**
     * 创建订单明细
     * @param item
     * @param num
     * @return
     */
    private TbOrderItem createOrderItem(TbItem item, Integer num){
        if (num<=0){
            throw new RuntimeException("数量非法");
        }

        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setItemId(item.getId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
        return orderItem;
    }

    /**
     * 根据商品明细id查询
     * @param orderItemList
     * @param itemId
     * @return
     */
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList ,Long itemId ){

        for (TbOrderItem tbOrderItem : orderItemList) {
            if (tbOrderItem.getItemId().longValue()==itemId.longValue()){
                return tbOrderItem;
            }
        }
        return null;
    }
}
