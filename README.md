# 簡易的な汎用サーバ

簡易的な汎用サーバを作成してみました。ブラウザからサーバの情報(サーバ名, IP, PORT)を入力して、Startボタンを押すとサーバが作成されます。
現在、TCPとUDPサーバの２つしか作成していないですが、今後増やしていく予定。

---

### UI 画面

<img width="2534" height="1540" alt="image" src="https://github.com/user-attachments/assets/1ce455e6-2782-42ef-9eca-c10324166fed" />


- **TCPエリア入力項目**
・**TCP 名称**：サーバの名称
・**TCP IP**：サーバのIPアドレスを入力(IPv4)
・**TCP PORT**：サーバのポート番号
- **UDPエリア入力項目**
・**TCP 名称**：サーバの名称
・**TCP IP**：サーバのIPアドレスを入力(IPv4)
・**TCP PORT**：サーバのポート番号

上記３項目を入力後、TCP Start / UDP Start ボタンを押下したらサーバが作成される。作成されたサーバは、下の**Statusエリア**に表示される。

---

### 使い方

TCP/UDPエリアの各入力項目を入力する。その後、TCP Start / UDP Start ボタンを押下したらサーバが作成される。作成されたサーバは、下の**Statusエリア**に表示される。

![動作確認](https://github.com/user-attachments/assets/125213ae-931a-4043-af85-ea5b4993e436)

---

### 使用技術
Java21, Gradle, Spring Boot
