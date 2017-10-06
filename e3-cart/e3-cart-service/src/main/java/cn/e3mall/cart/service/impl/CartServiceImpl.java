package cn.e3mall.cart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
/**
 * 购物车的业务层
 * <p>Title: CartServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public class CartServiceImpl implements CartService {
	@Autowired
	private TbItemMapper itemMapper;
	@Override
	//通过页面传递的商品id查询相应的商品
	public TbItem getItemById(Long itemId) {
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		return item;
	}

}
