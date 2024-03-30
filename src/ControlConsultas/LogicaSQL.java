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
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author Alexis
 */
public class LogicaSQL {
    
    private String consulta;

    private String[] ClausulasSQL = {"SELECT","FROM","WHERE"};

    public LogicaSQL(String consulta){
        this.consulta = consulta;
    }
    
    public String getConsulta() {
        return consulta;
    }

    private HashMap<String,Object[]> obtenerParametrosPorComando(){

        HashMap<String,Object[]> parametrosPorComando = ControlSintaxisConsulta.comprobarSintaxis(consulta,ClausulasSQL);//recibo el HashMap que contiene los 
        //parametros asignados a cada comando SQL// se manda a llamar a la clase que contiene todo el control de sintaxis
        if(parametrosPorComando == null){//en caso de ser nulo la sintaxis de la consulta no era correcta entonces
            //arrojo un error
            System.out.println("ERROR LA CONSULTA NO SE PUEDE EJECUTAR");
            return null;
        }
        return parametrosPorComando;

    }
    
    
    public MostarEnTabla ejecutarConsulta(){

        HashMap<String,Object[]> parametrosPorComando = obtenerParametrosPorComando();

        if(parametrosPorComando == null){
            System.out.println("ERROR: LA CONSULTA NO SE PUEDE EJECUTAR , funcion de ejecutarConsulta");
            return null;
        }

        ParametrosConsulta recibe = DiccionarioDeDatos.comprobarDiccionario(parametrosPorComando);
        
        if(recibe == null){
            System.out.println("ERROR: LA CONSULTA NO SE PUEDE EJECUTAR");
            return null;
        }
        String nombreTabla = recibe.getNombreTabla();//obtengo el nombre de la tabla que se va a consultar
        
        String[] ordenImpresion = recibe.getOrdenImpresion();//guardo en un arreglo el orden con el que se desea
        //que se imprima la consulta ingresada por el usuario
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
        
        MostarEnTabla tabla = new MostarEnTabla(datos,ordenImpresion,nombreTabla);

        return tabla;
  
    }

    public MostarEnTabla recuperarTablaOriginal(){//este metodo va a servir para recuperar la tabla original

        HashMap<String,Object[]> parametrosPorComando = obtenerParametrosPorComando();

        if(parametrosPorComando == null){
            System.out.println("ERROR: LA CONSULTA NO SE PUEDE EJECUTAR , funcion de ejecutarConsulta");
            return null;
        }

        ParametrosConsulta recibe = DiccionarioDeDatos.comprobarDiccionario(parametrosPorComando);//se verifica la sintaxis de todo, solo que ahora
        //ya no se va a hacer como en la parte de la consulta de solo mostrar los datos que se solicitan
        //sino que ahora se muestra todo, pero aun asi se ocupa lo del diccionario de datos porque no quiero que
        //funcione hasta que la consulta no se haya escrito correctamente

        if(recibe == null){
            System.out.println("ERROR: LA CONSULTA NO SE PUEDE EJECUTAR, parte de la original");
            return null;
        }

        String nombreTabla = recibe.getNombreTabla();//obtengo el nombre de la tabla que se va a consultar
        try{

            Vector<Vector> grande = recibe.getGrande();
            String[] atributos = recibe.getAtributosOriginales();
            

            MostarEnTabla tabla = new MostarEnTabla(grande,atributos,nombreTabla);

            return tabla;

        }catch(Exception e){
            System.out.println("ERROR: NO SE PUDO RECUPERAR LA TABLA ORIGINAL");
            return null;
        }
        
    }

    public MostarEnTabla tablaProyectada(){

        HashMap<String,Object[]> parametrosPorComando = obtenerParametrosPorComando();

        if(parametrosPorComando == null){
            System.out.println("ERROR: LA CONSULTA NO SE PUEDE EJECUTAR , funcion de ejecutarConsulta");
            return null;
        }

        ParametrosConsulta recibe = DiccionarioDeDatos.comprobarDiccionario(parametrosPorComando);
        
        if(recibe == null){
            System.out.println("ERROR: LA CONSULTA NO SE PUEDE EJECUTAR, error en el diccionario func tabla proy");
            return null;
        }

        String nombreTabla = recibe.getNombreTabla();//obtengo el nombre de la tabla que se va a consultar
        String[] ordenImpresion = recibe.getOrdenImpresion();//guardo en un arreglo el orden con el que se desea
        //que se imprima la consulta ingresada por el usuario
        Vector<Vector> datos = new Vector<>();
        Vector<Vector> grande = recibe.getGrande();
        HashMap<String,Integer> posicionesAtributos = recibe.getPosicionesAtributos();

        int posicion = 0;//defino un entero que me va a ayudar a poder ubicarme en la posicion que necesito del
        for(Vector fila:grande){//con este for voy a recorrer todos los vectores dentro de "grande"
            Vector<String> dentro = new Vector<>();//defino un nuevo vector de cadenas, este me va a servir para almacenar
            //los datos que quiero que se impriman
            for(String columna:ordenImpresion){
                posicion = posicionesAtributos.get(columna);
                dentro.add(fila.get(posicion).toString());

            }

            datos.add(dentro);

        }    

        MostarEnTabla tabla = new MostarEnTabla(datos,ordenImpresion,nombreTabla);
        return tabla;

    }

    public static void main(String args[]){
        
        LogicaSQL prueba = new LogicaSQL("SELECT \n employee_id, \n first_name,salary  ,manager_id  FROM tabla WHERE salary between 2500 AND 15000; ");
        
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
