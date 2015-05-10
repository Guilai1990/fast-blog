package org.wkh.fastblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.domain.Post;

import javax.annotation.Resource;

@Service
public class RedisService {
    @Bean
    StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Resource(name="redisTemplate")
    private ListOperations<String, Post> listOps;

    public void addPostToList(Post post) {
        listOps.leftPush("posts", post);
    }
}
