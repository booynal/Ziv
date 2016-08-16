package test.ehcache.utils;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 需要在classpath下放入配置文件: ehcache.xml，不放的会使用缺省配置
 *
 * @author ziv
 *
 */
public class EhCacheUtil {

	static CacheManager manager = CacheManager.create();

	public static void setValue(String cacheName, String key, String value) {
		setObject(cacheName, key, value, 60 * 2);
	}

	public static void setValue(String cacheName, String key, String value, Integer timeToLiveSeconds) {
		setObject(cacheName, key, value, timeToLiveSeconds);
	}

	public static void setObject(String cacheName, String key, Object value) {
		setObject(cacheName, key, value, 60 * 5);
	}

	private static void setObject(String cacheName, String key, Object value, int timeToLiveSeconds) {
		Cache cache = getCacheByName(cacheName);
		Element element = new Element(key, value, 0, timeToLiveSeconds);
		synchronized (cache) {
			cache.put(element);
		}
	}

	private static Cache getCacheByName(String cacheName) {
		Cache cache = manager.getCache(cacheName);
		if (cache == null) {
			synchronized (manager) {
				cache = manager.getCache(cacheName);
				if (cache == null) {
					cache = new Cache(cacheName, 1, true, false, 0, 0); // Cache上设置为永不过期
					manager.addCache(cache);
				}
			}
		}
		return cache;
	}

	public static String getValue(String cacheName, String key) {
		return (String) getObject(cacheName, key);
	}

	public static Object getObject(String cacheName, String key) {
		Object value = null;
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			Element element = cache.get(key);
			if (element != null) {
				value = element.getObjectValue();
			}

		}
		return value;
	}

	public static void remove(String cacheName, String key) {
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			synchronized (cache) {
				cache.remove(key);
			}
		}
	}

	public static List<String> getKeys(String cacheName) {
		Cache cache = manager.getCache(cacheName);
		return cache != null ? cache.getKeys() : null;
	}

}
