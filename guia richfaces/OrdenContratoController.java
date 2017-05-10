/**
 * 
 */
package pe.com.cmacpiura.logistica.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import pe.com.cmacpiura.logistica.beans.OrdenContratoBean;
import pe.com.cmacpiura.logistica.beans.ReportActionBean;
import pe.com.cmacpiura.logistica.beans.UsuarioBean;
import pe.com.cmacpiura.logistica.com.ArticuloConvenioMarco;
import pe.com.cmacpiura.logistica.com.ArticuloOrden;
import pe.com.cmacpiura.logistica.com.ArticuloPropuesta;
import pe.com.cmacpiura.logistica.com.ArticuloRequerimiento;
import pe.com.cmacpiura.logistica.com.CompraDirecta;
import pe.com.cmacpiura.logistica.com.Contrato;
import pe.com.cmacpiura.logistica.com.Mantenimiento;
import pe.com.cmacpiura.logistica.com.ModalidadCompra;
import pe.com.cmacpiura.logistica.com.Objeto;
import pe.com.cmacpiura.logistica.com.Orden;
import pe.com.cmacpiura.logistica.com.Penalidad;
import pe.com.cmacpiura.logistica.com.Proceso;
import pe.com.cmacpiura.logistica.com.Propuesta;
import pe.com.cmacpiura.logistica.com.Proveedor;
import pe.com.cmacpiura.logistica.com.RequerimientoProceso;
import pe.com.cmacpiura.logistica.com.RequerimientoProcesoExonerado;
import pe.com.cmacpiura.logistica.data.AdminComprasDirectas;
import pe.com.cmacpiura.logistica.data.AdminOrdenesContratos;
import pe.com.cmacpiura.logistica.data.AdminOrigenOrden;
import pe.com.cmacpiura.logistica.data.AdminParametros;
import pe.com.cmacpiura.logistica.data.AdminProcesos;
import pe.com.cmacpiura.logistica.data.AdminPropuestas;
import pe.com.cmacpiura.logistica.data.AdminRequerimientosExonerados;
import pe.com.cmacpiura.logistica.data.AdminRequerimientosProceso;
import pe.com.cmacpiura.logistica.data.AdminTipoOrden;
import pe.com.cmacpiura.logistica.util.Util;

/**
 * @author Juan Lama
 *
 */
public class OrdenContratoController extends AbstractController {
	
	private static Logger logger = Logger.getLogger(OrdenContratoController.class);
	
	public static final String ORDENCONTRATOBEAN = "ordenContratoBean"; 

	public OrdenContratoController() {	
	}
		
	public void filtrarOrdenContratoParaPenalidad() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			oOrdenContratoBean.getListaOrdenesContratos().clear();
			resetearMensajeError(facesContext);
			
			int tipoDocumento = 0;
			String descripcionRequerimiento = null;
			long ruc = 0;
			String razonSocial = null;
			long numero = 0;
			long year = 0;
			Calendar fechaEmision = null;
			
			if( oOrdenContratoBean.isFiltroAnioSeleccionado() ) {
				year = oOrdenContratoBean.getAnio();
			}
			
			if( oOrdenContratoBean.isFiltroNumeroSeleccionado() ) {
				numero = oOrdenContratoBean.getNumero();
			}
			
			if( oOrdenContratoBean.isFiltroDescripcionRequerimientoSeleccionado() ) {
				descripcionRequerimiento = oOrdenContratoBean.getDescripcionRequerimiento();
			}
			
			if( oOrdenContratoBean.isFiltroRazonSocialSeleccionado() ) {
				razonSocial = oOrdenContratoBean.getRazonSocial();
			}
			
			if( oOrdenContratoBean.isFiltroRUCSeleccionado() ) {
				ruc = oOrdenContratoBean.getNroRuc();
			}
			
			if( oOrdenContratoBean.isFiltroFechaEmisionSeleccionado() ) {
				fechaEmision = Util.getCalendar(oOrdenContratoBean.getFechaEmision());
			}
			
			if(  oOrdenContratoBean.isFiltroTipoOrdenSeleccionado() 
					&& oOrdenContratoBean.getComboBoxItemTipoOrden() != null 
					&& oOrdenContratoBean.getComboBoxItemTipoOrden().getValue() != null) {
				tipoDocumento = oOrdenContratoBean.getComboBoxItemTipoOrden().getValue().getCodigo();
			}
			
			try {
				oOrdenContratoBean.getListaOrdenesContratos().addAll( 
						AdminOrdenesContratos.buscar(
								tipoDocumento, descripcionRequerimiento, ruc, razonSocial, numero, year, fechaEmision) );
				if( oOrdenContratoBean.getListaOrdenesContratos().isEmpty() ) {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,"*",
							"No hay resultados, es posible que la orden de compra/orden de servicio/contrato consultado está anulado o resuelta");
					facesContext.addMessage(null, oFacesMessage);
				} else {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,"*",
							"Se han obtenido ".concat(Long.toString(oOrdenContratoBean.getListaOrdenesContratos().size())).
							concat(" órdenes"));
					facesContext.addMessage(null, oFacesMessage);
				}
			} catch (Exception e) {
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,"*",e.getMessage());
				facesContext.addMessage(null, oFacesMessage);
				mostrarMensajeError("", facesContext);
				logger.error(e.toString(), e);
			}
		}
	}
	
	public void filtrarContratosParaComplementaria() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			oOrdenContratoBean.getListaOrdenesContratos().clear();
			mostrarMensajeResultado("", facesContext);
			
			int tipoDocumento = AdminTipoOrden.CONTRATO;
			String descripcionRequerimiento = null;
			long ruc = 0;
			String razonSocial = null;
			long numero = 0;
			long year = 0;
			Calendar fechaEmision = null;
			
			if( oOrdenContratoBean.isFiltroAnioSeleccionado() ) {
				year = oOrdenContratoBean.getAnio();
			}
			
			if( oOrdenContratoBean.isFiltroNumeroSeleccionado() ) {
				numero = oOrdenContratoBean.getNumero();
			}
			
			if( oOrdenContratoBean.isFiltroDescripcionRequerimientoSeleccionado() ) {
				descripcionRequerimiento = oOrdenContratoBean.getDescripcionRequerimiento();
			}
			
			if( oOrdenContratoBean.isFiltroRUCSeleccionado() ) {
				ruc = oOrdenContratoBean.getNroRuc();
			}
			
			if( oOrdenContratoBean.isFiltroRazonSocialSeleccionado() ) {
				razonSocial = oOrdenContratoBean.getRazonSocial();
			}
			
			if( oOrdenContratoBean.isFiltroFechaEmisionSeleccionado() ) {
				fechaEmision = Util.getCalendar(oOrdenContratoBean.getFechaEmision());
			}
			
			try {
				
				oOrdenContratoBean.getListaOrdenesContratos().addAll( 
						AdminOrdenesContratos.buscar(
								tipoDocumento, descripcionRequerimiento, ruc, razonSocial, numero, year, fechaEmision) );
				mostrarMensajeResultado("Se han obtenido ".
						concat(Long.toString(oOrdenContratoBean.getListaOrdenesContratos().size())).
						concat(" contratos"), facesContext);
			} catch (Exception e) {
				mostrarMensajeError(e.getMessage(), facesContext);
				logger.error(e.toString(), e);
			}
		}
	}
	
	public void cargarOrdenSeleccionadaParaPenalidad() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			Contrato oContrato = oOrdenContratoBean.getOrdenContrato();
			oOrdenContratoBean.setYearOrdenContrato( oContrato.getYear() );
			oOrdenContratoBean.setNumeroOrdenContrato( oContrato.getNumero() );
			oOrdenContratoBean.setSerieOrdenContrato( oContrato.getSerie() );
			oOrdenContratoBean.setCodigoOrden( oContrato.getCodigo() );
			oOrdenContratoBean.setTipoOrden( oContrato.getTipoOrden() );
			oOrdenContratoBean.setOrigenOrdenContrato( oContrato.getOrigen() );
			oOrdenContratoBean.setDescripcionOrdenContrato( oContrato.getDescripcionRequerimiento() );
			oOrdenContratoBean.setProveedor( oContrato.getProveedor() );
			oOrdenContratoBean.setFechaIngreso( oContrato.getFechaIngreso() );
			oOrdenContratoBean.setAfectoIGV(oContrato.isAfectoIGV());
			oOrdenContratoBean.setAfectoImpuestoRenta(oContrato.isImpuestoRenta());
			
			switch (oContrato.getOrigen().getCodigo()) {
				case 1:
					try {
						CompraDirecta oCompraDirecta = AdminComprasDirectas.obtenerCompraDirecta(
									oContrato.getNumeroReferencia(), false);						
						oOrdenContratoBean.setDetalle("Compra Directa N° " .concat(
								Long.toString(oCompraDirecta.getNumero())).concat(oCompraDirecta.getSerie()).concat(" - ").concat(
								Long.toString(oCompraDirecta.getYear())));
					} catch (Exception e1) {
						oOrdenContratoBean.setDetalle("Información adicional no disponible");
					}
					break;
				case 3:
					try {
						Proceso oProceso = AdminProcesos.obtenerProceso(oContrato.getNumeroReferencia());
						oOrdenContratoBean.setDetalle(oProceso.getTipoProceso().getDescripcion().concat( " N° " ).concat(
							Integer.toString(oProceso.getNumeroProceso())).concat(" - ").concat(
										Long.toString(oProceso.getYearProceso())));
					} catch (Exception e1) {
						oOrdenContratoBean.setDetalle("Información adicional no disponible");
					}
					break;
				default:
					oOrdenContratoBean.setDetalle("");
					break;
			}
			
			oOrdenContratoBean.getListaOrdenesContratos().clear();
			
			resetearMensajeError(facesContext);
			try {
				oOrdenContratoBean.getListaArticulosOrden().clear();
				oOrdenContratoBean.getListaArticulosOrden().addAll(
						AdminOrdenesContratos.obtenerArticulos( oOrdenContratoBean.getCodigoOrden(), false, true ) );
				//oContrato = AdminOrdenesContratos.obtenerContrato(oContrato.getCodigo());
				//oOrdenContratoBean.getListaArticulosOrden().addAll(oContrato.getArticulos());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	public void cargarOrdenSeleccionadaParaComplementaria() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			Contrato oContrato = oOrdenContratoBean.getOrdenContrato();
			oOrdenContratoBean.setYearOrdenContrato( oContrato.getYear() );
			oOrdenContratoBean.setNumeroOrdenContrato( oContrato.getNumero() );
			oOrdenContratoBean.setSerieOrdenContrato( oContrato.getSerie() );
			oOrdenContratoBean.setCodigoOrden( oContrato.getCodigo() );
			oOrdenContratoBean.setTipoOrden( oContrato.getTipoOrden() );
			oOrdenContratoBean.setOrigenOrdenContrato( oContrato.getOrigen() );
			oOrdenContratoBean.setDescripcionOrdenContrato( oContrato.getDescripcionRequerimiento() );
			oOrdenContratoBean.setProveedor( oContrato.getProveedor() );
			oOrdenContratoBean.setFechaIngreso( oContrato.getFechaIngreso() );
			oOrdenContratoBean.setAfectoIGV(oContrato.isAfectoIGV());
			oOrdenContratoBean.setAfectoImpuestoRenta(oContrato.isImpuestoRenta());
			
			switch (oContrato.getOrigen().getCodigo()) {
				case 1:
					try {
						CompraDirecta oCompraDirecta = AdminComprasDirectas.obtenerCompraDirecta(
									oContrato.getNumeroReferencia(), false);						
						oOrdenContratoBean.setDetalle("Compra Directa N° " .concat(
								Long.toString(oCompraDirecta.getNumero())).concat(oCompraDirecta.getSerie()).concat(" - ").concat(
								Long.toString(oCompraDirecta.getYear())));
						oContrato = AdminOrdenesContratos.obtenerContrato(oContrato.getCodigo());
						oOrdenContratoBean.setFechaComplementaria(oContrato.getFechaComplementaria());
						oOrdenContratoBean.setInformeAprobacionComplementaria(oContrato.getInformeAprobacionComplementaria());
						oOrdenContratoBean.setMontoComplementaria(oContrato.getMontoComplementaria());
					} catch (Exception e1) {
						oOrdenContratoBean.setDetalle("Información adicional no disponible");
					}
					break;
				case 3:
					try {
						Proceso oProceso = AdminProcesos.obtenerProceso(oContrato.getNumeroReferencia());
						oOrdenContratoBean.setDetalle(oProceso.getTipoProceso().getDescripcion().concat( " N° " ).concat(
							Integer.toString(oProceso.getNumeroProceso())).concat(" - ").concat(
										Long.toString(oProceso.getYearProceso())));
					} catch (Exception e1) {
						oOrdenContratoBean.setDetalle("Información adicional no disponible");
					}
					break;
				default:
					oOrdenContratoBean.setDetalle("");
					break;
			}
			
			oOrdenContratoBean.getListaOrdenesContratos().clear();
			
			resetearMensajeError(facesContext);
			try {
				oOrdenContratoBean.getListaArticulosOrden().clear();
				oOrdenContratoBean.getListaArticulosOrden().addAll(
						AdminOrdenesContratos.obtenerArticulos( oOrdenContratoBean.getCodigoOrden(), false, true ) );
				//oContrato = AdminOrdenesContratos.obtenerContrato(oContrato.getCodigo());
				//oOrdenContratoBean.getListaArticulosOrden().addAll(oContrato.getArticulos());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	public void cargarOrdenSeleccionada() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			Contrato oContrato = oOrdenContratoBean.getOrdenContrato();
			oOrdenContratoBean.setYearOrdenContrato( oContrato.getYear() );
			oOrdenContratoBean.setNumeroOrdenContrato( oContrato.getNumero() );
			oOrdenContratoBean.setSerieOrdenContrato( oContrato.getSerie() );
			oOrdenContratoBean.setCodigoOrden( oContrato.getCodigo() );
			oOrdenContratoBean.setTipoOrden( oContrato.getTipoOrden() );
			oOrdenContratoBean.setOrigenOrdenContrato( oContrato.getOrigen() );
			oOrdenContratoBean.setDescripcionOrdenContrato( oContrato.getDescripcionRequerimiento() );
			oOrdenContratoBean.setProveedor( oContrato.getProveedor() );
			oOrdenContratoBean.setFechaIngreso( oContrato.getFechaIngreso() );
			oOrdenContratoBean.setAfectoIGV(oContrato.isAfectoIGV());
			oOrdenContratoBean.setAfectoImpuestoRenta(oContrato.isImpuestoRenta());
			
			switch (oContrato.getOrigen().getCodigo()) {
				case 1:
					try {
						CompraDirecta oCompraDirecta = AdminComprasDirectas.obtenerCompraDirecta(
									oContrato.getNumeroReferencia(), false);						
						oOrdenContratoBean.setDetalle("Compra Directa N° " .concat(
							Long.toString(oCompraDirecta.getNumero())).concat(oCompraDirecta.getSerie()).concat(" - ").concat(
							Long.toString(oCompraDirecta.getYear())));
						oContrato = AdminOrdenesContratos.obtenerContrato(oContrato.getCodigo());
						oOrdenContratoBean.setFechaComplementaria(oContrato.getFechaComplementaria());
						oOrdenContratoBean.setInformeAprobacionComplementaria(oContrato.getInformeAprobacionComplementaria());
						oOrdenContratoBean.setMontoComplementaria(oContrato.getMontoComplementaria());
					} catch (Exception e1) {
						oOrdenContratoBean.setDetalle("Información adicional no disponible");
					}
					break;
				case 3:
					try {
						Proceso oProceso = AdminProcesos.obtenerProceso(oContrato.getNumeroReferencia());
						oOrdenContratoBean.setDetalle(oProceso.getTipoProceso().getDescripcion().concat( " N° " ).concat(
							Integer.toString(oProceso.getNumeroProceso())).concat(" - ").concat(
										Long.toString(oProceso.getYearProceso())));
					} catch (Exception e1) {
						oOrdenContratoBean.setDetalle("Información adicional no disponible");
					}
					break;
				default:
					oOrdenContratoBean.setDetalle("");
					break;
			}
			
			oOrdenContratoBean.getListaOrdenesContratos().clear();
			
			resetearMensajeError(facesContext);
			try {
				oOrdenContratoBean.getListaArticulosOrden().clear();
				oOrdenContratoBean.getListaArticulosOrden().addAll(
						AdminOrdenesContratos.obtenerArticulos( oOrdenContratoBean.getCodigoOrden(), true, true ) );
				//oContrato = AdminOrdenesContratos.obtenerContrato(oContrato.getCodigo());
				//oOrdenContratoBean.getListaArticulosOrden().addAll(oContrato.getArticulos());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	public void eliminarPenalidadSeleccionada() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			oOrdenContratoBean.getArticuloOrden().getPenalizacion().remove( oOrdenContratoBean.getPenalidad() );
		}
	}
	
	public void actualizaPenalidad() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			
			try{
				Long.parseLong(oOrdenContratoBean.getPenalidad().getStrDiasAtraso());
			}catch (Exception e) {
				FacesMessage oMessage = new FacesMessage();
				oMessage.setDetail("Los días atraso deben ser enteros");
				facesContext.addMessage( null , oMessage);
				return;
			}
			
			if( oOrdenContratoBean.getPenalidad().getDiasAtraso() <= AdminParametros.getNumeroDiasFactorPenalidad() ) {
				oOrdenContratoBean.getPenalidad().setMonto( Util.roundToDecimals(
															oOrdenContratoBean.getPenalidad().getDiasAtraso() *
															(AdminParametros.getPorcentajePenalidad() / 100.0) *
															oOrdenContratoBean.getArticuloOrden().getPrecio() /
															(oOrdenContratoBean.getArticuloOrden().getPlazo() *
															AdminParametros.getFactorPenalidadHastaNDias()) , 2) );
			} else {
				if( oOrdenContratoBean.getOrigenOrdenContrato().getCodigo() == 3 ) { //el origen es un proceso
					try {
						Proceso oProceso = AdminProcesos.obtenerProceso( oOrdenContratoBean.getOrdenContrato().getNumeroReferencia() );
						Objeto oObjeto = AdminRequerimientosProceso.obtenerDatosRequerimiento( 
												oProceso.getCodigoRequerimiento() ).getObjetoProceso();
						if( oObjeto.getCodigo() == 1 || oObjeto.getCodigo() == 2 ) {
							oOrdenContratoBean.getPenalidad().setMonto( Util.roundToDecimals(
																		oOrdenContratoBean.getPenalidad().getDiasAtraso() *
																		(AdminParametros.getPorcentajePenalidad() / 100.0) *
																		oOrdenContratoBean.getArticuloOrden().getPrecio() /
																		(oOrdenContratoBean.getArticuloOrden().getPlazo() *
																		AdminParametros.getFactorPenalidadPostNDiasBS()) , 2) );
						} else if( oObjeto.getCodigo() == 3 ) {
							oOrdenContratoBean.getPenalidad().setMonto( Util.roundToDecimals(
																		oOrdenContratoBean.getPenalidad().getDiasAtraso() *
																		(AdminParametros.getPorcentajePenalidad() / 100.0) *
																		oOrdenContratoBean.getArticuloOrden().getPrecio() /
																		(oOrdenContratoBean.getArticuloOrden().getPlazo() *
																		AdminParametros.getFactorPenalidadPostNDiasO()) , 2) );
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
	}
	
	public void agregarPenalidad() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			Penalidad oPenalidad = new Penalidad();
			oPenalidad.setFecha(Util.getOnlyFecha(Calendar.getInstance()));
			oPenalidad.setModificable(true);
			oOrdenContratoBean.getArticuloOrden().getPenalizacion().add( oPenalidad );
		}
	}
	
	public void guardarPenalidades() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			UsuarioBean oUsuarioBean = (UsuarioBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, LoginController.USUARIOBEAN);
			Contrato oContrato = new Contrato();
			oContrato.setCodigo( oOrdenContratoBean.getCodigoOrden() );
			oContrato.setArticulos( oOrdenContratoBean.getListaArticulosOrden() );
			
			try {
				resetearMensajeError(facesContext);
				if( AdminOrdenesContratos.nuevaPenalidad(oUsuarioBean.getUsuario(), oContrato) ) {
					RequestContext.getCurrentInstance().addCallbackParam("showDialog", true);
					facesContext.renderResponse();
				} else {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","No se pudo completar la operación");
					facesContext.addMessage(null, oFacesMessage);
					mostrarMensajeError("", facesContext);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*",e.getMessage());
				facesContext.addMessage(null, oFacesMessage);
				mostrarMensajeError("", facesContext);
				return;
			}
		}
	}
	
	@Override
	public void doReset() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			oOrdenContratoBean.setYearOrdenContrato( 0 );
			oOrdenContratoBean.setNumeroOrdenContrato( 0 );
			oOrdenContratoBean.setSerieOrdenContrato( null );
			oOrdenContratoBean.setCodigoOrden( 0 );
			oOrdenContratoBean.setTipoOrden( null );
			oOrdenContratoBean.setOrigenOrdenContrato( null );
			oOrdenContratoBean.setDescripcionOrdenContrato( null );
			oOrdenContratoBean.setProveedor( null );
			oOrdenContratoBean.getListaArticulosOrden().clear();
			oOrdenContratoBean.getListaOrdenesContratos().clear();
		}
	}
	
	public void construirFormatoOrdenComprayServicioConsulta() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			UsuarioBean oUsuarioBean = (UsuarioBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, LoginController.USUARIOBEAN);
			AdminOrdenesContratos.construyeFormatoOrdenComprayServicio(oUsuarioBean.getUsuario(), 
						oOrdenContratoBean.getOrdenContrato().getCodigo());
			ReportActionBean oReportActionBean = ReportActionBean.getInstanceReport();
			String reporte = null;
			switch (oOrdenContratoBean.getOrdenContrato().getTipoOrden().getCodigo()) {
				case 1:
					reporte = "FORMATOORDENCOMPRA";
					break;
				case 2:
					reporte = "FORMATOORDENSERVICIO";
					break;
				case 3:
					reporte = "FORMATOCONTRATO";
					break;
			}			
			try {
				oReportActionBean.setIdReport(reporte); 
				oReportActionBean.clearParameters();
				oReportActionBean.addValueParameter("PCODORD", Long.toString(oOrdenContratoBean.getOrdenContrato().getCodigo()));
				oReportActionBean.addValueParameter("PUSUCOD", oUsuarioBean.getUsuario());
			} catch (Exception e) {			
				logger.error(e.getMessage(), e);
			}
		}
		
	}
	
	public void construirFormatoOrdenComprayServicio(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			UsuarioBean oUsuarioBean = (UsuarioBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, LoginController.USUARIOBEAN);
			
			AdminOrdenesContratos.construyeFormatoOrdenComprayServicio(oUsuarioBean.getUsuario(), 
					oOrdenContratoBean.getCodigoOrden());
			ReportActionBean oReportActionBean = ReportActionBean.getInstanceReport();
			String reporte = null;
			switch (oOrdenContratoBean.getComboBoxItemTipoOrden().getValue().getCodigo()) {
				case 1:
					reporte = "FORMATOORDENCOMPRA";
					break;
				case 2:
					reporte = "FORMATOORDENSERVICIO";
					break;
				case 3:
					reporte = "FORMATOCONTRATO";
					break;
			}
			try {
				oReportActionBean.setIdReport(reporte); 
				oReportActionBean.clearParameters();
				oReportActionBean.addValueParameter("PCODORD", Long.toString(oOrdenContratoBean.getCodigoOrden()));
				oReportActionBean.addValueParameter("PUSUCOD", oUsuarioBean.getUsuario());
			} catch (Exception e) {			
				logger.error(e.getMessage(), e);
			}
		}		
	}
	
	public void agregarSeccionGarantiaPostVenta(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			oOrdenContratoBean.getArticuloOrden().setDetalleGarantia("");
			oOrdenContratoBean.getArticuloOrden().setFechaInicioGarantia(Util.getFechaActual());
			oOrdenContratoBean.getArticuloOrden().setFechaFinalGarantia(Util.getFechaActual());
		}
	}

	public void guardarGarantiaPostVenta() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			UsuarioBean oUsuarioBean = (UsuarioBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, LoginController.USUARIOBEAN);
			Contrato oContrato = new Contrato();
			oContrato.setCodigo( oOrdenContratoBean.getCodigoOrden() );
			oContrato.setArticulos( oOrdenContratoBean.getListaArticulosOrden() );
			
			try {
				resetearMensajeError(facesContext);
				if( AdminOrdenesContratos.nuevaGarantiaPostVenta(oUsuarioBean.getUsuario(), oContrato) ) {
					RequestContext.getCurrentInstance().addCallbackParam("showDialog", true);
					facesContext.renderResponse();
				} else {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","No se pudo completar la operación");
					facesContext.addMessage(null, oFacesMessage);
					mostrarMensajeError("", facesContext);
				}
			} catch (NullPointerException e) {
				logger.error(e.getMessage(), e);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","No se pudo completar la operación");
				facesContext.addMessage(null, oFacesMessage);
				return;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*",e.getMessage());
				facesContext.addMessage(null, oFacesMessage);
				return;
			}
		}
	}
	
	public void guardarComplementaria() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			UsuarioBean oUsuarioBean = (UsuarioBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, LoginController.USUARIOBEAN);
						
			try {
				resetearMensajeError(facesContext);
				if( oOrdenContratoBean.getOrdenContrato() == null ) {
					throw new Exception("No hay una orden/contrato seleccionada");
				}
				Contrato oContrato = AdminOrdenesContratos.obtenerContrato(oOrdenContratoBean.getOrdenContrato().getCodigo());
				if( oContrato.isFlagComplementaria() ) {
					throw new Exception("Ya hay registrada una complementaria para este contrato con fecha " + 
								Util.getFechaActa(oContrato.getFechaComplementaria()));
				}
				oContrato = oOrdenContratoBean.getOrdenContrato();
				oContrato.setFlagComplementaria(true);
				oContrato.setMontoComplementaria(oOrdenContratoBean.getMontoComplementaria());
				oContrato.setInformeAprobacionComplementaria(oOrdenContratoBean.getInformeAprobacionComplementaria());
				oContrato.setFechaComplementaria(oOrdenContratoBean.getFechaComplementaria());
				oContrato.setArticulos(oOrdenContratoBean.getListaArticulosOrden());
				
				if( AdminOrdenesContratos.nuevaComplementaria(oUsuarioBean.getUsuario(), oContrato) ) {
					RequestContext.getCurrentInstance().addCallbackParam("showDialog", true);
					facesContext.renderResponse();
				} else {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","No se pudo completar la operación");
					facesContext.addMessage(null, oFacesMessage);
					mostrarMensajeError("", facesContext);
				}
			} catch (NullPointerException e) {
				logger.error(e.getMessage(), e);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","No se pudo completar la operación");
				facesContext.addMessage(null, oFacesMessage);
				return;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*",e.getMessage());
				facesContext.addMessage(null, oFacesMessage);
				return;
			}
		}
	}
	
	public void filtrarComprasDirectas() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		if( facesContext != null ) {
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			
			if( oOrdenContratoBean.getFiltroDescripcion().isSeleccionado() 
					|| oOrdenContratoBean.getFiltroNumero().isSeleccionado()
					|| oOrdenContratoBean.getFiltroYear().isSeleccionado()
					|| oOrdenContratoBean.getFiltroSerie().isSeleccionado()
					|| oOrdenContratoBean.getFiltroFecha().isSeleccionado() ) {
				resetearMensajeError(facesContext);
				
				long numeroCompraDirecta = 0;
				long yearCompraDirecta = 0; 
				String serieCompraDirecta = null;
				String descripcion = null;
				Calendar fecha = null;
				
				boolean hayError = false;
				
				if( oOrdenContratoBean.getFiltroFecha().isSeleccionado() ) {
					if( oOrdenContratoBean.getFiltroFecha().getValue() != null ) {
						fecha = Calendar.getInstance();
						fecha.setTime(oOrdenContratoBean.getFiltroFecha().getValue());
					} else {
						FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Fecha no válida");
						facesContext.addMessage(null, oFacesMessage);
						hayError = true;
					}
				}
				
				try{
					if( oOrdenContratoBean.getFiltroNumero().isSeleccionado() ) {
						numeroCompraDirecta = Long.parseLong(oOrdenContratoBean.getFiltroNumero().getValue());
					}
				}catch (Exception e) {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Número de Compra Directa no válido");
					facesContext.addMessage(null, oFacesMessage);
					hayError = true;
				}
				
				try{
					if( oOrdenContratoBean.getFiltroYear().isSeleccionado() ) {
						yearCompraDirecta = Long.parseLong(oOrdenContratoBean.getFiltroYear().getValue());
					}
				}catch (Exception e) {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Año de Compra directa no válido ");
					facesContext.addMessage(null, oFacesMessage);
					hayError = true;
				}
				if( hayError ) {
					mostrarMensajeError("", facesContext);
					return;
				}
				
				if( oOrdenContratoBean.getFiltroDescripcion().isSeleccionado() ) {
					descripcion = oOrdenContratoBean.getFiltroDescripcion().getValue().toUpperCase();
				}
				
				if( oOrdenContratoBean.getFiltroSerie().isSeleccionado() ) {
					serieCompraDirecta = oOrdenContratoBean.getFiltroSerie().getValue().toUpperCase();
				}
				
				
				try {
					oOrdenContratoBean.getListaComprasDirectas().clear();
					oOrdenContratoBean.getListaComprasDirectas().addAll(
							AdminComprasDirectas.buscarComprasDirectas(numeroCompraDirecta, yearCompraDirecta, 
									serieCompraDirecta, descripcion, fecha));
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,"*","Se han obtenido ".concat( 
							Integer.toString(oOrdenContratoBean.getListaComprasDirectas().size())
							).concat(" compras directas"));
					facesContext.addMessage(null, oFacesMessage);
				} catch (Exception e) {
					mostrarMensajeError(e.getMessage(), facesContext);
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*",e.getMessage());
			facesContext.addMessage(null, oFacesMessage);
				}
			} else {
				mostrarMensajeError("", facesContext);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*",
						"Debe de escoger por lo menos un filtro");
				facesContext.addMessage(null, oFacesMessage);
			}
		}
	}
	
	public void filtrarProcesos() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		if( facesContext != null ) {
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			
			if( oOrdenContratoBean.getFiltroDescripcion().isSeleccionado() 
					|| oOrdenContratoBean.getFiltroNumero().isSeleccionado()
					|| oOrdenContratoBean.getFiltroYear().isSeleccionado()
					|| oOrdenContratoBean.isFiltroTipoProceso()
					|| oOrdenContratoBean.getFiltroFecha().isSeleccionado() ) {
				resetearMensajeError(facesContext);
				
				long numeroCompraDirecta = 0;
				long yearCompraDirecta = 0;
				String descripcion = null;
				Calendar fecha = null;
				long tipoProceso = 0;
				
				boolean hayError = false;
				
				if( oOrdenContratoBean.getFiltroFecha().isSeleccionado() ) {
					if( oOrdenContratoBean.getFiltroFecha().getValue() != null ) {
						fecha = Calendar.getInstance();
						fecha.setTime(oOrdenContratoBean.getFiltroFecha().getValue());
					} else {
						FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Fecha no válida");
						facesContext.addMessage(null, oFacesMessage);
						hayError = true;
					}
				}
				
				try{
					if( oOrdenContratoBean.getFiltroNumero().isSeleccionado() ) {
						numeroCompraDirecta = Long.parseLong(oOrdenContratoBean.getFiltroNumero().getValue());
					}
				}catch (Exception e) {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Número de Proceso no válido");
					facesContext.addMessage(null, oFacesMessage);
					hayError = true;
				}
				
				try{
					if( oOrdenContratoBean.getFiltroYear().isSeleccionado() ) {
						yearCompraDirecta = Long.parseLong(oOrdenContratoBean.getFiltroYear().getValue());
					}
				}catch (Exception e) {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Año de Proceso no válido ");
					facesContext.addMessage(null, oFacesMessage);
					hayError = true;
				}
				if( oOrdenContratoBean.getFiltroDescripcion().isSeleccionado() ) {
					descripcion = oOrdenContratoBean.getFiltroDescripcion().getValue().toUpperCase();
				}
				
				if( oOrdenContratoBean.isFiltroTipoProceso() ) {
					if( !(oOrdenContratoBean.getComboBoxItemTipoProceso() == null 
							|| oOrdenContratoBean.getComboBoxItemTipoProceso().getValue() == null) ) {
						tipoProceso = oOrdenContratoBean.getComboBoxItemTipoProceso().getValue().getCodigo();
					} else {
						FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Tipo de Proceso no seleccionado");
						facesContext.addMessage(null, oFacesMessage);
						hayError = true;
					}
				}
				if( hayError ) {
					mostrarMensajeError("", facesContext);
					return;
				}
				try {
					oOrdenContratoBean.getListaProcesos().clear();
					oOrdenContratoBean.getListaProcesos().addAll(
							AdminProcesos.buscarProcesos(numeroCompraDirecta, yearCompraDirecta, 
									tipoProceso, descripcion, Util.formatearFechaBaseDatos(fecha)) );
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,"*","Se han obtenido ".concat( 
							Integer.toString(oOrdenContratoBean.getListaProcesos().size())
							).concat(" procesos"));
					facesContext.addMessage(null, oFacesMessage);
				} catch (Exception e) {
					mostrarMensajeError(e.getMessage(), facesContext);
				}
				
			} else {
				mostrarMensajeError("", facesContext);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Debe de escoger por lo menos un filtro");
				facesContext.addMessage(null, oFacesMessage);
			}
		}
	}
	
	public void filtrarProcesosExonerados() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		if( facesContext != null ) {
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			
			if( oOrdenContratoBean.getFiltroDescripcion().isSeleccionado() 
					|| oOrdenContratoBean.getFiltroNumero().isSeleccionado()
					|| oOrdenContratoBean.getFiltroYear().isSeleccionado()
					|| oOrdenContratoBean.isFiltroTipoProceso()
					|| oOrdenContratoBean.getFiltroFecha().isSeleccionado() ) {
				resetearMensajeError(facesContext);
				
				long numeroCompraDirecta = 0;
				long yearCompraDirecta = 0;
				String descripcion = null;
				Calendar fecha = null;
				long tipoProceso = 0;
				
				boolean hayError = false;
				
				if( oOrdenContratoBean.getFiltroFecha().isSeleccionado() ) {
					if( oOrdenContratoBean.getFiltroFecha().getValue() != null ) {
						fecha = Calendar.getInstance();
						fecha.setTime(oOrdenContratoBean.getFiltroFecha().getValue());
					} else {
						FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Fecha no válida");
						facesContext.addMessage(null, oFacesMessage);
						hayError = true;
					}
				}
				
				try{
					if( oOrdenContratoBean.getFiltroNumero().isSeleccionado() ) {
						numeroCompraDirecta = Long.parseLong(oOrdenContratoBean.getFiltroNumero().getValue());
					}
				}catch (Exception e) {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Número de Proceso no válido");
					facesContext.addMessage(null, oFacesMessage);
					hayError = true;
				}
				
				try{
					if( oOrdenContratoBean.getFiltroYear().isSeleccionado() ) {
						yearCompraDirecta = Long.parseLong(oOrdenContratoBean.getFiltroYear().getValue());
					}
				}catch (Exception e) {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Año de Proceso no válido ");
					facesContext.addMessage(null, oFacesMessage);
					hayError = true;
				}
				if( oOrdenContratoBean.getFiltroDescripcion().isSeleccionado() ) {
					descripcion = oOrdenContratoBean.getFiltroDescripcion().getValue().toUpperCase();
				}
				
				if( oOrdenContratoBean.isFiltroTipoProceso() ) {
					if( !(oOrdenContratoBean.getComboBoxItemTipoProceso() == null 
							|| oOrdenContratoBean.getComboBoxItemTipoProceso().getValue() == null) ) {
						tipoProceso = oOrdenContratoBean.getComboBoxItemTipoProceso().getValue().getCodigo();
					} else {
						FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Tipo de Proceso no seleccionado");
						facesContext.addMessage(null, oFacesMessage);
						hayError = true;
					}
				}
				if( hayError ) {
					mostrarMensajeError("", facesContext);
					return;
				}
				try {
					oOrdenContratoBean.getListaProcesos().clear();
					oOrdenContratoBean.getListaProcesos().addAll(
							AdminProcesos.buscarProcesosExonerados(numeroCompraDirecta, yearCompraDirecta, 
									tipoProceso, descripcion, Util.formatearFechaBaseDatos(fecha)) );
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,"*","Se han obtenido ".concat( 
							Integer.toString(oOrdenContratoBean.getListaProcesos().size())
							).concat(" procesos"));
					facesContext.addMessage(null, oFacesMessage);
				} catch (Exception e) {
					mostrarMensajeError(e.getMessage(), facesContext);
				}
				
			} else {
				mostrarMensajeError("", facesContext);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Debe de escoger por lo menos un filtro");
				facesContext.addMessage(null, oFacesMessage);
			}
		}
	}
	
	public void limpiarBusquedaCompraDirecta(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			resetearMensajeError(facesContext);
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
					getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			
			resetearMensajeError(facesContext);
			
			oOrdenContratoBean.setFiltroDescripcion(null);
			oOrdenContratoBean.setFiltroFecha(null);
			oOrdenContratoBean.setFiltroNumero(null);
			oOrdenContratoBean.setFiltroSerie(null);
			oOrdenContratoBean.setFiltroYear(null);
			
			oOrdenContratoBean.setSeleccionRequerimiento(false);
			
			oOrdenContratoBean.getListaComprasDirectas().clear();
			
			CompraDirecta oCompraDirecta = null;
			try {
				oCompraDirecta = AdminComprasDirectas.obtenerCompraDirecta(
						oOrdenContratoBean.getCompraDirecta().getCodigoRequerimiento());
				
				if( oCompraDirecta.isAfectoIGV() ) {
					if( oCompraDirecta.getTipoComprobantePago().isEmiteComprobante() ) {
						switch (oCompraDirecta.getTipoComprobantePago().getCodigo()) {
							case 1:
							case 2:
								oOrdenContratoBean.setAfectoIGV(true);
								break;
							case 3:
								oOrdenContratoBean.setAfectoImpuestoRenta(true);
								break;
						}
					}
				}
				
				oOrdenContratoBean.setProveedor(oCompraDirecta.getProveedor());
				oOrdenContratoBean.getListaArticulosOrden().clear();
				
				ArticuloOrden oArticuloOrden = null;
				for( ArticuloRequerimiento oArticuloRequerimiento : oCompraDirecta.getArticulos() ) {
					oArticuloOrden = new ArticuloOrden();
					oArticuloOrden.setCantidad(oArticuloRequerimiento.getCantidad());
					oArticuloOrden.setEspecificaciones(oArticuloRequerimiento.getEspecificaciones());
					oArticuloOrden.setPrecio(oArticuloRequerimiento.getPrecioUnitario());
					oArticuloOrden.setConsecutivo(oArticuloRequerimiento.getConsecutivo());
					oArticuloOrden.setCodigo(oArticuloRequerimiento.getArticulo().getCodigo());
					oArticuloOrden.setDescripcion(oArticuloRequerimiento.getArticulo().getDescripcion());
					oArticuloOrden.setUnidadMedida(oArticuloRequerimiento.getArticulo().getUnidadMedida());
					oArticuloOrden.setPlazo(oCompraDirecta.getPlazoEntrega());
					oArticuloOrden.setMarca("");
					oArticuloOrden.setModelo("");
					oArticuloOrden.setAplicaMatenimiento(false);
					oArticuloOrden.setMantenimientos(new ArrayList<Mantenimiento>());
					oOrdenContratoBean.getListaArticulosOrden().add(oArticuloOrden);
				}
			} catch (Exception e) {
				mostrarMensajeError(e.getMessage(), facesContext);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Debe de escoger por lo menos un filtro");
				facesContext.addMessage(null, oFacesMessage);
			}
		}
	}
	
	public void limpiarBusquedaProcesos(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			resetearMensajeError(facesContext);
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
					getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			
			resetearMensajeError(facesContext);
			
			oOrdenContratoBean.setFiltroDescripcion(null);
			oOrdenContratoBean.setFiltroFecha(null);
			oOrdenContratoBean.setFiltroNumero(null);
			oOrdenContratoBean.setFiltroYear(null);
			
			oOrdenContratoBean.setSeleccionRequerimiento(false);
			
			oOrdenContratoBean.getListaProcesos().clear();
			oOrdenContratoBean.getListaPropuestas().clear();
			oOrdenContratoBean.getListaArticulosOrden().clear();
			
			try {
				oOrdenContratoBean.getListaPropuestas().addAll(AdminPropuestas.obtenerPropuestasParaOrdenesyContratos(
						oOrdenContratoBean.getProceso().getCodigoProceso()));
			} catch (Exception e) {
				mostrarMensajeError(e.getMessage(), facesContext);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Debe de escoger por lo menos un filtro");
				facesContext.addMessage(null, oFacesMessage);
			}
		}
	}
	
	public void limpiarBusquedaProcesosExonerados(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			resetearMensajeError(facesContext);
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
					getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			
			resetearMensajeError(facesContext);
			
			oOrdenContratoBean.setFiltroDescripcion(null);
			oOrdenContratoBean.setFiltroFecha(null);
			oOrdenContratoBean.setFiltroNumero(null);
			oOrdenContratoBean.setFiltroYear(null);
			
			oOrdenContratoBean.setSeleccionRequerimiento(false);
			
			oOrdenContratoBean.getListaProcesos().clear();
			oOrdenContratoBean.getListaPropuestas().clear();
			oOrdenContratoBean.getListaArticulosOrden().clear();
			
			try {
				List<Proveedor> listaProveedores =  AdminRequerimientosExonerados.obtenerProveedoresConRequerimientoExonerado( 
														oOrdenContratoBean.getProceso().getCodigoProceso() );
				Propuesta oPropuesta = null;
				for( Proveedor oProveedor : listaProveedores ) {
					oPropuesta = new Propuesta();
					oPropuesta.setProveedor(oProveedor);
					oOrdenContratoBean.getListaPropuestas().add(oPropuesta);
				}
			} catch (Exception e) {
				mostrarMensajeError(e.getMessage(), facesContext);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Debe de escoger por lo menos un filtro");
				facesContext.addMessage(null, oFacesMessage);
			}
		}
	}
	
	public void cargarArticulosPropuestaParaOrden() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ) {
			resetearMensajeError(facesContext);
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
					getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			oOrdenContratoBean.getListaArticulosOrden().clear();
			oOrdenContratoBean.setAfectoIGV(oOrdenContratoBean.getPropuesta().isAfectoIGV());
			ArticuloOrden oArticuloOrden = null;
			for( ArticuloPropuesta oArticuloPropuesta : oOrdenContratoBean.getPropuesta().getArticulos() ) {
				oArticuloOrden = new ArticuloOrden();
				oArticuloOrden.setCantidad( oArticuloPropuesta.getCantidadGanada() );
				oArticuloOrden.setCodigo( oArticuloPropuesta.getArticulo().getCodigo() );
				oArticuloOrden.setConsecutivo(oArticuloPropuesta.getConsecutivo());
				oArticuloOrden.setEspecificaciones(oArticuloPropuesta.getEspecificaciones());
				oArticuloOrden.setPrecio(oArticuloPropuesta.getPrecio());				
				oArticuloOrden.setDescripcion(oArticuloPropuesta.getArticulo().getDescripcion());
				oArticuloOrden.setUnidadMedida(oArticuloPropuesta.getArticulo().getUnidadMedida());
				oArticuloOrden.setMarca(oArticuloPropuesta.getMarca());
				oArticuloOrden.setModelo(oArticuloPropuesta.getModelo());
				oArticuloOrden.setAplicaMatenimiento(false);
				oArticuloOrden.setMantenimientos(new ArrayList<Mantenimiento>());
				oOrdenContratoBean.getListaArticulosOrden().add(oArticuloOrden);
			}
		}
	}
	
	public void cargarArticulosRequerimientoExoneradoParaOrden() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ) {
			resetearMensajeError(facesContext);
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
					getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			oOrdenContratoBean.getListaArticulosOrden().clear();
			RequerimientoProcesoExonerado oRequerimientoProcesoExonerado = null;
			try {
				oRequerimientoProcesoExonerado = AdminRequerimientosExonerados.obtenerRequerimientoProcesoExonerado( 
						oOrdenContratoBean.getProceso().getCodigoProceso() , oOrdenContratoBean.getProveedor().getNroRuc());
				oOrdenContratoBean.setAfectoIGV( oRequerimientoProcesoExonerado.isAfectoIgv() );
				ArticuloOrden oArticuloOrden = null;
				int i = 1;
				for( ArticuloConvenioMarco oArticuloConvenioMarco : oRequerimientoProcesoExonerado.getArticulos() ) {
					oArticuloOrden = new ArticuloOrden();
					oArticuloOrden.setCantidad( oArticuloConvenioMarco.getCantidad() );
					oArticuloOrden.setCodigo( oArticuloConvenioMarco.getCodigo() );
					oArticuloOrden.setConsecutivo(i++);
					oArticuloOrden.setEspecificaciones(oArticuloConvenioMarco.getEspecificaciones());
					oArticuloOrden.setPrecio(oArticuloConvenioMarco.getPrecio());				
					oArticuloOrden.setDescripcion(oArticuloConvenioMarco.getDescripcion());
					oArticuloOrden.setUnidadMedida(oArticuloConvenioMarco.getUnidadMedida());
					oArticuloOrden.setMarca("");
					oArticuloOrden.setModelo("");
					oArticuloOrden.setAplicaMatenimiento(false);
					oArticuloOrden.setMantenimientos(new ArrayList<Mantenimiento>());
					oArticuloOrden.setPlazo( oRequerimientoProcesoExonerado.getPlazoEntrega() );
					oOrdenContratoBean.getListaArticulosOrden().add(oArticuloOrden);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
			
		}
	}
	
	public void agregarMantenimiento() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ) {
			resetearMensajeError(facesContext);
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
					getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			Mantenimiento oMantenimiento = new Mantenimiento();
			oMantenimiento.setFecha(Util.getFechaActual());
			oOrdenContratoBean.getArticuloOrden().getMantenimientos().add(oMantenimiento);
		}
	}
	
	public void eliminarMantenimiento() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ) {
			resetearMensajeError(facesContext);
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
					getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);			
			oOrdenContratoBean.getArticuloOrden().getMantenimientos().remove(oOrdenContratoBean.getMantenimiento());
		}
	}
	
	public void guardarOrdenContratoCompraDirecta() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ) {
			resetearMensajeError(facesContext);
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
					getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			UsuarioBean oUsuarioBean = (UsuarioBean)facesContext.getApplication().
					getELResolver().getValue(elContext, null, LoginController.USUARIOBEAN);
			try {
				if( oOrdenContratoBean.getCompraDirecta() == null ) {
					throw new Exception("Debe de seleccionar una compra directa");
				}
				if( oOrdenContratoBean.getComboBoxItemTipoOrden() == null ) {
					throw new Exception("Debe de seleccionar un tipo de orden");
				}
				if( oOrdenContratoBean.getFechaEmision() == null ) {
					throw new Exception("Debe de seleccionar una fecha");
				}
				Calendar calendarFechaActual = Util.getFechaActual();
				Calendar calendarFechaRegistro = Util.getCalendar(oOrdenContratoBean.getFechaEmision());
				
				int comparacion = Util.compare(calendarFechaActual, calendarFechaRegistro);
				
				if( comparacion == -1 ){
					throw new Exception("La Fecha que se ha introducido es posterior a la actual");
				}
				
				if( comparacion == 1 ){
					if( !oUsuarioBean.isUsuarioAdministrador() ){
						if( !LoginController.isAdministradorValido(facesContext) ) {
							throw new Exception("Se requiere clave de administrador para efectuar esta operación");
						}
					}
				}
				
				int i = 1;
				for( ArticuloOrden oArticuloOrden : oOrdenContratoBean.getListaArticulosOrden() ) {
					oArticuloOrden.setConsecutivo(i++);
				}
				
				if( oOrdenContratoBean.getComboBoxItemTipoOrden().getValue().getCodigo() == AdminTipoOrden.CONTRATO ){
					Contrato oContrato = null;
					oContrato = new Contrato();
					
					oContrato.setProveedor(oOrdenContratoBean.getProveedor());
					oContrato.setFechaIngreso(calendarFechaRegistro);
					oContrato.setTipoOrden(oOrdenContratoBean.getComboBoxItemTipoOrden().getValue());
					oContrato.setTotal(oOrdenContratoBean.getTotalListaArticulosOrden());
					oContrato.setNumeroReferencia(oOrdenContratoBean.getCompraDirecta().getCodigoRequerimiento());
					oContrato.setMoneda(oOrdenContratoBean.getCompraDirecta().getMoneda());
					oContrato.setOrigen(AdminOrigenOrden.obtenerOrigenOrden(AdminOrigenOrden.COMPRADIRECTA));
					oContrato.setModalidadCompra(new ModalidadCompra());
					oContrato.getModalidadCompra().setCodigo(1);
					
					oContrato.setArticulos(oOrdenContratoBean.getListaArticulosOrden());
					
					oContrato.setDocumento("");
					oContrato.setAfectoIGV(oOrdenContratoBean.isAfectoIGV());
					oContrato.setImpuestoRenta(oOrdenContratoBean.isAfectoImpuestoRenta());
					
					AdminOrdenesContratos.nuevoContrato(oUsuarioBean.getUsuario(), oContrato);
					oOrdenContratoBean.setCodigoOrden(oContrato.getCodigo());
					
					oOrdenContratoBean.setNumeroOrdenContrato(oContrato.getNumero());
					oOrdenContratoBean.setYearOrdenContrato(oContrato.getYear());
					oOrdenContratoBean.setSerieOrdenContrato(oContrato.getSerie());
					
					RequestContext.getCurrentInstance().addCallbackParam("showDialog", true);
					facesContext.renderResponse();
					
					//this.setMensajeGenerado("La operación ha sido realizada exitosamente para " + 
					//		this.getTipoOrden().getComboBoxItemSeleccion().getValue().getDescripcion() + 
					//		":\nNúmero: " + oContrato.getNumero() + " Año: " + oContrato.getYear() + 
					//		" Serie: " + oContrato.getSerie() );
									
				} else {
					Orden oOrden = null;
					oOrden = new Orden();
					oOrden.setProveedor(oOrdenContratoBean.getProveedor());
					oOrden.setFechaIngreso(calendarFechaRegistro);
					oOrden.setTipoOrden(oOrdenContratoBean.getComboBoxItemTipoOrden().getValue());
					
					oOrden.setTotal(oOrdenContratoBean.getTotalListaArticulosOrden());
					oOrden.setNumeroReferencia(oOrdenContratoBean.getCompraDirecta().getCodigoRequerimiento());
					oOrden.setMoneda(oOrdenContratoBean.getCompraDirecta().getMoneda());
					oOrden.setOrigen(AdminOrigenOrden.obtenerOrigenOrden(AdminOrigenOrden.COMPRADIRECTA));
					oOrden.setModalidadCompra(new ModalidadCompra());
					oOrden.getModalidadCompra().setCodigo(1);
					
					oOrden.setArticulos(oOrdenContratoBean.getListaArticulosOrden());
					
					oOrden.setAfectoIGV(oOrdenContratoBean.isAfectoIGV());
					oOrden.setImpuestoRenta(oOrdenContratoBean.isAfectoImpuestoRenta());
									
					AdminOrdenesContratos.nuevaOrden(oUsuarioBean.getUsuario(), oOrden);
					oOrdenContratoBean.setCodigoOrden(oOrden.getCodigo());
					
					oOrdenContratoBean.setNumeroOrdenContrato(oOrden.getNumero());
					oOrdenContratoBean.setYearOrdenContrato(oOrden.getYear());
					oOrdenContratoBean.setSerieOrdenContrato(oOrden.getSerie());
					
					RequestContext.getCurrentInstance().addCallbackParam("showDialog", true);
					facesContext.renderResponse();
					//this.setMensajeGenerado("La operación ha sido realizada exitosamente para " + 
					//this.getTipoOrden().getComboBoxItemSeleccion().getValue().getDescripcion() + ":\nNúmero: " + oOrden.getNumero() + " Año: " + oOrden.getYear() 
					//		+ ( oOrden.getSerie() != null && oOrden.getSerie().trim().length() > 0 ? " Serie: " + oOrden.getSerie() : "" ));
				}
				
			} catch (Exception e) {
				mostrarMensajeError(e.getMessage(), facesContext);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*",e.getMessage());
				facesContext.addMessage(null, oFacesMessage);
			}
		}
	}
	
	public void guardarOrdenContratoProceso() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ) {
			resetearMensajeError(facesContext);
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
					getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			UsuarioBean oUsuarioBean = (UsuarioBean)facesContext.getApplication().
					getELResolver().getValue(elContext, null, LoginController.USUARIOBEAN);
			try {
				boolean hayError = false;
				if( oOrdenContratoBean.getProceso() == null ) {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Debe de seleccionar un proceso");
					facesContext.addMessage(null, oFacesMessage);
					hayError = true;
				}
				if( oOrdenContratoBean.getProveedor() == null ) {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Debe de seleccionar un proveedor");
					facesContext.addMessage(null, oFacesMessage);
					hayError = true;
				}
				if( oOrdenContratoBean.getComboBoxItemTipoOrden() == null ) {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Debe de seleccionar un tipo de orden");
					facesContext.addMessage(null, oFacesMessage);
					hayError = true;
				}
				if( oOrdenContratoBean.getFechaEmision() == null ) {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","Debe de seleccionar una fecha");
					facesContext.addMessage(null, oFacesMessage);
					hayError = true;
				}
				if( hayError ) {
					mostrarMensajeError("", facesContext);
					return;
				}
				Calendar calendarFechaActual = Util.getFechaActual();
				Calendar calendarFechaRegistro = Util.getCalendar(oOrdenContratoBean.getFechaEmision());
				
				int comparacion = Util.compare(calendarFechaActual, calendarFechaRegistro);
				
				if( comparacion == -1 ){
					throw new Exception("La Fecha que se ha introducido es posterior a la actual");
				}
				
				if( comparacion == 1 ){
					if( !oUsuarioBean.isUsuarioAdministrador() ){
						if( !LoginController.isAdministradorValido(facesContext) ) {
							throw new Exception("Se requiere clave de administrador para efectuar esta operación");
						}
					}
				}
				
				int i = 1;
				for( ArticuloOrden oArticuloOrden : oOrdenContratoBean.getListaArticulosOrden() ) {
					if( oArticuloOrden.getPlazo() <= 0 ) {
						throw new Exception("Plazo no válido para el artículo " + oArticuloOrden.getDescripcion());
					}
					oArticuloOrden.setConsecutivo(i++);
				}
				RequerimientoProceso oRequerimientoProceso = 
					AdminRequerimientosProceso.obtenerRequerimiento( oOrdenContratoBean.getProceso().getCodigoRequerimiento() );
				if( oOrdenContratoBean.getComboBoxItemTipoOrden().getValue().getCodigo() == AdminTipoOrden.CONTRATO ){
					Contrato oContrato = null;
					oContrato = new Contrato();
					
					oContrato.setProveedor(oOrdenContratoBean.getProveedor());
					oContrato.setFechaIngreso(calendarFechaRegistro);
					oContrato.setTipoOrden(oOrdenContratoBean.getComboBoxItemTipoOrden().getValue());
					oContrato.setTotal(oOrdenContratoBean.getTotalListaArticulosOrden());
					oContrato.setNumeroReferencia(oOrdenContratoBean.getProceso().getCodigoProceso());
					oContrato.setMoneda(oRequerimientoProceso.getMoneda());
					oContrato.setOrigen(AdminOrigenOrden.obtenerOrigenOrden(AdminOrigenOrden.PROCESO));
					oContrato.setModalidadCompra(new ModalidadCompra());
					oContrato.getModalidadCompra().setCodigo(1);
					
					oContrato.setArticulos(oOrdenContratoBean.getListaArticulosOrden());
					
					oContrato.setDocumento("");
					oContrato.setAfectoIGV(oOrdenContratoBean.isAfectoIGV());
					oContrato.setImpuestoRenta(oOrdenContratoBean.isAfectoImpuestoRenta());
					
					AdminOrdenesContratos.nuevoContrato(oUsuarioBean.getUsuario(), oContrato);
					oOrdenContratoBean.setCodigoOrden(oContrato.getCodigo());
					
					oOrdenContratoBean.setNumeroOrdenContrato(oContrato.getNumero());
					oOrdenContratoBean.setYearOrdenContrato(oContrato.getYear());
					oOrdenContratoBean.setSerieOrdenContrato(oContrato.getSerie());
					RequestContext.getCurrentInstance().addCallbackParam("showDialog", true);
					facesContext.renderResponse();
				} else {
					Orden oOrden = null;
					oOrden = new Orden();
					oOrden.setProveedor(oOrdenContratoBean.getProveedor());
					oOrden.setFechaIngreso(calendarFechaRegistro);
					oOrden.setTipoOrden(oOrdenContratoBean.getComboBoxItemTipoOrden().getValue());
					
					oOrden.setTotal(oOrdenContratoBean.getTotalListaArticulosOrden());
					oOrden.setNumeroReferencia(oOrdenContratoBean.getProceso().getCodigoProceso());
					oOrden.setMoneda(oRequerimientoProceso.getMoneda());
					oOrden.setOrigen(AdminOrigenOrden.obtenerOrigenOrden(AdminOrigenOrden.PROCESO));
					oOrden.setModalidadCompra(new ModalidadCompra());
					oOrden.getModalidadCompra().setCodigo(1);
					
					oOrden.setArticulos(oOrdenContratoBean.getListaArticulosOrden());
					
					oOrden.setAfectoIGV(oOrdenContratoBean.isAfectoIGV());
					oOrden.setImpuestoRenta(oOrdenContratoBean.isAfectoImpuestoRenta());
									
					AdminOrdenesContratos.nuevaOrden(oUsuarioBean.getUsuario(), oOrden);
					oOrdenContratoBean.setCodigoOrden(oOrden.getCodigo());
					
					oOrdenContratoBean.setNumeroOrdenContrato(oOrden.getNumero());
					oOrdenContratoBean.setYearOrdenContrato(oOrden.getYear());
					oOrdenContratoBean.setSerieOrdenContrato(oOrden.getSerie());
					RequestContext.getCurrentInstance().addCallbackParam("showDialog", true);
					facesContext.renderResponse();
				}
			} catch (Exception e) {
				mostrarMensajeError("", facesContext);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*",e.getMessage());
				facesContext.addMessage(null, oFacesMessage);
			}
		}
	}
	
	public void resolverOrdenContrato() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			UsuarioBean oUsuarioBean = (UsuarioBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, LoginController.USUARIOBEAN);
						
			try {
				resetearMensajeError(facesContext);
				if( oOrdenContratoBean.getOrdenContrato() == null ) {
					throw new Exception("No hay una orden/contrato seleccionada");
				}
				Contrato oContrato = oOrdenContratoBean.getOrdenContrato();
				oContrato.setMotivoResolucion(oOrdenContratoBean.getMotivoResolucion());
				oContrato.setDocumentoResolucion(oOrdenContratoBean.getDocumentoResolucion());
				oContrato.setFechaResolucion(Util.getFechaActual());
				
				if( AdminOrdenesContratos.resolverOrdenContrato(oUsuarioBean.getUsuario(), oContrato) ) {
					RequestContext.getCurrentInstance().addCallbackParam("showDialog", true);
					facesContext.renderResponse();
				} else {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","No se pudo completar la operación");
					facesContext.addMessage(null, oFacesMessage);
					mostrarMensajeError("", facesContext);
				}
				return;
			} catch (NullPointerException e) {
				logger.error(e.getMessage(), e);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","No se pudo completar la operación");
				facesContext.addMessage(null, oFacesMessage);
				mostrarMensajeError("", facesContext);
				return;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*",e.getMessage());
				facesContext.addMessage(null, oFacesMessage);
				mostrarMensajeError("", facesContext);
				return;
			}
		}
	}
	
	public void anularOrdenContrato() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if( facesContext != null ){
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			OrdenContratoBean oOrdenContratoBean = (OrdenContratoBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, ORDENCONTRATOBEAN);
			UsuarioBean oUsuarioBean = (UsuarioBean)facesContext.getApplication().
										getELResolver().getValue(elContext, null, LoginController.USUARIOBEAN);
						
			try {
				resetearMensajeError(facesContext);
				if( oOrdenContratoBean.getOrdenContrato() == null ) {
					throw new Exception("No hay una orden/contrato seleccionada");
				}
				Contrato oContrato = oOrdenContratoBean.getOrdenContrato();
				oContrato.setMotivoEstado(oOrdenContratoBean.getMotivoEstado());
				oContrato.setFechaResolucion(Util.getFechaActual());
				
				if( AdminOrdenesContratos.anularOrdenContrato(oUsuarioBean.getUsuario(), oContrato) ) {
					RequestContext.getCurrentInstance().addCallbackParam("showDialog", true);
					facesContext.renderResponse();
				} else {
					FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","No se pudo completar la operación");
					facesContext.addMessage(null, oFacesMessage);
					mostrarMensajeError("", facesContext);
				}
			} catch (NullPointerException e) {
				logger.error(e.getMessage(), e);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*","No se pudo completar la operación");
				facesContext.addMessage(null, oFacesMessage);
				return;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				FacesMessage oFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"*",e.getMessage());
				facesContext.addMessage(null, oFacesMessage);
				return;
			}
		}
	}
}
