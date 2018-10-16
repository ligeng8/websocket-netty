package com.it.nio;


import java.io.IOException;
import java.net.InetSocketAddress;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.nio.ByteBuffer;  
import java.nio.channels.SelectionKey;  
import java.nio.channels.Selector;  
import java.nio.channels.ServerSocketChannel;  
import java.nio.channels.SocketChannel;  
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;  
import java.util.Map;  
import java.util.Set;  

/**
 * @author cyq
 * NIOͨѶ�����
 */
public class NIOSServer {

    private int port = 8888;
    //����buffer
    private CharsetDecoder decode = Charset.forName("UTF-8").newDecoder();
    /*�������ݻ�����*/
    private ByteBuffer sBuffer = ByteBuffer.allocate(1024);
    /*�������ݻ�����*/
    private ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    /*ӳ��ͻ���channel */
    private Map<String, SocketChannel> clientsMap = new HashMap<String, SocketChannel>(); 
    private Selector selector;
    private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", java.util.Locale.US);

    public NIOSServer(){
        try {
            init();
            listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void init() throws IOException{
        /* 
         *�����������ˣ�����Ϊ���������󶨶˿ڣ�ע��accept�¼� 
         *ACCEPT�¼�����������յ��ͻ�����������ʱ���������¼� 
         */  
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();  
        serverSocketChannel.configureBlocking(false);  
        ServerSocket serverSocket = serverSocketChannel.socket();  
        serverSocket.bind(new InetSocketAddress(port));  
        selector = Selector.open();  
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
        System.out.println("server start on port:"+port);  
    }

    /** 
     * ����������ѯ������select������һֱ����ֱ��������¼�������ʱ 
     */  
    private void listen(){

        while (true) {
            try {
                selector.select();//����ֵΪ���δ������¼���  
                Set<SelectionKey> selectionKeys = selector.selectedKeys();  
                for(SelectionKey key : selectionKeys){
                    handle(key);
                }
                selectionKeys.clear();//�����������¼� 
            } catch (Exception e) {
                e.printStackTrace();  
                break;  
            }  
        }
    }

    /**
     * ����ͬ���¼� 
     */ 
    private void handle(SelectionKey selectionKey) throws IOException { 

        ServerSocketChannel server = null;
        SocketChannel client = null;
        String receiveText=null;
        int count=0;
        if (selectionKey.isAcceptable()) {
            /* 
             * �ͻ������������¼� 
             * serversocketΪ�ÿͻ��˽���socket���ӣ�����socketע��READ�¼��������ͻ������� 
             * READ�¼������ͻ��˷������ݣ����ѱ������������߳���ȷ��ȡʱ���������¼� 
             */  
            server = (ServerSocketChannel) selectionKey.channel();
            client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            /*
             * READ�¼����յ��ͻ��˷������ݣ���ȡ���ݺ����ע������ͻ��� 
             */
            client = (SocketChannel) selectionKey.channel();
            rBuffer.clear();
            count = client.read(rBuffer);
            if (count > 0) {
                rBuffer.flip();
                receiveText = decode.decode(rBuffer.asReadOnlyBuffer()).toString();
                System.out.println(client.toString()+":"+receiveText);
                sBuffer.clear();
                sBuffer.put((sdf.format(new Date())+"�������յ������Ϣ").getBytes());
                sBuffer.flip();
                client.write(sBuffer);
                dispatch(client, receiveText);
                client = (SocketChannel) selectionKey.channel();
                client.register(selector, SelectionKey.OP_READ);
            }  
        }   
    }  

    /** 
     * �ѵ�ǰ�ͻ�����Ϣ ���͵������ͻ��� 
     */
    private void dispatch(SocketChannel client,String info) throws IOException{  

        Socket s = client.socket();  
        String name = "["+s.getInetAddress().toString().substring(1)+":"+Integer.toHexString(client.hashCode())+"]";  
        if(!clientsMap.isEmpty()){
            for(Map.Entry<String, SocketChannel> entry : clientsMap.entrySet()){
                SocketChannel temp = entry.getValue();
                if(!client.equals(temp)){
                    sBuffer.clear();
                    sBuffer.put((name+":"+info).getBytes());
                    sBuffer.flip();
                    //�����ͨ��
                    temp.write(sBuffer);
                }
            }
        }
        clientsMap.put(name, client);
    }

    public static void main(String[] args) throws IOException {

        new NIOSServer();
    }  
}  
