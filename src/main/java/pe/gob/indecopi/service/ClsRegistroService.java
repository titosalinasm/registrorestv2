package pe.gob.indecopi.service;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import oracle.jdbc.OracleTypes;
import pe.gob.indecopi.bean.ClsArchivoBean;
import pe.gob.indecopi.bean.ClsClaseSolBean;
import pe.gob.indecopi.bean.ClsParametrosBean;
import pe.gob.indecopi.bean.ClsPosicionBean;
import pe.gob.indecopi.bean.ClsPrioridadBean;
import pe.gob.indecopi.bean.ClsPrioridadExtBean;
import pe.gob.indecopi.bean.ClsSolicitudBean;
import pe.gob.indecopi.bean.ClsTramiteSELBean;
import pe.gob.indecopi.exception.ClsException;
import pe.gob.indecopi.main.AppRegMarcas;
import pe.gob.indecopi.param.ClsConstantes;
import pe.gob.indecopi.repository.ClsParametroRepositoryI;
import pe.gob.indecopi.repository.ClsRegistroRepositoryI;
import pe.gob.indecopi.repository.ClsTramiteSELRepositoryI;
import pe.gob.indecopi.result.ClsLstGeneralResult;
import pe.gob.indecopi.result.ClsRegistroResult;
import pe.gob.indecopi.util.ClsResultDAO;
import pe.gob.indecopi.util.ClsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ClsRegistroService implements Serializable, ClsRegistroServiceI{
	
	private static Logger logger = LoggerFactory.getLogger(ClsRegistroService.class);


	/**
	 * 
	 */
	private static final long serialVersionUID = -4583204397936297279L;
	
	@Autowired
	private ClsRegistroRepositoryI objConn;
	
	@Autowired
	private ClsTramiteSELRepositoryI objConnSEL;
	
	@Autowired
	private ClsParametroRepositoryI objConnParam;
	
	@Autowired
	private ClsResultDAO objResultDAO;
	
	@Override
	public Object doRegistr2(ClsSolicitudBean objSolicitud) {
		
		ClsRegistroResult objResult=new ClsRegistroResult();

		try {
			
			ClsParametrosBean objParam=new ClsParametrosBean();
			objResultDAO=objConnParam.doParametro();
			objParam=(ClsParametrosBean)objResultDAO.get("POUT_PARAMETRO");
			objParam.setNuCantClases(objSolicitud.getLstClases().size());
			
			logger.info("Haciendo Comprobación");

			
			//cuando es lema comercial
			if(objSolicitud.getObjDatosSigno().getNuIdTipoSolicitud()==2) {
				ClsSolicitudBean objSolicitudTmp=new ClsSolicitudBean();
				objParam.setNuCantClases(1);
				objResultDAO=objConnSEL.doCompruebaPagos(objParam, objSolicitud);
			}else {
				//Cuando es marca colectiva
				if(objSolicitud.getObjDatosSigno().getNuIdTipoSolicitud()==4) {
					
				}else {
					objResultDAO=objConnSEL.doCompruebaPagos(objParam, objSolicitud);
				}
			}
			

			
		    if(Integer.parseInt(objResultDAO.get("POUT_NU_ERROR")+"")==0) {
		    	logger.info("Comprobado correctamente");
		    	
		    	//INICIO Proceso de archivos en base64
		    	//Archivos de mi signo
		    	for(int i=0; i<objSolicitud.getObjDatosSigno().getLstArchivosSigno().size(); i++) {
		    		
		    		ClsArchivoBean objArchivoSigno =new ClsArchivoBean();
		    		objArchivoSigno=objSolicitud.getObjDatosSigno().getLstArchivosSigno().get(i);
		    		
					String vVcFileName = "";
					String vVcFileNameOriginal = "";
					String vVcExtension = "";
					String vVcUriDescarga = "";
					int vNuNroPaginas = 0;
					
		    		//logger.info("SERVICE: doValidarArchivo()... Decodificando cada archivo entrante de Base64");
		    		String vVcDataDoc = objArchivoSigno.getVcFileBase64().toString();
					List<String> list = Stream.of(vVcDataDoc.split(",")).collect(Collectors.toList());
					
					vVcExtension = FilenameUtils.getExtension(objArchivoSigno.getVcNomOriginal());
					vVcFileNameOriginal = objArchivoSigno.getVcNomOriginal();
					vVcFileName = "doc_" + ClsUtil.doGenerarNombreArchivoAleatorio().toString() + "." + vVcExtension;
					
					//logger.info("SERVICE: doValidarArchivo()... Nombre original: " + vVcFileNameOriginal);
					//logger.info("SERVICE: doValidarArchivo()... Nombre generado: " + vVcFileName);
					String vcRutaFinal="";
					if(vVcExtension.equals("pdf") || vVcExtension.equals("PDF") ) {
						vcRutaFinal=objParam.getVcRutaFinal();
					}else {
						vcRutaFinal=objParam.getVcRutaTemporal();
					}
					
					logger.info("vcRutaFinal:"+vcRutaFinal);
					Path vVcRutaFile =null;

					vVcRutaFile = Paths.get(vcRutaFinal, vVcFileName);

					logger.info("vVcRutaFile.toAbsolutePath():"+vVcRutaFile.toAbsolutePath());
				    logger.info("vcRutaFinal:"+vcRutaFinal);

					logger.info("SERVICE: doValidarArchivo()... Verifica si el archivo ya existe");
					if (Files.exists(vVcRutaFile)) {
						vVcFileName = "doc_" + ClsUtil.doGenerarNombreArchivoAleatorio().toString() + "." + vVcExtension;
						vVcRutaFile = Paths.get(vcRutaFinal, vVcFileName);
						logger.info("SERVICE: doValidarArchivo()... Nuevo nombre generado: " + vVcFileName);
					}
					logger.info("SERVICE: doValidarArchivo()... Fin de verificación existencia del archivo.");
					
					logger.info("SERVICE: doValidarArchivo()... Iniciando escritura del archivo en disco");
					byte[] vVcArchivoDecode = Base64.getDecoder().decode(list.get(1));
					Files.write(vVcRutaFile, vVcArchivoDecode);
					
					logger.info("SERVICE: doValidarArchivo()... Finalizando escritura del archivo en disco");

					//vVcUriDescarga = ServletUriComponentsBuilder.fromCurrentContextPath().path("/general/downloadfile/")
					//		.path(vVcFileName).toUriString();
					objSolicitud.getObjDatosSigno().getLstArchivosSigno().get(i).setVcExtension(vVcExtension);
					objSolicitud.getObjDatosSigno().getLstArchivosSigno().get(i).setVcNomFinal(vVcFileName);
					objSolicitud.getObjDatosSigno().getLstArchivosSigno().get(i).setVcRuta(vcRutaFinal);
		    	}
		    	
		    	//prioridad extranjera
		    	
		    	//Archivos prioridad extranjera
		    	for(int i=0; i<objSolicitud.getObjPrioridadExtr().getLstPrioridad().size(); i++) {
		    		
		    		for(int j=0; j<objSolicitud.getObjPrioridadExtr().getLstPrioridad().get(i).getLstArchivoPrioridad().size(); j++) {
		    		
		    		ClsArchivoBean objArchivoPrioExtranjera =new ClsArchivoBean();
		    		objArchivoPrioExtranjera=objSolicitud.getObjPrioridadExtr().getLstPrioridad().get(i).getLstArchivoPrioridad().get(j);
		    		
					String vVcFileName = "";
					String vVcFileNameOriginal = "";
					String vVcExtension = "";
					String vVcUriDescarga = "";
					int    vNuNroPaginas = 0;
					
		    		//logger.info("SERVICE: doValidarArchivo()... Decodificando cada archivo entrante de Base64");
		    		String vVcDataDoc = objArchivoPrioExtranjera.getVcFileBase64().toString();
					List<String> list = Stream.of(vVcDataDoc.split(",")).collect(Collectors.toList());
					
					vVcExtension = FilenameUtils.getExtension(objArchivoPrioExtranjera.getVcNomOriginal());
					vVcFileNameOriginal = objArchivoPrioExtranjera.getVcNomOriginal();
					vVcFileName = "doc_" + ClsUtil.doGenerarNombreArchivoAleatorio().toString() + "." + vVcExtension;
					
					//logger.info("SERVICE: doValidarArchivo()... Nombre original: " + vVcFileNameOriginal);
					//logger.info("SERVICE: doValidarArchivo()... Nombre generado: " + vVcFileName);

					Path vVcRutaFile = Paths.get(objParam.getVcRutaFinal(), vVcFileName);

					//logger.info("SERVICE: doValidarArchivo()... Verifica si el archivo ya existe");
					if (Files.exists(vVcRutaFile)) {
						vVcFileName = "doc_" + ClsUtil.doGenerarNombreArchivoAleatorio().toString() + "." + vVcExtension;
						vVcRutaFile = Paths.get(objParam.getVcRutaFinal(), vVcFileName);
						//logger.info("SERVICE: doValidarArchivo()... Nuevo nombre generado: " + vVcFileName);
					}
					//logger.info("SERVICE: doValidarArchivo()... Fin de verificación existencia del archivo.");
					
					//logger.info("SERVICE: doValidarArchivo()... Iniciando escritura del archivo en disco");
					byte[] vVcArchivoDecode = Base64.getDecoder().decode(list.get(1));
					Files.write(vVcRutaFile, vVcArchivoDecode);
					//logger.info("SERVICE: doValidarArchivo()... Finalizando escritura del archivo en disco");

					//vVcUriDescarga = ServletUriComponentsBuilder.fromCurrentContextPath().path("/general/downloadfile/")
					//		.path(vVcFileName).toUriString();
					objSolicitud.getObjPrioridadExtr().getLstPrioridad().get(i).getLstArchivoPrioridad().get(j).setVcExtension(vVcExtension);
					objSolicitud.getObjPrioridadExtr().getLstPrioridad().get(i).getLstArchivoPrioridad().get(j).setVcNomFinal(vVcFileName);
					objSolicitud.getObjPrioridadExtr().getLstPrioridad().get(i).getLstArchivoPrioridad().get(j).setVcRuta(objParam.getVcRutaFinal());
					
		    		}
		    	}
		    	
		    	for(int i=0; i<objSolicitud.getObjPrioridadExtr().getLstPrioridad().size(); i++) {
		    		for(int j=0; j<objSolicitud.getObjPrioridadExtr().getLstPrioridad().get(i).getLstArchivoPrioridad().size(); j++) {
		    			logger.info("Nombre final: "+objSolicitud.getObjPrioridadExtr().getLstPrioridad().get(i).getLstArchivoPrioridad().get(j).getVcNomFinal());
		    		}
		    	}
		    	
		    	
		    	
		    	
		    	//Archivos de representantes
		    	for(int i=0; i<objSolicitud.getLstPersona().size(); i++) { //Solicitante
		    		for(int j=0; j<objSolicitud.getLstPersona().get(i).getLstRepresentante().size(); j++) {//Representante
		    			for(int k=0; k<objSolicitud.getLstPersona().get(i).getLstRepresentante().get(j).getLstArchivo().size(); k++) {//Archivo de representante
		    		
					    		ClsArchivoBean objArchivoRepresentante =new ClsArchivoBean();
					    		objArchivoRepresentante=objSolicitud.getLstPersona().get(i).getLstRepresentante().get(j).getLstArchivo().get(k);
					    		
								String vVcFileName = "";
								String vVcFileNameOriginal = "";
								String vVcExtension = "";
								String vVcUriDescarga = "";
								int    vNuNroPaginas = 0;
								
					    		//logger.info("SERVICE: doValidarArchivo()... Decodificando cada archivo entrante de Base64");
					    		String vVcDataDoc = objArchivoRepresentante.getVcFileBase64().toString();
								List<String> list = Stream.of(vVcDataDoc.split(",")).collect(Collectors.toList());
								
								vVcExtension = FilenameUtils.getExtension(objArchivoRepresentante.getVcNomOriginal());
								vVcFileNameOriginal = objArchivoRepresentante.getVcNomOriginal();
								vVcFileName = "doc_" + ClsUtil.doGenerarNombreArchivoAleatorio().toString() + "." + vVcExtension;
								
								//logger.info("SERVICE: doValidarArchivo()... Nombre original: " + vVcFileNameOriginal);
								//logger.info("SERVICE: doValidarArchivo()... Nombre generado: " + vVcFileName);
			
								Path vVcRutaFile = Paths.get(objParam.getVcRutaFinal(), vVcFileName);
			
								//logger.info("SERVICE: doValidarArchivo()... Verifica si el archivo ya existe");
								if (Files.exists(vVcRutaFile)) {
									vVcFileName = "doc_" + ClsUtil.doGenerarNombreArchivoAleatorio().toString() + "." + vVcExtension;
									vVcRutaFile = Paths.get(objParam.getVcRutaFinal(), vVcFileName);
									//logger.info("SERVICE: doValidarArchivo()... Nuevo nombre generado: " + vVcFileName);
								}
								//logger.info("SERVICE: doValidarArchivo()... Fin de verificación existencia del archivo.");
								
								//logger.info("SERVICE: doValidarArchivo()... Iniciando escritura del archivo en disco");
								byte[] vVcArchivoDecode = Base64.getDecoder().decode(list.get(1));
								Files.write(vVcRutaFile, vVcArchivoDecode);
								//logger.info("SERVICE: doValidarArchivo()... Finalizando escritura del archivo en disco");
			
								objSolicitud.getLstPersona().get(i).getLstRepresentante().get(j).getLstArchivo().get(k).setVcExtension(vVcExtension);
								objSolicitud.getLstPersona().get(i).getLstRepresentante().get(j).getLstArchivo().get(k).setVcNomFinal(vVcFileName);
								objSolicitud.getLstPersona().get(i).getLstRepresentante().get(j).getLstArchivo().get(k).setVcRuta(objParam.getVcRutaFinal());
		    			}
					
		    		}
		    	}

		    			    	
		     	//FIN Proceso de archivos en base64
		    	
				objResultDAO=objConn.doRegistro(objSolicitud);
				
				if(Integer.parseInt(objResultDAO.get("POUT_NU_ERROR")+"")==0) {
				objResult.setNuExpediente(Integer.parseInt(objResultDAO.get("POUT_NU_NRO_EXPEDIENTE")+""));
				objResult.setNuAnio(	  Integer.parseInt(""+objResultDAO.get("POUT_NU_ANIO_EXPEDIENTE")));
				objResult.setVcArea(	  ClsConstantes.vcArea);
				objResult.setVcNomArchivo(objResultDAO.get("VC_NOM_ARCHIVO")+"");
				
				}else {
					throw new ClsException(""+objResultDAO.get("POUT_VC_ERROR"), new Throwable("-1"));
				}
				
		    }else {
		    	throw new ClsException(""+objResultDAO.get("POUT_VC_ERROR"), new Throwable("-1"));
		    }
			
			
		}catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new ClsException(""+objResultDAO.get("POUT_VC_ERROR"), new Throwable("-1"));
			}
		
		return objResult;
	}
	
	public void doPrioridad() {
		
	}
	
}
