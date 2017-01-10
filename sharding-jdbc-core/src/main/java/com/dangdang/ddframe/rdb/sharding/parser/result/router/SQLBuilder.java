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

package com.dangdang.ddframe.rdb.sharding.parser.result.router;

import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * SQL构建器.
 * 
 * @author gaohongtao
 */
public class SQLBuilder implements Appendable {
    
    private final List<SQLBuilder> derivedSQLBuilders = new ArrayList<>();
    
    private final List<Object> segments;
    
    private final Map<String, StringToken> tokenMap;
    
    private final List<StringToken> newTokenList = new LinkedList<>();
    
    private StringBuilder currentSegment;
    
    @Getter
    private boolean changed;
    
    @Getter(AccessLevel.PRIVATE)
    private boolean removeDerivedSQLToken;
    
    private boolean hasExistedDerivedSQLToken;
    
    public SQLBuilder() {
        segments = new LinkedList<>();
        tokenMap = new HashMap<>();
        currentSegment = new StringBuilder();
        segments.add(currentSegment);
    }
    
    private SQLBuilder(final SQLBuilder originBuilder) {
        segments = new LinkedList<>(originBuilder.segments);
        tokenMap = new HashMap<>(originBuilder.tokenMap);
        changeState();
    }
    
    /**
     * 增加占位符.
     * 
     * @param token 占位符
     */
    public void appendToken(final String token) {
        appendToken(token, token);
    }
    
    /**
     * 增加占位符.
     * 
     * @param label 占位符标签
     * @param token 占位符
     */
    public void appendToken(final String label, final String token) {
        StringToken stringToken;
        if (tokenMap.containsKey(label)) {
            stringToken = tokenMap.get(label);
        } else {
            stringToken = new StringToken();
            stringToken.label = label;
            stringToken.value = token;
            tokenMap.put(label, stringToken);
        }
        stringToken.indices.add(segments.size());
        segments.add(stringToken);
        currentSegment = new StringBuilder();
        segments.add(currentSegment);
    }

    /**
     * 用实际的值替代占位符.
     * 
     * @param label 占位符
     * @param token 实际的值
     */
    public void buildSQL(final String label, final String token) {
        buildSQL(label, token, false);
    }
    
    /**
     * 用实际的值替代占位符,并可以标记该SQL是否为派生SQL.
     * 
     * @param label 占位符
     * @param token 实际的值
     * @param isDerived 是否是派生的SQL
     */
    public void buildSQL(final String label, final String token, final boolean isDerived) {
        if (!tokenMap.containsKey(label)) {
            return;
        }
        if (isDerived) {
            hasExistedDerivedSQLToken = true;
        }
        StringToken labelSQL = tokenMap.get(label);
        labelSQL.isDerived = isDerived;
        labelSQL.value = token;
        changeState();
    }
    
    /**
     * 记录新的Token.
     * 
     * @param label 占位符
     * @param token 实际的值
     */
    public void recordNewToken(final String label, final String token) {
        StringToken newToken = new StringToken();
        newToken.label = label;
        newToken.value = token;
        newTokenList.add(newToken);
    }
    
    /**
     * 用实际的值替代占位符,并返回新的构建器.
     * 
     * @return 新SQL构建器
     */
    public SQLBuilder buildSQLWithNewToken() {
        if (!newTokenList.isEmpty()) {
            changeState();
        }
        SQLBuilder result = new SQLBuilder(this);
        for (StringToken each : newTokenList) {
            StringToken origin = result.tokenMap.get(each.label);
            each.indices.addAll(origin.indices);
            result.tokenMap.put(each.label, each);
            for (Integer index : origin.indices) {
                result.segments.set(index, each);
            }
        }
        derivedSQLBuilders.add(result);
        newTokenList.clear();
        return result;
    }
    
    /**
     * 生成SQL语句.
     * 
     * @return SQL语句
     */
    public String toSQL() {
        clearState();
        StringBuilder result = new StringBuilder();
        for (Object each : segments) {
            result.append(each.toString());
        }
        return result.toString();
    }
    
    @Override
    public Appendable append(final CharSequence sql) throws IOException {
        currentSegment.append(sql);
        changeState();
        return this;
    }
    
    @Override
    public Appendable append(final CharSequence sql, final int start, final int end) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Appendable append(final char c) throws IOException {
        currentSegment.append(c);
        changeState();
        return this;
    }

    private void changeState() {
        changed = true;
        for (SQLBuilder each : derivedSQLBuilders) {
            each.changeState();
        }
    }
    
    private void clearState() {
        changed = false;
    }
    
    /**
     * 移除衍生的SQL片段.
     */
    public void removeDerivedSQL() {
        if (hasExistedDerivedSQLToken) {
            removeDerivedSQLToken = true;
            changeState();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Object each : segments) {
            if (each instanceof StringToken) {
                result.append(((StringToken) each).toToken());
            } else {
                result.append(each.toString());
            }
        }
        return result.toString();
    }
    
    private class StringToken {
        
        private String label;
        
        private String value;

        private boolean isDerived;
        
        private final List<Integer> indices = new LinkedList<>();
        
        String toToken() {
            if (isEmptyValueOutput()) {
                return "";
            }
            Joiner joiner = Joiner.on("");
            return label.equals(value) ? joiner.join("[Token(", value, ")]") : joiner.join("[", label, "(", value, ")]");
        }
        
        private boolean isEmptyValueOutput() {
            return null == value || isDerived && isRemoveDerivedSQLToken();
        }
    
        @Override
        public String toString() {
            return isEmptyValueOutput() ? "" : value;
        }
    }
}
