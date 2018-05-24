/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clienttictactoe;



import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author ale
 */
public class Boton extends JButton implements ActionListener { 
    private char seleccion;
    private final String RUTA_X = "./imagenes/x.gif"; 
    private final String RUTA_O ="./imagenes/o.gif";
                   
    public Boton(int posX, int posY, int ancho, int alto, char seleccion){
        setBounds(posX, posY, ancho, alto);
        addActionListener(this);
        this.seleccion = seleccion;        
    }

    public char getSeleccion() {
        return seleccion;
    }           
    /**
     * Quitar el codigo duplicado
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
       if(seleccion == 'x'){
           ImageIcon gif = new ImageIcon(RUTA_X);
           Image imagen = gif.getImage().getScaledInstance(this.getWidth(),this.getHeight(), 0);
           this.setIcon(new ImageIcon(imagen));           
           this.setEnabled(false);
           
        }else{
           ImageIcon gif = new ImageIcon(RUTA_O);
           Image imagen = gif.getImage().getScaledInstance(this.getWidth(),this.getHeight(), 0);
           this.setIcon(new ImageIcon(imagen));
           this.setEnabled(false);
        }
        actualizarGato(this.getName());
    }
    
    private void actualizarGato(String nombre){
        ControladorBuscando.botonPresionado = Integer.parseInt(nombre); 
        System.out.println("se presionó : "+ControladorBuscando.botonPresionado);
    }
}
