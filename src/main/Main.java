package main;

import bbdd.Persistencia;
import controlador.Controlador;
import modelo.EjObjeto;
import vista.PanelForm;
import vista.PanelShow;
import vista.VistaPrincipal;


public class Main {
	
	public static final int WIDTH_VISTA_PRINCIPAL = 600;
	public static final int HEIGHT_VISTA_PRINCIPAL = 400;
	
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// la vista: 
				VistaPrincipal vistaPrincipal = new VistaPrincipal();
				
				// los paneles 
				PanelForm panelForm = new PanelForm();
				PanelShow panelShow = new PanelShow();
				
				// y el control: 
				Controlador controlador = new Controlador(vistaPrincipal);
				controlador.setPanelForm(panelForm);
				controlador.setPanelShow(panelShow);
				
				// configura la vista 
				vistaPrincipal.setControlador(controlador);
				panelForm.setControlador(controlador);
				panelShow.setControlador(controlador);

				
				// y arranca la interfaz (vista): 
				vistaPrincipal.hacerVisible();
				
				//mostrar PanelShow
				panelShow.setDatos(new Persistencia().obtenerLista());
				vistaPrincipal.definirPanel(panelShow);	
			}
		});

	}

}
