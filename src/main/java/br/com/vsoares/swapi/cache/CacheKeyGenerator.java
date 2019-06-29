package br.com.vsoares.swapi.cache;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.cache.interceptor.KeyGenerator;

public class CacheKeyGenerator implements KeyGenerator {

	private String targetPackage;  
	
	public CacheKeyGenerator(String targetPackage) {
		this.targetPackage = targetPackage;
	}
	
	@Override
	public Object generate(Object target, Method method, Object... params) {
		final StringBuilder sb = new StringBuilder();

		Class<?> c = null;
		
		if(AopUtils.isAopProxy(target)) {
			c = Arrays.stream(AopProxyUtils.proxiedUserInterfaces(target))
					.filter(cl -> cl.getName().contains(targetPackage))
					.findFirst().orElse(target.getClass());			
		} else {
			c = target.getClass();
		}
		
		sb.append(c.getName()).append(".")
			.append(method.getName()).append("(");

		if (params != null && params.length > 0) {
			final String p = Arrays.asList(params).toString();
			sb.append(p.substring(1, p.length() - 1));
		}

		sb.append(")");

		return sb.toString();
	}

}
