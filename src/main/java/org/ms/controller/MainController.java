/**
 * 
 */
package org.ms.controller;

import java.util.Set;

import org.ms.service.JedisServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	@Autowired
	private JedisServices jedisServices;
	
	@GetMapping("/")
	public String index() {
		return "Redis Web Services";
	}
	
	@GetMapping("/info")
	public String info() {
		return jedisServices.getInfo();
	}
	
	@GetMapping("/keys")
	public Set<String> keys() {
		return jedisServices.keys();
	}
	
	@GetMapping("/get/{key}")
	public String getValue(@PathVariable String key) {
		return jedisServices.getValue(key).orElse(key + " not found");
	}
	
	@PostMapping("/set/{key}")
	public String setValue(@PathVariable String key, @RequestParam String value) {
		return jedisServices.setValue(key, value);
	}
	
	@DeleteMapping("/delete/{keys}")
	public Long deleteKeys(@PathVariable String...keys) {
		return jedisServices.deleteKeys(keys);
	}

}
