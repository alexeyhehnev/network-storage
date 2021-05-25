package com.hehnev.client;

import com.hehnev.client.handlers.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Network {
    public String host;
    public int port;

    private SocketChannel channel;

    public Network(Callback onMessageReceivedCallback) {
        Thread t = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(
                                        new StringDecoder(),
                                        new StringEncoder(),
                                        new ClientHandler(onMessageReceivedCallback)
                                );
                            }
                        });
                if (host != null && port != 0) {
                    run(bootstrap, host, port);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.start();
    }

    private void run(Bootstrap bootstrap, String host, int port) throws InterruptedException {
        ChannelFuture future = bootstrap.connect(host, port);
        future.channel().closeFuture().sync();
    }


    public void sendHostAndPort(String host, String port) {

    }
    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }
    public void close() {
        channel.close();
    }
}
