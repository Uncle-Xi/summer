package com.summerframework.boot.web.server.netty;

import com.summerframework.boot.web.server.HttpServer;
import com.summerframework.core.logger.LogFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @description: HttpNettyServer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpNettyServer extends HttpServer {

    private static final LogFactory logger = new LogFactory(HttpNettyServer.class);

    private ServerBootstrap server = new ServerBootstrap();

    public HttpNettyServer(String... port){
        super(port);
    }

    @Override
    public void start() throws RuntimeException {
        logger.info("HttpNettyServer start...");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {

//                            client.pipeline().addLast(new HttpResponseEncoder());
//                            client.pipeline().addLast(new HttpRequestDecoder());

                            client.pipeline().addLast(new HttpResponseEncoder());
                            client.pipeline().addLast(new HttpRequestDecoder());

//                            client.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
//                            client.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));

//                            client.pipeline().addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
//                            client.pipeline().addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));
                            client.pipeline().addLast(new HttpNettyHandler(httpServlet));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = server.bind(DEFAULT_PORT).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void listening() {
        new Thread(() -> start()).start();
    }

    @Override
    public void stop() throws RuntimeException { }

}
