package com.dengxingda.rtsp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created on 2019/1/31<br>
 *
 * @author dengxingda
 * @version 1.0
 */
public class VideoPlayerHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SharedChannel.getInstance().addChannel(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
    }
}
