import java.awt.Point;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Automata {
	
	private boolean ultEsKleene=false;
	
	private char[] pat,
				   simbolos,
				   analizar;
	
	private int[][] tablaEstados;
	
	private Queue<Integer> operadores;
	
	private LinkedList<Point> indices;
	
	public Automata(String fullPat, char[] analizar,char[] reemplazo) {
		
		String[] recibidos=fullPat.split("\\+");

		this.analizar=analizar;
		
		for (int i = 0; i < recibidos.length; i++) {
			operadores = procesarOperadores(recibidos[i].toCharArray());
			indices=new LinkedList<>();
			this.pat=limpiarPatron(recibidos[i].toCharArray());
			this.simbolos=encontrarSimbolos(this.pat);
			
			this.tablaEstados=new int[this.pat.length+1][simbolos.length];
			this.crearTablaEst(this.pat, simbolos, tablaEstados, operadores);
			this.analizarTabla(tablaEstados, simbolos);
			this.reemplazador(indices,reemplazo);
		}
		
		
		
	}
	//Verifica cuantos simbolos se pueden recibir.
	private char[] encontrarSimbolos(char[] cadena) {
		Hashtable<Character, Character> temp=new Hashtable<>();
		for (int i = 0; i < cadena.length; i++) {
			temp.put(cadena[i], cadena[i]);
		}
		
		char[] simbolos=new char[temp.size()];
		
		Set<Character> keys = temp.keySet();
		int it=0;
		for(Character key: keys){
			simbolos[it]=temp.get(key);
			it++;
		}
		
		return simbolos;
	}
	
	//Guarda en orden los operadores * y +
	private Queue<Integer> procesarOperadores(char[] cadena) {
		Queue<Integer> lista = new LinkedList<>();
		for (int i = 0; i < cadena.length; i++) {
			if(cadena[i]=="*".charAt(0)) {
				lista.add(1);
			}else if(cadena[i]=="+".charAt(0)) {
				lista.add(2);
			}else {
				lista.add(0);
			}
		}
		lista.add(0);
		
		return lista;
	}
	
	//Para meter una cadena sin * o +
	private char[] limpiarPatron(char[] cadena) {
		LinkedList<Character> temp=new LinkedList<Character>();
		
		for (char character : cadena) {
			if(character!="*".charAt(0) && character!="+".charAt(0)) {
				temp.add(character);
			}
		}
		
		int cont=0;
		char[] nueva = new char[temp.size()];
		while (!temp.isEmpty()) {
			if(temp.peek()!="*".charAt(0) && temp.peek()!="+".charAt(0)) {
				nueva[cont]=temp.poll();
				cont++;
			}
			
		}
		
		return nueva;
	}
	
	//Hace la tabla de estados.
	private void crearTablaEst(char[] patron, char[] simb, int tablaEst[][], Queue<Integer> op) 
    {
		int repeticiones=0;
		//La i es el renglon de estados
        for (int i = 0; i <= patron.length; i++) {
        	//La j es la columna de simbolos
            for (int j = 0; j < simb.length; j++) {
                tablaEst[i][j] = siguienteEst(patron, simb, i,j);
            }
            
            //Cuando hay un operador *
            if(op.poll()==1) {
            	op.poll();
            	//System.out.println("Kleene en "+ patron[i-1]);
            	
            	repeticiones++;
            	//Ciclado
            	for (int j = 0; j < simb.length; j++) {
					if(simb[j]==patron[i-1]) {
						tablaEst[i][j]=i;
					}
				}
            	//Si X*=0
            		for (int j = 0; j < simb.length; j++) {
    					if(tablaEst[i][j]==i+1) {
    						for (int k = repeticiones; k > 0; k--) {
    							tablaEst[i-k][j]=i+1;
							}
    						
    					}
    					if(i==patron.length && simb[j]==patron[i-1]) {
                			//tablaEst[i-1][j]=i+1;
    						ultEsKleene=true;
                			//System.out.println("kleen al final, el anterior es "+patron[i-2]);
                		}
    				}
            		
            }else {
            	repeticiones=0;
            }
        }
        
        //Imprimir el encabezado de la tabla
      	System.out.print("[");
      	int it=0;
      	for (char c : simb) {
      		System.out.print(c);
      		if(it<simb.length-1) {
      			System.out.print(", ");
      		}
      		it++;
      	}
      	System.out.print("]");
      	System.out.println();
      	//-----------------------------------
        //Imprimir la tabla
        System.out.println(Arrays.deepToString(tablaEst).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
    }
	
	//Parte de crearTablaEst(), se encarga de buscar el siguiente estado correspondiente.
	private int siguienteEst(char[] patron, char[] simb, int estado, int simbolo) {
		
		if(estado<patron.length && patron[estado]==simb[simbolo]) {
			return estado+1;
		}
		
		int i;
        for (int est = estado; est > 0; est--){ 
        	
            if (patron[est-1] == simb[simbolo]){ 
            	
                for (i = 0; i < est-1; i++) { 
                    if (patron[i] != patron[estado-est+1+i]) { 
                    	//System.out.println("entre en "+estado+" y "+simb[simbolo]);
                        break; 
                    }
                }
                if (i == est-1) { 
                    	//System.out.println("entre en "+estado+" y "+simb[simbolo]);
                        return est; 
                }
            } 
        } 
  
        return 0;
		
	}
	
	private void analizarTabla(int[][] tablaEst, char[] simb) {
		int contador=0,
			pastState=0;
		boolean sigIt=false;
		
		int estado = 0;
		
	    for (int i = 0; i < analizar.length; i++) 
	    {
			boolean permitido=false;
			
	    	int index=0;
	    	for (int j = 0; j < simb.length; j++) {
	    		
				if(simb[j]==analizar[i]){
					index=j;
					permitido=true;
				}
			}
	    	if(!permitido) {
	    		System.out.println("Se ingreso un no permitido");
	    		estado=0;
	    		sigIt=false; //Esto tambien
	    		contador=i+1; //Acuerdate que agregaste esto para arreglar el problema de a*
	    		continue;
	    	}
	    	//System.out.println(i);
	        estado = tablaEst[estado][index]; 
	        
//	        System.out.println("E:" +estado);
	        
	        if(pastState>estado) {
	        	contador+=2;
	        	System.out.println("in");
	        }
	        
	        if(sigIt || (pastState==1 && estado>1)) {
	        	contador=i-1;
	        	sigIt=false;
	        }
	        
	        if(estado==tablaEst.length-1) {
	        	sigIt=true;
	        }
	        
	        pastState=estado;
	        System.out.println(contador);
	        
	        if (estado == pat.length || (ultEsKleene && estado==pat.length-1)) {
	        	this.indices.add(new Point(contador,i));
	        	if(this.indices.size()-2>=0) {
	        		
		        	if(this.indices.get(this.indices.size()-1).x==this.indices.get(this.indices.size()-2).x &&
		        			this.indices.get(this.indices.size()-2).y==this.indices.get(this.indices.size()-1).y-1) {
		        		//System.out.println("hi");
		        		this.indices.remove(this.indices.size()-2);
		        		
		        	}else if(this.indices.get(this.indices.size()-1).x==this.indices.get(this.indices.size()-2).y) {
		        		
		        			int tmpX=this.indices.get(this.indices.size()-2).x,
		        				tmpY=this.indices.get(this.indices.size()-1).y;
							this.indices.remove(this.indices.size()-1);
							this.indices.remove(this.indices.size()-1);
							this.indices.add(new Point(tmpX,tmpY));
							//System.out.println("hi2");
		        	}
	        	}
	        	//System.out.println(this.indices.size());
	        	//System.out.println(i);
//	        	if(i-2>-1) {
//	        		System.out.println("in");
//		        	if(this.indices.get(i-2-reductor).x==this.indices.get(i-1-reductor).x &&
//		        			this.indices.get(i-2-reductor).y==this.indices.get(i-1-reductor).y-1) {
//						this.indices.remove(i-2-reductor);
//						System.out.println("hi");
//						reductor++;
//					}else if(this.indices.get(i-2-reductor).y==this.indices.get(i-1-reductor).x) {
//						int tmpX=this.indices.get(i-2-reductor).x,
//							tmpY=this.indices.get(i-1-reductor).y;
//						this.indices.remove(i-2-reductor);
//						this.indices.add(i-2-reductor, new Point(tmpX,tmpY));
//						System.out.println("hi2");
//						reductor++;
//					}
//	        	}
        		//this.indices.add(new Point(contador,i));
	        	System.out.println("Patrón encontrado de (" + contador + " a " + i +")");
	        }
	        
	    }

	}
	
	private void reemplazador(LinkedList<Point> indices, char[] simbolo) {
//		while (!indices.isEmpty()) {
//			Point tmp= indices.poll();
//			System.out.println("("+tmp.x+", "+tmp.y+")");
//			
//		}
		LinkedList<Character> cadena=new LinkedList<>();
		
		LinkedList<Integer> donde=new LinkedList<>();
		
		char[] simboloR=new char[simbolo.length];
		for (int i = 0; i < simboloR.length; i++) {
			simboloR[i]=simbolo[simbolo.length-1-i];
		}
		
		for (char c : this.analizar) {
			cadena.add(c);
		}
		//System.out.println(simboloR);
		int cambioIndice=0;
		while (!indices.isEmpty()) {
			Point puntoTmp=indices.poll();
			System.out.println(puntoTmp.x);
			if(puntoTmp.x-cambioIndice>=0) {
				puntoTmp=new Point(puntoTmp.x-cambioIndice,puntoTmp.y-cambioIndice);
			}else {
				puntoTmp=new Point(0,puntoTmp.y-cambioIndice);
			}
			
			//System.out.println(puntoTmp.x);
			//System.out.println(cambioIndice);
			System.out.println("("+puntoTmp.x+", "+puntoTmp.y+")");
			for (int i = puntoTmp.x; i < puntoTmp.y+1; i++) {
				cambioIndice++;
				cadena.remove(puntoTmp.x);
			}
			donde.add(puntoTmp.x);
//			for (int i = 0; i < simboloR.length; i++) {
//				cadena.add(puntoTmp.x,simboloR[i]);
//			}
			
		}
		
		cambioIndice=0;
		while (!donde.isEmpty()) {
			int indx=donde.poll()+cambioIndice;
			for (int i = 0; i < simboloR.length; i++) {
				cadena.add(indx,simboloR[i]);
				cambioIndice++;
			}
			
		}
		
		char[] nueva=new char[cadena.size()];
		for (int i = 0; i < nueva.length; i++) {
			nueva[i]=cadena.poll();
		}
		
		this.analizar=nueva;
		System.out.println(this.analizar);
//		while (!cadena.isEmpty()) {
//			System.out.print(cadena.poll());
//			
//		}
//		while (!indices.isEmpty()) {
//			int in=indices.poll(),
//				fin=indices.poll();
//			System.out.println("("+in+", "+fin+")");
//			for (int i = 0; i <= fin-in; i++) {
//				System.out.println(tmp.get(in+i));
//			}
//			
//		}
	}
	
	public char[] getFinal() {
		return this.analizar;
	}
	
	public static void main(String[] args) {
		
//		char[] analizar="abaaaabaaanbbb".toCharArray();
//		Automata aut=new Automata("aba*+bbb*", analizar,"ccc".toCharArray());
		
		
//		char[] pat = "ACB*A".toCharArray();
//		Queue<Integer> op = procesarOperadores(pat);
//		pat=limpiarPatron(pat);
//		char[] simb=encontrarSimbolos(pat);
//		
//		
//		//System.out.println(op.poll());
//		int[][] tablaEst = new int[pat.length+1][simb.length];
//		crearTablaEst(pat, simb, tablaEst,op);
//		char[] analizar = "ACBBBBBBBBBBBBBBBBBBACBBBBBBA".toCharArray(); 
//		
//		//-----
//		int contador=0,
//			pastState=0;
//		boolean sigIt=false;
//		
//		int estado = 0;
//        for (int i = 0; i < analizar.length; i++) 
//        {
//        	int index=0;
//        	for (int j = 0; j < simb.length; j++) {
//        		
//				if(simb[j]==analizar[i]){
//					index=j;
//				}
//			}
//            estado = tablaEst[estado][index]; 
//            
//            //System.out.println(state);
//            
//            if(sigIt || (pastState==1 && estado>1)) {
//            	contador=i-1;
//            	sigIt=false;
//            }
//            
//            if(estado==tablaEst.length-1) {
//            	sigIt=true;
//            }
//            
//            pastState=estado;
//            
//            if (estado == pat.length || (ultEsKleene && estado==pat.length-1)) {
//            	System.out.println("Puntero f: "+i);
//            	System.out.println("Puntero i: "+contador);
//            	System.out.println("Pattern found " + "at index " + (i));
//            }
//            
//        } 
	}

}