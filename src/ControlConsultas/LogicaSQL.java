/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControlConsultas;

import EstructurasDeDatosTemporales.DescriptorArchivos;
import EstructurasDeDatosTemporales.MostarEnTabla;
import EstructurasDeDatosTemporales.ParametrosConsulta;
import EstructurasDeDatosTemporales.PasosTablasContenido;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author Alexis
 */
public class LogicaSQL {
    
    private String consulta;
    private String nombreTabla;
    private String[] ClausulasSQL = {"SELECT","FROM","WHERE"};
    private HashMap<String,Integer> comandosSQL = new HashMap<>();
    private DescriptorArchivos descriptor;

    
    public LogicaSQL(String consulta,String nombreTabla){
        this.consulta = consulta;
        this.nombreTabla = nombreTabla;
        this.comandosSQL.put("SELECT", 1);
        this.comandosSQL.put("FROM", 2);
        this.comandosSQL.put("WHERE", 3);
    }
    
    public String getConsulta() {
        return consulta;
    }
    
    private ParametrosConsulta diccionarioDeDatos(){//se va a verificar los datos de la consulta se encuentre en el archivo a leer
        
        HashMap<String,Object[]> parametrosConsulta = ControlSintaxisConsulta.comprobarSintaxis(consulta,ClausulasSQL);//recibo el HashMap que contiene los 
        //parametros asignados a cada comando SQL
        if(parametrosConsulta == null){//en caso de ser nulo la sintaxis de la consulta no era correcta entonces
            //arrojo un error
            System.out.println("ERROR LA CONSULTA NO SE PUEDE EJECUTAR");
            return null;
        }
        
        Object[] llavesParametros = parametrosConsulta.keySet().toArray();//recupero los comandos que ejecuto el usuario
        Object[] nombreT = parametrosConsulta.get("FROM");//voy a recuperar el nombre del archivo que vaya a leer
        //debo hacerlo con tipo Object porque es como estan guardados los parametros de cada comando, en este caso como
        //quiero saber el nombre del archivo especifico que se desea abrir por eso se accede al HashMap con la llave
        //"FROM" recordando que si se llega a la ejecucion de esta parte es porque ya se comprobo que la sintaxis de la
        //consulta es correcta
        this.nombreTabla = nombreT[0].toString().toLowerCase() + ".txt";//leo el nombre , lo convierto a una cadena,
        //lo hago minusculas y le agrego la terminacion.txt
        System.out.println("NOMBRE :" + nombreTabla);
        String[] atributos;
        HashMap<String,Integer> posicionesAtributos = new HashMap<>();
        Vector<Vector> grande = new Vector<>(); //defino un vector de vectores
        try {
            descriptor = new DescriptorArchivos(nombreTabla);
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
            for(String at:atributos){//voy a asignarle a cada atributo su posicion en el Vector llamado "grande"
                posicionesAtributos.put(at,contador );
                contador++;
            }

            
            grande = descriptor.contenido(); //voy a guardar dentro de grande el vector de vectores que se obtiene
            //de leer los datos del .txt
            if (grande == null){
                System.out.println("ERROR");
                return null;
            }
            
            for(Vector vectorcitos: grande){ //se va a iterar en este ciclo a traves de todos los vectores que esten dentro
                //del vector "grande", dentro de estos "vectorcitos" se encuentran 3 cadenas (recordando del ejemplo que esta
                //explicado en la funcion contenido de la clase DescriptorArchivos), es necesario regresarlo asi, porque
                //el metodo addRow añade las cadenas dentro de un Vector, por eso se tiene que entrar de esta forma
                //en este caso, el vectorcito contiene 3 cadenas, por lo que el metodo pone la primera cadena
                //en la primera columna, la segunda en la segunda columna y de esa manera.
                
            } 
           
        } catch (IOException ex) {
            System.out.println("ERROR");
            return null;
        }
        
        //voy a verificar que los atributos que se desean mostrar, que son los parametros que vienen con el SELECT
        //realmente se encuentren en la tabla solicitada
        Object[] selectParam = parametrosConsulta.get("SELECT");
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
        
        if(parametrosConsulta.get("WHERE")!=null){//en caso de que se haya ejecutado el comando WHERE ahora debo
            //comprobar que su primer parametro se encuentre en la tabla, pues es a quien voy a realizarle las
            //comparaciones
            Object[] parametrosWhere = parametrosConsulta.get("WHERE");
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
        
        ParametrosConsulta retorno = new ParametrosConsulta(parametrosConsulta, posicionesAtributos, grande, ordenImpresion);
        //retorno una objeto con las herramientas necesarias para poder ejecutar la consulta que se solicita
        return retorno;
        
    }
    
    public MostarEnTabla ejecutarConsulta(){//falta agregar lo de comprobar sintaxis
        ParametrosConsulta recibe = diccionarioDeDatos();
        
        if(recibe == null){
            System.out.println("ERROR: LA CONSULTA NO SE PUEDE EJECUTAR");
            return null;
        }
        
        String[] impresion = new String[recibe.getOrdenImpresion().length];//creo un arreglo de cadenas que va a tener
        //el tamanio especifico de los parametros que deseo imprimir
        Vector<Vector> datos = new Vector<>();
        Vector<Vector> grande = recibe.getGrande();
        
        if(recibe.getParametrosConsulta().get("WHERE")!=null){//en caso de que la consulta tenga al comando WHERE
            HashMap<String,Object[]> parametrosConsulta = recibe.getParametrosConsulta();
            Object[] parametrosWhere = parametrosConsulta.get("WHERE");//se pasan los parametros que estan asociados
            //al comando Where
            String comparacion = parametrosWhere[0].toString();//el primer parametro del comando Where siempre es
            //el atributo con el cual se van a realizar las comparaciones con las condiciones que ingrese el usuario
            //para que solo se muestren ciertas tuplas del total de datos registrados, entonces como en parametrosWhere
            //tengo a los parametros de este comando, en la posicion cero debe estar la columna con la cual voy a 
            //realizar las comparaciones
            int ubicacion = recibe.getPosicionesAtributos().get(comparacion);//quiero saber en que columna de mi vector
            //grande esta ubicado el atributo con el cual voy a realizar todas las comparaciones, es por esto que 
            //obtengo su ubicacion en el arreglo grande para agilizar en gran medida las comparaciones
            
            comandoWhere cw = new comandoWhere(grande,parametrosWhere,ubicacion);
            //grande = cw.igualWhere();
            grande = cw.aplicacionWhere();
            if(grande == null){
                System.out.println("ERROR: HUBO UN ERROR A LA HORA DE HACER LAS COMPARACIONES CON EL COMANDO WHERE");
                return null;
            }


        }

        
        
        
        int posicion = 0;//defino un entero que me va a ayudar a poder ubicarme en la posicion que necesito del
        //vector llamado grande, este vector contiene a todos los datos de la tabla, al ser un vector de vectores,
        //cada vector dentro del vector grandote es como si fuese una fila, entonces si por ejemplo necesito recuperar
        //el email de un empleado, en mi objeto recibe primero en el ordenImpresion tengo registrados como es que quiero
        //que se muestren mis datos, ejemplo: email, manager id, primero se imprimiria el email y luego el manager id
        //luego, en mi objeto recibe en la parte de getPosicionesAtributos de ahi estoy sacando de donde es que se encuentran
        //los datos correspondientes a los atributos ejemplo: en la tabla estan definidos una cierta cantidad de atributos
        //estos estan ya definidos y siguen un orden, podria ser que al inicio este el nombre,luego el apellido luego el ID
        //y luego el managerID, o sea que en los datos de los empleados su nombre va a estar guardado en la posicion[0] del
        //vector "grande" su apellido en la posicion[1] y asi sucesivamente, es por eso que guarde estas posiciones en 
        //un HashMap teniendo como clave al nombre del atributo y su valor asignado la posicion que corresponderia a su 
        //ubicacion en el vector "grande" (recordando que este vector contiene toooodos los datos del archivo de texto)
        
        for(Vector fila:grande){//con este for voy a recorrer todos los vectores dentro de "grande"
            Vector<String> dentro = new Vector<>();//defino un nuevo vector de cadenas, este me va a servir para almacenar
            //los datos que quiero que se impriman
            for(String columna :recibe.getOrdenImpresion()){//voy a iterar por las cadenas que tiene guardado OrdenImpresion
                //que son los parametros que el usuario especifico que queria mostrar en la consulta
                posicion = recibe.getPosicionesAtributos().get(columna);//la posicion se toma dependiendo del atributo
                //que se desea imprimir y en que orden, ejemplo si tengo que el usuario quiere primero el email, ahora busco
                //en el HashMap que tiene guardadas las posiciones de los atributos en el vector "Grande", y me regresa un valor
                //ahora voy a tomaresa posicion y luego voy guardar dentro de mu nuevo vector "dentro" al dato de ese atributo
                //en especifico// puede verse como si es que fuera la columna, imaginando que grande se trata de una "matriz"
                //que tiene filas y columnas, lo que tengo que hacer es recorrer todas las filas, y luego si es que el usuario
                //quiere que le muestre la columna que tiene los EMAILS, entonces posicion va a tomar el valor de la ubicacion
                //de los EMAILS, entonces se va a guardar ese valor en "dentro", cuando se registre como sigo en un "for"
                //ahora voy a usar el siguiente Parametro especificado, digamos que es ManagerID, entoncs ahora del HashMap se 
                //obtiene su ubicacion y ahora de "grande" en la fila que me encuentre actualmente me muevo especificamente
                //a esa ubicacion que tiene guardada la variable "posicion" y la agrego a "dentro"
                dentro.add(fila.get(posicion).toString());   
            }
            datos.add(dentro);//se agrega el vector "dentro" a "datos" que es como si fuera el nuevo "grande", este va 
            //almacenando fila por fila los vectores con los datos especificos que se desean mostrar
                    
        }
        System.out.println("Dentro del nuevo vectorsote");//esto es para verificar que se imprime correctamente
        for(Vector dent:datos){
            for(Object cont:dent){
                System.out.print(cont);
            }
            System.out.println("");
        }
        
        System.out.println("Parametros de la consulta:");
        HashMap<String,Object[]> parametrosConsulta = recibe.getParametrosConsulta();
        Object[] parall = parametrosConsulta.keySet().toArray();
        for(Object llave:parall){
            System.out.println("ComandoSQL: " + llave);
            Object[] paramC = parametrosConsulta.get(llave);
            for(Object parametro:paramC){
                System.out.println(parametro);
            }
        }
        
        MostarEnTabla tabla = new MostarEnTabla(datos,recibe.getOrdenImpresion());

        return tabla;
  
    }

    public PasosTablasContenido recuperarTablaOriginal(){//este metodo va a servir para recuperar la tabla original
        ParametrosConsulta recibe = diccionarioDeDatos();//se verifica la sintaxis de todo, solo que ahora
        //ya no se va a hacer como en la parte de la consulta de solo mostrar los datos que se solicitan
        //sino que ahora se muestra todo, pero aun asi se ocupa lo del diccionario de datos porque no quiero que
        //funcione hasta que la consulta no se haya escrito correctamente

        if(recibe == null){
            System.out.println("ERROR: LA CONSULTA NO SE PUEDE EJECUTAR, parte de la original");
            return null;
        }
        try{

            Vector<Vector> grande = recibe.getGrande();
            descriptor = new DescriptorArchivos(nombreTabla);
            String[] atributos = descriptor.vaciarContenido();
            

            PasosTablasContenido retorno = new PasosTablasContenido(grande,atributos,nombreTabla);

            return retorno;

        }catch(Exception e){
            System.out.println("ERROR: NO SE PUDO RECUPERAR LA TABLA ORIGINAL");
            return null;
        }
        

    }

    
    public static void main(String args[]){
        
        LogicaSQL prueba = new LogicaSQL("SELECT \n employee_id, \n first_name,salary  ,manager_id  FROM tabla WHERE salary between 2500 AND 15000; ","hola.txt");
        
        System.out.println("prueba = " + prueba.getConsulta());
        
        
        System.out.println("prueba = " + prueba.getConsulta());
        
        prueba.ejecutarConsulta();
        
//        String p = "SELECT \n ,DE, \n PRUEBA  FROM employe WHERE j;";
//        System.out.println(p);
//        p=p.replaceAll("\\n", " ");//estas son pruebas para la limpieza de la entrada de texto
//        p=p.replace(",", " , ");
//        p=p.replace(";", " ;");
//        p=p.replaceAll("\\s+", " ");
//        System.out.println(p);
//        String[] a = p.split("\\s");
//        System.out.println("p = " + p);
//        for(String b :a){
//            System.out.println("aqui: " + b);
//        }
        
    }
    
}
