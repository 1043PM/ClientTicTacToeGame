/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttictactoe;

import java.io.IOException;

/**
 *
 * @author ale
 */
public class main{

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
         
        VistaMenu menu = new VistaMenu();
        ControladorMenu controlador = new ControladorMenu(menu);        
        controlador.iniciarVista();
        
        
    }
    
}
