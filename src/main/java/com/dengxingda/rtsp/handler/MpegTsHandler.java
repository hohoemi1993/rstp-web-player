package com.dengxingda.rtsp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * Created on 2019/1/31<br>
 *
 * @author dengxingda
 * @version 1.0
 */
public class MpegTsHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        SharedChannel.getInstance().share(datagramPacket.content());
    }

}
