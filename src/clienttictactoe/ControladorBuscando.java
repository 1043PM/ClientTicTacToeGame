/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttictactoe;

import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javazoom.jl.player.Player;

/**
 *
 * @author ale
 */
public class ControladorBuscando extends Thread {

    private VistaBuscandoContrincante vistaBuscando;
    private volatile boolean oponenteEncontrado;
    private Thread hiloTiempo;
    private Thread hiloMusica;
    private Thread hiloConexion;
    private volatile int segundo;
    private int contadorLabel;
    private boolean cronometro;
    private Configuracion usuario;
    private DataOutputStream DOS;
    private DataInputStream DIS;
    private Socket s;
    private FileInputStream archivo;
    private Player playMP3;
    public static volatile int botonPresionado;
    int ultimoBotonPresionado;
    private final String DIRECCION_GIF_BUSCANDO= "./imagenes/BuscandoContrincante.gif";
    private final String DIRECCION_GIF_enPARTIDA= "./imagenes/soldados_disparandose.gif";
    private final String DIRECCION_GIF_VICTORIA = "./imagenes/victoria.gif";
    private final String DIRECCION_GIF_CORRIENDO = "./imagenes/corriendo.gif";

    public ControladorBuscando(VistaBuscandoContrincante buscando, Configuracion usuario) {
        this.vistaBuscando = buscando;
        this.usuario = usuario;
        botonPresionado = 0;
        ultimoBotonPresionado = 0;
    }

    public void iniciarVistaBuscando() throws IOException {
        oponenteEncontrado = false;
        vistaBuscando.setTitle("¡Tic Tac Toe!");
        vistaBuscando.pack();
        vistaBuscando.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vistaBuscando.setLocationRelativeTo(null);
        vistaBuscando.setVisible(true);
        cargarGif(DIRECCION_GIF_BUSCANDO, vistaBuscando.labelGif.getWidth());
        empezar();
        empezarMusicaEspera();
        EscucharServer();
        vistaBuscando.panelTicTacToe.setVisible(false);

    }

    private void cargarGif(String direccionGif,int ancho) {
        ImageIcon gif = new ImageIcon(direccionGif);
        Image imagen = gif.getImage().getScaledInstance(ancho, vistaBuscando.labelGif.getHeight(), 0);
        vistaBuscando.labelGif.setIcon(new ImageIcon(imagen));
        vistaBuscando.labelGif.revalidate();

    }

    private void empezar() {
        segundo = 0;
        cronometro = true;
        contadorLabel = 0;
        

        hiloTiempo = new Thread(new Runnable() {
            boolean primeraVez=true;
            @Override
            public void run() {
                while (cronometro) {
                    try {
                        Thread.sleep(1000);
                        switch (contadorLabel) {

                            case 0:
                                vistaBuscando.jLabel1.setText("Buscando contrincante.");
                                break;
                            case 1:
                                vistaBuscando.jLabel1.setText("Buscando contrincante..");
                                break;
                            case 2:
                                vistaBuscando.jLabel1.setText("Buscando contrincante...");
                                break;
                            case 3:
                                vistaBuscando.jLabel1.setText("Buscando contrincante....");
                                break;
                            default:
                                vistaBuscando.jLabel1.setText("Buscando contrincante.....");
                                break;
                        }

                    } catch (InterruptedException ex) {
                    }
                    segundo++;
                    contadorLabel++;
                    if (contadorLabel == 5) {
                        contadorLabel = 0;
                    }
                    //----------------------------------------------------------
                    if (oponenteEncontrado && primeraVez==true) {
                        playMP3.close();
                        hiloMusica.stop();
                        vistaBuscando.jLabel1.setText("En batalla!!!");                        
                        vistaBuscando.labelGif.setSize(270,vistaBuscando.labelGif.getHeight());
                        cargarGif(DIRECCION_GIF_enPARTIDA,270);
                        primeraVez = false;
                        
                    }
                    //----------------------------------------------------------
                    if(botonPresionado != ultimoBotonPresionado){ 
                        try {
                            System.out.println("Se entró a botonPresionado");
                            DOS.writeUTF("movimientoPropio#"+String.valueOf(botonPresionado));
                            ultimoBotonPresionado = botonPresionado;
                            vistaBuscando.botonesSeleccionables(false);
                        } catch (IOException ex) {
                            Logger.getLogger(ControladorBuscando.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }   
                    if (segundo == 61 && oponenteEncontrado==false){
                        VistaMenu menu = new VistaMenu();
                        ControladorMenu controlador = new ControladorMenu(menu);
                        try {
                            controlador.iniciarVista();
                        } catch (IOException ex) {
                            Logger.getLogger(ControladorBuscando.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        vistaBuscando.setVisible(false);
                        hiloTiempo.stop();

                    }
                    //System.out.println(segundo);

                }
            }
        });
        hiloTiempo.start();
    }

    private void empezarMusicaEspera() {

        hiloMusica = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("reproduciendo mp3");
                    archivo = new FileInputStream("./sounds/buscando.mp3");
                    playMP3 = new Player(archivo);                    
                    playMP3.play();

                    if (segundo == 58) {
                        JOptionPane.showMessageDialog(null, "Oponente no encontrado ");
                        FileInputStream archivoSalida = new FileInputStream("./sounds/alert.mp3");
                        Player playMP3Salida = new Player(archivoSalida);
                        playMP3Salida.play();
                    }

                } catch (Exception exc) {
                    exc.printStackTrace();
                    System.out.println("No se pudo reproducir el archivo");
                }

            }
        });
        hiloMusica.start();
    }

    private void EscucharServer() throws IOException {

        hiloConexion = new Thread(new Runnable() {            
            boolean primeraVez = true;
            @Override
            public void run() {
                try {
                    s = new Socket(usuario.getIP(), Integer.parseInt(usuario.getSocket()));
                    DOS = new DataOutputStream(s.getOutputStream());
                    DIS = new DataInputStream(s.getInputStream());
                    String respuesta = null;
                    if(primeraVez==true){
                        DOS.writeUTF("primera#" + String.valueOf(usuario.getNumero()));
                        System.out.println("esperando...");
                        primeraVez = false;
                    }
                    while(true){                        
                        respuesta = DIS.readUTF();
                        if (respuesta.isEmpty() == false) {
                            interpretarMensajeServer(respuesta);
                            System.out.println("Se interpreta el mensaje: "+respuesta);
                        }                        
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                    System.out.println("Error en hilo coexion");
                }

            }

        });
        hiloConexion.start();
    }

    private void interpretarMensajeServer(String respuestaServer) throws IOException {
        String datos[] = respuestaServer.split("#");
        String primeraParte = datos[0];
        System.out.println("primeraParte: " + primeraParte);
        switch (primeraParte) {
            case "oponenteEncontrado":
                vistaBuscando.panelTicTacToe.setVisible(true);
                oponenteEncontrado = true;
                //cronometro = false;
                llenarPanel();
                break;

            case "movimientoOponente":
                vistaBuscando.mostrarMovimientoOponente(datos[1]);
                vistaBuscando.botonesSeleccionables(true);
                break;

            case "gameOver":
                mensajeGameOver(datos[1]);
                break;

            case "activarBotones":
                vistaBuscando.botonesSeleccionables(true);
                break;
            
            case "comenzarPrimero":
                JOptionPane.showMessageDialog(null, "Tu numero fue el mas cercano!\n comienzas primero!");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Este mensaje no se puede interpretar: " + respuestaServer);
        }
    }
 //wi = 176;
    private void mensajeGameOver(String status) {
        switch(status){
            case "ganador":                 
                cargarGif(DIRECCION_GIF_VICTORIA, 176);
                JOptionPane.showMessageDialog(null, "¡ganaste el juego!");
                break;
            case "perdedor":                 
                cargarGif(DIRECCION_GIF_CORRIENDO, 176);
                JOptionPane.showMessageDialog(null, "¡perdiste el juego!");
                break;
            case "empate":
                JOptionPane.showMessageDialog(null, "empate!"); 
        }
        //System.exit(0);
        vistaBuscando.botonesSeleccionables(false);
    }

    private void llenarPanel() {
        vistaBuscando.generarBotones(usuario.getSeleccion());
    }

}
