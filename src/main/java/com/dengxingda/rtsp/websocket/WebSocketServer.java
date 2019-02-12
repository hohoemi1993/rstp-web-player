package com.dengxingda.rtsp.websocket;

import com.dengxingda.rtsp.handler.VideoPlayerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Created on 2019/1/31<br>
 *
 * @author dengxingda
 * @version 1.0
 */
public class WebSocketServer implements Runnable{

    private static int port = 14000;

    private static final String WEBSOCKET_PATH = "/play";

    public void run() {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            // 长时间不写会断
                            ch.pipeline().addLast("readTimeout", new ReadTimeoutHandler(45));
                            // http decode
                            ch.pipeline().addLast("HttpServerCodec", new HttpServerCodec());
                            //ChunkedWriteHandler分块写处理，文件过大会将内存撑爆
                            ch.pipeline().addLast("ChunkedWriter", new ChunkedWriteHandler());
                            /*
                             * 作用是将一个Http的消息组装成一个完成的HttpRequest或者HttpResponse，那么具体的是什么
                             * 取决于是请求还是响应, 该Handler必须放在HttpServerCodec后的后面
                             */
                            ch.pipeline().addLast("HttpAggregator", new HttpObjectAggregator(65535));
                            //用于处理websocket, /ws为访问websocket时的uri
                            ch.pipeline().addLast("WsProtocolHandler",
                                    new WebSocketServerProtocolHandler(WEBSOCKET_PATH,"web_rtsp",true));
                            ch.pipeline().addLast(new VideoPlayerHandler());
                        }
                    });

            try {
                ChannelFuture channelFuture = bootstrap.bind(port).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }



}
