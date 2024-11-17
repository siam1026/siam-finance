package com.siam.system.modular.mod_finance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class ActuatorController {

    @GetMapping("/user/info/{id}")
    public ResponseEntity<Map<String, Object>> queryUserInfoById(@PathVariable("id") long id)
            throws InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", "shawn");
        map.put("realname", "shawn");
        map.put("age", 25);
        TimeUnit.SECONDS.sleep(2);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/user/list")
    public ResponseEntity<List<Map<String, Object>>> listUser()
            throws InterruptedException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i);
            map.put("username", "shawn_" + i);
            map.put("realname", "shawn_" + i);
            map.put("age", 25 + i);
            list.add(map);
        }
        TimeUnit.SECONDS.sleep(2);
        return ResponseEntity.ok(list);
    }
}