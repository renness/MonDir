import java.nio.file.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
 
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
 
public class Main {
 
    public static void main(String[] args) throws IOException 
    {
    	// Obtenemos el directorio
    	Path directoryToWatch = Paths.get("c://MonDir//");
    	if (directoryToWatch == null)
    	{
            throw new UnsupportedOperationException("Directorio no existe");
        }
    	
    	// Solicitamos el servicio WatchService
        WatchService watchService = directoryToWatch.getFileSystem().newWatchService();
        
        // Registramos los eventos que queremos monitorear
        directoryToWatch.register(watchService, new WatchEvent.Kind[] {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY});
  
        System.out.println("Started WatchService in " + "c://MonDir//");
        try 
        {
            // Esperamos que algo suceda con el directorio
            WatchKey key = watchService.take();
  
            // Algo ocurrio en el directorio para los eventos registrados
            while (key != null) 
            {
                for (WatchEvent event : key.pollEvents()) 
                {
                    String eventKind = event.kind().toString();
                    String file = event.context().toString();
                    
                    if(eventKind.equals("ENTRY_CREATE") && (file.equals("Temp.txt")))
                    {
                    	String nombre = CambiarNombre();
                    	abrir(nombre);
                    	//System.out.println("Event : " + eventKind + " in File " +  file);
                    }
                }
  
                // Volvemos a escuchar. Lo mantenemos en un loop para escuchar indefinidamente.
                key.reset();
                key = watchService.take();
            }
        } 
        catch (InterruptedException e) 
        {
            throw new RuntimeException(e);
        }
    }
 
    public static String CambiarNombre() 
    {
    	//Obtener hora y fecha para el nuevo nombre:
    	DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    	Date date = new Date();
    	String NombreArchivo = "TMP" + hourdateFormat.format(date) + ".txt";
    	
    	//Cambiar el nombre del archivo Temp.txt
    	File fichero = new File("c://MonDir//Temp.txt");
        File fichero2 = new File("c://MonDir//" + NombreArchivo);
        boolean success = fichero.renameTo(fichero2);
        if (!success)
        {
            //System.out.println("Error intentando cambiar el nombre de fichero");
            return CambiarNombre();
        }
        else
        {
        	return NombreArchivo;
        }
    }
    
    public static void abrir(String archivo) 
    {
    	 //ruta del archivo en el pc
    	 String file = new String("c://MonDir//"+archivo);  
    	 try
    	 { 
    		 //definiendo la ruta en la propiedad file
    		 Runtime.getRuntime().exec("cmd /c start "+file);
    	 }
    	 catch(IOException e)
    	 {
    		 e.printStackTrace();
    	 } 
    }
}

/*
Codigo recopilado de las siguientes fuentes
http://gustavopeiretti.com/observar-cambios-en-directorios-con-java/
http://blog.openalfa.com/como-cambiar-de-nombre-mover-o-copiar-un-fichero-en-java
https://geekytheory.com/tip-java-obtener-fecha-y-hora/
http://codejavu.blogspot.com/2014/10/como-abrir-archivos-externos-desde-java.html
