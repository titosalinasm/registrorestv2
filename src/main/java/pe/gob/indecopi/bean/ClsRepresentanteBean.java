package pe.gob.indecopi.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClsRepresentanteBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8688518899719777396L;
	private Integer nuTipoOrigen;
	private Integer nuTipoPersoneria;
	private Integer nuTipoDocumento;
	private String vcNroDocumento;
	private String vcNombres;
	private String vcPrimerApellido;
	private String vcSegundoApellido;
	private String vcRsocial;
	private String vcCorreo;
	
	private Integer nuPaisOrigen;
	private Integer nuPaisResidencia;
	private Integer nuDepartamento;
	private Integer nuProvincia;
	private Integer nuDistrito;
	private String vcDireccion;
	private Integer nuGenero;
	private String vcTelefono;
	private String vcPartidaRegistral;
	private String vcNroExpediente;
	private Integer nuFlagSolicitante;
	private List<ClsArchivoBean> lstArchivo;
	
	public ClsRepresentanteBean() {
		this.setLstArchivo(new ArrayList<ClsArchivoBean>());
	}
	
	public Integer getNuFlagSolicitante() {
		return nuFlagSolicitante;
	}
	public void setNuFlagSolicitante(Integer nuFlagSolicitante) {
		this.nuFlagSolicitante = nuFlagSolicitante;
	}
	public Integer getNuTipoOrigen() {
		return nuTipoOrigen;
	}
	public void setNuTipoOrigen(Integer nuTipoOrigen) {
		this.nuTipoOrigen = nuTipoOrigen;
	}
	public String getVcRsocial() {
		return vcRsocial;
	}
	public void setVcRsocial(String vcRsocial) {
		this.vcRsocial = vcRsocial;
	}
	public Integer getNuTipoPersoneria() {
		return nuTipoPersoneria;
	}
	public void setNuTipoPersoneria(Integer nuTipoPersoneria) {
		this.nuTipoPersoneria = nuTipoPersoneria;
	}
	public Integer getNuTipoDocumento() {
		return nuTipoDocumento;
	}
	public void setNuTipoDocumento(Integer nuTipoDocumento) {
		this.nuTipoDocumento = nuTipoDocumento;
	}
	public String getVcNroDocumento() {
		return vcNroDocumento;
	}
	public void setVcNroDocumento(String vcNroDocumento) {
		this.vcNroDocumento = vcNroDocumento;
	}
	public String getVcNombres() {
		return vcNombres;
	}
	public void setVcNombres(String vcNombres) {
		this.vcNombres = vcNombres;
	}
	public String getVcPrimerApellido() {
		return vcPrimerApellido;
	}
	public void setVcPrimerApellido(String vcPrimerApellido) {
		this.vcPrimerApellido = vcPrimerApellido;
	}
	public String getVcSegundoApellido() {
		return vcSegundoApellido;
	}
	public void setVcSegundoApellido(String vcSegundoApellido) {
		this.vcSegundoApellido = vcSegundoApellido;
	}
	public String getVcCorreo() {
		return vcCorreo;
	}
	public void setVcCorreo(String vcCorreo) {
		this.vcCorreo = vcCorreo;
	}
	public Integer getNuPaisOrigen() {
		return nuPaisOrigen;
	}
	public void setNuPaisOrigen(Integer nuPaisOrigen) {
		this.nuPaisOrigen = nuPaisOrigen;
	}
	public Integer getNuPaisResidencia() {
		return nuPaisResidencia;
	}
	public void setNuPaisResidencia(Integer nuPaisResidencia) {
		this.nuPaisResidencia = nuPaisResidencia;
	}
	public Integer getNuDepartamento() {
		return nuDepartamento;
	}
	public void setNuDepartamento(Integer nuDepartamento) {
		this.nuDepartamento = nuDepartamento;
	}
	public Integer getNuProvincia() {
		return nuProvincia;
	}
	public void setNuProvincia(Integer nuProvincia) {
		this.nuProvincia = nuProvincia;
	}
	public Integer getNuDistrito() {
		return nuDistrito;
	}
	public void setNuDistrito(Integer nuDistrito) {
		this.nuDistrito = nuDistrito;
	}
	public String getVcDireccion() {
		return vcDireccion;
	}
	public void setVcDireccion(String vcDireccion) {
		this.vcDireccion = vcDireccion;
	}
	public Integer getNuGenero() {
		return nuGenero;
	}
	public void setNuGenero(Integer nuGenero) {
		this.nuGenero = nuGenero;
	}
	public String getVcTelefono() {
		return vcTelefono;
	}
	public void setVcTelefono(String vcTelefono) {
		this.vcTelefono = vcTelefono;
	}
	public String getVcPartidaRegistral() {
		return vcPartidaRegistral;
	}
	public void setVcPartidaRegistral(String vcPartidaRegistral) {
		this.vcPartidaRegistral = vcPartidaRegistral;
	}
	public String getVcNroExpediente() {
		return vcNroExpediente;
	}
	public void setVcNroExpediente(String vcNroExpediente) {
		this.vcNroExpediente = vcNroExpediente;
	}
	public List<ClsArchivoBean> getLstArchivo() {
		return lstArchivo;
	}
	public void setLstArchivo(List<ClsArchivoBean> lstArchivo) {
		this.lstArchivo = lstArchivo;
	}
	
	
	
}
