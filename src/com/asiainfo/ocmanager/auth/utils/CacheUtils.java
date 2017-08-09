package com.asiainfo.ocmanager.auth.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Created by gq on 17/7/31.
 */
public class CacheUtils {
    static CacheManager manager = CacheManager.create(CacheUtils.class.getResourceAsStream("/../conf/ehcache.xml"));
    static Cache cache = manager.getCache("AuthenticationTokenCache");
    public static void addToken(String username, String token) {
        Element element = new Element(username, token);
        cache.put(element);
    }
    public static boolean isTokenInCache(String token) {
        return cache.isValueInCache(token);
    }

    public static void removeToken(String username) {
        cache.remove(username);
    }
}
