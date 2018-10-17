package com.victor;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;

public class SecKill_redis {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SecKill_redis.class);

	public static void main(String[] args) {

		Jedis jedis = new Jedis("192.168.67.143", 6379);

		System.out.println(jedis.ping());

		jedis.close();

	}

	public static boolean doSecKill(String uid, String prodid) throws IOException {

		// Jedis jedis =new Jedis("192.168.67.143",6379);
		JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();

		Jedis jedis = jedisPool.getResource();

		String qtKey = "sk:" + prodid + ":qt";

		String userKey = "sk:" + prodid + ":usr";

		// 判断是否已经抢到
		if (jedis.sismember(userKey, uid)) {
			// System.err.println( uid +"已抢到过！！！！！！！！！！");
			jedis.close();
			return false;
		}

		jedis.watch(qtKey);

		// 判断库存是否为空
		String qt = jedis.get(qtKey); 
		int num = Integer.parseInt(qt);
		if (num <= 0) {
			// System.err.println( uid +"已抢空！！！");
			jedis.close();
			return false;
		}
		
		Transaction transaction = jedis.multi();
		
		// 减库
		transaction.decr(qtKey);
		
		// 加人
		transaction.sadd(userKey, uid);

		List<Object> list = transaction.exec();
		
		if (list == null || list.size() == 0) {
			// System.err.println( uid +"抢购失败！！");
		} else {
			// System.out.println( uid +"抢购成功！！");
		}
		
		// 打印连接池， 连接激活   等待连接
		System.out.println("active:" + jedisPool.getNumActive() + "|wait:" + jedisPool.getNumWaiters());

		jedis.close();
		return true;
	}

}
