package com.practice.server.admin;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.server.dto.TcpDTO;
import com.practice.server.dto.UdpDTO;

@RestController
@RequestMapping("/api/server")
public class ServerController {
    private final ServerManager manager;

    public ServerController(ServerManager manager) { this.manager = manager; }

    @PostMapping("/tcp/start")
    public Map<String,Object> startTcp(@RequestBody TcpDTO req) throws Exception {
        String srvName = (req.srvName == null || req.srvName.isBlank()) ? "TCP Server" : req.srvName;
        manager.startTcp(srvName, req.ip, req.port);
        return Map.of("ok", true);
    }

    @PostMapping("/udp/start")
    public Map<String,Object> startUdp(@RequestBody UdpDTO req) throws Exception {
        String srvName = (req.srvName == null || req.srvName.isBlank()) ? "UDP Server" : req.srvName;
        manager.startUdp(srvName, req.ip, req.port);
        return Map.of("ok", true);
    }
    
    @PostMapping("/{kind}/stop")
    public ResponseEntity<?> stop(@PathVariable String kind) {
        manager.stop(kind);
        return ResponseEntity.ok(Map.of("status", "stopped", "kind", kind));
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        var tcp = new java.util.LinkedHashMap<String, Object>();
        tcp.put("name", "TCP Server");
        tcp.put("running", manager.isRunning("tcp"));
        Integer tport = manager.port("tcp");
        if (tport != null) tcp.put("port", tport);

        var udp = new java.util.LinkedHashMap<String, Object>();
        udp.put("name", "UDP Server");
        udp.put("running", manager.isRunning("udp"));
        Integer uport = manager.port("udp");
        if (uport != null) udp.put("port", uport);

        var out = new java.util.LinkedHashMap<String, Object>();
        out.put("tcp", tcp);
        out.put("udp", udp);
        return out;
    }
}