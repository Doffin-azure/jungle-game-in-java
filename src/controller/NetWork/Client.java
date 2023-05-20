
package controller.NetWork;
import controller.GameController;
import model.ChessboardPoint;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public void connectToServer(String host, int port) {
        try {
            // 建立与服务端的连接
            socket = new Socket(host, port);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("与服务端连接成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData(ChessboardPoint[] SrcAndDestPoints) {
        try {
            // 发送数据给服务端
            objectOutputStream.writeObject(SrcAndDestPoints);
            objectOutputStream.flush();
            System.out.println("数据已发送给服务端");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receivingDataAndUpdate(GameController gameController) {
        try {
            // 创建线程处理接收数据和处理的逻辑
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            // 接收服务端发送的数据
                            ChessboardPoint[] SrcAndDestPoints = (ChessboardPoint[]) objectInputStream.readObject();
                            System.out.println("接收到来自客户端的数据");

                            // 处理接收到的数据
                            if (!gameController.getModel().isNull(SrcAndDestPoints[1])) {//如果destPoint有棋子,要吃了
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

            // 启动线程
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromServer() {
        try {
            // 关闭资源
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
            System.out.println("与服务端断开连接");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
