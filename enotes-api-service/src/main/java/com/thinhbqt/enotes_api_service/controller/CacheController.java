package com.thinhbqt.enotes_api_service.controller;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.thinhbqt.enotes_api_service.endpoint.CacheEndpoint;
import com.thinhbqt.enotes_api_service.service.CacheManagerService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CacheController implements CacheEndpoint {
    private CacheManagerService cacheService;

    @Override
    public ResponseEntity<?> getAllCache() {
        Collection<String> cache = cacheService.getCache();
        return CommonUtil.createBuildResponse(cache, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getCache(String cache_name) {
        Cache cacheName = cacheService.getCacheName(cache_name);
        return CommonUtil.createBuildResponse(cacheName, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeAllCache() {
        cacheService.removeAllCache();
        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Remove all cache");
    }
}
