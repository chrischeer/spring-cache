package com.example.cache.controller;

import com.example.cache.model.User;
import com.example.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chris
 * @description
 * @date 2020/9/17 15:09
 */
@RestController
@RequestMapping("users")
public class CacheController {

    @Autowired
    private CacheService cacheService;
    @Autowired
    CacheManager cacheManager;

    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public User cache(@PathVariable("id")int id){

        System.out.println(cacheManager.getCache("user"));

        User user = cacheService.getUserById(id);
        return user;
    }


    @RequestMapping(value = "{id}",method = RequestMethod.PATCH)
    public User cacheUpdate(@PathVariable("id")int id){
        User user = cacheService.update(id);
        return user;
    }

    @RequestMapping(value = "{id}",method = RequestMethod.DELETE)
    public int cacheDelete(@PathVariable("id")int id){
        return cacheService.delete(id);
    }
}
