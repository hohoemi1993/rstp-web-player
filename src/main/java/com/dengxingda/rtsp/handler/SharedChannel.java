package com.dengxingda.rtsp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * Created on 2019/1/31<br>
 * @author dengxingda
 * @version 1.0
 */
public class SharedChannel {

    private static final SharedChannel INSTANCE = new SharedChannel();

    private SharedChannel() {}

    public static SharedChannel getInstance(){
        return INSTANCE;
    }

    private final ChannelGroup channelGroup
            = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    public void addChannel(Channel channel) {
        channelGroup.add(channel);
    }

    public void removeChannel(Channel channel) {
        channelGroup.remove(channel);
    }

    public void share(ByteBuf message) {
        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(message);
        message.retain();
        channelGroup.writeAndFlush(frame);
    }

     public void destory() {
        if (channelGroup.isEmpty()) {
            return;
        }
        channelGroup.close();
    }
}
