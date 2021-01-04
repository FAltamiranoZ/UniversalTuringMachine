
public class UTM{

private static int[][] tm;


/*
 * NewTape es una función que utiliza el arreglo de instrucciones generado por
 * llenaArreglo para trabajar la cinta que se le dió en FeedTM
 * N = Número máximo de transiciones, P = Posición del puntero
 */
public static String NewTape(String TT, String Cinta, int N, int P){
    llenaArreglo(TT);
    int estado = 0;
    char in;
    int maquina, unos = 0, totTransiciones = N;
    char[] newTape = Cinta.toCharArray();
    //La cinta se va a leer hasta que se llegue al estado de HALT o se agote el 
    //número de transiciones disponibles
    while (estado < 63 && N > 0){
      in = newTape[P];
      //Se lee la cinta para saber si se recibió un 0 o un 1, y a partir de
      //ello, se define que reglas se van a usar.
      maquina = in == '0' ? 0 : 3;
      //Se realiza aquello que esté indicado en el arreglo de instrucciones, 
      //siendo que primero se escribe en la cinta, después se mueve el puntero,
      //posteriormente se pasa al siguiente estado y finalmente se disminuye el
      //número de transiciones disponibles.
      newTape[P] = tm[estado][maquina] == 1 ? '1' : '0';
      P = P + tm[estado][maquina+1];
      estado = tm[estado][maquina+2];
      N--;
    }
    //Se calcula la productividad de la máquina.
    for (int i = 0; i < newTape.length; i++) {
        char c = newTape[i];
        if(c == '1') unos++;
    }
    //Se calculan el número de transiciones realizadas
    totTransiciones = totTransiciones - N;
    if(totTransiciones == 0){
        System.out.println("Se excedió la cantidad de transiciones permitida");
    }
    else{
        System.out.println("Se realizaron " + totTransiciones + " transiciones y se llegó al estado de HALT");
    }
    System.out.println("La productividad de esta máquina es de: " + unos);
    return String.valueOf(newTape);
  }

/*
 * llenaArreglo es una función cuyo propósito es generar una matriz que tenga 
 * tantas filas como estados y 6 columnas, siendo que las primeras 3 columnas 
 * representan lo que sucede cuando el estado recibe un 0; y las últimas 3, lo 
 * lo que sucede cuando el estado recibe un 1.
 * Las columnas 1 y 4 indican lo que se va a escribir, las columnas 2 y 5 
 * indican a donde se moverá el puntero y las columnas 3 y 6 indican cual es el 
 * siguiente estado
 */
  private static void llenaArreglo(String TT){
    int MTLen = TT.length();
    int NumStates = MTLen / 16;
    int ix16, Estado;
    String x0_M, x1_M;

    tm = new int[NumStates][6];

    for (int i = 0; i < NumStates; i++) {
      ix16 = i * 16; //La información que se va a leer sigue el estándar de 
      //usar los primeros 16 dígitos para un estado, siendo los primeros 8 para
      //representar cuando se le da un 0 y los últimos 8 cuando se le da un uno.
      //Dentro de estos 8, el primero indica que se va a escribir, el segundo a 
      //dónde se va a mover y los siguientes 4 el estado siguiente, utiliza 4
      //dígitos porque se representa de manera binaria
      
      //Caso en el que se le da un 0:
      tm[i][0] = Integer.parseInt(TT.substring(ix16, ix16 + 1));//Indica que se escribe.
      x0_M = TT.substring(ix16 + 1, ix16 + 2);
      //Si es un 1, entonces se mueve a la derecha; si no, a la izquierda.
      if (x0_M.equals("0")) tm[i][1] = 1;
      else tm[i][1] = -1;
      Estado = 0;
      for (int j = ix16 + 2; j < ix16 + 8; j++) { //Define cual es el próximo estado.
        Estado = Estado * 2;
        if (TT.substring(j, j + 1).equals("1")) Estado++;
        //endif
      } //endFor
      tm[i][2] = Estado;
      //endif
      
      //Caso en el que se le da un 1: (sigue la misma estructura que arriba, por
      //lo que no se comenta como funciona).
      tm[i][3] = Integer.parseInt(TT.substring(ix16 + 8, ix16 + 9));
      x1_M = TT.substring(ix16 + 9, ix16 + 10);
      if (x1_M.equals("0")) tm[i][4] = 1;
      else tm[i][4] = -1;
      Estado = 0;
      for (int j = ix16 + 10; j < ix16 + 16; j++) {
        Estado = Estado * 2;
        if (TT.substring(j, j + 1).equals("1")) Estado++;
        //endif
      } //endFor
      tm[i][5]= Estado;
    } //endFor
  }
}