package org.ms.service;

import java.util.Optional;
import java.util.Set;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Component("jedisServices")
public class JedisServices {
	
	private static Logger log = Logger.getLogger(JedisServices.class);
	
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
	
	public Set<String> keys() {
		try (Jedis jedis = getPool().getResource()) {
			return jedis.keys("*");
		}
	}
	
	public Optional<String> getValue(String key) {
		String returnValue = null;
		try (Jedis jedis = getPool().getResource()) {
			returnValue = jedis.get(key);
		} catch(JedisConnectionException e) {
			log.error("Error while getting connection", e);
			returnValue = e.getCause().getLocalizedMessage();
		} catch(Exception e) {
			log.error("Error while getValue", e);
			returnValue = e.getCause().getLocalizedMessage();
		}
		return Optional.ofNullable(returnValue);
	}
	
	public String setValue(String key, String value) {
		try (Jedis jedis = getPool().getResource()) {
			return jedis.set(key, value);
		}		
	}
	
	public Long deleteKeys(String...keys) {
		try (Jedis jedis = getPool().getResource()) {
			return jedis.del(keys);
		}		
	}
	
	@PreDestroy
	public void preDestroy() {
		pool.destroy();
	}

}
