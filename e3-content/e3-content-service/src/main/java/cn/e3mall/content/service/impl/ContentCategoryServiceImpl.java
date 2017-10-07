package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	//通过父类id查询内容分类
	public List<EasyUITreeNode> showContentCategory(long parentId) {
		TbContentCategoryExample example =new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			node.setText(tbContentCategory.getName());
			resultList.add(node);
		}
		return resultList;
	}
	//通过传递的id和name属性增加内容分类
	@Override
	public E3Result addContentCategory(long parentId, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//不全contentCategory的其他属性
		//排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		contentCategory.setIsParent(false);
		//状态。可选值:1(正常),2(删除)
		contentCategory.setStatus(1);
		contentCategoryMapper.insert(contentCategory);
		TbContentCategory primaryKey = contentCategoryMapper.selectByPrimaryKey(parentId);
		//将增加的节点所在的节点的id变成父类节点
		if(!primaryKey.getIsParent()) {
			primaryKey.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(primaryKey);
		}
		//返回E3Result，其中包装TbContentCategory对象
		return E3Result.ok(contentCategory);
	}

}
