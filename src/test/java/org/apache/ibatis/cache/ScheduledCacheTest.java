/*
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.ibatis.cache.decorators.LoggingCache;
import org.apache.ibatis.cache.decorators.ScheduledCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.Test;

class ScheduledCacheTest {

  /**
   * 测试定时清空缓存功能
   * 设置定时间隔为2500毫秒
   * 缓存存储数据后,等待5s,缓存被清空
   * @author qiaok
   * @date 2021-09-02
   * @throws Exception
   */
  @Test
  void shouldDemonstrateHowAllObjectsAreFlushedAfterBasedOnTime() throws Exception {
    Cache cache = new PerpetualCache("DefaultCache");
    cache = new ScheduledCache(cache);
    ((ScheduledCache) cache).setClearInterval(2500);
    cache = new LoggingCache(cache);
    for (int i = 0; i < 100; i++) {
      cache.putObject(i, i);
      assertEquals(i, cache.getObject(i));
    }
    Thread.sleep(5000);
    assertEquals(0, cache.getSize());
  }

  /**
   * 测试定时清空缓存功能
   * 设置定时间隔为60000毫秒
   * 定时情况下，put、remove未到定时时间，可正常使用
   * @author qiaok
   * @date 2021-09-02
   * @throws Exception
   */
  @Test
  void shouldRemoveItemOnDemand() {
    Cache cache = new PerpetualCache("DefaultCache");
    cache = new ScheduledCache(cache);
    ((ScheduledCache) cache).setClearInterval(60000);
    cache = new LoggingCache(cache);
    cache.putObject(0, 0);
    assertNotNull(cache.getObject(0));
    cache.removeObject(0);
    assertNull(cache.getObject(0));
  }

  /**
   * 测试定时清空缓存功能
   * 设置定时间隔为60000毫秒
   * 定时情况下，clear未到定时时间，可正常使用
   * @author qiaok
   * @date 2021-09-02
   * @throws Exception
   */
  @Test
  void shouldFlushAllItemsOnDemand() {
    Cache cache = new PerpetualCache("DefaultCache");
    cache = new ScheduledCache(cache);
    ((ScheduledCache) cache).setClearInterval(60000);
    cache = new LoggingCache(cache);
    for (int i = 0; i < 5; i++) {
      cache.putObject(i, i);
    }
    assertNotNull(cache.getObject(0));
    assertNotNull(cache.getObject(4));
    cache.clear();
    assertNull(cache.getObject(0));
    assertNull(cache.getObject(4));
  }

}
