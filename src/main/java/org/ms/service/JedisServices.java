package org.ms.service;

import java.util.Optional;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component("jedisServices")
public class JedisServices {
	
	@Value("${redis-host}")
    private String redisHost;	
	@Value("${redis-port}")
    private int redisPort;
	private JedisPool pool;
	
	private JedisPool getPool() {
		if (pool == null) {
			pool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort);
		}
		return pool;
	}
	
	public String getInfo() {
		try (Jedis jedis = getPool().getResource()) {
			return jedis.info();
		}
	}
	
	public Optional<String> getValue(String key) {
		try (Jedis jedis = getPool().getResource()) {
			return Optional.ofNullable(jedis.get(key));
		}
	}
	
	public String setValue(String key, String value) {
		try (Jedis jedis = getPool().getResource()) {
			return jedis.set(key, value);
		}		
	}
	
	@PreDestroy
	public void preDestroy() {
		pool.destroy();
	}

}
