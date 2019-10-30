package com.esb.order;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esb.goods.GoodsService;
import com.esb.redis.RedisService;
import com.esb.redis.key.OrderKey;
import com.esb.seckill.SeckillOrder;
import com.esb.seckill.SeckillService;
import com.esb.user.User;
import com.esb.user.UserServiceImpl;
import com.esb.vo.GoodsVo;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service("orderService")
public class OrderServiceImpl implements OrderService{
	
	@Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisService redisService;

	@Override
	public SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId) {
		return (SeckillOrder)redisService.get(OrderKey.getSeckillOrderByUidGid, "" + userId + "_" + goodsId);
	}

	@Override
	public OrderInfo getOrderById(long orderId) {
		return orderMapper.getOrderById(orderId);
	}

	/**
     * 因为要同時分别在訂單和秒殺訂單都新增一筆資料，所以要保證個個操作是一個事物
     */
	@Override
    @Transactional
	public OrderInfo createOrder(User user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getGoodsPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.insert(orderInfo);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);

        redisService.set(OrderKey.getSeckillOrderByUidGid, "" + user.getId() + "_" + goods.getId(), seckillOrder);

        return orderInfo;
	}

}
