package com.example.cache.service;

import com.example.cache.model.User;

/**
 * @author Chris
 * @description
 * @date 2020/9/17 15:09
 */
public interface CacheService {

    User getUserById(Integer id);

    User update(Integer id);

    int delete(Integer id);
}
