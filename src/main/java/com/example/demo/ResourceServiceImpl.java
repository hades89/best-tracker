package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Override
    public String getName(String name) {
        return name;
    }
}
