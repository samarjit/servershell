package com.ycs.fe.cache;

import net.sf.ehcache.Cache;

public interface EnableCaching {
	public void putInCache( Cache cache1);
	public Cache getFromCache(String key);
	public void refreshCache();
}
