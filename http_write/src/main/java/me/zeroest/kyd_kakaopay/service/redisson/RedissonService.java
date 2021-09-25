package me.zeroest.kyd_kakaopay.service.redisson;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class RedissonService {

    private final RedissonClient redissonClient;

    public boolean locking(String lockKey, LockingWork work) {

        final RLock lock = redissonClient.getLock(lockKey);
        lock.lock(10, TimeUnit.SECONDS);

        try {
            work.invoke();
        }catch (Exception e){
            return false;
        }finally {
            lock.unlock();
        }

        return true;

    }

}
