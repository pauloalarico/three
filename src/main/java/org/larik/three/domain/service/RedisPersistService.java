package org.larik.three.domain.service;

import lombok.RequiredArgsConstructor;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisPersistService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void persist(ComparisonTransactionResult c) {
        String key = c.rawTransaction().getTransactionId() + "client" + c.rawTransaction().getClient().getClientId();
        redisTemplate.opsForValue().set(key, c, 24, TimeUnit.HOURS);
    }

}
