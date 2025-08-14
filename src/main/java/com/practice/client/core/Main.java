package com.practice.client.core;

import java.io.IOException;
import java.util.Map;

import com.practice.client.AccessInfo;
import com.practice.client.ClientManager;
import com.practice.client.dto.TcpDTO;

public class Main {
	public static void main(String[] args) {
		
		AccessInfo info = new AccessInfo();
		
		try {
			// プロパティファイルから接続情報を取得
			Map<String, AccessInfo> accessMap = (Map<String, AccessInfo>) info.loadAccessProp("/Users/yamashirokazuaki/Documents/003_WORK/git_practice_20250802/src/main/resources/client/access_info.properties");
			String clientName = "Client1#0";
			String message = "aaaaaaaaiueoooooo";
			TcpDTO dto = new TcpDTO();
			dto.setDTO(message);
			
			ClientManager cm = new ClientManager();
			
			// マネージャーに接続情報をセット
			cm.loadAccessInfo(accessMap);
			
			// 接続
			cm.connectClient(clientName);
			
			// 送信
			cm.sendToClient(clientName, dto);
			
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		
	}
}
