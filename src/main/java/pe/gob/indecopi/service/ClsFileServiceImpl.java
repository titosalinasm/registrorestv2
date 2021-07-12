package pe.gob.indecopi.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pe.gob.indecopi.bean.ClsParametrosBean;
import pe.gob.indecopi.exception.ClsException;
import pe.gob.indecopi.repository.ClsParametroRepositoryI;
import pe.gob.indecopi.repository.ClsTramiteSELRepositoryI;
import pe.gob.indecopi.result.ClsDescargarResult;
import pe.gob.indecopi.result.ClsFileUploadResult;
import pe.gob.indecopi.util.ClsConfigFilePath;
import pe.gob.indecopi.util.ClsMediaTypeUtils;
import pe.gob.indecopi.util.ClsResultDAO;
import pe.gob.indecopi.util.ClsUtil;

@Service
public class ClsFileServiceImpl implements Serializable, ClsFileService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Logger logger = Logger.getLogger(ClsFileServiceImpl.class);
	
	@Autowired
	ClsConfigFilePath propiedades;
	
	@Autowired
	private ClsParametroRepositoryI objConnParam;
	
	
	@Autowired
	private ClsResultDAO objResultDAO;
	
	@Autowired
	private ServletContext servletContext;
	

public ClsDescargarResult doDowloadFile(HttpServletResponse resonse,
										String fileName){
	  logger.info("doDowloadFile()");
	  ClsDescargarResult objRespuesta=new ClsDescargarResult();
	  objRespuesta.setNuError(0L);
	  objRespuesta.setVcError("");
	  try {
		  
		    ClsParametrosBean objParam=new ClsParametrosBean();
			objResultDAO=objConnParam.doParametro();
			objParam=(ClsParametrosBean)objResultDAO.get("POUT_PARAMETRO");
		  
	  MediaType mediaType = ClsMediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
      logger.info("fileName: " + fileName);
      logger.info("mediaType: " + mediaType);
      
      File file = new File(objParam.getVcRutaFinal()+"/"+fileName);
  
      String vcExtension=file.getName().split("\\.")[1];
      
   // Content-Type
      //ejemplo
      // application/pdf
      resonse.setContentType(mediaType.getType());
      
   // Content-Disposition
      resonse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" 
    		  											  +"INDECOPI-"+ClsUtil.doGenerarNombreArchivoAleatorio() //Nombre generado
    		  											  +"."+vcExtension); //Extension

      // Content-Length
      resonse.setContentLength((int) file.length());

      
      BufferedInputStream inStream;
      BufferedOutputStream outStream;

		inStream = new BufferedInputStream(new FileInputStream(file));
		outStream = new BufferedOutputStream(resonse.getOutputStream());
	      byte[] buffer = new byte[1024];
	      int bytesRead = 0;
	      while ((bytesRead = inStream.read(buffer)) != -1) {
	          outStream.write(buffer, 0, bytesRead);
	      	}
	      outStream.flush();
	      inStream.close();
	      
	  }catch( Exception e) {
		  objRespuesta.setNuError(-1L);
		  objRespuesta.setVcError("Error "+e);
	      }
	      
	      return objRespuesta;

}
	

}
