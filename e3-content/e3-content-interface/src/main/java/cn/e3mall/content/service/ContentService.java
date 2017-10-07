package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {

	List<TbContent> getContentList(long categoryId);
	E3Result addContent(TbContent content);
}
