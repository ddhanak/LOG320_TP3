package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Timer;

public class Main {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8888;

        if (args != null && args.length > 0) {
            host = args[0];
            if (args.length > 1)
                port = Integer.parseInt(args[1]);
        }

        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        int[][] board = new int[8][8];
        Board myBoard = null;
        int couleurEquipe = 0;
        try {
            MyClient = new Socket(host, port);
            input    = new BufferedInputStream(MyClient.getInputStream());
            output   = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while(1 == 1){
                char cmd = 0;

                cmd = (char)input.read();

                // D�but de la partie en joueur blanc
                if(cmd == '1'){
                    couleurEquipe = Board.PION_BLANC;
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    initialiserBoard(board, boardValues);

                    myBoard = new Board(board, null);
                    System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
                    String move = null;
                    //move = console.readLine();
                    Board finalMyBoard = myBoard;
                    int finalCouleurEquipe = couleurEquipe;
                    final Coup[] prochainCoup = new Coup[1];
                    final int[] i = new int[1];
                    Thread t1 = new Thread(() -> {
                        for (i[0] = 1; ; i[0]++) {
                            prochainCoup[0] = finalMyBoard.getProchainCoup(finalCouleurEquipe, i[0]);
                        }
                    });
                    t1.start();

                    try {
                        Thread.sleep(4800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    t1.stop();
                    System.out.println(i[0]);

                    System.out.println("Mon Dernier coup : " + prochainCoup[0]);
                    myBoard.effectuerCoup(prochainCoup[0]);
                    move = prochainCoup[0].toString();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }
                // D�but de la partie en joueur Noir
                if(cmd == '2'){
                    couleurEquipe = Board.PION_NOIR;
                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    initialiserBoard(board, boardValues);
                    myBoard = new Board(board, null);
                }


                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup jou�.
                if(cmd == '3'){
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer);
                    s = s.substring(1, 8);
                    System.out.println("Dernier coup : "+ s);
                    System.out.println("Entrez votre coup : ");
                    String move = null;
                    //move = console.readLine();
                    Coup dernierCoupJoue = new Coup(s);
                    myBoard.effectuerCoup(dernierCoupJoue);
                    Board finalMyBoard = myBoard;
                    int finalCouleurEquipe = couleurEquipe;
                    final Coup[] prochainCoup = new Coup[1];
                    final int[] i = new int[1];
                    Thread t1 = new Thread(() -> {
                        for (i[0] = 1; ; i[0]++) {
                            prochainCoup[0] = finalMyBoard.getProchainCoup(finalCouleurEquipe, i[0]);
                        }
                    });
                    t1.start();

                    try {
                        Thread.sleep(4800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    t1.stop();
                    System.out.println(i[0]);

                    System.out.println("Mon Dernier coup : " + prochainCoup[0]);
                    myBoard.effectuerCoup(prochainCoup[0]);
                    move = prochainCoup[0].toString();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
                // Le dernier coup est invalide
                if(cmd == '4'){
                    System.out.println("Coup invalide, entrez un nouveau coup : ");
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void initialiserBoard(int[][] board, String[] boardValues) {
        int x=0,y=0;
        for(int i=0; i<boardValues.length;i++){
            board[x][y] = Integer.parseInt(boardValues[i]);
            y++;
            if(y == 8){
                y = 0;
                x++;
            }
        }
    }

    private static void attendreProchainCoup(Coup[] prochainCoup) {
        try {
            for (int i = 1; i <= 4; i++) {
                Thread.sleep(1000);
                if (prochainCoup[0] != null)
                    break;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
