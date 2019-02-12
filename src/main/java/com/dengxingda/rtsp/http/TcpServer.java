package com.dengxingda.rtsp.http;

import java.net.InetSocketAddress;

import com.dengxingda.rtsp.handler.MpegTsHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created on 2019/2/12<br>
 *
 * @author dengxingda
 * @version 1.0
 */
public class TcpServer implements Runnable{

    ServerBootstrap bootstrap = new ServerBootstrap();

    EventLoopGroup bossLoop = new NioEventLoopGroup();
    EventLoopGroup workLoop = new NioEventLoopGroup();

    @Override
    public void run() {
        try {
            bootstrap
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .option(ChannelOption.SO_SNDBUF, 1024*2560)
                    .option(ChannelOption.SO_RCVBUF, 1024*2560)
                    .option(ChannelOption.TCP_NODELAY, true);

            bootstrap
                    .group(bossLoop,workLoop)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new MpegTsHandler());
                        }
                    });
            bootstrap
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(15000));
            ChannelFuture future = bootstrap.bind().sync();
            if (future.isSuccess()) {
                System.out.println("TCP stream server start at port: " + 15000 + ".");
            }

            future.channel().closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if (bossLoop != null)
                bossLoop.shutdownGracefully();
            if (workLoop != null)
                workLoop.shutdownGracefully();
        }
    }
}
