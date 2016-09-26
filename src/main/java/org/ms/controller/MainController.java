/**
 * 
 */
package org.ms.controller;

import org.ms.service.JedisServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	@Autowired
	private JedisServices jedisServices;
	
	@RequestMapping("/")
	public String index() {
		return "Redis Web Services";
	}
	
	@RequestMapping(produces=MediaType.TEXT_PLAIN_VALUE, value="/info")
	public String info() {
		return jedisServices.getInfo();
	}
	
	@RequestMapping(produces=MediaType.TEXT_PLAIN_VALUE, value="/get/{key}")
	public String getValue(@PathVariable(value="key") String key) {
		return jedisServices.getValue(key).orElse(key + " not found");
	}
	
	@RequestMapping(produces=MediaType.TEXT_PLAIN_VALUE, value="/set/{key}")
	public String setValue(@PathVariable(value="key") String key, @RequestBody String value) {
		return jedisServices.setValue(key, value);
	}

}
