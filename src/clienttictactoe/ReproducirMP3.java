/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttictactoe;

/**
 *
 * @author ale
 */
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class ReproducirMP3{

    public void reproducir(String mp3) {
        try {
            System.out.println("reproduciendo mp3");
            //abrimos un flujo de entrada de archivo para abrir nuestro archivo mp3 -
            FileInputStream archivo = new FileInputStream(mp3);
            //creamos un objeto de tipo player, player es una clase se la libreria Jlayer que miramos en el video de convertir texto a voz -
            Player playMP3 = new Player(archivo);
            //y reproducimos -
            playMP3.play();
           
        } catch (Exception exc) {
            //en caso de no encontrar el archivo o otro tipo de error mostramos el archivo -
            exc.printStackTrace();
            System.out.println("No se pudo reproducir el archivo");

        }
    } 
    
    public static void pararReproducir(Player closeMP3){
        closeMP3.close();
        
    }
}