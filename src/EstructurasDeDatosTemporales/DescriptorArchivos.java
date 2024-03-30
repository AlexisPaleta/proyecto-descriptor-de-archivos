/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EstructurasDeDatosTemporales;

import java.io.*;
import java.util.Vector;



/**
 *
 * @author Alexis
 */
public class DescriptorArchivos {
    
    private String NombreTabla;
    private String nombreArchivo;
    private AtributoTabla[] atributosTabla;
    private int cantidadAtributos;
    

    
    public DescriptorArchivos(String nombreArchivo){
        this.NombreTabla = "";
        this.nombreArchivo = nombreArchivo;
        this.atributosTabla = null;
        this.cantidadAtributos = 0;
        
    }
    
    public String getNombreTabla() {
        return NombreTabla;
    }

    public void setNombreTabla(String NombreTabla) {
        this.NombreTabla = NombreTabla;
    }
    
    private String[] atributos(){
        String[] atributos = new String[cantidadAtributos];//el valor debe ser modificado a la cantidad de atributos que se hayan leido
        int contador = 0;
        for(AtributoTabla atributo : atributosTabla){
            atributos[contador]=atributo.getNombreAtributo();
            contador++;
        }
        if(contador == 0){
            return null;
        }
        return atributos;
    }
    
    public Vector<Vector> contenido(){
        
        if(atributosTabla == null){ //se usa en caso de que no se haya usado antes el metodo de vaciar contenido
            try {
                vaciarContenido();
            } catch (IOException ex) {
                //Logger.getLogger(DescriptorArchivos.class.getName()).log(Level.SEVERE, null, ex);
                return null; //se retorna nulo en caso de que haya un error a la hora de leer el archivo
            }
        }
//        ArrayList<String> contenido = new ArrayList<>();
        Vector<Vector> grande = new Vector<>();
        
        
        try (FileReader fr = new FileReader("tabla.txt")) {
         BufferedReader br = new BufferedReader(fr);
         // Lectura del fichero
         br.readLine();//aqui se salta la primera linea
         
         String linea;
         
         while((linea=br.readLine())!=null){//voy a leer todas las lineas de la tabla a partir de la segunda
             
              int[] limites = new int[2];//voy a definir un nuevo arreglo de enteros en cada iteracion del while,
              //aunque revisando de nuevo, creo que se podrian declarar fuera
              Vector<String> contenido = new Vector<String>();//es importante redefinir al vector de cadenas "contenido"
              //en cada iteracion del while, porque sino se hace, entonces en la segunda linea todo va a ir bien
              //pero cuando me pase a la segunda, los valores de la anterior se habrán quedado en el vector
              //por lo que tendria contenido que ya no me interesa, y el que necesito esta hasta el final del vector
              //de contenido
            for(AtributoTabla control: atributosTabla){//un ciclo que va a ejecutarse dependiendo de la cantidad de
                //atributos en la tabla, en el ejemplo que tengo ahora son solo 3, nombre, apellido y matricula
                //entonces se ejecuta 3 veces, se necesita que se ejecute esa cantidad para que se guarde el contenido
                //de cada atributo en el vector de contenido, este va a guardar la cantidad de cadenas equivalentes
                //a la cantidad de atributos de la tabla
                limites = control.getPosicionesAtributo();//se redefinen los limites despues de avanzar en cada atributo
                //guardado en la lista de atributos que tiene la clase de DescriptorArchivos, ejemplo:
                //tengo mis atributos que leí de mi tabla, que son:nombre, apellido y matricula, son 3, entonces en mi
                //clase DescriptorArchivos se guardaron en su Lista de "atributosTabla" los 3 atributos que leí antes,
                //cada uno de estos atributos tiene un: NombreAStributo y posicionesAtributo[], en la posicion cero de
                //este arreglo es la inicial y en la posicion 1 es la final, entonces ahora en este ciclo for que entra
                //a cada atributoTabla guardado en la lista de AtributosTabla, que son 3, voy a guardar en limites
                //los valores que ese atributo tenga registrados, recordando que cada atributo tiene limites en posiciones
                //distintas, por eso es necesario que se establezcan en cada iteracion de este ciclo
                String valor;
                valor = linea.substring(limites[0], limites[1]+1).replaceAll("\\s","" );//el metodo substring va a recoger la cadena
                //entre los limites que se le establezcan, en este caso el arreglo de limites en la posicion [0] contiene la posicion inicial
                //del valor de ese atributo en las lineas del archivo de texto, y en la posicion [1] el final, sin embargo este metodo recoge
                //la cantidad de caracteres resultante entre hacer la diferencia entre los numeros que se tengas en limites[1] - limites[0], por lo que
                //la cantidad que realmente lee este metodo es 1 menos del que se tendria que leer si se considereran estrictamente los limites establecidos
                //para que se lea todo se le suma 1 al limite del final para que se lea todo el valor correctamente
                if(valor.equals("")){
                    valor = "(null)";
                }
                    contenido.add(valor);
                //despues le agrego al Vector contenido, de la linea en la que me ubique, que al inicio sera la segunda,
                //una subcadena en los limites establecidos, y quitando los espacios en blanco sobrantes, porque puede
                //que un nombre no utilice todo el espacio de los limites definidos.
                //Esto de agregar la subcadena al arreglo llamado contenido en este ejemplo se ejecuta 3 veces, es decir,
                //el vector contenido tiene dentro 3 cadenas, que es la informacion de alguna persona, cada persona equivale
                //a una linea
            }
            grande.add(contenido);//ahora que se leyó la informacion de una linea, al vector "grande" le agrego el vector
            //"contenido", que tiene 3 cadenas dentro
            
            //Este proceso se ejecutara por todas las lineas hasta que ya no tengan contenido
         
         }
            
            
           
         }catch(Exception e){
         //e.printStackTrace();
         }
        
        
//        for(String dentro: contenido){
//                System.out.println("aqui: "+dentro);
//            }
        return grande;//regreso el vector de vectores
    }
    
    private FileReader abrirArchivo(){
        
//        try(FileReader archivo = new FileReader("C:\\Users\\Alexis\\Desktop\\tabla.txt")){
//            System.out.println("El archivo se leyo con exito");
//            return archivo;
//        }catch(Exception e){
//            System.out.println("Hola");
//            return null;
//        }

           try {
                FileReader fr = new FileReader(nombreArchivo);
                System.out.println("Se leyo bien");
                return  fr;
                // Lectura del fichero
//                String linea;
//                while((linea=br.readLine())!=null)
//                System.out.println(linea);
                }catch(Exception e){
                    //e.printStackTrace();
                    return null;
                }
        
    }
    
    public String[] vaciarContenido() throws IOException{
        
        
        try{
            FileReader fr = abrirArchivo();
        BufferedReader br = new BufferedReader(fr);
        
//        char[] c = new char[100];
//        fr.read(c);
//        int contador =0;
//        for(char ch:c){
//            
//            System.out.print(ch);
//            contador++;
//        }
//        System.out.println("diferencia");
//        for(char ch:c){
//            if(ch == ','){
//                break;
//            }
//            System.out.print(ch);
//        }

        String linea = br.readLine();
        String[] lu = linea.split(",");
        
        this.setNombreTabla(lu[0]); 
        
        cantidadAtributos = (lu.length-1)/3;//esto me da el total de atributos que se tienen en la tabla del archivo .txt
        //si por ejemplo tengo que mi arreglo lu[] tiene una longitud de 10, este 10 considera al nombre de la tabla,
        //por eso tengo que restarle uno, entonces tengo 9, y se divide entre 9 porque quiero ver los 3 valores
        //para guardar de cada atributo, si fuesen mas entonces se dividiria entre el numero de valores que quiero
        //rescatar por atributo
        
        AtributoTabla[] atributosArreglo = new AtributoTabla[cantidadAtributos];//defino un arreglo de AtributoTabla en base a la cantidad de atributos totales
        //en la tabla que se esta leyendo
        int indice = 0;
        for(int contador = 1;contador<=cantidadAtributos;contador++){//el contador inicia desde 1 porque en la posicion cero de 
            //la primera de la linea del archivo se encuentra el nombre de la tabla, entonces los atributos se encuentran
            //a partir de la posicion[1] en las dos siguientes estan sus limites y luego sigue el sig atributo y asi
            //sucesivamente
            int vaciado=3*contador;//esto es porque al ser 3 valores los que se quieren guardar por cada atributo
            //de la tabla, el primero es el nombre, luego la posicion inicial de ese atributo en el registro
            //y luego cuando termina
            
            String nombre =lu[vaciado-2];//el vaciado menos dos es porque el vaciado se multiplicara por los valores de
            //3 por el del contador, entoneces para registrar al primer nombre del atributo, como ya se tiene
            //todo guardado en el arreglo de cadenas llamado lu, que contiene a todas las cadenas de la primera linea
            //del archivo de texto que se separan por una coma (','), el valor de lu[0] siempre sera el nombre de la
            //tabla que me encuentre, luego los valores de lu[1] , lu[2] y lu[3] seran el nombre, inicio y fin del primer
            //atributo de la tabla, entonces el valor de vaciado ira aumentanto para ir recogiendo las posiciones correctas
            //a cada atributo, al si se tuvieran 3 atributos, para recibir este ultimo atributo, el contador valdria 3
            //entonces seria vaciado = 3*3 =9, en lu[] se tendrian 10 cosas guardadas y la ultima posicion seria lu[9]
            //porque hay indice 0, entonces por eso estos valores para vaciado, el vaciado-2= 7 lo que seria el nombre del
            //atributo que se esta leyendo, vaciado-1=8, es el la posicion inicial del atributo, y vaciado=9 es el fin
            //del atributo
            int inicio = Integer.parseInt(lu[vaciado-1]);
            int fin = Integer.parseInt(lu[vaciado]);
            AtributoTabla atributo = new AtributoTabla(nombre,inicio,fin);//se crea un atributo con los parametros especificos que se lean de la
            //primera linea del descriptor de archivos, luego se van a agregar en el arreglo de atributos para que se tenga facilmente acceso a ellos
            
            atributosArreglo[indice] = atributo;
            indice++;
            
        }
            this.atributosTabla = atributosArreglo; //el arreglo que habia definido en esta funcion sirve de auxiliar, ahora se va a guardar su contenido
            //en la variable principal de la clase de descriptor de archivos, o sea el arreglo de AtributoTabla llamado "atributosTabla"
        
//        int contador = 1;  esto es para comprobar que todo se leyo correctamente
//        for(AtributoTabla atib: atributosTabla){
//            int[] posicion = atib.getPosicionesAtributo();
//            System.out.println("Nombre del atributo numero "+contador+": "+atib.getNombreAtributo());
//            System.out.println("Inicio del atributo en :" +posicion[0]);
//            System.out.println("Final del atributo en :" +posicion[1]);
//            contador++;
//        }
//        System.out.println("longitud "+lu.length);
        
        
        return atributos();//se manda a llamar a la funcion atributos, que retorna un arreglo de Strings, estos se van a usar en la vista para que
        //en la tabla que muestre el contenido de un archivo pueda poner los nombres de los atributos en las columnas
        
        }catch(Exception e){
            //e.printStackTrace(); los comento para que no salgan en la consola en caso de errores
            return null;
        }
        
        
        
    }
    
    
    
}
