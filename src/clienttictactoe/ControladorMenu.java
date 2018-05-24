/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttictactoe;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

/**
 *
 * @author ale
 */
public class ControladorMenu implements ActionListener {

    private VistaMenu menu;
    private char seleccion;

    public ControladorMenu(VistaMenu menu) {

        this.menu = menu;
        this.menu.botonEmpezar.addActionListener(this);

    }

    public void iniciarVista() throws IOException {

        menu.setTitle("¡Tic Tac Toe!");
        menu.pack();
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        // para probar rapido
        menu.campoTextoSocket.setText("7777");
        menu.campoTextoIP.setText("localhost");
        cargarGIFS();
        accionesLabel();
        

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == menu.botonEmpezar) {

            if (validarInputs() && validarSeleccionColor()) {
                Configuracion usuario = new Configuracion(menu.campoTextoSocket.getText(),
                    menu.campoTextoIP.getText(),Integer.parseInt(menu.campoTextoNumero.getText())
                  , seleccion); 
                VistaBuscandoContrincante buscando = new VistaBuscandoContrincante();
                ControladorBuscando cBuscando = new ControladorBuscando(buscando, usuario);

                try {                    
                    cBuscando.iniciarVistaBuscando();
                    menu.setVisible(false);
                } catch (IOException ex) {
                    Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private boolean validarSeleccionColor(){
        boolean seleccionado = true;
        
        Color defecto = new Color(51,51,51);
        Color colorX = new Color(menu.labelX.getBackground().getRGB());
        Color colorO = new Color(menu.labelO.getBackground().getRGB());
        
        if(colorX.getRGB() == defecto.getRGB() && colorO.getRGB() == defecto.getRGB()){
            seleccionado = false;
            JOptionPane.showMessageDialog(null, "¡Selecciona X o O!");
        }
        
        return seleccionado;
    }

    private boolean validarInputs() {

        if (menu.campoTextoSocket.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El hay que especificar el socket");
            return false;

        } else {

            try {
                int numero = Integer.parseInt(menu.campoTextoNumero.getText());
                if(menu.campoTextoIP.getText().equalsIgnoreCase("localhost") &&
                        (numero >= 0 && numero <= 100)){
                    return true;
                }else{
                    int socket = Integer.parseInt(menu.campoTextoSocket.getText());
                    String[] IP = menu.campoTextoIP.getText().split("\\.");

                    if ((socket >= 0 && socket <= 9999) && (IP.length == 4)&& (numero >= 0 && numero <= 100)) {
                        return true;
                    } else {
                        return false;
                    }
                }  
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "IP incompleta o inexistente\n o \nnumero no valido");

            }

        }
        return false;

    }
    
    public void cargarGIFS(){
        
        ImageIcon gif = new ImageIcon("./imagenes/x.gif");
        Image imagen = gif.getImage().getScaledInstance(menu.labelX.getWidth(),menu.labelX.getHeight(),0);
        menu.labelX.setIcon(new ImageIcon(imagen));
        
        ImageIcon gif2 = new ImageIcon("./imagenes/o.gif");
        Image imagen2 = gif2.getImage().getScaledInstance(menu.labelO.getWidth(),menu.labelO.getHeight(),0);
        menu.labelO.setIcon(new ImageIcon(imagen2));
    }
    
    private void accionesLabel() {

        menu.labelX.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent arg0) {
                Color rojo = new Color(204, 0, 0);
                Color defecto = new Color(51, 51, 51);
                menu.labelX.setBackground(rojo);
                menu.labelO.setBackground(defecto);
                seleccion = 'x';
            }

            public void mouseEntered(MouseEvent arg0) {

            }

            public void mouseExited(MouseEvent arg0) {

            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });

        menu.labelO.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent arg0) {
                Color rojo = new Color(204, 0, 0);
                Color defecto = new Color(51, 51, 51);
                menu.labelO.setBackground(rojo);
                menu.labelX.setBackground(defecto);
                seleccion = 'o';
            }

            public void mouseEntered(MouseEvent arg0) {

            }

            public void mouseExited(MouseEvent arg0) {

            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });

    }

}
