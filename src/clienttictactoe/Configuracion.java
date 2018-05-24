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
public class Configuracion {
    
    private String IP;
    private String socket;
    private char seleccion;
    private int numero;
    
    
    public Configuracion(String socket, String IP, int numero,char seleccion) {
        this.IP = IP;
        this.socket = socket;
        this.numero = numero;
        this.seleccion = seleccion;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public char getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(char seleccion) {
        this.seleccion = seleccion;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
           
}
