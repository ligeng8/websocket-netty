//package com.it.netty.bootstrap.cllient;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.channel.oio.OioEventLoopGroup;
//import io.netty.channel.socket.oio.OioSocketChannel;
//
//public class OIOClientBootStrap {
//	public static void main(String[] args) {
//		OioEventLoopGroup oioEventLoopGroup = new OioEventLoopGroup();
//		Bootstrap bootstrap = new Bootstrap();
//		bootstrap.group(oioEventLoopGroup)
//		         .channel(OioSocketChannel.class)
//		         .handler(new SimpleChannelInboundHandler<ByteBuf>() {
//
//					@Override
//					protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//		bootstrap.
//		
//	}
//}
