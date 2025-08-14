package com.practice.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.practice.client.dto.AbstractDTO;

@SuppressWarnings("rawtypes")
public class ClientManager {
	
	private Map<String, Client<? extends AbstractDTO>> clients = new HashMap<>();
    
    // sendToClientメソッドを実行する前に当メソッドでDTOにメッセージを設定する。
    public void setMessage() {
    	
    }

    @SuppressWarnings("unchecked")
	public void loadAccessInfo(Map<String, AccessInfo> accessMap) {
        for (Map.Entry<String, AccessInfo> entry : accessMap.entrySet()) {
            AccessInfo info = entry.getValue();
            if (info.getEnabled()) {
                for (int i = 0; i < info.getConnectionCount(); i++) {
                    clients.put(entry.getKey() + "#" + i, ClientFactory.create(info));
                }
            }
        }
    }

	public void connectClient(String name) throws IOException {
    	// 有効な接続情報は全て接続する
    	for (Map.Entry<String,Client<? extends AbstractDTO>> clientMap : clients.entrySet()) {
    		String serverName = clientMap.getKey();
    		clients.get(serverName).connect();
    	}
    }

	public <T extends AbstractDTO> void sendToClient(String name, T dto) throws IOException {
		// サーバにメッセージを送信する
	    clients.values().stream()
        .filter(c -> {
            AbstractDTO existingDto = c.getDTO();
            return existingDto != null && existingDto.getClass().equals(dto.getClass());
        })
        .forEach(c -> {
            @SuppressWarnings("unchecked")
            Client<T> client = (Client<T>) c;
            try {
                client.setDTO(dto);
                client.send();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

	public <T extends AbstractDTO> T receiveFromClient(String name) throws IOException {
		@SuppressWarnings("unchecked")
		Client<T> client = (Client<T>) clients.get(name);
		client.receive();
		return client.getDTO();
    }

    public void closeClient(String name) throws IOException {
        clients.get(name).close();
    }
    
}
