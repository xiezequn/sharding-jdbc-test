package com.share.multikeys.algorithm;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.MultipleKeysTableShardingAlgorithm;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.share.multikeys.utils.TableNameUtil;

import java.util.*;

/** 
* @author weigen.ye 
* @date 创建时间：2016年10月8日 下午1:48:04 
* 多键分片，采用主键id+create_time进行路由，路由时create_time将被转化成具体的季度q0-q3
* sql语句中where条件中至少包含主键id或create_time才能路由到真实表，否则将抛出"no table route info"异常
*/
@SuppressWarnings("unchecked")
public class MultipleKeysIdAndTimeShardingAlgorithm implements MultipleKeysTableShardingAlgorithm {

	    @Override
	    public Collection<String> doSharding(final Collection<String> availableTargetNames, final Collection<ShardingValue<?>> shardingValues) {
	        Set<String> orderIdValueSet =  this.<Integer>getShardingValue(shardingValues, "order_id");
	        Set<String> createTimeValueSet = this.<Date>getShardingValue(shardingValues, "create_time");
	        boolean middleRouteFlag=false;
	        boolean suffixRouteFlag=false;
	        //只采用主键id路由
        	if(createTimeValueSet.isEmpty()){
        		middleRouteFlag=true;
        	}
        	//只采用create_time转化后的季度值路由
        	if(orderIdValueSet.isEmpty()){
        		suffixRouteFlag=true;
        	}
        	
	        List<String> result = new ArrayList<>();
			Set<List<String>> valueResult =null; 
			if(!middleRouteFlag&&!suffixRouteFlag){
				valueResult=Sets.cartesianProduct(orderIdValueSet, createTimeValueSet);
			}else if(middleRouteFlag){
				valueResult=Sets.cartesianProduct(orderIdValueSet);
			}else{
				valueResult=Sets.cartesianProduct(createTimeValueSet);
			}
			
	        for (List<String> value : valueResult) {
	        	String middle =null;
	        	String suffix =null;
	        	String allKeysSuffix =null;
	        	if(middleRouteFlag){
	        		middle ="_"+Integer.parseInt(value.get(0)) % 2+"_";
	        	}
	        	if(suffixRouteFlag){
	        		suffix=value.get(0);
	        	}
	        	if(!middleRouteFlag&&!suffixRouteFlag){
	        		allKeysSuffix=Joiner.on("_").join(Integer.parseInt(value.get(0)) % 2, value.get(1));
	        	}
	            for (String tableName : availableTargetNames) {
	            	//只采用主键id路由
	            	if(middleRouteFlag&&tableName.contains(middle)){
	            		result.add(tableName);
	            	}
	            	//只采用create_time转化后的季度值路由
	            	if(suffixRouteFlag&&tableName.endsWith(suffix)){
	            		result.add(tableName);
	            	}
	            	//默认采用采用主键id+create_time转化后的季度值路由
	            	if(null!=allKeysSuffix&&tableName.endsWith(allKeysSuffix)){
		                result.add(tableName);
	            	}
	            }
	        }
	        return result;
	    }
	    
	    @SuppressWarnings("rawtypes")
	    private <T> Set<String> getShardingValue(final Collection<ShardingValue<?>> shardingValues, final String shardingKey) {
	    	Set<String> resultSet=new HashSet<String>();
	    	Set<T> valueSet = new HashSet<T>();
			ShardingValue shardingValue = null;
	        for (ShardingValue<?> each : shardingValues) {
	            if (each.getColumnName().equals(shardingKey)) {
	                shardingValue = (ShardingValue) each;
	                break;
	            }
	        }
	        if (null == shardingValue) {
	            return resultSet;
	        }
	        switch (shardingValue.getType()) {
	            case SINGLE:
	                valueSet.add((T)shardingValue.getValue());
	                break;
	            case LIST:
	                valueSet.addAll(shardingValue.getValues());
	                break;
	            case RANGE:
	            	T temp=(T)shardingValue.getValueRange().lowerEndpoint();
	            	if(temp instanceof Integer){
	            		valueSet.add((T)shardingValue.getValueRange().lowerEndpoint());
		            	valueSet.add((T)shardingValue.getValueRange().upperEndpoint());
	            	}
//	            	if(temp instanceof Integer){
//	            		for (Integer i = (Integer)shardingValue.getValueRange().lowerEndpoint(); i <= (Integer)shardingValue.getValueRange().upperEndpoint(); i++) {
//		                    valueSet.add((T)i);
//		                }
//	            	}
	            	if(temp instanceof Date){
	            		valueSet.add((T)shardingValue.getValueRange().lowerEndpoint());
		            	valueSet.add((T)shardingValue.getValueRange().upperEndpoint());
	            	}
	                break;
	            default:
	                throw new UnsupportedOperationException();
	        }
	        for(T value:valueSet){
	        	if(value instanceof Integer){
	        		resultSet.add(value.toString());
	        	}
	        	if(value instanceof Date){
	        		resultSet.add(TableNameUtil.getTableNameSuffix((Date)value));
	        	}
	        }
	        return resultSet;
	    }
	    
}
