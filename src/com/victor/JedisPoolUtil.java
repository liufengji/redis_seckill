package com.victor;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {
	
	private static volatile JedisPool jedisPool = null;

	private JedisPoolUtil() {
	}

	//Jedis 连接池
	public static JedisPool getJedisPoolInstance() {
		
		if (null == jedisPool) {
			synchronized (JedisPoolUtil.class) {
				if (null == jedisPool) {
					JedisPoolConfig poolConfig = new JedisPoolConfig();
					//最大连接数
					poolConfig.setMaxTotal(200);
					//最大闲置数
					poolConfig.setMaxIdle(32);
					// 连接满了， 等多长时间
					poolConfig.setMaxWaitMillis(100 * 1000);
					// 连接已经满了，等不等
					poolConfig.setBlockWhenExhausted(true);
					//借连接，会还的， 会检测一下 连接是否有效
					poolConfig.setTestOnBorrow(true);

					jedisPool = new JedisPool(poolConfig, "192.168.67.143", 6379, 60000);

				}
			}
		}
		return jedisPool;
	}

	//连接池，还连接方法，jedis.close(); 在连接池中也是还连接的意思
	public static void release(JedisPool jedisPool, Jedis jedis) {
		if (null != jedis) {
			jedisPool.returnResource(jedis);
		}
	}

}
