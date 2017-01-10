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

package com.dangdang.ddframe.rdb.sharding.id.generator.self;

import com.dangdang.ddframe.rdb.sharding.id.generator.self.fixture.FixClock;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.time.AbstractClock;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CommonSelfIdGeneratorTest {
    
    @Before
    public void init() {
        CommonSelfIdGenerator.setClock(AbstractClock.systemClock());
    }
    
    @Test
    public void generateId() throws Exception {
        int threadNumber = Runtime.getRuntime().availableProcessors() << 1;
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
    
        final int taskNumber = threadNumber << 2;
        final CommonSelfIdGenerator idGenerator = new CommonSelfIdGenerator();
        Set<Long> hashSet = new HashSet<>();
        for (int i = 0; i < taskNumber; i++) {
            hashSet.add(executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return (Long) idGenerator.generateId();
                }
            }).get());
        }
        assertThat(hashSet.size(), is(taskNumber));
    }
    
    @Test
    public void testMaxSequence() throws Exception {
        assertThat(maxId((1 << 12) - 1), is((1L << 12L) - 2));
        assertThat(maxId(1 << 12), is((1L << 12L) - 1));
        assertThat(maxId((1 << 12) + 1), is((1L << 12L) - 1));
        assertThat(maxId(1 << 13), is((1L << 12L) - 1));
        assertThat(maxId((1 << 13) + 1), is((1L << 12L) - 1));
    }
    
    private long maxId(final int maxSequence) {
        CommonSelfIdGenerator idGenerator = new CommonSelfIdGenerator();
        CommonSelfIdGenerator.setClock(new FixClock(maxSequence));
        long id = 0;
        long preId = 0;
        while (id < (1L << 12)) {
            preId = id;
            id = (Long) idGenerator.generateId();
        }
        return preId;
    }
}
