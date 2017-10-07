package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_KEY}")
	private String CONTENT_KEY;
	@Override
	//对内容列表进行首页动态展示
	public List<TbContent> getContentList(long categoryId) {
		//向缓存中添加数据
		try {
			String json = jedisClient.hget(CONTENT_KEY, categoryId+"");
			if(StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		//执行查询,查询所有,包含content的大文本类型
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		
		//如果数据不一样,先查询数据库,然后再放入缓存中
		
		try {
			jedisClient.hset(CONTENT_KEY, categoryId+"", JsonUtils.objectToJson(list));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	//对内容节点进行添加
	//如果增加了内容节点，需要进行缓存同步
	//缓存同步：就是将缓存删了，让请求直接查询数据库，然后再放入缓存中
	public E3Result addContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		//缓存同步
		jedisClient.hdel(CONTENT_KEY, content.getCategoryId().toString());
		return E3Result.ok();
	}

}
