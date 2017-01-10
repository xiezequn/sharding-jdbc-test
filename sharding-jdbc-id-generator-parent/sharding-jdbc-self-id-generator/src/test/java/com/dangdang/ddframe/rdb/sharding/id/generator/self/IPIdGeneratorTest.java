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

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest(IPIdGenerator.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IPIdGeneratorTest {
    
    private static InetAddress address;

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @BeforeClass
    public static void init() throws UnknownHostException {
        String ipv4 = "192.168.1.108";
        byte[] ipv4Byte = new byte[4];
        String[] ipv4StingArray = ipv4.split("\\.");
        for (int i = 0; i < 4; i++) {
            ipv4Byte[i] = (byte) Integer.valueOf(ipv4StingArray[i]).intValue();
        }
        address = InetAddress.getByAddress("dangdang-db-sharding-dev-233", ipv4Byte);
        PowerMockito.mockStatic(InetAddress.class);
        PowerMockito.when(InetAddress.getLocalHost()).thenReturn(address);
        IPIdGenerator.initWorkerId();
    }

    @Test
    public void testIP() throws UnknownHostException {
        PowerMockito.mockStatic(InetAddress.class);
        PowerMockito.when(InetAddress.getLocalHost()).thenReturn(address);
        IPIdGenerator.initWorkerId();
        assertThat(CommonSelfIdGenerator.getWorkerId(), is(364L));
    }

    @Test
    public void testUnknownHost() throws UnknownHostException {
        PowerMockito.mockStatic(InetAddress.class);
        PowerMockito.when(InetAddress.getLocalHost()).thenThrow(new UnknownHostException());
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Cannot get LocalHost InetAddress, please check your network!");
        IPIdGenerator.initWorkerId();
    }

    @Test
    public void generateId() throws Exception {
        PowerMockito.mockStatic(InetAddress.class);
        PowerMockito.when(InetAddress.getLocalHost()).thenReturn(address);
        IPIdGenerator.initWorkerId();
        int threadNumber = Runtime.getRuntime().availableProcessors() << 1;
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

        final int taskNumber = threadNumber << 2;
        final IPIdGenerator idGenerator = new IPIdGenerator();
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
}
