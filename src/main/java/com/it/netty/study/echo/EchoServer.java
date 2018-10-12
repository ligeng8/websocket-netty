package com.it.netty.study.echo;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }
        public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println(
//                    "Usage: " + EchoServer.class.getSimpleName() +
//                    " <port>");
//            return;
//        }
//        int portort = Integer.parseInt(args[0]);        
        int portort = 8085;   //	1.���ö˿�ֵ���׳�һ�� NumberFormatException ����ö˿ڲ����ĸ�ʽ����ȷ��
        new EchoServer(portort).start();                //	2.���з������� start() ����
    }

    public void start() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup(); //	3.���� EventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)                                //	4.���� ServerBootstrap
             .channel(NioServerSocketChannel.class)        //	5.ָ��ʹ�� NIO �Ĵ��� Channel
             .localAddress(new InetSocketAddress(port))   //	6.���� socket ��ַʹ����ѡ�Ķ˿�
             .childHandler(new ChannelInitializer<SocketChannel>() { 
            	 //	7.��� EchoServerHandler �� Channel �� ChannelPipeline
                 @Override
                 public void initChannel(SocketChannel ch) 
                     throws Exception {
                     ch.pipeline().addLast(
                             new EchoServerHandler());
                 }
             });

            ChannelFuture f = b.bind().sync();           //	8.�󶨵ķ�����;sync �ȴ��������ر�
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();            //	9.�ر� channel �� �飬ֱ�������ر�
        } finally {
            group.shutdownGracefully().sync();           //	10.�ر� EventLoopGroup���ͷ�������Դ��
        }
    }

}