package com.practice.server.admin;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/server")
public class ServerController {
    private final ServerManager manager;

    public ServerController(ServerManager manager) { this.manager = manager; }

    @PostMapping("/tcp/start")
    public ResponseEntity<?> startTcp(@RequestBody Map<String, Object> body) throws Exception {
        String ip = (String) body.getOrDefault("ip", "0.0.0.0");
        int port = ((Number) body.getOrDefault("port", 5000)).intValue();
        manager.startTcp(ip, port);
        return ResponseEntity.ok(Map.of("status", "started", "kind", "tcp", "ip", ip, "port", port));
    }

    @PostMapping("/udp/start")
    public ResponseEntity<?> startUdp(@RequestBody Map<String, Object> body) throws Exception {
        String ip = (String) body.getOrDefault("ip", "0.0.0.0");
        int port = ((Number) body.getOrDefault("port", 5001)).intValue();
        manager.startUdp(ip, port);
        return ResponseEntity.ok(Map.of("status", "started", "kind", "udp", "ip", ip, "port", port));
    }

    @PostMapping("/{kind}/stop")
    public ResponseEntity<?> stop(@PathVariable String kind) {
        manager.stop(kind);
        return ResponseEntity.ok(Map.of("status", "stopped", "kind", kind));
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        var tcp = new java.util.LinkedHashMap<String, Object>();
        tcp.put("running", manager.isRunning("tcp"));
        Integer tport = manager.port("tcp");            // ← 未起動なら null
        if (tport != null) tcp.put("port", tport);      // null は入れない

        var udp = new java.util.LinkedHashMap<String, Object>();
        udp.put("running", manager.isRunning("udp"));
        Integer uport = manager.port("udp");
        if (uport != null) udp.put("port", uport);

        var out = new java.util.LinkedHashMap<String, Object>();
        out.put("tcp", tcp);
        out.put("udp", udp);
        return out;
    }
}