package controller.NetWork;

import controller.GameController;
import model.ChessboardPoint;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public void startServer(int port) {
        try {
            // 创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
            serverSocket = new ServerSocket(port);
            System.out.println("服务器启动，等待客户端连接...");

            // 等待客户端连接
            socket = serverSocket.accept();
            System.out.println("客户端连接成功！");

            // 初始化输入输出流
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendData(ChessboardPoint[] SrcAndDestPoints) {
        try {
            // 发送数据给服务端
            objectOutputStream.writeObject(SrcAndDestPoints);
            objectOutputStream.flush();
            System.out.println("数据已发送给 客户端");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receivingDataAndUpdate(GameController gameController) {//第一个是sourcePooint，第二个是destPoint
        //循环监听等待客户端的更新棋盘
        try {
            //创建线程处理接受数据
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            // 接收服务端发送的数据
                            ChessboardPoint[] SrcAndDestPoints = (ChessboardPoint[]) objectInputStream.readObject();
                            System.out.println("接收到来自客户端的数据");

                            // 处理接收到的数据
                            if (!gameController.getModel().isNull(SrcAndDestPoints[1])) {//如果destPoint有棋子
                                gameController.getModel().captureChessPiece(SrcAndDestPoints[0], SrcAndDestPoints[1]);
                                gameController.setCount(gameController.getCount() + 1);
                                gameController.view.removeChessComponentAtGrid(SrcAndDestPoints[1]);
                                gameController.view.setChessComponentAtGrid(SrcAndDestPoints[1], gameController.view.removeChessComponentAtGrid(SrcAndDestPoints[0]));
                                gameController.setSelectedPoint(null);
                                gameController.view.repaint();
                                gameController.swapColor();
                                //把view改成public？
                                System.out.println("已经更新棋盘");
                            } else {//如果destPoint没有棋子
                                gameController.setCount(gameController.getCount() + 1);
                                gameController.swapColor();
                                gameController.getModel().moveChessPiece(SrcAndDestPoints[0], SrcAndDestPoints[1]);
                                gameController.view.setChessComponentAtGrid(SrcAndDestPoints[1], gameController.view.removeChessComponentAtGrid(SrcAndDestPoints[0]));
                                gameController.view.repaint();

                                System.out.println("已经更新棋盘");

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }}


    public void stopServer() {
        try {
            // 关闭资源
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
            serverSocket.close();
            System.out.println("服务器已停止");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
