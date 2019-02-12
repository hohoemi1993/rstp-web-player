package com.dengxingda.rtsp.http;

import com.dengxingda.rtsp.handler.MpegTsHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * Created on 2019/1/31<br>
 *
 * @author dengxingda
 * @version 1.0
 */
public class UdpServer implements Runnable{

    private static int port = 15000;

    public void run() {

        EventLoopGroup bossLoop = null;
        try {
            bossLoop = new NioEventLoopGroup();

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioDatagramChannel.class);

            bootstrap
                    .group(bossLoop)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_SNDBUF, 1024 * 2560)
                    .option(ChannelOption.SO_RCVBUF, 1024 * 2560);

            bootstrap
                    .handler(new MpegTsHandler());

            ChannelFuture future = bootstrap.bind(port).sync();

            if (future.isSuccess()) {
                System.out.println("UDP stream server start at port: " + port + ".");
            }


            future.channel().closeFuture().await();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (bossLoop != null) {
                bossLoop.shutdownGracefully();
            }
        }


    }


}
