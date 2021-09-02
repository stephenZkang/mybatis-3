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

import org.apache.ibatis.cache.decorators.FifoCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.Test;

class FifoCacheTest {

  /**
   * 测试缓存先进先出
   * 1. 放入0-4，
   * 2. 获取key=0,
   * 3. 放入一个5
   * 4. 再获取0则为空
   * 5. 再获取长度为5
   * @author qiaok
   * @date 2021-09-02
   */
  @Test
  void shouldRemoveFirstItemInBeyondFiveEntries() {
    FifoCache cache = new FifoCache(new PerpetualCache("default"));
    cache.setSize(5);
    for (int i = 0; i < 5; i++) {
      cache.putObject(i, i);
    }
    assertEquals(0, cache.getObject(0));
    cache.putObject(5, 5);
    assertNull(cache.getObject(0));
    assertEquals(5, cache.getSize());
  }

  /**
   * 测试从缓存中删除,再获取为空
   * @author qiaok
   * @date 2021-09-02
   */
  @Test
  void shouldRemoveItemOnDemand() {
    FifoCache cache = new FifoCache(new PerpetualCache("default"));
    cache.putObject(0, 0);
    assertNotNull(cache.getObject(0));
    cache.removeObject(0);
    assertNull(cache.getObject(0));
  }

  /**
   * 测试缓存的清空作用
   * 1. 放入0-4，
   * 2. 获取key=0不为空
   * 3. 获取key=4不为空
   * 4. 清空缓存
   * 5. 获取key=0为空
   * 6. 获取key=4为空
   *
   * @author qiaok
   * @date 2021-09-02
   */
  @Test
  void shouldFlushAllItemsOnDemand() {
    FifoCache cache = new FifoCache(new PerpetualCache("default"));
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
