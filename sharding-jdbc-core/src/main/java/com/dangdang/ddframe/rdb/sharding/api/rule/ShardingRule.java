/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.rdb.sharding.api.rule;

import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.NoneDatabaseShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.NoneTableShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.exception.ShardingJdbcException;
import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * 分库分表规则配置对象.
 * 
 * @author zhangliang
 */
@Getter
public final class ShardingRule {
    
    private final DataSourceRule dataSourceRule;
    
    private final Collection<TableRule> tableRules;
    
    private final Collection<BindingTableRule> bindingTableRules;
    
    private final DatabaseShardingStrategy databaseShardingStrategy;
    
    private final TableShardingStrategy tableShardingStrategy;
    
    /**
     * 全属性构造器.
     * 
     * <p>用于Spring非命名空间的配置.</p>
     * 
     * <p>未来将改为private权限, 不在对外公开, 不建议使用非Spring命名空间的配置.</p>
     * 
     * @deprecated 未来将改为private权限, 不在对外公开, 不建议使用非Spring命名空间的配置.
     */
    @Deprecated
    public ShardingRule(
            final DataSourceRule dataSourceRule, final Collection<TableRule> tableRules, final Collection<BindingTableRule> bindingTableRules, 
            final DatabaseShardingStrategy databaseShardingStrategy, final TableShardingStrategy tableShardingStrategy) {
        Preconditions.checkNotNull(dataSourceRule);
        Preconditions.checkNotNull(tableRules);
        this.dataSourceRule = dataSourceRule;
        this.tableRules = tableRules;
        this.bindingTableRules = null == bindingTableRules ? Collections.<BindingTableRule>emptyList() : bindingTableRules;
        this.databaseShardingStrategy = null == databaseShardingStrategy ? new DatabaseShardingStrategy(
                Collections.<String>emptyList(), new NoneDatabaseShardingAlgorithm()) : databaseShardingStrategy;
        this.tableShardingStrategy = null == tableShardingStrategy ? new TableShardingStrategy(
                Collections.<String>emptyList(), new NoneTableShardingAlgorithm()) : tableShardingStrategy;
    }
    
    /**
     * 获取表规则配置对象构建器.
     *
     * @return 分片规则配置对象构建器
     */
    public static ShardingRuleBuilder builder() {
        return new ShardingRuleBuilder();
    }
    
    /**
     * 试着根据逻辑表名称查找分片规则.
     * 
     * @param logicTableName 逻辑表名称
     * @return 该逻辑表的分片规则
     */
    public Optional<TableRule> tryFindTableRule(final String logicTableName) {
        for (TableRule each : tableRules) {
            if (each.getLogicTable().equalsIgnoreCase(logicTableName)) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }
    
    /**
     * 根据逻辑表名找到指定分片规则.
     * 
     * @param logicTableName 逻辑表名称
     * @return 该逻辑表的分片规则
     */
    public TableRule findTableRule(final String logicTableName) {
        Optional<TableRule> tableRuleOptional = tryFindTableRule(logicTableName);
        if (tableRuleOptional.isPresent()) {
            return tableRuleOptional.get();
        }
        throw new ShardingJdbcException(String.format("%s does not exist in ShardingRule", logicTableName));
    }
    
    /**
     * 获取数据库分片策略.
     * 
     * <p>
     * 根据表规则配置对象获取分片策略, 如果获取不到则获取默认分片策略.
     * </p>
     * 
     * @param tableRule 表规则配置对象
     * @return 数据库分片策略
     */
    public DatabaseShardingStrategy getDatabaseShardingStrategy(final TableRule tableRule) {
        DatabaseShardingStrategy result = tableRule.getDatabaseShardingStrategy();
        if (null == result) {
            result = databaseShardingStrategy;
        }
        Preconditions.checkNotNull(result, "no database sharding strategy");
        return result;
    }
    
    /**
     * 获取表分片策略.
     * 
     * <p>
     * 根据表规则配置对象获取分片策略, 如果获取不到则获取默认分片策略.
     * </p>
     * 
     * @param tableRule 表规则配置对象
     * @return 表分片策略
     */
    public TableShardingStrategy getTableShardingStrategy(final TableRule tableRule) {
        TableShardingStrategy result = tableRule.getTableShardingStrategy();
        if (null == result) {
            result = tableShardingStrategy;
        }
        Preconditions.checkNotNull(result, "no table sharding strategy");
        return result;
    }
    
    /**
     * 判断逻辑表名称集合是否全部属于Binding表.
     *
     * @param logicTables 逻辑表名称集合
     * @return 是否全部属于Binding表
     */
    public boolean isAllBindingTables(final Collection<String> logicTables) {
        Collection<String> bindingTables = filterAllBindingTables(logicTables);
        return !bindingTables.isEmpty() && bindingTables.containsAll(logicTables);
    }
    
    /**
     * 过滤出所有的Binding表名称.
     * 
     * @param logicTables 逻辑表名称集合
     * @return 所有的Binding表名称集合
     */
    public Collection<String> filterAllBindingTables(final Collection<String> logicTables) {
        if (logicTables.isEmpty()) {
            return Collections.emptyList();
        }
        Optional<BindingTableRule> bindingTableRule = findBindingTableRule(logicTables);
        if (!bindingTableRule.isPresent()) {
            return Collections.emptyList();
        }
        Collection<String> result = new ArrayList<>(bindingTableRule.get().getAllLogicTables());
        result.retainAll(logicTables);
        return result;
    }
    
    private Optional<BindingTableRule> findBindingTableRule(final Collection<String> logicTables) {
        for (String each : logicTables) {
            Optional<BindingTableRule> result = findBindingTableRule(each);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.absent();
    }
    
    /**
     * 根据逻辑表名称获取binding表配置的逻辑表名称集合.
     *
     * @param logicTable 逻辑表名称
     * @return binding表配置的逻辑表名称集合
     */
    public Optional<BindingTableRule> findBindingTableRule(final String logicTable) {
        for (BindingTableRule each : bindingTableRules) {
            if (each.hasLogicTable(logicTable)) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }
    
    /**
     * 获取所有的分片列名.
     *
     * @param tableName 表名
     * @return 分片列名集合
     */
    public Collection<String> getAllShardingColumns(final String tableName) {
        Set<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        result.addAll(databaseShardingStrategy.getShardingColumns());
        result.addAll(tableShardingStrategy.getShardingColumns());
        for (TableRule each : tableRules) {
            if (!each.getLogicTable().equalsIgnoreCase(tableName)) {
                continue;
            }
            if (null != each.getDatabaseShardingStrategy()) {
                result.addAll(each.getDatabaseShardingStrategy().getShardingColumns());
            }
            if (null != each.getTableShardingStrategy()) {
                result.addAll(each.getTableShardingStrategy().getShardingColumns());
            }
        }
        return result;
    }
    
    /**
     * 获取所有需要自增的列名.
     * 
     * @param tableName 表名
     * @return 自增列
     */
    public Collection<String> getAutoIncrementColumns(final String tableName) {
        for (TableRule each : tableRules) {
            if (!each.getLogicTable().equalsIgnoreCase(tableName)) {
                continue;
            }
            return Sets.newLinkedHashSet(each.getAutoIncrementColumnMap().keySet());
        }
        return Collections.emptySet();
    }
    
    /**
     * 分片规则配置对象构建器.
     */
    @RequiredArgsConstructor
    public static class ShardingRuleBuilder {
        
        private DataSourceRule dataSourceRule;
        
        private Collection<TableRule> tableRules;
        
        private Collection<BindingTableRule> bindingTableRules;
        
        private DatabaseShardingStrategy databaseShardingStrategy;
        
        private TableShardingStrategy tableShardingStrategy;
        
        private Class<? extends IdGenerator> idGeneratorClass;
        
        /**
         * 构建数据源配置规则.
         *
         * @param dataSourceRule 数据源配置规则
         * @return 分片规则配置对象构建器
         */
        public ShardingRuleBuilder dataSourceRule(final DataSourceRule dataSourceRule) {
            this.dataSourceRule = dataSourceRule;
            return this;
        }
        
        /**
         * 构建表配置规则.
         *
         * @param tableRules 表配置规则
         * @return 分片规则配置对象构建器
         */
        public ShardingRuleBuilder tableRules(final Collection<TableRule> tableRules) {
            this.tableRules = tableRules;
            return this;
        }
        
        /**
         * 构建绑定表配置规则.
         *
         * @param bindingTableRules 绑定表配置规则
         * @return 分片规则配置对象构建器
         */
        public ShardingRuleBuilder bindingTableRules(final Collection<BindingTableRule> bindingTableRules) {
            this.bindingTableRules = bindingTableRules;
            return this;
        }
        
        /**
         * 构建默认分库策略.
         *
         * @param databaseShardingStrategy 默认分库策略
         * @return 分片规则配置对象构建器
         */
        public ShardingRuleBuilder databaseShardingStrategy(final DatabaseShardingStrategy databaseShardingStrategy) {
            this.databaseShardingStrategy = databaseShardingStrategy;
            return this;
        }
        
        /**
         * 构建数据源分片规则.
         *
         * @param tableShardingStrategy 默认分表策略
         * @return 分片规则配置对象构建器
         */
        public ShardingRuleBuilder tableShardingStrategy(final TableShardingStrategy tableShardingStrategy) {
            this.tableShardingStrategy = tableShardingStrategy;
            return this;
        }
    
        /**
         * 构建默认id生成器.
         * 
         * @param idGeneratorClass 默认的Id生成器
         * @return 分片规则配置对象构建器
         */
        public ShardingRuleBuilder idGenerator(final Class<? extends IdGenerator> idGeneratorClass) {
            this.idGeneratorClass = idGeneratorClass;
            return this;
        }
        
        /**
         * 构建分片规则配置对象.
         *
         * @return 分片规则配置对象
         */
        public ShardingRule build() {
            ShardingRule result = new ShardingRule(dataSourceRule, tableRules, bindingTableRules, databaseShardingStrategy, tableShardingStrategy);
            if (null == idGeneratorClass) {
                return result;
            }
            for (TableRule each : tableRules) {
                each.fillIdGenerator(idGeneratorClass);
            }
            return result;
        }
    }
}
