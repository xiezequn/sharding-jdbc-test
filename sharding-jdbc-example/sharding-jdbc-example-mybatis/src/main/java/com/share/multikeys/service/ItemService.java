package com.share.multikeys.service;

import com.google.gson.Gson;
import com.share.multikeys.entity.Item;
import com.share.multikeys.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/** 
* @author weigen.ye 
* @date 创建时间：2016年10月8日 下午5:46:52 
*
*/
@Service
@Transactional
public class ItemService {

	@Resource
    private ItemRepository itemRepository;
	
	public void insert(Item item){
		this.itemRepository.insert(item);
	}
	
	@Transactional(readOnly = true)
    public void select() {
        System.out.println(itemRepository.selectAll());
    }
	    
    public void selectByKey(Integer itemId) {
        System.out.println(itemRepository.selectByKey(itemId));
    }
    
    public void selectJoin(Integer orderId) {
        System.out.println(new Gson().toJson(itemRepository.selectJoin(orderId)));
    }
}
