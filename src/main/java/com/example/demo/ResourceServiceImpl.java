package com.example.demo;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {

    private String name = "";
    @Override
    @Cacheable("names")
    public String getName() {
        System.out.println("getting name without cache");
        return this.name;
    }

    @Override
    @CacheEvict("names")
    public void updateName(String name){
        this.name = name;
    }
}
