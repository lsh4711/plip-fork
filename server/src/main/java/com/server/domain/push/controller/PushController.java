package com.server.domain.push.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.domain.push.dto.PushDto;
import com.server.domain.push.entity.Push;
import com.server.domain.push.mapper.PushMapper;
import com.server.domain.push.service.PushService;
import com.server.global.utils.UriCreator;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pushs")
public class PushController {
    private final PushService pushService;
    private final PushMapper pushMapper;

    @PostMapping("/write")
    public ResponseEntity postPush(@RequestBody PushDto.Post postdto) {
        Push push = pushMapper.postDtoToPush(postdto);
        Push savedPush = pushService.savePush(push);

        long pushId = savedPush.getPushId();
        URI location = UriCreator.createUri("/api/pushs", pushId);

        return ResponseEntity.created(location).build();
    }
}
