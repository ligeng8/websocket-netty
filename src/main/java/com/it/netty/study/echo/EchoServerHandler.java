package com.it.netty.study.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
//这种使用 ChannelHandler 的方式体现了关注点分离的设计原则，并简化业务逻辑的迭代开发的要求。
//处理程序很简单;它的每一个方法可以覆盖到“hook（钩子）”在活动周期适当的点。很显然，我们覆盖 channelRead因为我们需要处理所有接收到的数据。
//覆盖 exceptionCaught 使我们能够应对任何 Throwable 的子类型。在这种情况下我们记录，并关闭所有可能处于未知状态的连接。
//它通常是难以 从连接错误中恢复，所以干脆关闭远程连接。当然，也有可能的情况是可以从错误中恢复的，所以可以用一个更复杂的措施来尝试识别和处理 这样的情况。
//如果异常没有被捕获，会发生什么？
//每个 Channel 都有一个关联的 ChannelPipeline，它代表了 ChannelHandler 实例的链。适配器处理的实现只是将一个处理方法调用转发到链中的下一个处理器。因此，如果一个 Netty 应用程序不覆盖exceptionCaught ，那么这些错误将最终到达 ChannelPipeline，并且结束警告将被记录。出于这个原因，你应该提供至少一个 实现 exceptionCaught 的 ChannelHandler。
//关键点要牢记：
//ChannelHandler 是给不同类型的事件调用
//应用程序实现或扩展 ChannelHandler 挂接到事件生命周期和提供自定义应用逻辑。
@Sharable // 标识这类的实例之间可以在 channel 里面共享
public class EchoServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf) msg;
		System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8)); // 2日志消息输出到控制台
		ctx.write(in); // 3将所接收的消息返回给发送者。注意，这还没有冲刷数据
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)// 4冲刷所有待审消息到远程节点。关闭通道后，操作完成
				.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace(); // 5打印异常堆栈跟踪
		ctx.close(); // 6关闭通道
	}
}