package org.wkh.fastblog.redis;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisService implements InitializingBean, DisposableBean {
    private JedisPool pool;
    private Jedis jedis;

    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public String get(String key) {
        return jedis.get(key);
    }

    @Override
    public void destroy() throws Exception {
        jedis.close();
        pool.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
        jedis = pool.getResource();
    }
}
