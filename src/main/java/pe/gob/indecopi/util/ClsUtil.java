package pe.gob.indecopi.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import java.sql.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.itextpdf.html2pdf.HtmlConverter;

import pe.gob.indecopi.bean.ClsParametrosBean;
import pe.gob.indecopi.repository.ClsParametroRepository;
import pe.gob.indecopi.repository.ClsParametroRepositoryI;

public class ClsUtil implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7850368188088895287L;

	public static File doDescargarDocumento(URL vcUrlOrigen, String strNombreDocumento)
			throws IOException {
		String strResponse = "";
		URLConnection connection = vcUrlOrigen.openConnection();

		connection.setUseCaches(false);
		InputStream in = connection.getInputStream();

		int contentLength = connection.getContentLength();
		ByteArrayOutputStream tmpOut;
		if (contentLength != -1) {
			tmpOut = new ByteArrayOutputStream(contentLength);
		} else {
			tmpOut = new ByteArrayOutputStream(16384);

		}
		byte[] buf = new byte[512];
		while (true) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}
			tmpOut.write(buf, 0, len);
		}
		in.close();
		tmpOut.close();

		byte[] array = tmpOut.toByteArray();
		File fileTmp = File.createTempFile(strNombreDocumento, ".pdf");
		FileOutputStream fos = new FileOutputStream(fileTmp);
		fos.write(array);
		fos.close();
		strResponse = fileTmp.getName();
		return fileTmp;
	}
	
	
	public static String doGenerarNombreArchivoAleatorio() {
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyyMMddHHmmss");
		String response = "";
		try {
			response = formatoDeFecha.format(new Date());
			response += "" + aleatorio(1000, 9999);
		} catch (Exception e) {
			response = "";
		}
		return response;
	}

	public static int aleatorio(int max, int min) {
		return (int) (Math.random() * (max - min)) + min;
	}
	
	public static String clobToString(java.sql.Clob data)
	{
	    final StringBuilder sb = new StringBuilder();

	    try
	    {
	        final Reader         reader = data.getCharacterStream();
	        final BufferedReader br     = new BufferedReader(reader);

	        int b;
	        while(-1 != (b = br.read()))
	        {
	            sb.append((char)b);
	        }

	        br.close();
	    }
	    catch (SQLException e)
	    {
	    	System.out.println("aqu?? 1");
	    	e.printStackTrace();
	        return e.toString();
	    }
	    catch (IOException e)
	    {
	    	System.out.println("aqu?? 2");
	        return e.toString();
	        
	    } 
	    return sb.toString();
	}
	
	@Bean
	public static int doGenerarHTML2PDF_v2(String pVcHTML, String pVcRutaPDF, String pVcNombreArchivo) {
		int n = 0;

		try {
			HtmlConverter.convertToPdf(pVcHTML, new FileOutputStream(pVcRutaPDF + pVcNombreArchivo));
		} catch (Exception e) {
			// "Excepcion Falla al crear el archivo pdf desde html: ;
			n = -1;
		}

		return n;
	}
	
	public static Clob createClob(String string) {
		try {
			return new SerialClob(string.toCharArray());
		} catch (SerialException e) {
			return null;
		} catch (SQLException e) {
			return null;
		}
	}
	
}
