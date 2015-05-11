package org.wkh.fastblog.services;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.domain.Post;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;


@Service
public class RedisService implements InitializingBean, DisposableBean {
    private JedisPool pool;
    private Jedis jedis;

    @Autowired
    private SerializationService serializationService;

    public void addPostToList(Post post) throws Exception {
        String key = PostCreationService.postListKey;

        byte[] bytes = serializationService.serializePost(post);

        double score = (double) post.getCreatedAt().getTime();

        Map<byte[], Double> scoreMembers = new HashMap<byte[], Double>(1);
        scoreMembers.put(bytes, score);

        jedis.zadd(key.getBytes(), scoreMembers);
    }

    public List<Post> getPosts() throws Exception {
        String key = PostCreationService.postListKey;
        Set<byte[]> postBytesSet = jedis.zrange(key.getBytes(), 0, -1);

        List<Post> posts = new ArrayList<Post>();

        for(byte[] postBytes : postBytesSet) {
            Post post = serializationService.deserializePost(postBytes);

            posts.add(post);
        }

        return posts;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
        jedis = pool.getResource();
    }

    @Override
    public void destroy() throws Exception {
        pool.close();
    }
}
