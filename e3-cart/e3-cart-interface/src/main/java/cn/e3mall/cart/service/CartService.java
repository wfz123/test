package cn.e3mall.cart.service;

import cn.e3mall.pojo.TbItem;

public interface CartService {
	TbItem getItemById(Long itemId);
}
