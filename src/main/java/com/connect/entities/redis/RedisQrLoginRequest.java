package com.connect.entities.redis;

import com.connect.models.QrLoginRequest;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(value = "qr-login-request", timeToLive = 60*15)
public class RedisQrLoginRequest {

    @Id
    private String id;
    private QrLoginRequest qrLoginRequest;

    public RedisQrLoginRequest(String id, QrLoginRequest qrLoginRequest){
        this.id = id;
        this.qrLoginRequest = qrLoginRequest;
    }

}
