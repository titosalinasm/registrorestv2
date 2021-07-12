package pe.gob.indecopi.controller;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.indecopi.bean.ClsFiltroConfiguracionBean;
import pe.gob.indecopi.result.ClsConfiguracionResult;
import pe.gob.indecopi.service.ClsConfiguracionService;
import pe.gob.indecopi.service.ClsRegistroService;


@CrossOrigin(origins ="*", allowedHeaders = "*", maxAge = 3600)

@RestController
@RequestMapping({"/configuracion"})
public class ClsConfiguracionController implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4137031170215308077L;

	private static Logger logger = LoggerFactory.getLogger(ClsConfiguracionController.class);
	
	@Autowired
	private ClsConfiguracionService objConnConfiguracionService;
	
	@RequestMapping(method = RequestMethod.POST, path = "/config", consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ClsConfiguracionResult> doConfiguracion(@RequestBody ClsFiltroConfiguracionBean objConfiguracion) {
			logger.info("doConfiguracion()");
		return ResponseEntity.ok().body(objConnConfiguracionService.doConfiguracion(objConfiguracion)) ;
	}

}
