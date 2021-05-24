package com.hehnev.server.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

public class CommandInHandler extends SimpleChannelInboundHandler<String> {
    private static final String LS_COMMAND = "\tls    view all files and directories\r";
    private static final String MKDIR_COMMAND = "\tmkdir [dirname]    create directory\r";
    private static final String CHANGE_NICKNAME = "\tnick    change nickname\r";
    private static final String TOUCH_COMMAND = "\ttouch [filename]    create file\r";
    private static final String CD_COMMAND = "\tcd [path]    moving through a folder\r";
    private static final String RM_COMMAND = "\trm [filename | dirname]    deleting a file or folder\r";
    private static final String COPY_COMMAND = "\tcopy [src] [target]    copying a file or folder\r";
    private static final String CAT_COMMAND = "\tcat [filename]    viewing content file\r";

    private static final String ROOT_NOTIFICATION = "You are already in the root directory\n\n\r";
    private static final String DIRECTORY_DOESNT_EXIST = "Directory %s doesn't exist\n\n\r";
    private static final String ROOT_PATH = "dir-server";

    private static final Map<Channel, String> clients = new HashMap<>();
    private static int newClientIndex = 1;
    private String clientName;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected: " + ctx.channel().remoteAddress());
        clientName = "User #" + newClientIndex;
        clients.put(ctx.channel(), clientName);
        newClientIndex++;

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.print("received message: " + s.concat("\n"));
        sendNickname(channelHandlerContext.channel());
        sendMsg(channelHandlerContext.channel(), s);
        switch (s) {
            case"--help":
                sendMsg(channelHandlerContext.channel(), LS_COMMAND);
                sendMsg(channelHandlerContext.channel(), MKDIR_COMMAND);
                sendMsg(channelHandlerContext.channel(), CHANGE_NICKNAME);
                sendMsg(channelHandlerContext.channel(), TOUCH_COMMAND);
                sendMsg(channelHandlerContext.channel(), CD_COMMAND);
                sendMsg(channelHandlerContext.channel(), RM_COMMAND);
                sendMsg(channelHandlerContext.channel(), COPY_COMMAND);
                sendMsg(channelHandlerContext.channel(), CAT_COMMAND);
                break;
        }

//        if (s.startsWith("/")) {
//            if (s.startsWith("/changename ")) { // change newnaem
//                clientName = s.split(" ", 2)[1];
//            }
//        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(clientName + " отвалился");
        clients.remove(ctx.channel());
        ctx.close();
    }

    private void sendMsg(Channel channel, String message) {
        for (Map.Entry<Channel, String> map : clients.entrySet()) {
            if (channel == map.getKey()) {
                map.getKey().writeAndFlush(message.concat("\n"));
            }
        }
    }

    private void sendNickname(Channel channel) {
        // [User #1]:
        String out = String.format("[%s]: ", clientName);
        for (Map.Entry<Channel, String> map : clients.entrySet()) {
            if (channel == map.getKey()) {
                map.getKey().writeAndFlush(out);
            }
        }
    }


}
