package com.dengxingda.rtsp;

import com.dengxingda.rtsp.http.TcpServer;
import com.dengxingda.rtsp.http.UdpServer;
import com.dengxingda.rtsp.websocket.WebSocketServer;

/**
 * Created on 2019/2/8<br>
 *
 * @author dengxingda
 * @version 1.0
 */
public class NettyServer {

    public void start() {
        TcpServer tcpServer = new TcpServer();
        UdpServer udpServer = new UdpServer();
        WebSocketServer webSocketServer = new WebSocketServer();

        new Thread(udpServer).start();
        new Thread(webSocketServer).start();
    }

    public static void main(String[] args) {
        new NettyServer().start();
    }
}
