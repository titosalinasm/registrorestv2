package pe.gob.indecopi.repository;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.support.oracle.SqlArrayValue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.CLOB;
import pe.gob.indecopi.bean.ClsArchivoBean;
import pe.gob.indecopi.bean.ClsClaseSolBean;
import pe.gob.indecopi.bean.ClsExpedienteBean;
import pe.gob.indecopi.bean.ClsParametrosBean;
import pe.gob.indecopi.bean.ClsPersonaBean;
import pe.gob.indecopi.bean.ClsPrioridadBean;
import pe.gob.indecopi.bean.ClsRepresentanteBean;
import pe.gob.indecopi.bean.ClsSolicitudBean;
import pe.gob.indecopi.bean.ClsTramiteSELBean;
import pe.gob.indecopi.exception.ClsException;
import pe.gob.indecopi.param.ClsConstantes;
import pe.gob.indecopi.service.ClsRegistroService;
import pe.gob.indecopi.util.ClsResultDAO;
import pe.gob.indecopi.util.ClsUtil;

@Repository
@Transactional
public class ClsRegistroRepository implements Serializable, ClsRegistroRepositoryI{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3789470206482678434L;

	private static Logger logger = LoggerFactory.getLogger(ClsRegistroService.class);
	
	private SimpleJdbcCall simpleJdbcCall;
	
	@Autowired
	private ClsExpedienteRepositoryI objConnExpe;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//DataSouser incio multiple
	  @Autowired
	  private DataSource dataSource;
	  
	  @PostConstruct
	  private void init()
	  {
	    this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	  }
	//DataSouser fin multiple
	
	@Autowired
	private ClsResultDAO objResultDAO;
	
	
	@Autowired
	private ClsTramiteSELRepositoryI objConnSEL;
	
	@Autowired
	private ClsParametroRepositoryI objConnParam;
	
	@Override
	public ClsResultDAO doRegistro(ClsSolicitudBean objSolicitud) {

		try {
			//procedure
			this.simpleJdbcCall=new SimpleJdbcCall(jdbcTemplate)
									.withSchemaName(ClsConstantes.SCHEMA_USR_MARCAS)
									.withCatalogName(ClsConstantes.PKG_REG_MARCA)
									.withProcedureName(ClsConstantes.SP_DO_REG_MARCAS)
									.declareParameters(
									new SqlParameter("PIN_ARR_VC_RUTA_S",			 OracleTypes.ARRAY,  "STR_ARRAY"), 	
									new SqlParameter("PIN_ARR_VC_NOM_ORIGINAL_S", 	 OracleTypes.ARRAY,  "STR_ARRAY"), 
									new SqlParameter("PIN_ARR_VC_NOM_FINAL_S", 		 OracleTypes.ARRAY,  "STR_ARRAY"), 
									new SqlParameter("PIN_ARR_VC_EXTENSION_S", 		 OracleTypes.ARRAY,  "STR_ARRAY"), 
									new SqlParameter("PIN_ARR_NU_ID_TIPO_ARCHIVO_S", OracleTypes.ARRAY,  "NUM_ARRAY"),  
									new SqlParameter("PIN_ARR_NU_LONGITUD_S", 		 OracleTypes.ARRAY, "NUM_ARRAY"), 
                       
									new SqlParameter("PIN_ARR_NU_ID_CLASE", OracleTypes.ARRAY, "NUM_ARRAY"),	
									new SqlParameter("PIN_ARR_VC_CLASE1"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE2"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE3"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE4"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE5"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE6"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE7"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE8"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE9"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE10"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE11"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE12"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE13"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE14"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CLASE15"	  , OracleTypes.ARRAY, "STR_ARRAY"),
									
									new SqlParameter("PIN_ARR_VC_IDENTI_A"		, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_NU_FLAG_TODOS"	, OracleTypes.ARRAY, "NUM_ARRAY"),	
									new SqlParameter("PIN_ARR_VC_CLASES"		, OracleTypes.ARRAY, "STR_ARRAY"),	
									new SqlParameter("PIN_ARR_VC_NRO_SOLICITUD"	, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_DT_FECHA_PRIORID"	, OracleTypes.ARRAY, "DATE_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_PAIS_P"		, OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_TIPO_PRIO"	, OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_VC_PROD_SERV"		, OracleTypes.ARRAY, "STR_ARRAY"),
									
									new SqlParameter("PIN_ARR_VC_IDENTI_B"			, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_RUTA_P"			, OracleTypes.ARRAY, "STR_ARRAY"),	
									new SqlParameter("PIN_ARR_VC_NOM_ORIGINAL_P"	, OracleTypes.ARRAY, "STR_ARRAY"),	
									new SqlParameter("PIN_ARR_VC_NOM_FINAL_P"		, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_EXTENSION_P"		, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_TIPO_ARCHIVO_P"	, OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_LONGITUD_P"		, OracleTypes.ARRAY, "NUM_ARRAY"),	
									//Solicitantes
									new SqlParameter("PIN_ARR_VC_IDENTI_C"	  	  ,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_NU_TIPO_ORIGEN"	  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_TIPO_PERSONA"	  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_TIPO_DOCUMENTO"  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_VC_DOC_IDENTIDAD" 	, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CORREO"		 	, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_NOMBRES"		  ,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_AP_PATERNO"		, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_AP_MATERNO"		, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_RAZON_SOCI"		, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_GENERO"		  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_PAIS"		  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_PAIS_ORI"     ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_DEPAR"		  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_PROVINCI"		  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_DISTRITO"		  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_VC_DIRECCION"		  ,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_TELEFONO"		  ,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_NU_FLAG_SOLICI"	  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_MYPE"	  		  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									
									//Representantes
									new SqlParameter("PIN_ARR_VC_IDENTI_D"	  	  ,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_IDENTI_E"	  	  ,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_NU_TIPO_ORIGEN_R"	  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_TIPO_PERSONA_R"	  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_TIPO_DOCUMENTO_R"  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_VC_DOC_IDENTIDAD_R" 	, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_CORREO_R"		 	, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_NOMBRES_R"		  ,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_AP_PATERNO_R"		, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_AP_MATERNO_R"		, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_RAZON_SOCI_R"		, OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_GENERO_R"		  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_PAIS_R"		  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_PAIS_ORI_R"     ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_DEPAR_R"		  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_PROVINCI_R"		  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_DISTRITO_R"		  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_VC_DIRECCION_R"		  ,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_TELEFONO_R"		  ,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_NU_FLAG_SOLICI_R"	  ,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_VC_NRO_EXP_REP_R"		  ,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_NRO_SOL_REP_R"		  ,   OracleTypes.ARRAY, "STR_ARRAY"),
									
									new SqlParameter("PIN_ARR_VC_IDENTI_F"	  			,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_RUTA_PE"	  			,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_NOM_ORIGINAL_PE"		,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_NOM_FINAL_PE"	  		,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_VC_EXTENSION_PE"	  		,   OracleTypes.ARRAY, "STR_ARRAY"),
									new SqlParameter("PIN_ARR_NU_ID_TIPO_ARCHIVO_PE"	,   OracleTypes.ARRAY, "NUM_ARRAY"),
									new SqlParameter("PIN_ARR_NU_LONGITUD_PE"	  		,   OracleTypes.ARRAY, "NUM_ARRAY"),
								
									
									new  SqlOutParameter("POUT_CL_CUERPO_CORREO", OracleTypes.CLOB ,"CLOB"),
									new  SqlOutParameter("POUT_NU_NRO_EXPEDIENTE", OracleTypes.NUMBER ,"NUMBER"),
									new  SqlOutParameter("POUT_VC_AREA_EXPEDIENTE", OracleTypes.VARCHAR ,"VARCHAR2"),
									new  SqlOutParameter("POUT_NU_ANIO_EXPEDIENTE", OracleTypes.NUMBER ,"NUMBER"),
			                        new  SqlOutParameter("POUT_NU_ESTADO_REGISTRO", OracleTypes.NUMBER ,"NUMBER"),
			                        new  SqlOutParameter("POUT_NU_PROCEDIMIENTO", OracleTypes.NUMBER ,"NUMBER"),
									
									new  SqlOutParameter("POUT_NU_ERROR", OracleTypes.NUMBER ,"NUMBER"),
									new  SqlOutParameter("POUT_VC_ERROR", OracleTypes.VARCHAR ,"VARCHAR2")
									);
			
			//ARCHIVOS DE MI SIGNO
			int nuTamanioLstArchSigno=objSolicitud.getObjDatosSigno().getLstArchivosSigno().size();
			String[]  var_vcRuta			= new String[nuTamanioLstArchSigno];
			String[]  var_vcNomOriginal     = new String[nuTamanioLstArchSigno];
			String[]  var_vcNomFinal        = new String[nuTamanioLstArchSigno];
			String[]  var_vcExtension       = new String[nuTamanioLstArchSigno];
			Integer[] var_nuIdTipoArchivo   = new Integer[nuTamanioLstArchSigno];
			Long[] var_nuLong            	= new Long[nuTamanioLstArchSigno];
			
			for (int i=0; i<nuTamanioLstArchSigno; i++) {
				ClsArchivoBean objArchivo=objSolicitud.getObjDatosSigno().getLstArchivosSigno().get(i);
				var_vcRuta			  [i]= objArchivo.getVcRuta();
				var_vcNomOriginal     [i]= objArchivo.getVcNomOriginal();
				var_vcNomFinal        [i]= objArchivo.getVcNomFinal();
				var_vcExtension       [i]= objArchivo.getVcExtension();
				var_nuIdTipoArchivo   [i]= objArchivo.getNuIdTipoArchivo();
				var_nuLong            [i]= objArchivo.getNuLong();
			}
			
			//CLASES
			
			int nuTaminioLstClase=objSolicitud.getLstClases().size();
			Integer[] var_nuIdClase = new Integer[nuTaminioLstClase];
			String[]  var_clClase1   = new String[nuTaminioLstClase];
			String[]  var_clClase2   = new String[nuTaminioLstClase];
			String[]  var_clClase3   = new String[nuTaminioLstClase];
			String[]  var_clClase4   = new String[nuTaminioLstClase];
			String[]  var_clClase5   = new String[nuTaminioLstClase];
			String[]  var_clClase6   = new String[nuTaminioLstClase];
			String[]  var_clClase7   = new String[nuTaminioLstClase];
			String[]  var_clClase8   = new String[nuTaminioLstClase];
			String[]  var_clClase9   = new String[nuTaminioLstClase];
			String[]  var_clClase10   = new String[nuTaminioLstClase];
			String[]  var_clClase11   = new String[nuTaminioLstClase];
			String[]  var_clClase12   = new String[nuTaminioLstClase];
			String[]  var_clClase13   = new String[nuTaminioLstClase];
			String[]  var_clClase14   = new String[nuTaminioLstClase];
			String[]  var_clClase15   = new String[nuTaminioLstClase];
			
			for(int i=0; i<nuTaminioLstClase; i++){
				ClsClaseSolBean objClases=objSolicitud.getLstClases().get(i);
				var_nuIdClase[i]=objClases.getNuIdClase();
				logger.info(objClases.getVcClase1());
				logger.info("|"+objClases.getVcClase2());
				var_clClase1	 [i]=objClases.getVcClase1();
				var_clClase2	 [i]=objClases.getVcClase2();
				var_clClase3	 [i]=objClases.getVcClase3();
				var_clClase4	 [i]=objClases.getVcClase4();
				var_clClase5	 [i]=objClases.getVcClase5();
				var_clClase6	 [i]=objClases.getVcClase6();
				var_clClase7	 [i]=objClases.getVcClase7();
				var_clClase8	 [i]=objClases.getVcClase8();
				var_clClase9	 [i]=objClases.getVcClase9();
				var_clClase10	 [i]=objClases.getVcClase10();
				var_clClase11	 [i]=objClases.getVcClase11();
				var_clClase12	 [i]=objClases.getVcClase12();
				var_clClase13	 [i]=objClases.getVcClase13();
				var_clClase14	 [i]=objClases.getVcClase14();
				var_clClase15	 [i]=objClases.getVcClase15();
			}
			
			
			//prioridad extranjera
			int nuTamanioPrioridad=objSolicitud.getObjPrioridadExtr().getLstPrioridad().size();
			String[] var_nuIdentificadorA= new String[nuTamanioPrioridad];
			Integer[] var_nuFlagTodos      =new Integer[nuTamanioPrioridad];
		    String[] var_vcNombreClases   =new String[nuTamanioPrioridad];
		    String[] var_vcClases         =new String[nuTamanioPrioridad];
		    String[] var_vcNroSolicitud   =new String[nuTamanioPrioridad];
		    java.sql.Date[] var_dtFechaPrioridad =new java.sql.Date[nuTamanioPrioridad];
		    Integer[] var_nuIdUbigeoPais   =new Integer[nuTamanioPrioridad];
		    Integer[] var_nuTipoPrioridad  =new Integer[nuTamanioPrioridad];
		    String[] var_vcTipoPrioridad  =new String[nuTamanioPrioridad];
		    String[] var_vcProductoServicio=new String[nuTamanioPrioridad];
		    
		    String[]  var_nuIdentificadorB  ;
		    String[]  var_vcRuta_p		    ;
			String[]  var_vcNomOriginal_p   ;
			String[]  var_vcNomFinal_p      ;
			String[]  var_vcExtension_p     ;
			Integer[] var_nuIdTipoArchivo_p ;
			Long[]    var_nuLong_p          ;
			
			List<String>   ar_nuIdentificadorB  = new  ArrayList<String>();
			List<String>   ar_vcRuta_p		   = new  ArrayList<String>();
		    List<String>   ar_vcNomOriginal_p  = new  ArrayList<String>();
		    List<String>   ar_vcNomFinal_p     = new  ArrayList<String>();
			List<String>   ar_vcExtension_p    = new  ArrayList<String>();
			List<Integer>  ar_nuIdTipoArchivo_p= new  ArrayList<Integer>();
			List<Long>     ar_nuLong_p         = new  ArrayList<Long>();
			
			for(int i=0; i<nuTamanioPrioridad; i++){
				ClsPrioridadBean objPrioridad=objSolicitud.getObjPrioridadExtr().getLstPrioridad().get(i);
				var_nuIdentificadorA[i]=ClsUtil.doGenerarNombreArchivoAleatorio();
			    var_nuFlagTodos     [i]= objPrioridad.getNuFlagTodos();
				var_vcNombreClases  [i]= objPrioridad.getVcNombreClases();
				
				var_vcClases        [i]= objPrioridad.getVcClases();
				var_vcNroSolicitud  [i]= objPrioridad.getVcNroSolicitud();
				var_dtFechaPrioridad[i]= objPrioridad.getDtFechaPrioridad()!=null?new java.sql.Date(objPrioridad.getDtFechaPrioridad().getTime()+1):null;
				var_nuIdUbigeoPais  [i]= objPrioridad.getNuIdUbigeoPais();
				var_nuTipoPrioridad [i]= objPrioridad.getNuTipoPrioridad();
				var_vcTipoPrioridad [i]= objPrioridad.getVcTipoPrioridad();
				var_vcProductoServicio[i]=objPrioridad.getVcProductoServicio();
				int nuTamanioArchP=objPrioridad.getLstArchivoPrioridad().size();
				for(int j=0; j<nuTamanioArchP; j++) {
					ClsArchivoBean objArchivo=objPrioridad.getLstArchivoPrioridad().get(j);
					logger.info("objArchivo.getVcNomFinal() : "+objArchivo.getVcNomFinal() );
					ar_nuIdentificadorB	  .add(var_nuIdentificadorA[i]);
					ar_vcRuta_p		      .add(objArchivo.getVcRuta()         );
					ar_vcNomOriginal_p    .add(objArchivo.getVcNomOriginal()  );
					ar_vcNomFinal_p       .add(objArchivo.getVcNomFinal()     );
					ar_vcExtension_p      .add(objArchivo.getVcExtension()    );
					ar_nuIdTipoArchivo_p  .add(objArchivo.getNuIdTipoArchivo());
					ar_nuLong_p           .add(objArchivo.getNuLong()         );
				}
				
			}
			
			var_nuIdentificadorB =new  String[ar_nuIdentificadorB.size()];
			var_vcRuta_p		 = new String[ar_nuIdentificadorB.size()];
			var_vcNomOriginal_p  = new String[ar_nuIdentificadorB.size()];
			var_vcNomFinal_p     = new String[ar_nuIdentificadorB.size()];
			var_vcExtension_p    = new String[ar_nuIdentificadorB.size()];
			var_nuIdTipoArchivo_p= new Integer[ar_nuIdentificadorB.size()];                         
			var_nuLong_p         = new Long[ar_nuIdentificadorB.size()];
			
			var_nuIdentificadorB = ar_nuIdentificadorB	 .toArray(var_nuIdentificadorB );
			var_vcRuta_p		 = ar_vcRuta_p		     .toArray(var_vcRuta_p		   );
			var_vcNomOriginal_p  = ar_vcNomOriginal_p    .toArray(var_vcNomOriginal_p  );
			var_vcNomFinal_p     = ar_vcNomFinal_p       .toArray(var_vcNomFinal_p     );
			var_vcExtension_p    = ar_vcExtension_p      .toArray(var_vcExtension_p    );
			var_nuIdTipoArchivo_p= ar_nuIdTipoArchivo_p  .toArray(var_nuIdTipoArchivo_p);
			var_nuLong_p         = ar_nuLong_p           .toArray(var_nuLong_p         );
			
			
			//Solicitante
			int nuTamanioLstPersona=objSolicitud.getLstPersona().size();
			String[]  var_vcIdentificadorC =new String[nuTamanioLstPersona];
			String[]  var_vcNombres        =new String[nuTamanioLstPersona];
		    String[]  var_vcApPaterno      =new String[nuTamanioLstPersona];
		    String[]  var_vcApMaterno      =new String[nuTamanioLstPersona];
		    String[]  var_vcRazonSocial    =new String[nuTamanioLstPersona];
		    Integer[] var_nuIdTipoOrigen   =new Integer[nuTamanioLstPersona];
		    Integer[] var_nuIdTipoPersona  =new Integer[nuTamanioLstPersona];
		    Integer[] var_nuIdTipoDocumento=new Integer[nuTamanioLstPersona];
		    String[]  var_vcDocIdentidad   =new String[nuTamanioLstPersona];
		    Integer[] var_nuIdPais         =new Integer[nuTamanioLstPersona];
		    Integer[] var_nuIdPaisOri      =new Integer[nuTamanioLstPersona];
		    Integer[] var_nuDepartamento   =new Integer[nuTamanioLstPersona];
		    Integer[] var_nuProvincia      =new Integer[nuTamanioLstPersona];
		    Integer[] var_nuDistrito       =new Integer[nuTamanioLstPersona];
		    String[]  var_vcDireccion      =new String[nuTamanioLstPersona];
		    String[]  var_vcCorreo         =new String[nuTamanioLstPersona];
		    String[]  var_vcTelefono       =new String[nuTamanioLstPersona];
		    Integer[] var_nuFlagSolicitante=new Integer[nuTamanioLstPersona];
		    Integer[] var_nuGenero		   =new Integer[nuTamanioLstPersona];
		    Integer[] var_nuMype      	   =new Integer[nuTamanioLstPersona];
		    
		    //Representante
		    String[]  r_vcIdentificadorD ;
		    String[]  r_vcIdentificadorE ;
			String[]  r_vcNombres        ;
		    String[]  r_vcApPaterno      ;
		    String[]  r_vcApMaterno      ;
		    String[]  r_vcRazonSocial    ;
		    Integer[] r_nuIdTipoOrigen   ;
		    Integer[] r_nuIdTipoPersona  ;
		    Integer[] r_nuIdTipoDocumento;
		    String[]  r_vcDocIdentidad   ;
		    Integer[] r_nuIdPais         ;
		    Integer[] r_nuIdPaisOri      ;
		    Integer[] r_nuDepartamento   ;
		    Integer[] r_nuProvincia      ;
		    Integer[] r_nuDistrito       ;
		    String[]  r_vcDireccion      ;
		    String[]  r_vcCorreo         ;
		    String[]  r_vcTelefono       ;
		    Integer[] r_nuFlagSolicitante;
		    Integer[] r_nuGenero		 ;
		    String[]  r_vcPartidaRegistra;
		    String[]  r_vcNroExpediente  ;
		    
		    List<String>  lstr_vcIdentificadorD =new ArrayList<String>();
		    List<String>  lstr_vcIdentificadorE =new ArrayList<String>();
			List<String>  lstr_vcNombres        =new ArrayList<String>();
		    List<String>  lstr_vcApPaterno      =new ArrayList<String>();
		    List<String>  lstr_vcApMaterno      =new ArrayList<String>();
		    List<String>  lstr_vcRazonSocial    =new ArrayList<String>();
		    List<Integer> lstr_nuIdTipoOrigen   =new ArrayList<Integer>();
		    List<Integer> lstr_nuIdTipoPersona  =new ArrayList<Integer>();
		    List<Integer> lstr_nuIdTipoDocumento=new ArrayList<Integer>();
		    List<String>  lstr_vcDocIdentidad   =new ArrayList<String>();
		    List<Integer> lstr_nuIdPais         =new ArrayList<Integer>();
		    List<Integer> lstr_nuIdPaisOri      =new ArrayList<Integer>();
		    List<Integer> lstr_nuDepartamento   =new ArrayList<Integer>();
		    List<Integer> lstr_nuProvincia      =new ArrayList<Integer>();
		    List<Integer> lstr_nuDistrito       =new ArrayList<Integer>();
		    List<String>  lstr_vcDireccion      =new ArrayList<String>();
		    List<String>  lstr_vcCorreo         =new ArrayList<String>();
		    List<String>  lstr_vcTelefono       =new ArrayList<String>();
		    List<Integer> lstr_nuFlagSolicitante=new ArrayList<Integer>();
		    List<Integer> lstr_nuGenero		    =new ArrayList<Integer>();
		    List<String>  lstr_vcPartidaRegistra=new ArrayList<String>();
		    List<String>  lstr_vcNroExpediente  =new ArrayList<String>();
		    

		    //Archivos de representantes
		    String[]  var_vcIdentificadorF  ;
		    String[]  var_vcRuta_pe		    ;
			String[]  var_vcNomOriginal_pe   ;
			String[]  var_vcNomFinal_pe     ;
			String[]  var_vcExtension_pe     ;
			Integer[] var_nuIdTipoArchivo_pe ;
			Long[]    var_nuLong_pe          ;
			
			List<String>   lstr_vcIdentificadorF  = new  ArrayList<String>();
			List<String>   lstr_vcRuta_pe		  = new  ArrayList<String>();
		    List<String>   lstr_vcNomOriginal_pe  = new  ArrayList<String>();
		    List<String>   lstr_vcNomFinal_pe     = new  ArrayList<String>();
			List<String>   lstr_vcExtension_pe    = new  ArrayList<String>();
			List<Integer>  lstr_nuIdTipoArchivo_pe= new  ArrayList<Integer>();
			List<Long>     lstr_nuLong_pe         = new  ArrayList<Long>();
			

			for(int i=0; i<nuTamanioLstPersona; i++) {
				ClsPersonaBean objPersona= objSolicitud.getLstPersona().get(i);
				var_vcIdentificadorC	  [i]=ClsUtil.doGenerarNombreArchivoAleatorio();
				var_vcNombres             [i]=objPersona.getVcNombres();
				var_vcApPaterno           [i]=objPersona.getVcPrimerApellido();
				var_vcApMaterno           [i]=objPersona.getVcSegundoApellido();
				var_vcRazonSocial         [i]=objPersona.getVcRsocial();
				var_nuIdTipoOrigen        [i]=objPersona.getNuTipoOrigen();
				var_nuIdTipoPersona       [i]=objPersona.getNuTipoPersoneria();
				var_nuIdTipoDocumento     [i]=objPersona.getNuTipoDocumento();
				var_vcDocIdentidad        [i]=objPersona.getVcNroDocumento();
				var_nuIdPaisOri		      [i]=objPersona.getNuPaisOrigen();
				var_nuIdPais              [i]=objPersona.getNuPaisResidencia();
				var_nuDepartamento        [i]=objPersona.getNuDepartamento();
				var_nuProvincia           [i]=objPersona.getNuProvincia();
				var_nuDistrito            [i]=objPersona.getNuDistrito();
				var_vcDireccion           [i]=objPersona.getVcDireccion();
				var_vcCorreo              [i]=objPersona.getVcCorreo();
				var_vcTelefono            [i]=objPersona.getVcTelefono();
				var_nuFlagSolicitante     [i]=objPersona.getNuFlagSolicitante();
				var_nuGenero			  [i]=objPersona.getNuGenero();
				var_nuMype				  [i]=objPersona.getNuFlagMype();
				logger.info(i+".-Solic: "+var_vcIdentificadorC[i]+"<=>"+var_vcDocIdentidad[i]);
				for(int j=0; j<objSolicitud.getLstPersona().get(i).getLstRepresentante().size(); j++) {
					ClsRepresentanteBean objRepresentante=objSolicitud.getLstPersona().get(i).getLstRepresentante().get(j);
					String vcCodigo=ClsUtil.doGenerarNombreArchivoAleatorio();
					lstr_vcIdentificadorD .add(var_vcIdentificadorC	  [i]);
					lstr_vcIdentificadorE .add(vcCodigo);
					lstr_vcNombres        .add(objRepresentante.getVcNombres());
					lstr_vcApPaterno      .add(objRepresentante.getVcPrimerApellido());
					lstr_vcApMaterno      .add(objRepresentante.getVcSegundoApellido());
					lstr_vcRazonSocial    .add(objRepresentante.getVcRsocial());
					lstr_nuIdTipoOrigen   .add(objRepresentante.getNuTipoOrigen());
					lstr_nuIdTipoPersona  .add(objRepresentante.getNuTipoPersoneria());
					lstr_nuIdTipoDocumento.add(objRepresentante.getNuTipoDocumento());
					lstr_vcDocIdentidad   .add(objRepresentante.getVcNroDocumento());
					lstr_nuIdPais         .add(objRepresentante.getNuPaisOrigen());
					lstr_nuIdPaisOri      .add(objRepresentante.getNuPaisResidencia());
					lstr_nuDepartamento   .add(objRepresentante.getNuDepartamento());
					lstr_nuProvincia      .add(objRepresentante.getNuProvincia());
					lstr_nuDistrito       .add(objRepresentante.getNuDistrito());
					lstr_vcDireccion      .add(objRepresentante.getVcDireccion());
					lstr_vcCorreo         .add(objRepresentante.getVcCorreo());
					lstr_vcTelefono       .add(objRepresentante.getVcTelefono());
					lstr_nuFlagSolicitante.add(objRepresentante.getNuFlagSolicitante());
					lstr_nuGenero		  .add(objRepresentante.getNuGenero());   
					lstr_vcPartidaRegistra.add(objRepresentante.getVcPartidaRegistral());
					lstr_vcNroExpediente  .add(objRepresentante.getVcNroExpediente());
					
					logger.info(j+".-Repre: "+var_vcIdentificadorC[i]+"<=>"+objRepresentante.getVcNroDocumento());
					
					for(int k=0; k<objSolicitud.getLstPersona().get(i).getLstRepresentante().get(j).getLstArchivo().size(); k++) {
						ClsArchivoBean objArchivo=objSolicitud.getLstPersona().get(i).getLstRepresentante().get(j).getLstArchivo().get(k);
						lstr_vcIdentificadorF  .add(vcCodigo);
						lstr_vcRuta_pe		   .add(objArchivo.getVcRuta());
						lstr_vcNomOriginal_pe  .add(objArchivo.getVcNomOriginal());
						lstr_vcNomFinal_pe     .add(objArchivo.getVcNomFinal());
						lstr_vcExtension_pe    .add(objArchivo.getVcExtension());
						lstr_nuIdTipoArchivo_pe.add(objArchivo.getNuIdTipoArchivo());
						lstr_nuLong_pe         .add(objArchivo.getNuLong());
					}	
				}	
			}
			
			
			r_vcIdentificadorD =new String[lstr_vcIdentificadorD.size()];
			r_vcIdentificadorE =new String[lstr_vcIdentificadorD.size()];
			r_vcNombres        =new String[lstr_vcIdentificadorD.size()];
			r_vcApPaterno      =new String[lstr_vcIdentificadorD.size()];
			r_vcApMaterno      =new String[lstr_vcIdentificadorD.size()];
			r_vcRazonSocial    =new String[lstr_vcIdentificadorD.size()];
			r_nuIdTipoOrigen   =new Integer[lstr_vcIdentificadorD.size()];
			r_nuIdTipoPersona  =new Integer[lstr_vcIdentificadorD.size()];
			r_nuIdTipoDocumento=new Integer[lstr_vcIdentificadorD.size()];
			r_vcDocIdentidad   =new String[lstr_vcIdentificadorD.size()];
			r_nuIdPais         =new Integer[lstr_vcIdentificadorD.size()];
			r_nuIdPaisOri      =new Integer[lstr_vcIdentificadorD.size()];
			r_nuDepartamento   =new Integer[lstr_vcIdentificadorD.size()];
			r_nuProvincia      =new Integer[lstr_vcIdentificadorD.size()];
			r_nuDistrito       =new Integer[lstr_vcIdentificadorD.size()];
			r_vcDireccion      =new String[lstr_vcIdentificadorD.size()];
			r_vcCorreo         =new String[lstr_vcIdentificadorD.size()];
			r_vcTelefono       =new String[lstr_vcIdentificadorD.size()];
			r_nuFlagSolicitante=new Integer[lstr_vcIdentificadorD.size()];
			r_nuGenero		   =new Integer[lstr_vcIdentificadorD.size()];
			r_vcPartidaRegistra=new String[lstr_vcIdentificadorD.size()];
			r_vcNroExpediente  =new String[lstr_vcIdentificadorD.size()];
			
			r_vcIdentificadorD  = lstr_vcIdentificadorD .toArray(r_vcIdentificadorD);
			r_vcIdentificadorE  = lstr_vcIdentificadorE .toArray(r_vcIdentificadorE);
			r_vcNombres         = lstr_vcNombres        .toArray(r_vcNombres);
			r_vcApPaterno       = lstr_vcApPaterno      .toArray(r_vcApPaterno);
			r_vcApMaterno       = lstr_vcApMaterno      .toArray(r_vcApMaterno);
			r_vcRazonSocial     = lstr_vcRazonSocial    .toArray(r_vcRazonSocial);
			r_nuIdTipoOrigen    = lstr_nuIdTipoOrigen   .toArray(r_nuIdTipoOrigen);
			r_nuIdTipoPersona   = lstr_nuIdTipoPersona  .toArray(r_nuIdTipoPersona);
			r_nuIdTipoDocumento = lstr_nuIdTipoDocumento.toArray(r_nuIdTipoDocumento);
			r_vcDocIdentidad    = lstr_vcDocIdentidad   .toArray(r_vcDocIdentidad);
			r_nuIdPais          = lstr_nuIdPais         .toArray(r_nuIdPais);
			r_nuIdPaisOri       = lstr_nuIdPaisOri      .toArray(r_nuIdPaisOri);
			r_nuDepartamento    = lstr_nuDepartamento   .toArray(r_nuDepartamento);
			r_nuProvincia       = lstr_nuProvincia      .toArray(r_nuProvincia);
			r_nuDistrito        = lstr_nuDistrito       .toArray(r_nuDistrito);
			r_vcDireccion       = lstr_vcDireccion      .toArray(r_vcDireccion);
			r_vcCorreo          = lstr_vcCorreo         .toArray(r_vcCorreo);
			r_vcTelefono        = lstr_vcTelefono       .toArray(r_vcTelefono);
			r_nuFlagSolicitante = lstr_nuFlagSolicitante.toArray(r_nuFlagSolicitante);
			r_nuGenero		    = lstr_nuGenero		    .toArray(r_nuGenero);
			r_vcPartidaRegistra = lstr_vcPartidaRegistra.toArray(r_vcPartidaRegistra);
			r_vcNroExpediente   = lstr_vcNroExpediente  .toArray(r_vcNroExpediente);
			
			
			
			var_vcIdentificadorF =new  String[lstr_vcIdentificadorF.size()];
			var_vcRuta_pe		 = new String[lstr_vcIdentificadorF.size()];
			var_vcNomOriginal_pe  = new String[lstr_vcIdentificadorF.size()];
			var_vcNomFinal_pe     = new String[lstr_vcIdentificadorF.size()];
			var_vcExtension_pe    = new String[lstr_vcIdentificadorF.size()];
			var_nuIdTipoArchivo_pe= new Integer[lstr_vcIdentificadorF.size()];                         
			var_nuLong_pe         = new Long[lstr_vcIdentificadorF.size()];
			
			var_vcIdentificadorF  = lstr_vcIdentificadorF  .toArray(var_vcIdentificadorF );
			var_vcRuta_pe		  = lstr_vcRuta_pe		   .toArray(var_vcRuta_pe		   );
			var_vcNomOriginal_pe  = lstr_vcNomOriginal_pe  .toArray(var_vcNomOriginal_pe  );
			var_vcNomFinal_pe     = lstr_vcNomFinal_pe     .toArray(var_vcNomFinal_pe     );
			var_vcExtension_pe    = lstr_vcExtension_pe    .toArray(var_vcExtension_pe    );
			var_nuIdTipoArchivo_pe= lstr_nuIdTipoArchivo_pe.toArray(var_nuIdTipoArchivo_pe);
			var_nuLong_pe         = lstr_nuLong_pe         .toArray(var_nuLong_pe         );
			
			
			
			 Map<String, Object> inParamMap = new HashMap();
			 
			
		     inParamMap.put("PIN_VC_USUARIO"			, objSolicitud.getVcUsuario());
		     inParamMap.put("PIN_VC_HOSTNAME"			, objSolicitud.getVcHostName());
		     
		     inParamMap.put("PIN_NU_FLAG_ASESORIA"		, objSolicitud.getObjAsesoria().getNuFlagAsesoria());
		     inParamMap.put("PIN_VC_NRO_ASESORIA"		, objSolicitud.getObjAsesoria().getVcNroAsesoria());
		     //MI SIGNO	     
		     inParamMap.put("PIN_NU_ID_TIPO_SOLICITUD"	, objSolicitud.getObjDatosSigno().getNuIdTipoSolicitud());
		     inParamMap.put("PIN_VC_DENOMINACION"		, objSolicitud.getObjDatosSigno().getVcDenominacion());
		     inParamMap.put("PIN_NU_FLAG_INT_REAL"		, objSolicitud.getObjDatosSigno().getNuFlagInteresReal());
		     inParamMap.put("PIN_VC_INT_NRO_EXPE"		, objSolicitud.getObjDatosSigno().getVcNroExpediente());
		     inParamMap.put("PIN_NU_FLAG_DER_PREF"		, objSolicitud.getObjDatosSigno().getNuFlaDerechoPreferente());
		     inParamMap.put("PIN_VC_DER_NRO_CERTI"		, objSolicitud.getObjDatosSigno().getVcNroCertificado());
		     inParamMap.put("PIN_NU_ID_TIPO_SIGNO"		, objSolicitud.getObjDatosSigno().getNuIdTipoSigno());
		     inParamMap.put("PIN_NU_FLAG_REIN_COL"		, objSolicitud.getObjDatosSigno().getNuFlagReinvindicaCol());
		     
		     inParamMap.put("PIN_VC_CERTIFICADO_LEMA"			, objSolicitud.getObjDatosSigno().getVcCertificadoLema());
		     inParamMap.put("PIN_VC_NRO_EXPEDIENTE_LEMA"		, objSolicitud.getObjDatosSigno().getVcNroExpedienteLema());
		     inParamMap.put("PIN_NU_CLASE_LEMA"					, objSolicitud.getObjDatosSigno().getNuClaseLema());
		     inParamMap.put("PIN_DT_FEHA_NOM_COME"				, objSolicitud.getObjDatosSigno().getVcFechaPrimerUsoNombreComercial()!=null?new java.sql.Date(objSolicitud.getObjDatosSigno().getVcFechaPrimerUsoNombreComercial().getTime()+1):null);
		     inParamMap.put("PIN_VC_NRO_EXPE_NOM_COME"			, objSolicitud.getObjDatosSigno().getVcNroExpedienteNombreComercial());
		     
		     
		     
		     //ARCHIVOS DE LOS SIGNOS
		     inParamMap.put("PIN_ARR_VC_RUTA_S"				, new SqlArrayValue<String>(var_vcRuta, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_NOM_ORIGINAL_S"		, new SqlArrayValue<String>(var_vcNomOriginal, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_NOM_FINAL_S"		, new SqlArrayValue<String>(var_vcNomFinal, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_EXTENSION_S"		, new SqlArrayValue<String>(var_vcExtension, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_TIPO_ARCHIVO_S"	, new SqlArrayValue<Integer>(var_nuIdTipoArchivo, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_LONGITUD_S"			, new SqlArrayValue<Long>(var_nuLong, "NUM_ARRAY"));

		     //CLASE
		     inParamMap.put("PIN_ARR_NU_ID_CLASE"			, new SqlArrayValue<Integer>(var_nuIdClase, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE1"				, new SqlArrayValue<String>(var_clClase1, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE2"				, new SqlArrayValue<String>(var_clClase2, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE3"				, new SqlArrayValue<String>(var_clClase3, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE4"				, new SqlArrayValue<String>(var_clClase4, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE5"				, new SqlArrayValue<String>(var_clClase5, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE6"				, new SqlArrayValue<String>(var_clClase6, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE7"				, new SqlArrayValue<String>(var_clClase7, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE8"				, new SqlArrayValue<String>(var_clClase8, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE9"				, new SqlArrayValue<String>(var_clClase9, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE10"				, new SqlArrayValue<String>(var_clClase10, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE11"				, new SqlArrayValue<String>(var_clClase11, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE12"				, new SqlArrayValue<String>(var_clClase12, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE13"				, new SqlArrayValue<String>(var_clClase13, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE14"				, new SqlArrayValue<String>(var_clClase14, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASE15"				, new SqlArrayValue<String>(var_clClase15, "STR_ARRAY"));
		    
		     //PRIORIDAD EXTRANJERA
		     inParamMap.put("PIN_NU_FLAG_PRIO_EXTRA"	, objSolicitud.getObjPrioridadExtr().getNuFlagPrioridadExtr());
		     inParamMap.put("PIN_ARR_VC_IDENTI_A"		, new SqlArrayValue<String>(var_nuIdentificadorA, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_FLAG_TODOS"		, new SqlArrayValue<Integer>(var_nuFlagTodos, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CLASES"			, new SqlArrayValue<String>(var_vcClases, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_NRO_SOLICITUD"	, new SqlArrayValue<String>(var_vcNroSolicitud, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_DT_FECHA_PRIORID"	, new SqlArrayValue<Date>(var_dtFechaPrioridad, "DATE_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_PAIS_P"		, new SqlArrayValue<Integer>(var_nuIdUbigeoPais, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_TIPO_PRIO"	, new SqlArrayValue<Integer>(var_nuTipoPrioridad, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_PROD_SERV"		, new SqlArrayValue<String>(var_vcProductoServicio, "STR_ARRAY"));
		    
		     //ARCHIVOS DE PRIORIDAD EXTRANJERA
		     inParamMap.put("PIN_ARR_VC_IDENTI_B"			, new SqlArrayValue<String>(var_nuIdentificadorB, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_RUTA_P"				, new SqlArrayValue<String>(var_vcRuta_p, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_NOM_ORIGINAL_P"		, new SqlArrayValue<String>(var_vcNomOriginal_p, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_NOM_FINAL_P"		, new SqlArrayValue<String>(var_vcNomFinal_p, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_EXTENSION_P"		, new SqlArrayValue<String>(var_vcExtension_p, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_TIPO_ARCHIVO_P"	, new SqlArrayValue<Integer>(var_nuIdTipoArchivo_p, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_LONGITUD_P"			, new SqlArrayValue<Long>(var_nuLong_p, "NUM_ARRAY"));
		     
		     //DATOS DEL SOLICITANTE
		     inParamMap.put("PIN_ARR_VC_IDENTI_C" 	 	 , new SqlArrayValue<String>(var_vcIdentificadorC, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_TIPO_ORIGEN"	 , new SqlArrayValue<Integer>(var_nuIdTipoOrigen, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_TIPO_PERSONA"	 , new SqlArrayValue<Integer>(var_nuIdTipoPersona, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_TIPO_DOCUMENTO"  , new SqlArrayValue<Integer>(var_nuIdTipoDocumento, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_DOC_IDENTIDAD" 	 , new SqlArrayValue<String>(var_vcDocIdentidad, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CORREO"		 	 , new SqlArrayValue<String>(var_vcCorreo, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_NOMBRES"		 , new SqlArrayValue<String>(var_vcNombres, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_AP_PATERNO"		 , new SqlArrayValue<String>(var_vcApPaterno, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_AP_MATERNO"		 , new SqlArrayValue<String>(var_vcApMaterno, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_RAZON_SOCI"		 , new SqlArrayValue<String>(var_vcRazonSocial, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_GENERO"		 , new SqlArrayValue<Integer>(var_nuGenero, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_PAIS"		 , new SqlArrayValue<Integer>(var_nuIdPais, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_PAIS_ORI"	 , new SqlArrayValue<Integer>(var_nuIdPaisOri, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_DEPAR"		 , new SqlArrayValue<Integer>(var_nuDepartamento, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_PROVINCI"		 , new SqlArrayValue<Integer>(var_nuProvincia, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_DISTRITO"		 , new SqlArrayValue<Integer>(var_nuDistrito, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_DIRECCION"		 , new SqlArrayValue<String>(var_vcDireccion, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_TELEFONO"		 , new SqlArrayValue<String>(var_vcTelefono, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_FLAG_SOLICI"	 , new SqlArrayValue<Integer>(var_nuFlagSolicitante, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_MYPE"	 		 , new SqlArrayValue<Integer>(var_nuMype, "NUM_ARRAY"));
		     
		     //DATOS DEL REPRESENTANTE
		     inParamMap.put("PIN_ARR_VC_IDENTI_D" 	 	 , new SqlArrayValue<String>(r_vcIdentificadorD, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_IDENTI_E" 	 	 , new SqlArrayValue<String>(r_vcIdentificadorE, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_TIPO_ORIGEN_R"	 , new SqlArrayValue<Integer>(r_nuIdTipoOrigen, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_TIPO_PERSONA_R"	 , new SqlArrayValue<Integer>(r_nuIdTipoPersona, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_TIPO_DOCUMENTO_R"  , new SqlArrayValue<Integer>(r_nuIdTipoDocumento, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_DOC_IDENTIDAD_R" 	 , new SqlArrayValue<String>(r_vcDocIdentidad, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_CORREO_R"		 	 , new SqlArrayValue<String>(r_vcCorreo, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_NOMBRES_R"		 , new SqlArrayValue<String>(r_vcNombres, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_AP_PATERNO_R"		 , new SqlArrayValue<String>(r_vcApPaterno, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_AP_MATERNO_R"		 , new SqlArrayValue<String>(r_vcApMaterno, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_RAZON_SOCI_R"		 , new SqlArrayValue<String>(r_vcRazonSocial, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_GENERO_R"		 , new SqlArrayValue<Integer>(r_nuGenero, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_PAIS_R"		 , new SqlArrayValue<Integer>(r_nuIdPais, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_PAIS_ORI_R"	 , new SqlArrayValue<Integer>(r_nuIdPaisOri, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_DEPAR_R"		 , new SqlArrayValue<Integer>(r_nuDepartamento, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_PROVINCI_R"		 , new SqlArrayValue<Integer>(r_nuProvincia, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_DISTRITO_R"		 , new SqlArrayValue<Integer>(r_nuDistrito, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_DIRECCION_R"		 , new SqlArrayValue<String>(r_vcDireccion, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_TELEFONO_R"		 , new SqlArrayValue<String>(r_vcTelefono, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_FLAG_SOLICI_R"	 , new SqlArrayValue<Integer>(r_nuFlagSolicitante, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_NRO_EXP_REP_R"	 , new SqlArrayValue<String>(r_vcNroExpediente, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_NRO_SOL_REP_R"	 , new SqlArrayValue<String>(r_vcPartidaRegistra, "STR_ARRAY"));
		     
		     
		     //ARCHIVOS DE LOS REPRESENTANTES LEGALES
		     inParamMap.put("PIN_ARR_VC_IDENTI_F"			, new SqlArrayValue<String>(var_vcIdentificadorF, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_RUTA_PE"				, new SqlArrayValue<String>(var_vcRuta_pe, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_NOM_ORIGINAL_PE"		, new SqlArrayValue<String>(var_vcNomOriginal_pe, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_NOM_FINAL_PE"		, new SqlArrayValue<String>(var_vcNomFinal_pe, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_VC_EXTENSION_PE"		, new SqlArrayValue<String>(var_vcExtension_pe, "STR_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_ID_TIPO_ARCHIVO_PE"	, new SqlArrayValue<Integer>(var_nuIdTipoArchivo_pe, "NUM_ARRAY"));
		     inParamMap.put("PIN_ARR_NU_LONGITUD_PE"			, new SqlArrayValue<Long>(var_nuLong_pe, "NUM_ARRAY"));
		     

			//execute
			Map<String, Object> out = this.simpleJdbcCall.execute(inParamMap);
			
			//response
			objResultDAO.put("POUT_NU_ID_SOLICITUD", 	out.get("POUT_NU_ID_SOLICITUD"));
			objResultDAO.put("POUT_NU_ERROR", 			out.get("POUT_NU_ERROR"));
			objResultDAO.put("POUT_VC_ERROR", 			out.get("POUT_VC_ERROR"));
			
			System.out.println("vcError: "+out.get("POUT_VC_ERROR"));
			System.out.println("NUM SOLI: "+Integer.parseInt(out.get("POUT_NU_ID_SOLICITUD")+""));
			System.out.println("COD ERROR: "+Integer.parseInt(out.get("POUT_NU_ERROR")+""));
			

			if(Integer.parseInt(out.get("POUT_NU_ERROR")+"")==0) { //CREA LA SOLICITUD
				ClsExpedienteBean objExpe= new ClsExpedienteBean();
				objExpe.setNuIdSolicitud(Integer.parseInt(out.get("POUT_NU_ID_SOLICITUD")+""));
				
				objResultDAO=objConnExpe.doExpediente(); 
				if(Integer.parseInt(out.get("POUT_NU_ERROR")+"")==0) {//SE CREO EL EXPEDIENTE
					objExpe.setNuNroExpediente(Integer.parseInt(objResultDAO.get("POUT_NU_NRO_EXPEDIENTE")+""));
					objExpe.setNuAnioExpediente(Integer.parseInt(objResultDAO.get("POUT_NU_ANIO_EXPEDIENTE")+""));
					objExpe.setVcAreaExpediente(objResultDAO.get("POUT_VC_AREA_EXPEDIENTE")+"");
					//System.out.println("objExpe.getNuNroExpediente():"+objExpe.getNuNroExpediente());
					
					objResultDAO=this.inExpediente(objExpe, objSolicitud);
					//System.out.println("mensaje de error:"+objResultDAO.get("POUT_VC_ERROR"));
					if(Integer.parseInt(objResultDAO.get("POUT_NU_ERROR")+"")==0) {//SE GUARDA EL EXPEDIENTE EN USR_MARCAS
						//System.out.println("hasta llegó");
					/*DESCOMENTAR
					objResultDAO=this.doMigraSSE(objExpe, objSolicitud);
					if(Integer.parseInt(objResultDAO.get("POUT_NU_ERROR")+"")==0) {//SI SE MIGRO CORRECTAMENTE
					
						ClsTramiteSELBean objTramite=new ClsTramiteSELBean();
						
						objTramite.setClDetTramite(			ClsUtil.clobToString((Clob)objResultDAO.get("POUT_CL_CUERPO_CORREO")));
						objTramite.setVcNroExpediente(		objResultDAO.get("POUT_NU_NRO_EXPEDIENTE")+"");
						objTramite.setVcSiglaExpediente(	""+objResultDAO.get("POUT_VC_AREA_EXPEDIENTE"));
						objTramite.setVcAnioExpediente(		""+objResultDAO.get("POUT_NU_ANIO_EXPEDIENTE"));
						objTramite.setNuIdEstadoTramite(	Integer.parseInt(""+objResultDAO.get("POUT_NU_ESTADO_REGISTRO")));
						objTramite.setNuIdProcedimiento(	Integer.parseInt(""+objResultDAO.get("POUT_NU_PROCEDIMIENTO")));
						
						ClsParametrosBean objParam=new ClsParametrosBean();
						objResultDAO=objConnParam.doParametro();
						objParam=(ClsParametrosBean)objResultDAO.get("POUT_PARAMETRO");
						
						//Para Lema comercial
						if(objSolicitud.getObjDatosSigno().getNuIdTipoSolicitud()==2)
						{
						 objParam.setNuCantClases(1);
						}else {
							objParam.setNuCantClases(objSolicitud.getLstClases().size());
						}

						objResultDAO=objConnSEL.doTramite(objTramite, objSolicitud, objParam, objSolicitud.getVcUsuario());
						if(Integer.parseInt(objResultDAO.get("POUT_NU_ERROR")+"")==0) {//SI SE MIGRO CORRECTAMENTE
							//System.out.println("Se registro correctamente");
							logger.info("Se registró correctamente.");
							String vcNombre="doc_"+ClsUtil.doGenerarNombreArchivoAleatorio()+".pdf";
							String clCorreo=objResultDAO.get("POUT_CL_HTML_SOLI")+"";
							objResultDAO.put("VC_NOM_ARCHIVO", vcNombre);
							//objTramite.setVcNombreArchivo(vcNombre);
							//logger.info(clCorreo);
							int nuError=ClsUtil.doGenerarHTML2PDF_v2(clCorreo, objParam.getVcRutaFinal()+"/", vcNombre);
							if(nuError==0) {
								ClsArchivoBean objArchivo = new ClsArchivoBean();
								objArchivo.setVcRuta(objParam.getVcRutaFinal());
								objArchivo.setVcNomOriginal(objExpe.getNuNroExpediente()+"-"+objExpe.getNuAnioExpediente()+"_DSD"+".pdf");
								objArchivo.setVcNomFinal(vcNombre);
								this.insertArchivoSolicitud(objExpe, objSolicitud,  objArchivo);
							}
						}else {
							throw new ClsException(objResultDAO.get("POUT_VC_ERROR")+"", new Throwable("ERROR"));
						}
					
					}else {
						
						throw new ClsException(objResultDAO.get("POUT_VC_ERROR")+"", new Throwable("ERROR"));
					}
					
					DESCOMENTAR */
					}else {
						throw new ClsException(objResultDAO.get("POUT_VC_ERROR")+"", new Throwable("ERROR"));
					}
				}else {
					throw new ClsException(objResultDAO.get("POUT_VC_ERROR")+"", new Throwable("ERROR"));
				}
				
			}else {
				throw new ClsException(objResultDAO.get("POUT_VC_ERROR")+"", new Throwable("ERROR"));
			}
			
								    		
		}catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
			throw new ClsException(e+"", new Throwable("ERROR"));
		}
		
		return objResultDAO;
	}
	
	
	@Override
	public ClsResultDAO inExpediente(ClsExpedienteBean objExpediente, ClsSolicitudBean objSolicitud) {

		try {
			//procedure
			this.simpleJdbcCall=new SimpleJdbcCall(jdbcTemplate)
									.withSchemaName(ClsConstantes.SCHEMA_USR_MARCAS)
									.withCatalogName(ClsConstantes.PKG_REG_MARCA)
									.withProcedureName(ClsConstantes.SP_I_EXPEDIENTE)
									.declareParameters(
									
									new  SqlOutParameter("POUT_NU_ERROR", OracleTypes.NUMBER ,"NUMBER"),
									new  SqlOutParameter("POUT_VC_ERROR", OracleTypes.VARCHAR ,"VARCHAR2")
									);
			
			
			
			 Map<String, Object> inParamMap = new HashMap();
			 
		     inParamMap.put("PIN_VC_USUARIO"			, objSolicitud.getVcUsuario());
		     inParamMap.put("PIN_VC_HOSTNAME"			, objSolicitud.getVcHostName());

		     inParamMap.put("PIN_NU_ID_SOLICITUD"			, objExpediente.getNuIdSolicitud());
		     inParamMap.put("PIN_NU_NRO_EXPEDIENTE"			, objExpediente.getNuNroExpediente());
		     inParamMap.put("PIN_NU_ANIO_EXPEDIENTE"		, objExpediente.getNuAnioExpediente());
		     inParamMap.put("PIN_VC_AREA_EXPEDIENTE"		, objExpediente.getVcAreaExpediente());
		     
			//execute
			Map<String, Object> out = this.simpleJdbcCall.execute(inParamMap);
			
			//response
			
			objResultDAO.put("POUT_NU_ERROR", 			out.get("POUT_NU_ERROR"));
			objResultDAO.put("POUT_VC_ERROR", 			out.get("POUT_VC_ERROR"));
								    		
		}catch(Exception e) {
			e.printStackTrace();
			throw new ClsException(e+"", new Throwable("ERROR1"));
		}
		
		return objResultDAO;
	}
	
	@Override
	public ClsResultDAO doMigraSSE(ClsExpedienteBean objExpediente, ClsSolicitudBean objSolicitud) {

		try {
			//procedure
			this.simpleJdbcCall=new SimpleJdbcCall(jdbcTemplate)
									.withSchemaName(ClsConstantes.SCHEMA_USR_MARCAS)
									.withCatalogName(ClsConstantes.PKG_REG_MARCA)
									.withProcedureName(ClsConstantes.SP_DO_MIGRA_MAR_SSE)
									.declareParameters(
								    new  SqlOutParameter("POUT_CL_CUERPO_CORREO", OracleTypes.CLOB ,"CLOB"),
									new  SqlOutParameter("POUT_NU_NRO_EXPEDIENTE", OracleTypes.VARCHAR ,"VARCHAR2"),     
									new  SqlOutParameter("POUT_NU_ERROR", OracleTypes.NUMBER ,"NUMBER"),
									new  SqlOutParameter("POUT_VC_ERROR", OracleTypes.VARCHAR ,"VARCHAR2")
									);
			
			
			
			 Map<String, Object> inParamMap = new HashMap();
			 
		     inParamMap.put("PIN_VC_USUARIO"			, objSolicitud.getVcUsuario());
		     inParamMap.put("PIN_VC_HOSTNAME"			, objSolicitud.getVcHostName());

		     inParamMap.put("PIN_NU_ID_SOLICITUD"			, objExpediente.getNuIdSolicitud());
		     
			//execute
			Map<String, Object> out = this.simpleJdbcCall.execute(inParamMap);
			System.out.println("DAO Migra: "+objExpediente.getNuIdSolicitud());
			
			//response
			
            objResultDAO.put("POUT_CL_CUERPO_CORREO", 			out.get("POUT_CL_CUERPO_CORREO"));
 			objResultDAO.put("POUT_NU_NRO_EXPEDIENTE", 			out.get("POUT_NU_NRO_EXPEDIENTE"));
 			
 			objResultDAO.put("POUT_VC_AREA_EXPEDIENTE", 			out.get("POUT_VC_AREA_EXPEDIENTE"));
 			objResultDAO.put("POUT_NU_ANIO_EXPEDIENTE", 			out.get("POUT_NU_ANIO_EXPEDIENTE"));
 			objResultDAO.put("POUT_NU_ESTADO_REGISTRO", 			out.get("POUT_NU_ESTADO_REGISTRO"));
 			objResultDAO.put("POUT_NU_PROCEDIMIENTO", 			out.get("POUT_NU_PROCEDIMIENTO"));
 			
			objResultDAO.put("POUT_NU_ERROR", 			out.get("POUT_NU_ERROR"));
			objResultDAO.put("POUT_VC_ERROR", 			out.get("POUT_VC_ERROR"));
								    		
		}catch(Exception e) {
			e.printStackTrace();
			throw new ClsException(e+"", new Throwable("ERROR1"));
		}
		
		return objResultDAO;
	}
	
	
	@Override
	public ClsResultDAO insertArchivoSolicitud(ClsExpedienteBean objExpediente, ClsSolicitudBean objSolicitud, ClsArchivoBean objArchivo) {
			logger.info("insertArchivoSolicitud()");
		try {
			//procedure
			this.simpleJdbcCall=new SimpleJdbcCall(jdbcTemplate)
									.withSchemaName(ClsConstantes.SCHEMA_USR_MARCAS)
									.withCatalogName(ClsConstantes.PKG_REG_MARCA)
									.withProcedureName(ClsConstantes.SP_I_GUARDA_ARCHIVO_SOLIC)
									.declareParameters(
									
									new  SqlOutParameter("POUT_NU_ERROR", OracleTypes.NUMBER ,"NUMBER"),
									new  SqlOutParameter("POUT_VC_ERROR", OracleTypes.VARCHAR ,"VARCHAR2")
									);
			
			
			
			 Map<String, Object> inParamMap = new HashMap();
			 
		     inParamMap.put("PIN_VC_USUARIO"			, objSolicitud.getVcUsuario());
		     inParamMap.put("PIN_NU_SOLICITUD", objExpediente.getNuIdSolicitud());
		     inParamMap.put("PIN_VC_RUTA", objArchivo.getVcRuta());
		     inParamMap.put("PIN_VC_NOMBRE_ORI", objArchivo.getVcNomOriginal());
		     inParamMap.put("PIN_VC_NOMBRE_FIN", objArchivo.getVcNomFinal());
		     inParamMap.put("PIN_VC_TAMANIO", "3000");
		     
		     
			//execute
			Map<String, Object> out = this.simpleJdbcCall.execute(inParamMap);
			
			//response
			logger.info(out.get("POUT_NU_ERROR")+"");
			
			objResultDAO.put("POUT_NU_ERROR", 			out.get("POUT_NU_ERROR"));
			objResultDAO.put("POUT_VC_ERROR", 			out.get("POUT_VC_ERROR"));
								    		
		}catch(Exception e) {
			e.printStackTrace();
			throw new ClsException(e+"", new Throwable("ERROR"));
		}
		
		return objResultDAO;
	}
	
	
}
