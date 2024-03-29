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
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author Alexis
 */
public class LogicaSQL {
    
    private String consulta;
    private String nombreTabla;
    private String[] ClausulasSQL = {"SELECT","FROM","WHERE","I","J"};
    private HashMap<String,Integer> comandosSQL = new HashMap<>();
    private DescriptorArchivos descriptor;

    
    public LogicaSQL(String consulta,String nombreTabla){
        this.consulta = consulta;
        this.nombreTabla = nombreTabla;
        this.comandosSQL.put("SELECT", 1);
        this.comandosSQL.put("FROM", 2);
        this.comandosSQL.put("WHERE", 3);
        this.comandosSQL.put("I", 4);
        this.comandosSQL.put("J", 5);
    }
    
    public String getConsulta() {
        return consulta;
    }
    
    private void limpiarConsulta(){//la consulta que se va a recibir desde la ventana de consultas se adapta a un formato
        //sencillo de manejar, para poder asi ejecutar la consulta especifica que se solicita;
        this.consulta = this.consulta.toUpperCase();
        this.consulta = this.consulta.replaceAll("\\n", " ");//se remueven todos los saltos de linea
        this.consulta = this.consulta.replace(","," , ");//se colocan en las comas un espacio a los lados
        this.consulta = this.consulta.replace(";"," ;");//un punto y coma se le coloca un espacio antes
        this.consulta = this.consulta.replaceAll("\\s+", " ");//se reemplazan en caso de haber mas de un
        //espacio, por solo uno
        
        
    }
    
    private Object[] sintaxisWhere(Object[] argumentos){
        
        int tam;
        String ve = "";
        for(Object arg: argumentos){
            ve+=arg;
        }
        System.out.println("al inicio es");
        System.out.println("ve = " + ve);
        
        
        String[] condicionesWhere = {"=",">","<","<=",">="};//estas son las posibles condiciones que puede
        //llegar a tener el where        
        
//        for(int i = 0;i<ve.length();i++){//voy a recorrer la cadena formada por los argumentos del comando where
//            //y voy a buscar en ella a las condiciones del arreglo anterior, en caso de que se encuentre una secuencua
//            //equivalente a las condiciones entonces voy a realizar una funcion para separar con espacios en esa cadena
//            //pero dependiendo de la condicion con la que me encuentre, en caso de que haya una
//            if(i>0){
//                for(String car: condicionesWhere){
//                    if(car.equals(ve.charAt(i))){
//                    }
//                }
//            }
//        }
        String antes = ve;//voy a guardar el estado de la cadena que tiene a los parametros del comando where antes
        //de que la modifique
        boolean sigue = true;//creo una bandera para que me indique si es que la cadena a sufrido modificaciones
        
            
        ve = ve.replace("<=", " <= ");//estas dos operaciones buscan la una secuencia de caracteres
        //especifica, en caso de que la cadena ve tenga esa secuencia realiza la separacion, no importa que se ejecuten
        //seguidas porque son secuencias distintas, si es que alguna de estas modificaciones surge efecto entonces quiero
        //saber que se realizo ese cambio, por eso se aplica la bandera
        ve = ve.replace(">=", " >= ");
            
        if(ve.equals(antes) == false){
            sigue = false;
        }
        
        
        if(sigue){//no quiero ejecutar estas condiciones en caso de que ha haya hecho una modificacion porque estas al solo
            //tener un caracter aunque ya haya hecho una modificacion antes van a hacer otra, ejemplo, si tenia el comando:
            //"WHERE id<=50" al inicio se iba a separar de manera que quedaba "WHERE id <= 50", pero si las operaciones de
            //abajo se ejecutaban entonces se iba a separar de manera que "WHERE id < =  50" y luego otra vez por la 
            //condicion que verifica si hay un '<' y queda al final "WHERE id  <  =  50" lo que es incorrecto y luego
            //esos varios espacio se reducirian a 1 cada conjunto de espacios, pero quedan mas argumentos de los que realmente
            //eran
            ve = ve.replace("=", " = ");
            ve = ve.replace(">", " > ");
            ve = ve.replace("<", " < ");
        }
        
////        ve = ve.replace("=", " = ");
////        ve = ve.replace(">", " > ");
////        ve = ve.replace("<", " < ");
////        ve = ve.replace("<=", " <= ");
////        ve = ve.replace(">=", " >= ");
        
        ve = ve.replaceAll("BETWEEN", " BETWEEN ");//estas operaciones se preguntan despues de las anteriores
        //en caso de se encuentren las palabras between y and, no deben estar las anteriores porque seria un error de sintaxis
        //entonces estas operaciones no se tienen que condicionar, no hace falta
        ve = ve.replaceAll("AND", " AND ");
        ve = ve.replaceAll("\\s+", " ");
        
        System.out.println("ve = " + ve);
        
        Object[] nuevo = ve.split("\\s");
        
        switch(nuevo.length){
            case 3:
//                String[] condicionesWhere = {"=",">","<","<=",">="};//estas son las posibles condiciones que puede
//                //llegar a tener el where
                boolean condicion = false;//se va a verificar que el segundo parametro del comando WHERE sea alguna
                //de las condiciones anteriores, en caso de serlo significa que es correcto, si nunca coincide
                //entonces se trata de hacer una operacion no permitida y se regresa un error
                for(String condiciones:condicionesWhere){
                    if(nuevo[1].equals(condiciones)){
                        condicion = true;
                        break;
                    }
                }
                if(condicion == false){
                    System.out.println("ERROR: la operacion con WHERE no es correcta");
                    return null;
                }
                break;
            case 5:
                if(nuevo[1].equals("BETWEEN") == false){//en caso de que sean 5 parametros del comando WHERE
                    //entonces se necesita que el segundo parametro  de este comando sea la palabra between, el 
                    //usuario puede ingresarla como minusculas o mayusculas, no importa
                    System.out.println("ERROR: NO SE ENCONTRO LA PALABRA CLAVE BETWEEEN EN LOS PARAMETROS DEL COMANDO WHERE");
                    return null;
                }
                
                if(nuevo[3].equals("AND") == false){//como el caso de que la cantidad de parametros para el comando
                    //where requiere que sean 5, entre estos parametros es obligatorio el uso de las palabras reservadas
                    //"BETWEEN" y "AND" para que pueda funcionar correctamente este sistema, entonces ahora en esta
                    //condicion se verifica que el cuarto parametro del comando WHERE sea dicho "AND"
                    System.out.println("ERROR: NO SE ENCONTRO LA PALABRA CLAVE AND EN LOS PARAMETROS DEL COMANDO WHERE");
                    return null;
                }
                
                //si se llega hasta aqui es porque entonces si estan las palabras BETWEEN y AND, ahora se debe comprobar que
                //los valores con los que van a realizar las comparaciones sean numeros
                
                try{
                    String conversion = nuevo[2].toString();//se trata de ver si se puede convertir el tercer parametro
                    //que se ingreso para el comando SQL a un numero , se prueba con flotante porque las comisiones son
                    //decimales
                    Double.parseDouble(conversion);
                    conversion = nuevo[4].toString();//lo mismo se intenta ahora con el quinto parametro
                    Double.parseDouble(conversion);
                    
                }catch(NumberFormatException e){
                    System.out.println("ERROR: LOS VALORES PARA LA COMPARACION \"BETWEEN\" \"AND\" NO SON VALIDOS");
                    return null;
                }
                break;
            default: System.out.println("ERROR:  Los parametros del WHERE NO SON CORRECTOS");
                     return null;
        }
        
       
        
        System.out.println("Parametros nuevos de where");
        int cont =0;
        for(Object den:nuevo){
            System.out.println(cont + " : "  +den);
            
            cont++;
        }
        
        return nuevo;
    }
    
    private HashMap<String,Object[]> comprobarSintaxis(){
        limpiarConsulta();//primero le quito los saltos de linea a la consulta que reciba
        String[] accionesConsulta = this.consulta.split("\\s");//la consulta escrita por el usuario voy a guardar
        //cada palabra que ingreso en un arreglo de cadenas, para poder iterar entre ellas
        
//        System.out.println("Palabras de la consulta");
//        int cont=0;
//        for(String palabra:accionesConsulta){
//            System.out.println(palabra + " :"+cont);
//            cont++;
//        }
        
        int correcto = 0;
        int contador = 0;
        HashMap<String,Integer> auxiliar = new HashMap<>();//defino un hashmap, que me va a servir para escribir
        //las instrucciones SQL que tiene la consulta y sus posiciones, las llaves van a ser las los comandos SQL
        //y el valor asignado a las llaves va a ser su posicion, la posicion de como viene escrita la consulta
        
        if(accionesConsulta[0].equals(ClausulasSQL[0])==false){//en caso de que la primera parte de la consulta no sea un SELECT
            //la consulta esta mal estructurada
            System.out.println("La consulta debe iniciar con la instruccion SELECT");
            System.out.println(ClausulasSQL[0]);
            System.out.println(accionesConsulta[0]);
            
            return null;//el null retornado va a ser mi indicador de error
        }
       
        
        for(String cadenas: accionesConsulta){//me voy a mover por todas las cadenas que contiene la consulta ingresada
            //para poder verificar las instrucciones SQL que contiene
            
            if(comandosSQL.containsKey(cadenas)){//en caso de que la consulta del usuario contenga las palabras 
                //reservadas de los comandos SQL se guarda la posicion en la que se recibieron, esto es porque se tiene
                //que respetar un cierto, orden, por ejemplo un FROM no puede ir antes que un SELECT y asi
                
                if(auxiliar.containsKey(cadenas)){//esta condicion se ejecuta en caso de que en la consulta del
                    //usuario haya ingresado dos veces un SELECT por ejemplo, lo cual debe arrojar un error
                    System.out.println("ERROR: Se ingreso mas de 1 vez la misma instruccion: " + cadenas);
                    return null;
                }
                
                auxiliar.put(cadenas, contador);
            } 
            contador++;//el contador va a servir para indicar la posicion en la que se encuentra la cadena de la consulta
            //en el arreglo de accionesConsulta, porque se inicio en cero
            if(cadenas.equals(";")){
                //en caso de encontrarse con un ";" se debe terminar la la lectura, pues este caracter indica el fin
                //de la consulta
                break;
            }
              
        }
       
        System.out.println("Lo que se va a usar como contenido de la consulta:");
        for(int i = 0;i<contador;i++){//aqui se muestra lo que se va a usar, y eso esta delimitado por el ";"
            System.out.println(accionesConsulta[i] + " :"+i);
            
        }
        if(accionesConsulta[contador-1].equals(";")==false){
            System.out.println("ERROR: Toda consulta debe terminar con \";\" ");
            return null;
            
        }
        
        

        Object[] llavesConsulta = auxiliar.keySet().toArray();//guardo los Comandos SQL encontrados en la consulta del
        //usuario
        int[] ordenInstrucciones = new int[llavesConsulta.length];//con un arreglo de enteros voy a saber en que orden se
        //ingresaron los Comandos SQL, porque como se menciono antes deben respetar un orden
        String[] instruccionesOrdenadas = new String[llavesConsulta.length];//guardo las instrucciones de la consulta
        //del usuario, esto sirve para que este sincronizado con el arreglo de ordenInstrucciones
//        for(int j=0;j<llavesConsulta.length;j++){//esto es solo para ver en que orden se guardan las llaves de un hashmap
//            //y no se guardan en el orden en el que fueron registradas, parece ser aleatorio, por eso no se va a usar
//            //en el orden de las llaves, porque no serviria
//            System.out.println("hola :" + auxiliar.get(llavesConsulta[j]));
//           
//        }
        
        int control = 0;//la variable control me va a ayudar a ubicar en el orden en que los comandos SQL fueron ingresados
        //en la consulta, inicia en cero porque va a servir para las posiciones del arreglo de enteros llamado
        //ordenInstrucciones, que en su posicion cero tendra en que posicion se escribio el ComandoSQL, pues va a recibir
        //el valor del que tenga almacenado el HashMap auxiliar.
        //Cada iteracion del ciclo siguiente se hace para verificar las posibles de todos los posibles comandos SQL que se 
        //pueden llegar a ingresar, al inicio se pregunta por SELECT, se le pregunta al HashMap de auxiliar si es que 
        //la palabra SELECT esta en el, en caso de tenerlo se va a guardar en la posicion cero de ordenInstrucciones
        //la posicion en la que se escribio el comando. Este arreglo funciona porque las posiciones siguientes de las anteriores
        //deben ser siempre mayores entre si, esto es porque como se esta recorriendo en orden el arreglo de ClausulasSQL este
        //contiene el orden que se debe respetar entre los comandos, entonces si por ejemplo al inicio se guardo en ordenInstrucciones
        //en su posicion cero un valor de "0" significa que la primera palabra de la consulta es SELECT, luego digamos que se 
        //pregunta por FROM en el HashMap auxiliar, y se guarda en ordenInstrucciones en la posicion 1, porque el control aumento
        //1 en valor, el valor de "8", y luego se pregunta por WHERE en el HashMap auxiliar y se guarda en ordenInstrucciones
        //en la posicion 2 un valor de "6", singifica que entonces la consulta por ejemplo seria:
        //"SELECT id,name,department,salary,phone WHERE id==1 FROM employees", lo cual esta mal, porque el FROM debe estar antes
        //que el WHERE siempre.
        
        for(int i = 0;i<ClausulasSQL.length;i++){//aqui voy a guardar Que instrucciones y en que posicion de la consulta
            //se encuentran
            if(auxiliar.containsKey(ClausulasSQL[i])){
                System.out.println("Intruccion " + ClausulasSQL[i] + " posicion " + auxiliar.get(ClausulasSQL[i]) );
                ordenInstrucciones[control] = auxiliar.get(ClausulasSQL[i]);
                instruccionesOrdenadas[control] = ClausulasSQL[i];
                control++;
            }
        }
        
        for(int i=0;i<ordenInstrucciones.length;i++){//este for comprueba que despues de los comandos SQL escritos en
            //la consulta del usuario no se encuentre inmediatamente una coma, esto es posible gracias a que el arreglo
            //de ordenInstrucciones tiene guardadas las posiciones de los comandos ingresados en la consulta
            if(accionesConsulta[ordenInstrucciones[i]+1].equals(",")){
            System.out.println("La sintaxis de la consulta no puede tener una coma seguida de un comando SQL");
            return null;
            }
            if(i>0 && accionesConsulta[ordenInstrucciones[i]-1].equals(",")){// se comprueba que antes del comando
                //SQL no se encuentre una coma, a excepcion de cuando se trate de un SELECT pues ese se encuentra al
                //inicio y no tiene a nadie antes, un ejemplo de cuando se ejecuta esta condicion es:
                //SELECT ID,NOMBRE,SALARIO, FROM employees; //como se ve hay una "," despues de salario que no le sigue
                //ningun parametro, lo cual debe marcarse como un error
                System.out.println(" ERROR: No puede haber una coma y despues un comando SQL");
                return null;
            }
        }
        
        for(int i=0;i<ordenInstrucciones.length-1;i++){//esto es para verificar que dos comandos SQL no se encuentren seguidos
            //porque eso es un error
            if((ordenInstrucciones[i] + 1) == ordenInstrucciones[i+1]){
                System.out.println("ERROR: DOS COMANDOS SQL NO PUEDEN ESTAR SEGUIDOS, SE ENCONTRO:");
                System.out.println(instruccionesOrdenadas[i]+" junto a " + instruccionesOrdenadas[i+1]);
                return null;
            }
        }
         
        
        
        for(int j=0;j<llavesConsulta.length;j++){//aqui es donde se comprueba que las posiciones en el arreglo de ordenInstrucciones
            //esten ordenadas de menor a mayor, pues esto significa que respetan la jerarquia/orden de Comandos
            System.out.println("numero en " +j+ " es :" + ordenInstrucciones[j]);
            for(int i = j;i<llavesConsulta.length;i++){
                if(ordenInstrucciones[j]>ordenInstrucciones[i]){
                    System.out.println("El orden de las instrucciones no es permitido");
                    return null;
                }
            }
        }
        
        if(auxiliar.containsKey(ClausulasSQL[0]) && auxiliar.containsKey(ClausulasSQL[1])){
        }else{
            System.out.println("La consulta no tiene SELECT Y/O FROM");
            return null;//se comprueba que en la consulta recibida esten las palabras SELECT
            //y FROM pues sin ellas la consulta no podria seleccionar que cosa va a mostrar ni de donde va a sacar
            //su informacion
        }
        ////HashMap<String,String[]> parametrosPorComando = new HashMap<>();
        HashMap<String,Object[]> parametrosPorComando = new HashMap<>();
        
        //System.out.println("Contador = :"+contador);
        //System.out.println("length " +llavesConsulta.length);
        
        int comprobadorFrom = 0;//esta variable es para asegurarme que los parametros del from solo es uno, esto es
        //porque solo puedo leer un archivo a la vez
        
        for(int i = 0;i<llavesConsulta.length;i++){//for que sirve para poner en un HashMap los parametros correspondientes
            //a cada comando SQL, ejemplo a SELECT esta asociado ID a FROM employees  y asi

            if(i==llavesConsulta.length-1){//esta condicion es para cuando me encuentre en la ultima instruccion ingresada
                //como en esta no tengo una siguiente entonces los parametros de este comando van a ser los que esten 
                //despues de el y antes del punto y coma
                int cantParametros = contador - ordenInstrucciones[i] - 2;//la cantidad de parametros
                //que se van a guardar por comando , a comparacion del siguiente debe ser 1 menos porque en la ultima 
                //instruccion se puede llegar a encontrar con el ";" por lo que haciendo la resta del -2 ya no se
                //va a meter entre los parametros
                
                ////String[] parametros = new String[cantParametros];
                ArrayList<String> parametros = new ArrayList<>();
                ////int ubi = 0;
                int ubicacionComando = ordenInstrucciones[i];
                for(int j = ordenInstrucciones[i]+1;j<contador-1;j++){
                    if(accionesConsulta[j].equals(",")){//en caso de que se encuentre una coma eso no lo quiero guardar como
                        //parametro de un comando, por lo que se ignora
                    }else{
                        //parametros[ubi] = accionesConsulta[j];
                        //ubi++;
                        parametros.add(accionesConsulta[j]);
                        if(accionesConsulta[ubicacionComando].equals("FROM")){
                            System.out.println("suma a comprobador");
                            comprobadorFrom++;
                        }
                    }
                    
                }
                Object[] paramConvertidos = parametros.toArray();
                parametrosPorComando.put(instruccionesOrdenadas[i], paramConvertidos);
                
            }else{
                int cantParametros = ordenInstrucciones[i+1] - ordenInstrucciones[i] - 1;//la cantidad de parametros
                //que se van a guardar por comando son la resta de la ubicacion del comando siguiente al comando anterior
                //ejemplo si esta el comando Select en la posicion 0 y el from en la 5, entonces quiero los parametros entre
                //ese select y el from, que serian los parametros del SELECT, por lo que estarian en las posiciones 1,2,3,4
                //o sea 4, y lo obtengo de 5-0-1=4
                ////String[] parametros = new String[cantParametros];//se crea un arreglo de cadenas que va a tener dentro
                //a los parametros asociados a cada comando SQL, se va reiniciando porque su cantidad de parametros es 
                //distinta para cada comando
                ////int ubi = 0;
                ArrayList<String> parametros = new ArrayList<>();
                int ubicacionComando = ordenInstrucciones[i];
                for(int j = ordenInstrucciones[i] + 1;j<ordenInstrucciones[i+1];j++){//j debe ubicarse un espacio despues
                    //del comando, por eso se pone el +1, y su ultima iteracion debe ser cuando valga 1 menos que
                    //ordenInstrucciones[i+1] porque ahi se ubica el siguiente comando
                    if(accionesConsulta[j].equals(",")){//en caso de que se encuentre una coma eso no lo quiero guardar como
                        //parametro de un comando, por lo que se ignora
                    }else{
                        ////parametros[ubi] = accionesConsulta[j];// ORIGINALMENTE LO PUSE con un arreglo de cadenas
                        //pero cuando agregue lo de que no se metan "," ahora el arreglo tenia espacios en null
                        //porque las comas se encuentran en el arreglo de accionesConsulta, entonces con un 
                        //ArrayList puedo solo poner a los elementos que necesito sin necesidad de saber cuantos voy
                        //a meter, pero despues se va a tener que convertir a un String[] porque ese parametro acepta
                        //la tabla de la vista//ACTUALIZACION: AHORA lo convierto a un Object[], porque este parametro
                        //si es aceptado por la tabla de vista, lo convierto a Object[] y no a String[] porque 
                        //el metodo .toArray() de las ArrayList solo convierte a Object
                        ////ubi++;
                        parametros.add(accionesConsulta[j]);
                        if(accionesConsulta[ubicacionComando].equals("FROM")){//en caso de que me encuentre en la parte
                            //de asignar los parametros al FROM, que seria la ubicacion que guarde antes del for que verifica los parametros
                            //de dicho comando, como quiero verificar el comando especifico en el que se esta iterando
                            //entonceso por eso esa ubicacion, ahora con la variable de comprobador me aseguro de quue
                            //el FROM solo tenga un unico parametro asignado, o sea que solo va a leer una tabla
                            System.out.println("suma a comprobador");
                            comprobadorFrom++;
                        }
                        
                    }
                }
                Object[] paramConvertidos = parametros.toArray();
                parametrosPorComando.put(instruccionesOrdenadas[i], paramConvertidos);
            }
                
        }//fin del for para crear el HashMap de parametros a cada comando
//            for(int i = 0;i<llavesConsulta.length;i++){
//                String[] dentro = parametrosPorComando.get(instruccionesOrdenadas[i]);
//                System.out.println("Los parametros del comando " + instruccionesOrdenadas[i] + " son:");
//                for(String param: dentro){
//                    System.out.println("--" + param);
//                }////// ESTO ES PARA IMPRIMIR EN CASO DE QUE USE UN ARREGLO DE CADENAS PARA GUARDAR LOS PARAMETROS
                   ////// DE CADA COMANDO
//            }
            
            for(int i = 0;i<llavesConsulta.length;i++){
                Object[] dentro = parametrosPorComando.get(instruccionesOrdenadas[i]);
                System.out.println("Los parametros del comando " + instruccionesOrdenadas[i] + " son:");
                for(Object param: dentro){
                    System.out.println("--" + param);
                    
                }
            }
            
            if(parametrosPorComando.get("WHERE")!=null){//esta condicion checa si es que se ingreso el comando WHERE
                //EN LA CONSULTA, en caso de que no este se sigue como si nada, pero si esta entonces se va a verificar
                //su sintaxis
                //tengo que verificar que el WHERE cumpla con las condiciones para que pueda ser ejecutado, debe tener de 3 a 
            //4 parametros, sin embargo se puede permitir que el usuario ingrese en caso de ser 3 comandos todo junto
            //ejemplo where salary=120, al inicio se va a leer como solo un argumento, pero mandando a llamar al metodo
            //de sintaxis where se van a separar en 3
            Object[] parametrosdelWhere = parametrosPorComando.get("WHERE");
            Object[] nuevosParametrosWhere = sintaxisWhere(parametrosdelWhere);
            if(nuevosParametrosWhere == null){//en caso de que no se respete la sintaxis del where en sus parametros
                //de arroja un error
                System.out.println("ERROR: Los parametros del WHERE NO SON CORECTOS");
                return null;
            }
            parametrosPorComando.remove("WHERE");
            parametrosPorComando.put("WHERE", sintaxisWhere(parametrosdelWhere));
            }//fin de la comprobacion de si esta el comando WHERE y su comprobacion de sintaxis
            
        
        if(comprobadorFrom!=1){//solo debe haber un parametro en para el Comando FROM , en caso de haber mas de uno
            //o de no haber ninguno, entonces se debe retornar un error
            System.out.println("ERROR LOS PARAMETROS DEL FROM NO SON CORRECTOS");
            return null;
        }
        

        
        return parametrosPorComando;
    }
    
    
    private ParametrosConsulta diccionarioDeDatos(){//se va a verificar los datos de la consulta se encuentre en el archivo a leer
        
        HashMap<String,Object[]> parametrosConsulta = comprobarSintaxis();//recibo el HashMap que contiene los 
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
        
        prueba.limpiarConsulta();
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
