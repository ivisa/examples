/**
 * 
 */
package pe.com.cmacpiura.logistica.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import pe.com.cmacpiura.jsf.ComboBoxItem;
import pe.com.cmacpiura.logistica.com.ArticuloOrden;
import pe.com.cmacpiura.logistica.com.CompraDirecta;
import pe.com.cmacpiura.logistica.com.Contrato;
import pe.com.cmacpiura.logistica.com.Mantenimiento;
import pe.com.cmacpiura.logistica.com.OrigenOrden;
import pe.com.cmacpiura.logistica.com.Penalidad;
import pe.com.cmacpiura.logistica.com.Proceso;
import pe.com.cmacpiura.logistica.com.Propuesta;
import pe.com.cmacpiura.logistica.com.Proveedor;
import pe.com.cmacpiura.logistica.com.TipoOrden;
import pe.com.cmacpiura.logistica.com.TipoProceso;
import pe.com.cmacpiura.logistica.data.AdminOrigenOrden;
import pe.com.cmacpiura.logistica.data.AdminTipoOrden;
import pe.com.cmacpiura.logistica.data.AdminTipoProceso;
import pe.com.cmacpiura.logistica.elements.SelectionElement;
import pe.com.cmacpiura.logistica.java.interfaces.Reseteable;
import pe.com.cmacpiura.logistica.util.Util;

/**
 * @author Juan Lama
 *
 */
public class OrdenContratoBean implements Reseteable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(OrdenContratoBean.class);

	private boolean seleccion;
	
	private boolean filtroOrigenOrdenSeleccionado;
	private ComboBoxItem<OrigenOrden> comboBoxItemOrigenOrden;
	private List<SelectItem> listaOrigenesOrden;
	
	private boolean filtroTipoOrdenSeleccionado;
	private ComboBoxItem<TipoOrden> comboBoxItemTipoOrden;
	private List<SelectItem> listaTiposOrden;
	
	private boolean filtroDescripcionRequerimientoSeleccionado;
	private String descripcionRequerimiento;
	
	private boolean filtroAnioSeleccionado;
	private long anio;
	
	private boolean filtroNumeroSeleccionado;
	private long numero;
	
	private boolean filtroRUCSeleccionado;
	private long nroRuc;
	
	private boolean filtroRazonSocialSeleccionado;
	private String razonSocial;
	
	private boolean filtroFechaEmisionSeleccionado;
	private Date fechaEmision;
	
	
	private List<Contrato> listaOrdenesContratos;
	private Contrato ordenContrato;
	
	private long numeroOrdenContrato;
	private int yearOrdenContrato;
	private String serieOrdenContrato;
	private long codigoOrden;
	private TipoOrden tipoOrden;
	private String descripcionOrdenContrato;
	private OrigenOrden origenOrdenContrato;
	private Proveedor proveedor;
	private Calendar fechaIngreso;
	
	private List<Propuesta> listaPropuestas;
	private Propuesta propuesta;
	
	private boolean afectoIGV;
	private boolean afectoImpuestoRenta;
	
	private List<ArticuloOrden> listaArticulosOrden;
	private ArticuloOrden articuloOrden;
	private Penalidad penalidad;
	
	private String detalle;
	
	private Mantenimiento mantenimiento;
	
	private boolean flagComplementaria;
	private Calendar fechaComplementaria;
	private double montoComplementaria;
	private String informeAprobacionComplementaria;
	
	//Nueva orden
	private boolean seleccionRequerimiento;
	
	private List<CompraDirecta> listaComprasDirectas;
	private CompraDirecta compraDirecta;
	
	private List<Proceso> listaProcesos;
	private Proceso proceso;
	
	private String documentoResolucion;
	private String motivoResolucion;

	private String motivoEstado;
	
	/* filtros */
	private SelectionElement<String> filtroYear;
	private SelectionElement<String> filtroNumero;
	private SelectionElement<String> filtroDescripcion;
	private SelectionElement<Date> filtroFecha;
	private SelectionElement<String> filtroSerie;
	
	private boolean filtroTipoProceso;
	private List<SelectItem> listaTiposProceso;	
	private ComboBoxItem<TipoProceso> comboBoxItemTipoProceso;
	
	public OrdenContratoBean() {	
	}

	/**
	 * @return the seleccion
	 */
	public boolean isSeleccion() {
		return seleccion;
	}

	/**
	 * @param seleccion the seleccion to set
	 */
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}

	/**
	 * @return the filtroOrigenOrdenSeleccionado
	 */
	public boolean isFiltroOrigenOrdenSeleccionado() {
		return filtroOrigenOrdenSeleccionado;
	}

	/**
	 * @param filtroOrigenOrdenSeleccionado the filtroOrigenOrdenSeleccionado to set
	 */
	public void setFiltroOrigenOrdenSeleccionado(
			boolean filtroOrigenOrdenSeleccionado) {
		this.filtroOrigenOrdenSeleccionado = filtroOrigenOrdenSeleccionado;
	}

	/**
	 * @return the comboBoxItemOrigenOrden
	 */
	public ComboBoxItem<OrigenOrden> getComboBoxItemOrigenOrden() {
		return comboBoxItemOrigenOrden;
	}

	/**
	 * @param comboBoxItemOrigenOrden the comboBoxItemOrigenOrden to set
	 */
	public void setComboBoxItemOrigenOrden(
			ComboBoxItem<OrigenOrden> comboBoxItemOrigenOrden) {
		this.comboBoxItemOrigenOrden = comboBoxItemOrigenOrden;
	}

	/**
	 * @return the listaOrigenesOrden
	 */
	public  List<SelectItem> getListaOrigenesOrden() {
		if( listaOrigenesOrden == null ){
			listaOrigenesOrden = new ArrayList<SelectItem>();
			List<OrigenOrden> listOrigenesOrden = null;
			try {
				listOrigenesOrden = AdminOrigenOrden.obtenerTiposOrden();
				ComboBoxItem<OrigenOrden> oComboBoxItem = null; 
				for( OrigenOrden oOrigenOrden : listOrigenesOrden ){
					oComboBoxItem = new ComboBoxItem<OrigenOrden>();
					oComboBoxItem.setTranslate(false);
					oComboBoxItem.setValue(oOrigenOrden);
					oComboBoxItem.setResValue(oOrigenOrden.getDescripcion());
					listaOrigenesOrden.add( new SelectItem(oComboBoxItem,oComboBoxItem.getTranslatedvalue()) );
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return listaOrigenesOrden;
	}

	/**
	 * @param listaOrigenesOrden the listaOrigenesOrden to set
	 */
	public void setListaOrigenesOrden(List<SelectItem> listaOrigenesOrden) {
		this.listaOrigenesOrden = listaOrigenesOrden;
	}

	/**
	 * @return the filtroTipoOrdenSeleccionado
	 */
	public boolean isFiltroTipoOrdenSeleccionado() {
		return filtroTipoOrdenSeleccionado;
	}

	/**
	 * @param filtroTipoOrdenSeleccionado the filtroTipoOrdenSeleccionado to set
	 */
	public void setFiltroTipoOrdenSeleccionado(boolean filtroTipoOrdenSeleccionado) {
		this.filtroTipoOrdenSeleccionado = filtroTipoOrdenSeleccionado;
	}

	/**
	 * @return the comboBoxItemTipoOrden
	 */
	public ComboBoxItem<TipoOrden> getComboBoxItemTipoOrden() {
		return comboBoxItemTipoOrden;
	}

	/**
	 * @param comboBoxItemTipoOrden the comboBoxItemTipoOrden to set
	 */
	public void setComboBoxItemTipoOrden(
			ComboBoxItem<TipoOrden> comboBoxItemTipoOrden) {
		this.comboBoxItemTipoOrden = comboBoxItemTipoOrden;
	}

	/**
	 * @return the listaTiposOrden
	 */
	public List<SelectItem> getListaTiposOrden() {
		if( listaTiposOrden == null ){
			listaTiposOrden = new ArrayList<SelectItem>();
			List<TipoOrden> listTiposOrden = null;
			try {
				listTiposOrden = AdminTipoOrden.obtenerTiposOrden();
				ComboBoxItem<TipoOrden> oComboBoxItem = null; 
				for( TipoOrden oTipoOrden : listTiposOrden ){
					oComboBoxItem = new ComboBoxItem<TipoOrden>();
					oComboBoxItem.setTranslate(false);
					oComboBoxItem.setValue(oTipoOrden);
					oComboBoxItem.setResValue(oTipoOrden.getDescripcion());
					listaTiposOrden.add( new SelectItem(oComboBoxItem,oComboBoxItem.getTranslatedvalue()) );
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return listaTiposOrden;
	}

	/**
	 * @param listaTiposOrden the listaTiposOrden to set
	 */
	public void setListaTiposOrden(List<SelectItem> listaTiposOrden) {
		this.listaTiposOrden = listaTiposOrden;
	}

	/**
	 * @return the filtroDescripcionRequerimientoSeleccionado
	 */
	public boolean isFiltroDescripcionRequerimientoSeleccionado() {
		return filtroDescripcionRequerimientoSeleccionado;
	}

	/**
	 * @param filtroDescripcionRequerimientoSeleccionado the filtroDescripcionRequerimientoSeleccionado to set
	 */
	public void setFiltroDescripcionRequerimientoSeleccionado(
			boolean filtroDescripcionRequerimientoSeleccionado) {
		this.filtroDescripcionRequerimientoSeleccionado = filtroDescripcionRequerimientoSeleccionado;
	}

	/**
	 * @return the descripcionRequerimiento
	 */
	public String getDescripcionRequerimiento() {
		return descripcionRequerimiento;
	}

	/**
	 * @param descripcionRequerimiento the descripcionRequerimiento to set
	 */
	public void setDescripcionRequerimiento(String descripcionRequerimiento) {
		this.descripcionRequerimiento = descripcionRequerimiento;
	}

	/**
	 * @return the filtroAnioSeleccionado
	 */
	public boolean isFiltroAnioSeleccionado() {
		return filtroAnioSeleccionado;
	}

	/**
	 * @param filtroAnioSeleccionado the filtroAnioSeleccionado to set
	 */
	public void setFiltroAnioSeleccionado(boolean filtroAnioSeleccionado) {
		this.filtroAnioSeleccionado = filtroAnioSeleccionado;
	}

	/**
	 * @return the anio
	 */
	public long getAnio() {
		return anio;
	}

	/**
	 * @param anio the anio to set
	 */
	public void setAnio(long anio) {
		this.anio = anio;
	}

	/**
	 * @return the filtroNumeroSeleccionado
	 */
	public boolean isFiltroNumeroSeleccionado() {
		return filtroNumeroSeleccionado;
	}

	/**
	 * @param filtroNumeroSeleccionado the filtroNumeroSeleccionado to set
	 */
	public void setFiltroNumeroSeleccionado(boolean filtroNumeroSeleccionado) {
		this.filtroNumeroSeleccionado = filtroNumeroSeleccionado;
	}

	/**
	 * @return the numero
	 */
	public long getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(long numero) {
		this.numero = numero;
	}

	/**
	 * @return the filtroRUCSeleccionado
	 */
	public boolean isFiltroRUCSeleccionado() {
		return filtroRUCSeleccionado;
	}

	/**
	 * @param filtroRUCSeleccionado the filtroRUCSeleccionado to set
	 */
	public void setFiltroRUCSeleccionado(boolean filtroRUCSeleccionado) {
		this.filtroRUCSeleccionado = filtroRUCSeleccionado;
	}

	/**
	 * @return the nroRuc
	 */
	public long getNroRuc() {
		return nroRuc;
	}

	/**
	 * @param nroRuc the nroRuc to set
	 */
	public void setNroRuc(long nroRuc) {
		this.nroRuc = nroRuc;
	}

	/**
	 * @return the filtroRazonSocialSeleccionado
	 */
	public boolean isFiltroRazonSocialSeleccionado() {
		return filtroRazonSocialSeleccionado;
	}

	/**
	 * @param filtroRazonSocialSeleccionado the filtroRazonSocialSeleccionado to set
	 */
	public void setFiltroRazonSocialSeleccionado(
			boolean filtroRazonSocialSeleccionado) {
		this.filtroRazonSocialSeleccionado = filtroRazonSocialSeleccionado;
	}

	/**
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return the filtroFechaEmisionSeleccionado
	 */
	public boolean isFiltroFechaEmisionSeleccionado() {
		return filtroFechaEmisionSeleccionado;
	}

	/**
	 * @param filtroFechaEmisionSeleccionado the filtroFechaEmisionSeleccionado to set
	 */
	public void setFiltroFechaEmisionSeleccionado(
			boolean filtroFechaEmisionSeleccionado) {
		this.filtroFechaEmisionSeleccionado = filtroFechaEmisionSeleccionado;
	}

	/**
	 * @return the fechaEmision
	 */
	public Date getFechaEmision() {
		if( fechaEmision == null ) {
			fechaEmision = new Date();
		}
		return fechaEmision;
	}

	/**
	 * @param fechaEmision the fechaEmision to set
	 */
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	/**
	 * @return the listaOrdenesContratos
	 */
	public List<Contrato> getListaOrdenesContratos() {
		if( listaOrdenesContratos == null ) {
			listaOrdenesContratos = new ArrayList<Contrato>();
		}
		return listaOrdenesContratos;
	}

	/**
	 * @param listaOrdenesContratos the listaOrdenesContratos to set
	 */
	public void setListaOrdenesContratos(List<Contrato> listaOrdenesContratos) {
		this.listaOrdenesContratos = listaOrdenesContratos;
	}

	/**
	 * @return the ordenContrato
	 */
	public Contrato getOrdenContrato() {
		return ordenContrato;
	}

	/**
	 * @param ordenContrato the ordenContrato to set
	 */
	public void setOrdenContrato(Contrato ordenContrato) {
		this.ordenContrato = ordenContrato;
	}

	/**
	 * @return the numeroOrdenContrato
	 */
	public long getNumeroOrdenContrato() {
		return numeroOrdenContrato;
	}

	/**
	 * @param numeroOrdenContrato the numeroOrdenContrato to set
	 */
	public void setNumeroOrdenContrato(long numeroOrdenContrato) {
		this.numeroOrdenContrato = numeroOrdenContrato;
	}

	/**
	 * @return the yearOrdenContrato
	 */
	public int getYearOrdenContrato() {
		return yearOrdenContrato;
	}

	/**
	 * @param yearOrdenContrato the yearOrdenContrato to set
	 */
	public void setYearOrdenContrato(int yearOrdenContrato) {
		this.yearOrdenContrato = yearOrdenContrato;
	}

	/**
	 * @return the serieOrdenContrato
	 */
	public String getSerieOrdenContrato() {
		return serieOrdenContrato;
	}

	/**
	 * @param serieOrdenContrato the serieOrdenContrato to set
	 */
	public void setSerieOrdenContrato(String serieOrdenContrato) {
		this.serieOrdenContrato = serieOrdenContrato;
	}

	/**
	 * @return the codigoOrden
	 */
	public long getCodigoOrden() {
		return codigoOrden;
	}

	/**
	 * @param codigoOrden the codigoOrden to set
	 */
	public void setCodigoOrden(long codigoOrden) {
		this.codigoOrden = codigoOrden;
	}

	/**
	 * @return the tipoOrden
	 */
	public TipoOrden getTipoOrden() {
		return tipoOrden;
	}

	/**
	 * @param tipoOrden the tipoOrden to set
	 */
	public void setTipoOrden(TipoOrden tipoOrden) {
		this.tipoOrden = tipoOrden;
	}

	/**
	 * @return the descripcionOrdenContrato
	 */
	public String getDescripcionOrdenContrato() {
		return descripcionOrdenContrato;
	}

	/**
	 * @param descripcionOrdenContrato the descripcionOrdenContrato to set
	 */
	public void setDescripcionOrdenContrato(String descripcionOrdenContrato) {
		this.descripcionOrdenContrato = descripcionOrdenContrato;
	}

	/**
	 * @return the origenOrdenContrato
	 */
	public OrigenOrden getOrigenOrdenContrato() {
		return origenOrdenContrato;
	}

	/**
	 * @param origenOrdenContrato the origenOrdenContrato to set
	 */
	public void setOrigenOrdenContrato(OrigenOrden origenOrdenContrato) {
		this.origenOrdenContrato = origenOrdenContrato;
	}
		
	/**
	 * @return the proveedor
	 */
	public Proveedor getProveedor() {
		return proveedor;
	}

	/**
	 * @param proveedor the proveedor to set
	 */
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	/**
	 * @return the afectoIGV
	 */
	public boolean isAfectoIGV() {
		return afectoIGV;
	}

	/**
	 * @param afectoIGV the afectoIGV to set
	 */
	public void setAfectoIGV(boolean afectoIGV) {
		this.afectoIGV = afectoIGV;
	}

	/**
	 * @return the afectoImpuestoRenta
	 */
	public boolean isAfectoImpuestoRenta() {
		return afectoImpuestoRenta;
	}

	/**
	 * @param afectoImpuestoRenta the afectoImpuestoRenta to set
	 */
	public void setAfectoImpuestoRenta(boolean afectoImpuestoRenta) {
		this.afectoImpuestoRenta = afectoImpuestoRenta;
	}

	/**
	 * @return the listaArticulosOrden
	 */
	public List<ArticuloOrden> getListaArticulosOrden() {
		if( listaArticulosOrden == null ) {
			listaArticulosOrden = new ArrayList<ArticuloOrden>();
		}
		return listaArticulosOrden;
	}

	/**
	 * @param listaArticulosOrden the listaArticulosOrden to set
	 */
	public void setListaArticulosOrden(List<ArticuloOrden> listaArticulosOrden) {
		this.listaArticulosOrden = listaArticulosOrden;
	}

	/**
	 * @return the articuloOrden
	 */
	public ArticuloOrden getArticuloOrden() {
		return articuloOrden;
	}

	/**
	 * @param articuloOrden the articuloOrden to set
	 */
	public void setArticuloOrden(ArticuloOrden articuloOrden) {
		this.articuloOrden = articuloOrden;
	}

	/**
	 * @return the penalidad
	 */
	public Penalidad getPenalidad() {
		return penalidad;
	}

	/**
	 * @param penalidad the penalidad to set
	 */
	public void setPenalidad(Penalidad penalidad) {
		this.penalidad = penalidad;
	}
	
	/**
	 * @return the fechaIngreso
	 */
	public Calendar getFechaIngreso() {
		return fechaIngreso;
	}
	
	/**
	 * @return the detalle
	 */
	public String getDetalle() {
		return detalle;
	}

	/**
	 * @param detalle the detalle to set
	 */
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public String getStrFechaIngreso() {
		return Util.formatearFecha(getFechaIngreso());
	}
	
	public void setStrFechaIngreso(String str) {
	}

	/**
	 * @param fechaIngreso the fechaIngreso to set
	 */
	public void setFechaIngreso(Calendar fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public double getTotalListaArticulosOrden() {
		BigDecimal oBigDecimal = new BigDecimal(0);
		for( ArticuloOrden oArticuloOrden : getListaArticulosOrden() ) {
			oBigDecimal = oBigDecimal.add(new BigDecimal( oArticuloOrden.getCantidad() * oArticuloOrden.getPrecio() ));
		}
		return oBigDecimal.doubleValue();
	}

	/**
	 * @return the seleccionRequerimiento
	 */
	public boolean isSeleccionRequerimiento() {
		return seleccionRequerimiento;
	}

	/**
	 * @param seleccionRequerimiento the seleccionRequerimiento to set
	 */
	public void setSeleccionRequerimiento(boolean seleccionRequerimiento) {
		this.seleccionRequerimiento = seleccionRequerimiento;
	}
	
	/**
	 * @return the listaComprasDirectas
	 */
	public List<CompraDirecta> getListaComprasDirectas() {
		if( listaComprasDirectas == null ) {
			listaComprasDirectas = new ArrayList<CompraDirecta>();
		}
		return listaComprasDirectas;
	}

	/**
	 * @param listaComprasDirectas the listaComprasDirectas to set
	 */
	public void setListaComprasDirectas(List<CompraDirecta> listaComprasDirectas) {
		this.listaComprasDirectas = listaComprasDirectas;
	}

	/**
	 * @return the compraDirecta
	 */
	public CompraDirecta getCompraDirecta() {
		return compraDirecta;
	}

	/**
	 * @param compraDirecta the compraDirecta to set
	 */
	public void setCompraDirecta(CompraDirecta compraDirecta) {
		this.compraDirecta = compraDirecta;
	}
	
	/**
	 * @return the filtroSerie
	 */
	public SelectionElement<String> getFiltroSerie() {
		if( filtroSerie == null ) {
			filtroSerie = new SelectionElement<String>();
		}
		return filtroSerie;
	}

	/**
	 * @param filtroSerie the filtroSerie to set
	 */
	public void setFiltroSerie(SelectionElement<String> filtroSerie) {
		this.filtroSerie = filtroSerie;
	}

	/**
	 * @return the filtroYear
	 */
	public SelectionElement<String> getFiltroYear() {
		if( filtroYear == null ){
			filtroYear = new SelectionElement<String>();
		}
		return filtroYear;
	}

	/**
	 * @param filtroYear the filtroYear to set
	 */
	public void setFiltroYear(SelectionElement<String> filtroYear) {
		this.filtroYear = filtroYear;
	}

	/**
	 * @return the filtroNumero
	 */
	public SelectionElement<String> getFiltroNumero() {
		if( filtroNumero == null ){
			filtroNumero = new SelectionElement<String>();
		}
		return filtroNumero;
	}

	/**
	 * @param filtroNumero the filtroNumero to set
	 */
	public void setFiltroNumero(SelectionElement<String> filtroNumero) {
		this.filtroNumero = filtroNumero;
	}

	/**
	 * @return the filtroDescripcion
	 */
	public SelectionElement<String> getFiltroDescripcion() {
		if( filtroDescripcion == null ){
			filtroDescripcion = new SelectionElement<String>();
			filtroDescripcion.setValue("");
		}
		return filtroDescripcion;
	}

	/**
	 * @param filtroDescripcion the filtroDescripcion to set
	 */
	public void setFiltroDescripcion(SelectionElement<String> filtroDescripcion) {
		this.filtroDescripcion = filtroDescripcion;
	}

	/**
	 * @return the filtroFecha
	 */
	public SelectionElement<Date> getFiltroFecha() {
		if( filtroFecha == null ){
			filtroFecha = new SelectionElement<Date>();
			filtroFecha.setValue(new Date());
		}
		return filtroFecha;
	}
	
	/**
	 * @param filtroFecha the filtroFecha to set
	 */
	public void setFiltroFecha(SelectionElement<Date> filtroFecha) {
		this.filtroFecha = filtroFecha;
	}

	/**
	 * @return the mantenimiento
	 */
	public Mantenimiento getMantenimiento() {
		return mantenimiento;
	}

	/**
	 * @param mantenimiento the mantenimiento to set
	 */
	public void setMantenimiento(Mantenimiento mantenimiento) {
		this.mantenimiento = mantenimiento;
	}

	/**
	 * @return the listaProcesos
	 */
	public List<Proceso> getListaProcesos() {
		if( listaProcesos == null ) {
			listaProcesos = new ArrayList<Proceso>();
		}
		return listaProcesos;
	}

	/**
	 * @param listaProcesos the listaProcesos to set
	 */
	public void setListaProcesos(List<Proceso> listaProcesos) {
		this.listaProcesos = listaProcesos;
	}

	/**
	 * @return the proceso
	 */
	public Proceso getProceso() {
		return proceso;
	}

	/**
	 * @param proceso the proceso to set
	 */
	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	/**
	 * @return the flagComplementaria
	 */
	public boolean isFlagComplementaria() {
		return flagComplementaria;
	}

	/**
	 * @param flagComplementaria the flagComplementaria to set
	 */
	public void setFlagComplementaria(boolean flagComplementaria) {
		this.flagComplementaria = flagComplementaria;
	}

	/**
	 * @return the fechaComplementaria
	 */
	public Calendar getFechaComplementaria() {
		if( fechaComplementaria == null ) {
			fechaComplementaria = Util.getFechaActual();
		}
		return fechaComplementaria;
	}

	/**
	 * @param fechaComplementaria the fechaComplementaria to set
	 */
	public void setFechaComplementaria(Calendar fechaComplementaria) {
		this.fechaComplementaria = fechaComplementaria;
	}

	/**
	 * @return the montoComplementaria
	 */
	public double getMontoComplementaria() {
		return montoComplementaria;
	}

	/**
	 * @param montoComplementaria the montoComplementaria to set
	 */
	public void setMontoComplementaria(double montoComplementaria) {
		this.montoComplementaria = montoComplementaria;
	}

	/**
	 * @return the informeAprobacionComplementaria
	 */
	public String getInformeAprobacionComplementaria() {
		return informeAprobacionComplementaria;
	}

	/**
	 * @param informeAprobacionComplementaria the informeAprobacionComplementaria to set
	 */
	public void setInformeAprobacionComplementaria(
			String informeAprobacionComplementaria) {
		this.informeAprobacionComplementaria = informeAprobacionComplementaria;
	}

	/**
	 * @return the documentoResolucion
	 */
	public String getDocumentoResolucion() {
		return documentoResolucion;
	}

	/**
	 * @param documentoResolucion the documentoResolucion to set
	 */
	public void setDocumentoResolucion(String documentoResolucion) {
		this.documentoResolucion = documentoResolucion;
	}

	/**
	 * @return the motivoResolucion
	 */
	public String getMotivoResolucion() {
		return motivoResolucion;
	}

	/**
	 * @param motivoResolucion the motivoResolucion to set
	 */
	public void setMotivoResolucion(String motivoResolucion) {
		this.motivoResolucion = motivoResolucion;
	}

	/**
	 * @return the motivoEstado
	 */
	public String getMotivoEstado() {
		return motivoEstado;
	}

	/**
	 * @param motivoEstado the motivoEstado to set
	 */
	public void setMotivoEstado(String motivoEstado) {
		this.motivoEstado = motivoEstado;
	}
	
	/**
	 * @return the filtroTipoProceso
	 */
	public boolean isFiltroTipoProceso() {
		return filtroTipoProceso;
	}

	/**
	 * @param filtroTipoProceso the filtroTipoProceso to set
	 */
	public void setFiltroTipoProceso(boolean filtroTipoProceso) {
		this.filtroTipoProceso = filtroTipoProceso;
	}

	/**
	 * @return the listaTiposProceso
	 */
	public List<SelectItem> getListaTiposProceso() {
		if( listaTiposProceso == null ) {
			listaTiposProceso = new ArrayList<SelectItem>();
			try {
				List<TipoProceso> listTipoProcesos = AdminTipoProceso.obtenerTiposProceso();
				ComboBoxItem<TipoProceso> oComboBoxItem = null; 
				for( TipoProceso oTipoProceso : listTipoProcesos ) {
					oComboBoxItem = new ComboBoxItem<TipoProceso>();
					oComboBoxItem.setTranslate(false);
					oComboBoxItem.setValue(oTipoProceso);
					oComboBoxItem.setResValue(oTipoProceso.getDescripcion());
					listaTiposProceso.add( new SelectItem(oComboBoxItem,oComboBoxItem.getTranslatedvalue()) );
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			
		}
		return listaTiposProceso;
	}

	/**
	 * @param listaTiposProceso the listaTiposProceso to set
	 */
	public void setListaTiposProceso(List<SelectItem> listaTiposProceso) {
		this.listaTiposProceso = listaTiposProceso;
	}

	/**
	 * @return the comboBoxItemTipoProceso
	 */
	public ComboBoxItem<TipoProceso> getComboBoxItemTipoProceso() {
		return comboBoxItemTipoProceso;
	}

	/**
	 * @param comboBoxItemTipoProceso the comboBoxItemTipoProceso to set
	 */
	public void setComboBoxItemTipoProceso(
			ComboBoxItem<TipoProceso> comboBoxItemTipoProceso) {
		this.comboBoxItemTipoProceso = comboBoxItemTipoProceso;
	}

	/**
	 * @return the listaPropuestas
	 */
	public List<Propuesta> getListaPropuestas() {
		if( listaPropuestas == null ) {
			listaPropuestas = new ArrayList<Propuesta>();
		}
		return listaPropuestas;
	}

	/**
	 * @param listaPropuestas the listaPropuestas to set
	 */
	public void setListaPropuestas(List<Propuesta> listaPropuestas) {
		this.listaPropuestas = listaPropuestas;
	}

	/**
	 * @return the propuesta
	 */
	public Propuesta getPropuesta() {
		return propuesta;
	}

	/**
	 * @param propuesta the propuesta to set
	 */
	public void setPropuesta(Propuesta propuesta) {
		this.propuesta = propuesta;
	}
}
