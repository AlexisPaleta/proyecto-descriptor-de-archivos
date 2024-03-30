package ControlConsultas;

import java.util.ArrayList;
import java.util.HashMap;

public class ControlSintaxisConsulta {//esta clase solo tiene metodos y se encarga de verificar la sintaxis de las consultas

    private static String limpiarConsulta(String consulta){//la consulta que se va a recibir desde la ventana de consultas se adapta a un formato
        //sencillo de manejar, para poder asi ejecutar la consulta especifica que se solicita;
        consulta = consulta.toUpperCase();
        consulta = consulta.replaceAll("\\n", " ");//se remueven todos los saltos de linea
        consulta = consulta.replace(","," , ");//se colocan en las comas un espacio a los lados
        consulta = consulta.replace(";"," ;");//un punto y coma se le coloca un espacio antes
        consulta = consulta.replaceAll("\\s+", " ");//se reemplazan en caso de haber mas de un
        //espacio, por solo uno
        
        return consulta;
    }

    private static Object[] sintaxisWhere(Object[] argumentos){
        
        String ve = "";
        for(Object arg: argumentos){//aqui se fusionan los argumentos que se ingresaron para el comando WHERE
            ve+=arg;
        }
        System.out.println("al inicio es");
        System.out.println("ve = " + ve);
        
        String[] condicionesWhere = {"=",">","<","<=",">="};//estas son las posibles condiciones que puede
        //llegar a tener el where        
        

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
        
        ve = ve.replaceAll("BETWEEN", " BETWEEN ");//estas operaciones se preguntan despues de las anteriores
        //en caso de se encuentren las palabras between y and, no deben estar las anteriores porque seria un error de sintaxis
        //entonces estas operaciones no se tienen que condicionar, no hace falta
        ve = ve.replaceAll("AND", " AND ");
        ve = ve.replaceAll("\\s+", " ");
        
        System.out.println("ve = " + ve);
        
        Object[] nuevo = ve.split("\\s");
        
        switch(nuevo.length){
            case 3:
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

    public static HashMap<String,Object[]> comprobarSintaxis(String consulta,String[] ClausulasSQL){
        consulta = limpiarConsulta(consulta);//primero le quito los saltos de linea a la consulta que reciba
        String[] accionesConsulta = consulta.split("\\s");//la consulta escrita por el usuario voy a guardar
        //cada palabra que ingreso en un arreglo de cadenas, para poder iterar entre ellas
        
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
            
            for(String comandosSQL: ClausulasSQL){//en caso de que la consulta del usuario contenga las palabras 
                //reservadas de los comandos SQL, se guarda la posicion en la que se recibieron, esto es porque se tiene
                //que respetar un cierto, orden, por ejemplo un FROM no puede ir antes que un SELECT y asi, por eso
                //se van a guardar los comandos SQL ingresados por el usuario en un HashMap, la llave va a ser
                //el nombre del comando que el usuario Ejecute y su valor asociado va a ser la posicion en la que
                //ese comando fue escrito en la consulta
                if(comandosSQL.equals(cadenas)){

                    if(auxiliar.containsKey(cadenas)){//esta condicion se ejecuta en caso de que en la consulta del
                        //usuario haya ingresado dos veces un SELECT por ejemplo, lo cual debe arrojar un error
                        //lo que hace es revisar si es que el HashMap que contiene a los comandos ingresados del usuario
                        //tiene ya guadado a la nueva cadena que se le quiere ingresar, si es que solo se ingreso una vez
                        //el comando entonces nunca se cumpliria esta condicion, pero si es que sale esto como
                        //verdadero entonces se tiene que aclarar que es un error
                        System.out.println("ERROR: Se ingreso mas de 1 vez la misma instruccion: " + cadenas);
                        return null;
                    }
                    
                    auxiliar.put(cadenas, contador);

                }
                
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
        if(accionesConsulta[contador-1].equals(";")==false){//el contador se va a quedar con un valor de
            //1 de mas de la cantidad de palabras que tiene la consulta, entonces ahora lo que quiero revisar es que
            //en la ultima posicion de la consulta este el ";", en caso de que no este entonces la consulta esta mal
            System.out.println("ERROR: Toda consulta debe terminar con \";\" ");
            return null;
            
        }
        for(int i = 0;i<accionesConsulta.length;i++){//voy a asegurarmde de que no haya comas seguidas de otras
        //comas, no debe fallar porque ya me asegure de que el ultimo caracter de la consulta sea un ";", entonces
        //en el primer if en caso de que en la posicion en la que me encuentre se encuentre una coma la siguiente
        //como maximo podria ser un ";" por ejemplo
            if(accionesConsulta[i].equals(",") && accionesConsulta[i+1].equals(",")){
                System.out.println("ERROR: No puede haber una coma seguida de otra");
                return null;
            }
        }
         
        Object[] llavesConsulta = auxiliar.keySet().toArray();//guardo los Comandos SQL encontrados en la consulta del
        //usuario,esto me sirve para luego poder iterar en el HashMap, pues asi obtengo las llaves de este
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
        //Cada iteracion del ciclo siguiente se hace para verificar la posicion de todos los posibles comandos SQL que se 
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
            if(auxiliar.containsKey(ClausulasSQL[i])){//voy a preguntar por cada uno de los comandos SQL que se pueden
                //ingresar, pregunto en orden entonces al inicio estoy diciendo: "esta la palabra SELECT en la consulta?"
                //recordando que en este momento en el HashMap auxiliar tengo los comandos registrados por el usuario
                System.out.println("Instruccion " + ClausulasSQL[i] + " posicion " + auxiliar.get(ClausulasSQL[i]) );//en 
                //esta parte imprimo el nombre del Comando por el que estoy preguntando, luego le digo al HashMap auxiliar
                //que me regrese el valor que tiene guardado para ese comando que estoy diciendo, entonces regresa la posicion
                //en la que el usuario escribio ese comando
                ordenInstrucciones[control] = auxiliar.get(ClausulasSQL[i]);//se guarda la posicion en la que se escribio
                //el comando SQL en cierta posicion, recordando que el tamano de este arreglo es igual a la cantidad de llaves
                //del HashMap auxiliar, es decir a la cantidad de comandos SQL que se ingresaron en la consulta
                instruccionesOrdenadas[control] = ClausulasSQL[i];//se guarda el comando ingresado en la consulta
                //entonces en conjunto estos dos arreglos van a  guardar en su posicion 0 al primer comando SQL que se haya
                //ingresado en la consulta, estos arreglos podran tener como tamano maximo a la cantidad de comandos SQL que yo
                //tenga contemplado ejecutar, en este caso solo se tienen a SELECT, FROM y WHERE, entonces en estos arreglos
                //se van a guardar las posiciones de los comandos ejecutados por el usuario
                control++;
            }
        }
        
        for(int i=0;i<ordenInstrucciones.length;i++){//este for comprueba que despues de los comandos SQL escritos en
            //la consulta del usuario no se encuentre inmediatamente una coma, esto es posible gracias a que el arreglo
            //de ordenInstrucciones tiene guardadas las posiciones de los comandos ingresados en la consulta
            if(accionesConsulta[ordenInstrucciones[i]+1].equals(",")){//lo que se hace es ubicarme en el arreglo
                //que tiene guardadas todas las palabras que ingreso el usuario, luego quiero verificar una posicion despues
                //de los comandos que estan en la consulta, entonces como el arreglo ordenInstrucciones tiene guardadas las
                //posiciones, si le sumo 1 entonces tiene que ser diferente a una coma o regresare un error, en este punto
                //no es posible que un comando este en la ultima posicion porque antes ya verifique que el ultimo caracter
                //ingresado en las consultas debe de ser un ";"
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
            //porque eso es un error, se van a revisar las posciones de los comandos SQL en el arreglo de ordenInstrucciones
            //con la siguiente posicion del siguiente comando SQL, si se encuentran seguidos entonces se arroja un error
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
        HashMap<String,Object[]> parametrosPorComando = new HashMap<>();//voy a guardar los parametros que se ingresaron
        //para cada comando SQL, esto es para poder ejecutar la consulta, entonces se va a guardar en un HashMap, la llave
        //va a ser el nombre del comando SQL y el valor asociado va a ser un arreglo de objetos que va a tener los parametros
        //que se ingresaron en la consulta, se guarda en un arreglo de objetos porque como voy a ir guardando cada parametro
        //en una lista, al tratar de convertirlos a un arreglo de cadenas no deja, solo deja a un arreglo de objetos
        
        //System.out.println("Contador = :"+contador);
        //System.out.println("length " +llavesConsulta.length);
        
        int comprobadorFrom = 0;//esta variable es para asegurarme que los parametros del from solo es uno, esto es
        //porque solo puedo leer un archivo a la vez
        
        for(int i = 0;i<llavesConsulta.length;i++){//for que sirve para poner en un HashMap los parametros correspondientes
            //a cada comando SQL, ejemplo a SELECT esta asociado ID a FROM employees  y asi

            if(i==llavesConsulta.length-1){//esta condicion es para cuando me encuentre en la ultima instruccion ingresada
                //como en esta no tengo una siguiente entonces los parametros de este comando van a ser los que esten 
                //despues de el y antes del punto y coma
                //int cantParametros = contador - ordenInstrucciones[i] - 2;//la cantidad de parametros
                //que se van a guardar por comando , a comparacion del siguiente debe ser 1 menos porque en la ultima 
                //instruccion se puede llegar a encontrar con el ";" por lo que haciendo la resta del -2 ya no se
                //va a meter entre los parametros
                
                ////String[] parametros = new String[cantParametros];
                ArrayList<String> parametros = new ArrayList<>();
                ////int ubi = 0;
                int ubicacionComando = ordenInstrucciones[i];
                for(int j = ordenInstrucciones[i]+1;j<contador-1;j++){//recordando que el contador tiene un valor de 1 de
                    //mas que la ultima posicion de la consulta, por eso se le resta 1, en este caso como se estan agregando
                    //los parametros del ultimo comando que el usuario haya ingresado como ya me asegure de que la consulta
                    //termine con un ";" entonces significa que el (contador-1) ahora vale la posicion del ";" por lo que quiero
                    //los parametros que esten antes de el, por eso se le resta 1 al contador y aparte j trabaja mientras sea menor que
                    //contador-1, entonces es como si solo trabajara hasta contador-2 realmente, o sea justo antes del ";"
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
                //int cantParametros = ordenInstrucciones[i+1] - ordenInstrucciones[i] - 1;//la cantidad de parametros
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
                Object[] paramConvertidos = parametros.toArray();//aqui se convierte a un arreglo de objetos
                parametrosPorComando.put(instruccionesOrdenadas[i], paramConvertidos);//se guarda en el HashMap los comandos
                //con sus parametros
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
            //5 parametros, sin embargo se puede permitir que el usuario ingrese en caso de ser 3 comandos todo junto
            //ejemplo where salary=120, al inicio se va a leer como solo un argumento, pero mandando a llamar al metodo
            //de sintaxis where se van a separar en 3
            Object[] parametrosdelWhere = parametrosPorComando.get("WHERE");
            Object[] nuevosParametrosWhere = sintaxisWhere(parametrosdelWhere);
            if(nuevosParametrosWhere == null){//en caso de que no se respete la sintaxis del where en sus parametros
                //de arroja un error
                System.out.println("ERROR: Los parametros del WHERE NO SON CORRECTOS");
                return null;
            }
            parametrosPorComando.remove("WHERE");//SE quitan los parametros que estaban antes en el Where que se habian
            //registrado antes porque esos no habian sido verificados 
            parametrosPorComando.put("WHERE", sintaxisWhere(parametrosdelWhere));
            }//fin de la comprobacion de si esta el comando WHERE y su comprobacion de sintaxis
            
        
        if(comprobadorFrom!=1){//solo debe haber un parametro en para el Comando FROM , en caso de haber mas de uno
            //o de no haber ninguno, entonces se debe retornar un error
            System.out.println("ERROR LOS PARAMETROS DEL FROM NO SON CORRECTOS");
            return null;
        }
        
        return parametrosPorComando;
    }


}
