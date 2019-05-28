package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import bbdd.Persistencia;
import modelo.EjObjeto;
import modelo.ReturnDatos;
import util.Log;
import vista.PanelForm;
import vista.PanelShow;
import vista.VistaPrincipal;

public class Controlador implements ActionListener {

	private VistaPrincipal vistaPrincipal;
	private PanelForm panelForm;
	private PanelShow panelShow;
	Log log = new Log(this.getClass().getSimpleName());
	
	public Controlador(VistaPrincipal vPrincipal) {
		this.vistaPrincipal = vPrincipal;
	}
	
	public void setVistaPrincipal(VistaPrincipal vistaPrincipal) {
		this.vistaPrincipal = vistaPrincipal;
	}

	public void setPanelForm(PanelForm panelForm) {
		this.panelForm = panelForm;
	}

	public void setPanelShow(PanelShow panelShow) {
		this.panelShow = panelShow;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//PersistenciaFake persistenciaFake = new PersistenciaFake();
		Persistencia persistencia = new Persistencia();
		
		log.print("INICIO DEL CONTROLADOR");
		Object opcion = e.getSource();
		
		
		if (opcion instanceof JMenuItem) {		//MENU
			log.print("JMenuItem");
			if (((JMenuItem) opcion).equals(vistaPrincipal.getMntmAdd())) {
				log.print("getMntmAdd");
				panelForm.limpiar();
				vistaPrincipal.definirPanel(panelForm);
				panelForm.tipoPanel(PanelForm.INSERTAR);
			} else if (((JMenuItem) opcion).equals(vistaPrincipal.getMntmShow())) {
				log.print("getMntmShow");
				ArrayList<EjObjeto> lista = persistencia.obtenerLista();
				panelShow.setDatos(lista);
				vistaPrincipal.definirPanel(panelShow);	
			}
		} else if(opcion instanceof JButton) {		//BOTONES
			log.print("JButton");
			if (((JButton) opcion).equals(panelForm.getBtnInsertarModificar())) {	//Insertar o modificar
				insertarModificar(persistencia);
			} else if (((JButton) opcion).equals(panelShow.getBtnModificar())) {	//Ir a modificar
				irModificar();
			} else if (((JButton) opcion).equals(panelShow.getBtnEliminar())) {		//Eliminar
				eliminar(persistencia);								
			} else if (((JButton) opcion).equals(panelShow.getBtnFiltrar())) {		//Filtrar
				String filtro = panelShow.getTxtFiltro();
				log.iniFunc("Filtrar", filtro);
				ArrayList<EjObjeto> lista = panelShow.getListaEjObjeto(filtro);
				panelShow.rellenarTabla(lista);					
			}
		}

		
		log.space();
	
	}

	
	private void insertarModificar(Persistencia persistencia) {			//INSERTAR y MODIFICAR
		EjObjeto ejObjeto = null;
		ReturnDatos ret = panelForm.getDatos();
		
		if (ret.getError() == null) {
			ejObjeto = (EjObjeto) ret.getObject();
			log.iniFunc(new Object(){}.getClass().getEnclosingMethod().getName(), ejObjeto.toString());
			
				int res = 0;
				if(ejObjeto.getId() == -2) {	//insertar
					res = persistencia.guardar(ejObjeto);
					
					if (res == 1) {
						vistaPrincipal.setMensaje(VistaPrincipal.INFO, "Objeto guardado");
						panelForm.limpiar();
					}
				} else {						//modificar
					res = persistencia.modificar(ejObjeto);
					
					if (res == 1) {
						panelShow.rellenarTabla(persistencia.obtenerLista());
						vistaPrincipal.setMensaje(VistaPrincipal.INFO, "Objeto modificado");
						vistaPrincipal.definirPanel(panelShow);	
					}	
				}

				if(res != 1) {
					error("ERROR al guardar en la base de datos");
				}
						
		} else {
			error(ret.getError());
		}
		
	}
	

	private void irModificar() {										//IR A MODIFICAR
		EjObjeto ejObjeto = panelShow.getDatoDeTabla();
		log.iniFunc(new Object(){}.getClass().getEnclosingMethod().getName(), ejObjeto.toString());
		if (ejObjeto.getEjInt() != -1) {
			panelForm.setDatos(ejObjeto);
			panelForm.tipoPanel(PanelForm.MODIFICAR);
			vistaPrincipal.definirPanel(panelForm);
		} else {
			error("Selecciona una fila");
		}
	}

	private void eliminar(Persistencia persistencia) {			//ELIMINAR
				EjObjeto ejObjeto= panelShow.getDatoDeTabla();
				int id = ejObjeto.getId();
				log.iniFunc(new Object(){}.getClass().getEnclosingMethod().getName(), id+"");
				if(id != -1) {
					int res = persistencia.eliminar(id);
					if (res == 1) {
						panelShow.eliminarObjeto();
						vistaPrincipal.setMensaje(VistaPrincipal.INFO, "el objeto " + ejObjeto.getEjString() + " ha sido eliminado");
					} else {
						error("ERROR al eliminar el contacto");
					}	
				} else {
					error("Selecciona una fila");
				}
	}
	
	
	public void error(String mensaje) {
		vistaPrincipal.setMensaje(VistaPrincipal.INFO_ERROR, mensaje);
	}

}
