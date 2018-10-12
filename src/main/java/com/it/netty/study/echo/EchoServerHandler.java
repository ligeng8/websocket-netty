package com.it.netty.study.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
//����ʹ�� ChannelHandler �ķ�ʽ�����˹�ע���������ԭ�򣬲���ҵ���߼��ĵ���������Ҫ��
//�������ܼ�;����ÿһ���������Ը��ǵ���hook�����ӣ����ڻ�����ʵ��ĵ㡣����Ȼ�����Ǹ��� channelRead��Ϊ������Ҫ�������н��յ������ݡ�
//���� exceptionCaught ʹ�����ܹ�Ӧ���κ� Throwable �������͡���������������Ǽ�¼�����ر����п��ܴ���δ֪״̬�����ӡ�
//��ͨ�������� �����Ӵ����лָ������Ըɴ�ر�Զ�����ӡ���Ȼ��Ҳ�п��ܵ�����ǿ��ԴӴ����лָ��ģ����Կ�����һ�������ӵĴ�ʩ������ʶ��ʹ��� �����������
//����쳣û�б����񣬻ᷢ��ʲô��
//ÿ�� Channel ����һ�������� ChannelPipeline���������� ChannelHandler ʵ�������������������ʵ��ֻ�ǽ�һ������������ת�������е���һ������������ˣ����һ�� Netty Ӧ�ó��򲻸���exceptionCaught ����ô��Щ�������յ��� ChannelPipeline�����ҽ������潫����¼���������ԭ����Ӧ���ṩ����һ�� ʵ�� exceptionCaught �� ChannelHandler��
//�ؼ���Ҫ�μǣ�
//ChannelHandler �Ǹ���ͬ���͵��¼�����
//Ӧ�ó���ʵ�ֻ���չ ChannelHandler �ҽӵ��¼��������ں��ṩ�Զ���Ӧ���߼���
@Sharable // ��ʶ�����ʵ��֮������� channel ���湲��
public class EchoServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf) msg;
		System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8)); // 2��־��Ϣ���������̨
		ctx.write(in); // 3�������յ���Ϣ���ظ������ߡ�ע�⣬�⻹û�г�ˢ����
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)// 4��ˢ���д�����Ϣ��Զ�̽ڵ㡣�ر�ͨ���󣬲������
				.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace(); // 5��ӡ�쳣��ջ����
		ctx.close(); // 6�ر�ͨ��
	}
}