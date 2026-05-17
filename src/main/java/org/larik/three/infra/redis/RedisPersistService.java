package org.larik.three.infra.redis;

import lombok.RequiredArgsConstructor;
import org.larik.three.domain.dto.comparison.ComparisonTransactionResult;
import org.larik.three.domain.port.out.MissingTransactionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisPersistService implements MissingTransactionRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(ComparisonTransactionResult c) {
        String key = buildKey(c);
        redisTemplate.opsForValue().set(key, c, 24, TimeUnit.HOURS);
    }

    private String buildKey(ComparisonTransactionResult c) {
        return c.rawTransaction().getTransactionId() +
                "client" +
                c.rawTransaction().getClient().getClientId();
    }
}
