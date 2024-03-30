package ControlConsultas;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import EstructurasDeDatosTemporales.DescriptorArchivos;
import EstructurasDeDatosTemporales.ParametrosConsulta;

public class DiccionarioDeDatos {

    public static ParametrosConsulta comprobarDiccionario(HashMap<String,Object[]> parametrosPorComando){
        //Object[] llavesParametros = parametrosPorComando.keySet().toArray();//recupero los comandos que ejecuto el usuario
        Object[] nombreT = parametrosPorComando.get("FROM");//voy a recuperar el nombre del archivo que vaya a leer
        //debo hacerlo con tipo Object porque es como estan guardados los parametros de cada comando, en este caso como
        //quiero saber el nombre del archivo especifico que se desea abrir por eso se accede al HashMap con la llave
        //"FROM" recordando que si se llega a la ejecucion de esta parte es porque ya se comprobo que la sintaxis de la
        //consulta es correcta
        String nombreTabla = nombreT[0].toString().toLowerCase() + ".txt";//leo el nombre , lo convierto a una cadena,
        //lo hago minusculas y le agrego la terminacion.txt
        System.out.println("NOMBRE :" + nombreTabla);
        String[] atributos;
        HashMap<String,Integer> posicionesAtributos = new HashMap<>();
        Vector<Vector> grande = new Vector<>(); //defino un vector de vectores, los vectores que esten dentro
        //de este vector de vectores por defecto guardan objetos, por eso despues tengo que hacer la conversion de
        //objeto a string o a lo que sea que necesite
        try {
            DescriptorArchivos descriptor = new DescriptorArchivos(nombreTabla);
             atributos = descriptor.vaciarContenido();//se manda a llamar al metodo que va a leer el archivo especificado, y luego
            //se recibe de ese mismo metodo un arreglo de Strings, que contiene los nombres de los atributos de la tabla para que se puedan poner
            //en las columnas de la tabla
            if (atributos == null){
                   System.out.println("ERROR");
                return null;
            }
//            System.out.println("Atributos de la tabla:");
//            for(String at:atributos){
//                System.out.println(at);
//            }
            int contador = 0;
            for(String at:atributos){//voy a asignarle a cada atributo su posicion en el Vector llamado "grande"//
            //los atributos que recupero del descriptor de archivos vienen en orden, lo que esoy haciendo de organizarlos
            //en un HashMap es para facilitar la ubicacion de los atributos, para no tener que crear otro arreglo que
            //despues tenga que ir sincronizando para que guarde las posiciones (como se hace en la clase de 
            //ControlConsultas.java) esta seria la otra forma de hacer la asociacion de los atributos con sus posiciones
                posicionesAtributos.put(at,contador );//la idea de guardar los atributos en un HashMap es para que
                //luego para hacer la comprobacion de los parametros que se estan solicitando en la consulta sea de
                //menos lineas de codigo, y para que al momento de que se aplique un where se recupera rapidamente
                //la posicion del atributo que se va a comparar, sino se podria ir verificando el arreglo de atributos
                //y luego ir verificando posicion por posicion si es que se trata del atributo que quiero ubicar y guardar
                //esa posicion , seria otra forma de hacerlo
                contador++;
            }

            
            grande = descriptor.contenido(); //voy a guardar dentro de grande el vector de vectores que se obtiene
            //de leer los datos del .txt
            if (grande == null){
                System.out.println("ERROR");
                return null;
            }
            
           
        } catch (IOException ex) {
            System.out.println("ERROR");
            return null;
        }
        
        //voy a verificar que los atributos que se desean mostrar, que son los parametros que vienen con el SELECT
        //realmente se encuentren en la tabla solicitada
        Object[] selectParam = parametrosPorComando.get("SELECT");
        String[] ordenImpresion = new String[selectParam.length];//voy a guardar los parametros que se desean imprimir
        //en el orden especificado por la consulta
        boolean diccionario = false;
        int control = 0;
        for(Object parametro:selectParam){//este for es para comprobar que los parametros del SELECT esten en la tabla
            for(String atributo: atributos){
                if(parametro.equals(atributo)){//en caso de que el parametro especificado para el comando SELECT se 
                    //encuentre como atributo de la tabla seleccionada se marca diccionario como true, eso significa que
                    //si se va a poder mostrar
//                    System.out.println(parametro +"=="+atributo);
                    diccionario = true;
                    ordenImpresion[control] = atributo;
                    control++;
                    break;
                }
                
            }
            if(diccionario == false){//si el parametro no se encuentra en la tabla se muestra un error
                    
                    System.out.println("ERROR: El atributo: " + parametro + " no se encuentra en la tabla de " + nombreTabla);
                    return null;
                }
                diccionario = false;// se establece como falso ahora el diccionario porque se va a comparar con el 
                //siguiente parametro a verificar , en caso de haber
        }//fin del for que se asegura que los parametros del SELECT esten en la tabla
        
        //ahora debo verificar que los parametros de WHERE realmente existan en la tabla, que solo es uno de hecho, esta
        //comprobacion solo se hace en caso de que se haya ejecutado el comando WHERE, en caso contrario no se realiza
        
        if(parametrosPorComando.get("WHERE")!=null){//en caso de que se haya ejecutado el comando WHERE ahora debo
            //comprobar que su primer parametro se encuentre en la tabla, pues es a quien voy a realizarle las
            //comparaciones
            Object[] parametrosWhere = parametrosPorComando.get("WHERE");
            Object parametroWhere = parametrosWhere[0];//como solo necesito al primer parametro ingresado para el comando
            //WHERE entonces lo saco de esta forma del arreglo que tiene todos los parametros de ese comando
            String param = parametroWhere.toString();//convierto a una cadena el primer parametro del comando WHERE para
            //que lo pueda usar en el comando del HashMap de posicionesAtributos, porque si ingreso el primer parametro
            //del comando WHERE en este HashMap me debe devolver algun numero entero, pero en caso de que se devuelva
            //nulo significa que no hay ningun atributo guardado con ese nombre por lo que asi se hace la comprobacion
            //este metodo se puede usar o tambien el otro de los fors anidados que se usa para la comprobacion en SELECT
            if(posicionesAtributos.get(param)==null){
                System.out.println("ERROR: no se encontro al parametro " + param +" en la tabla: " + nombreTabla + " que usa el comando WHERE");
                return null;
            }
            boolean numerico = false;
            if(parametrosWhere.length == 3){//en caso de que haya solo 3 parametros para el comando WHERE debo verificar
                //si es que se trata de los casos para ">","<","<=",">=", porque para esos necesito garantizar que se trata
                //de un numero, en este caso como antes habia hecho la condicion para el BETWEEN AND , y ese ya se encarga
                //de verificar si es un numero, no voy a colocar de nuevo lo que ya hace esa funcion entonces, con una
                //bandera voy a indicar que quiero que se revise que el primer parametro de la consulta sea uno que 
                //pueda manejarse como numerico, se va a poner como true en caso de que el segundo parametro del comando
                //where en la consulta sea alguno de los simbolos anteriores 
                String[] condicionesWhere = {">","<","<=",">="};//estas son las posibles condiciones que puede
                //llegar a tener el where para cuando necesito verificar que el primer parametro sea un numero
                boolean condicion = false;//se va a verificar que el segundo parametro del comando WHERE sea alguna
                //de las condiciones anteriores, en caso de serlo significa que es correcto, si nunca coincide
                //entonces se trata de hacer una operacion no permitida y se regresa un error
                for(String condiciones:condicionesWhere){
                    if(parametrosWhere[1].equals(condiciones)){//aqui se va a checar si es que el segundo parametro
                        //del comando where es alguno de los anteriores
                        numerico = true;
                        break;
                    }
                }
                
            }
            
            if(parametrosWhere.length == 5 || numerico){//si es que la cantidad de parametros del where es de cinco significa que
                //se esta ejecutando un WHERE con las condiciones del "BETWEEN AND" por lo que como esta funcion
                //solo maneja numeros debo asegurarme de que el parametro con el que voy a realizar las comparaciones
                //se pueda convertir a un numero sin problema
                int posicion = posicionesAtributos.get(param);
                for(Vector fila: grande){
                    try{
                        String convertir = fila.get(posicion).toString();//convierto lo que obtenga en la fila que
                        //esta en la columna del atributo en el que voy a hacer las comparaciones y voy a tratar de ver
                        //que ocurre cuando trato de convertir a cada uno en un numero, si es que todos se pueden 
                        //convertir sin problema, entonces el comando WHERE con el BETWEEN se podra ejecutar sin problema
                        Double.parseDouble(convertir);
                    }catch(NumberFormatException e){
                        System.out.println("ERROR: PARA poder ejecutar el where con estas condiciones el parametro a comparar debe ser numerico");
                    }
                }
            }
        }
        
        ParametrosConsulta retorno = new ParametrosConsulta(nombreTabla,atributos,parametrosPorComando, posicionesAtributos, grande, ordenImpresion);
        //retorno una objeto con las herramientas necesarias para poder ejecutar la consulta que se solicita
        return retorno;
    }

}
