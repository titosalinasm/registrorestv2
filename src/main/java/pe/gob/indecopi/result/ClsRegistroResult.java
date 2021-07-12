package pe.gob.indecopi.result;

import java.io.Serializable;

public class ClsRegistroResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2256779790488974813L;
	
	private String vcArea;
	private Integer nuAnio;
	private Integer nuExpediente;
	private String vcNomArchivo;
	
	
	public String getVcNomArchivo() {
		return vcNomArchivo;
	}
	public void setVcNomArchivo(String vcNomArchivo) {
		this.vcNomArchivo = vcNomArchivo;
	}
	public String getVcArea() {
		return vcArea;
	}
	public void setVcArea(String vcArea) {
		this.vcArea = vcArea;
	}
	public Integer getNuAnio() {
		return nuAnio;
	}
	public void setNuAnio(Integer nuAnio) {
		this.nuAnio = nuAnio;
	}
	public Integer getNuExpediente() {
		return nuExpediente;
	}
	public void setNuExpediente(Integer nuExpediente) {
		this.nuExpediente = nuExpediente;
	}
	
	
	

}
