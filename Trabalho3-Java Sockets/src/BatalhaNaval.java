import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {

	public static Scanner scan =  new Scanner(System.in);
	static int Colunas,Linhas;
	static int TotalNavios;
	static int vitorias,derrotas,abandonado=0;
	static int totalshotsUsed;
	static int gameCount=0 ;

	static char[][] tabuleiro;
	static final char Agua = '.';
	static final char Hit = 'X';
	static final char Miss = 'O';
	static final char Navio = 'S';
	static ArrayList<String> Resultados = new ArrayList<>();
	


	public static void main(String[] args) {

		int opcao ;

		boolean gameStarted = false;
		while (!gameStarted) {
			System.out.println("Battleship Menu:");
			System.out.println("1) Jogar uma nova partida");
			System.out.println("2) Historico de Partidas");
			System.out.println("3) Sair do jogo");
			System.out.print("Escolha a opção: ");



			while(!scan.hasNextInt()){
				System.out.println();
				System.out.println("------------------------");
				System.out.println();
				System.out.println("Escolha invalida, Por favor repita: ");
				System.out.println("Battleship Menu:");
				System.out.println("1) Jogar uma nova partida");
				System.out.println("2) Historico de Partidas");
				System.out.println("3) Sair do jogo");
				System.out.print("Escolha a opção: ");
				scan.next();
			}

			opcao = scan.nextInt();


			switch (opcao) {
			case 1:
				boolean playAgain = true; 
				gameStarted = true;
				boolean shipsPlaced = false; // condicao para continuar. Os navios tem que ser colocados em 100 t para ser true
				ArrayList<Integer> shipList = new ArrayList<>(); //Lista onde guarda os navios gerados
				
				while (playAgain) {
					
				
				while(!shipsPlaced) {
				dimensoes();
				tabuleiro = tabuleiroInicial();
				shipList = GerarNavios(TotalNavios);
				shipsPlaced = placeShips(tabuleiro, shipList, Linhas, Colunas);
				}
			
				int totalShots = NumeroShots(shipList);
				char[][] tabuleiroJogador = tabuleiroInicial();
				playGame(tabuleiro, tabuleiroJogador, totalShots, shipList);
				shipsPlaced = false; // para repetir o input das dimensoes e gerar novos navios
			    playAgain = rematch(); 
                
				}//Loop do rematch
				
				if (!playAgain) {
		            gameStarted = false; // Reset a variavel gameStarted 
		        }

				break;

			case 2:
				System.out.println("---------------------");
				System.out.println("Historico de partidas ");
				Historico();
				
				double winPercentage = ((double) vitorias / (double) gameCount) * 100;
			    double losePercentage = ((double) derrotas / (double) gameCount) * 100;
			    double averageShotsPerGame = ((double) totalshotsUsed / (double) gameCount);

			    System.out.println("\nEstatísticas:");
			    System.out.printf("Percentagem de vitórias: %.2f%%\n", winPercentage);
			    System.out.printf("Percentagem de derrotas: %.2f%%\n", losePercentage);
			    System.out.printf("Média de tiros por jogo: %.2f\n", averageShotsPerGame);
				
				System.out.println("---------------------");
				break;
			case 3:
				System.out.println("Sair do jogo, Adeus!");
				System.exit(0);
				break;
			default:
				System.out.println();
				System.out.println("------------------------");
				System.out.println();
				System.out.println("Escolha invalida, Por favor repita: ");
				break;
			}
		}


	} // fim do metodo main


	public static void dimensoes() {
		Colunas=0;
		System.out.println("Introduza um número (entre 15 e 30) de colunas:");
		while(Colunas<15 || Colunas>30) {
			while (!scan.hasNextInt()) {

				scan.next();
				System.out.println("Apenas numeros inteiros são validos\nPor favor repita: ");
			}
			Colunas  = scan.nextInt();
			if (Colunas<15 || Colunas>30) {
				System.out.println("O numero tem que estar entre 15 e 30\nPor favor repita: ");}

		}
		Linhas=0;
		System.out.println("Introduza um numero (entre 15 e 30) de Linhas:");
		while(Linhas<15 || Linhas>30) {
			while (!scan.hasNextInt()) {
				scan.next();
				System.out.println("Apenas numeros inteiros são validos\nPor favor repita: ");
			}
			Linhas  = scan.nextInt();
			if (Linhas<15 || Linhas>30) {
				System.out.println("O numero tem que estar entre 15 e 30\nPor favor repita: ");}

		}


		TotalNavios=0;
		System.out.println("Introduza o numero de navios (minimo 3):");
		while(TotalNavios<3) {

			while (!scan.hasNextInt()) {

				scan.next();
				System.out.println("Apenas numeros inteiros são validos\nPor favor repita: ");
			}
			TotalNavios  = scan.nextInt();
			if (TotalNavios<3) {
				System.out.println("Numero minimo 3\nPor favor repita: ");}
		}


	}// fim do metodo dimensoes
	
	
	
	// cria o tabuleiro onde vao ser colocados os navios o jogador so ve este tabuleiro no final
	private static char[][] tabuleiroInicial() { 
        char[][] board = new char[Linhas][Colunas];

        for (int i = 0; i < Linhas; i++) {
            for (int j = 0; j < Colunas; j++) {
                board[i][j] = Agua;
            }
        }

        return board;
    }
	
	
	public static void printBoard(char[][] tabuleiro) {
	    System.out.print("  ");
	    for (int col = 0; col < Colunas; col++) {
	        System.out.print(col + " ");
	    }
	    System.out.println();

	    for (int row = 0; row < Linhas; row++) {
	        System.out.print(row + "  ");
	        for (int col = 0; col < Colunas; col++) {
	            System.out.print(tabuleiro[row][col] + " ");
	        }
	        System.out.println();
	    }
	}

	public static ArrayList<Integer> GerarNavios (int TotalNavios){

		ArrayList<Integer> ListaNavios = new ArrayList<>();

		// Acrescenta os 3 primeiros navios
		// 1 de cada tamanho
		ListaNavios.add(1);
		ListaNavios.add(2);
		ListaNavios.add(4);
		// Add remaining ships with random sizes
		Random random = new Random();
		for (int i = 3; i < TotalNavios; i++) {
			int randomSize = random.nextInt(3);
			switch (randomSize) {
			case 0:
				ListaNavios.add(1);
				break;
			case 1:
				ListaNavios.add(2);
				break;
			case 2:
				ListaNavios.add(4);
				break;
			}
		}
		System.out.println("Array 1:" + ListaNavios);

		return ListaNavios;	

	}
	
	public static int NumeroShots(ArrayList<Integer> shipList) {
        int minShots = 0;
        for (int navio : shipList) { // pega nos elementos da lista de navios e soma todos
            minShots += navio;
        }

        int totalShots = 0;
        System.out.println("Introduza o numero de shots (minimo " + minShots + "):");
        while (totalShots < minShots) {
            while (!scan.hasNextInt()) {
                scan.next();
                System.out.println("Apenas numeros inteiros\nPor favor repita: ");
            }
            totalShots = scan.nextInt();
            if (totalShots < minShots) {
                System.out.println("Numero minimo: " + minShots + "\nPor favor repita: ");
            }
        }
        //System.out.println(totalShots);
        return totalShots;
    }
	
	public static boolean placeShips(char[][] tabuleiro, ArrayList<Integer> shipList, int nLinhas, int nColunas) {
	    Random rand = new Random();
	    int t = 0; // tentativas para posicionar os navios
	    int maxt = 100;
	    
	    for (int navio : shipList) {
	        boolean placed = false;
	        while (!placed && t< maxt) {
	            int row = rand.nextInt(nLinhas);
	            int col = rand.nextInt(nColunas);
	            int direction = rand.nextInt(2); // 0: horizontal, 1: vertical

	            if (direction == 0) { // horizontal
	                if (col + navio <= nColunas) {
	                    boolean canPlace = true;
	                    for (int i = 0; i < navio; i++) {
	                        if (tabuleiro[row][col + i] != '.') {
	                            canPlace = false;
	                            break;
	                        }
	                    }

	                    if (canPlace) {
	                        for (int i = 0; i < navio; i++) {
	                            tabuleiro[row][col + i] = 'S';
	                        }
	                        placed = true;
	                    }
	                }
	            } else { // vertical
	                if (row + navio <= nLinhas) {
	                    boolean canPlace = true;
	                    for (int i = 0; i < navio; i++) {
	                        if (tabuleiro[row + i][col] != '.') {
	                            canPlace = false;
	                            break;
	                        }
	                    }

	                    if (canPlace) {
	                        for (int i = 0; i < navio; i++) {
	                            tabuleiro[row + i][col] = 'S';
	                        }
	                        placed = true;
	                    }
	                }
	            }

	            t++;
	        }
	        if (t >= maxt) {
	        	System.out.println();
	        	System.out.println("Não foi possivel colocar os todos os navios\nPor favor coloque novos valores de configuração");
	            return false;
	        }
	    }
	    return true;
	}
	
	
	
	
	public static void playGame(char[][] tabuleiro, char[][] tabuleiroJogador, int totalShots, ArrayList<Integer> shipList) {
	    int MinShots = 0; // soma todos os navios para depois comparar com os hits
	    for (int navio : shipList) {
	    	MinShots += navio;
	    }
	    int initialShots = totalShots; // totalShots escolhidos pelo user
	    int totalHits = 0;
	    boolean userQuit = false;

	    while (totalShots > 0 && totalHits < MinShots && !userQuit) {
	        printBoard(tabuleiroJogador); // tabuleiro que o user vê
	        System.out.println("--------------------");
	        System.out.println("Tentativas restantes: " + totalShots);
	        
	        int row =-1; 
            int col =-1;
            
            while (row==-1) {
            	 System.out.println("Introduza a linha ('quit' or 'exit' to stop the game): ");
                 if (scan.hasNextInt()) {
                     row = scan.nextInt();
                 } else {
                     String userInput = scan.next();
                     if (userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("exit")) {
                         userQuit = true;
                         break;
                     } else {
                         System.out.println("Input invalido, Por favor repita:");
                     }
                 }
             }

             if (userQuit) {
                 break;
             }
             while (col==-1) {
            	 System.out.println("Introduza a Coluna ('quit' or 'exit' to stop the game): ");
                 if (scan.hasNextInt()) {
                     col = scan.nextInt();
                 } else {
                     String userInput = scan.next();
                     if (userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("exit")) {
                         userQuit = true;
                         break;
                     } else {
                         System.out.println("Input invalido, Por favor repita:");
                     }
                 }
             }
             if (userQuit) {
                 break;
             }

	            if (row < 0 || row >= Linhas || col < 0 || col >= Colunas) {
	                System.out.println("Coordenada invalida, Por favor repita");
	                continue;
	            }

	            if (tabuleiroJogador[row][col] != '.') {
	                System.out.println("Ja tentou esta coordenada.Por favor repita:");
	                continue;
	            }

	            if (tabuleiro[row][col] == 'S') {
	                tabuleiroJogador[row][col] = Hit;
	                tabuleiro[row][col] = Hit;

	                totalHits++; // Incrementa o hits totais

	                System.out.println("Hit!");
	            } else {
	                tabuleiroJogador[row][col] = Miss;
	                System.out.println("Miss!");
	            }
	            totalShots--;
	    } // fim do loop do jogo
	    
	     String result;
	    if (totalHits == MinShots) {
	        System.out.println("Parabens!! Afundou todos os navios");
	        result = "Ganhou";
	        vitorias++;
	        
	    } else if (userQuit) {
	        System.out.println("Abandonou o jogo, Adeus!!");
	        printBoard(tabuleiro);
	        result = "Abandonado";
	        abandonado++;
	    } else {
	        System.out.println("Perdeu o jogo, Adeus!!");
	        printBoard(tabuleiro);
	        result = "Perdeu";
	        derrotas++;
	        
	        
	    }
	    int shotsUsed = initialShots - totalShots;
        Resultados.add(result + " - Shots used: " + shotsUsed);
        totalshotsUsed = totalshotsUsed + shotsUsed;
        gameCount++;
	}
	
	
	 public static void Historico() {
	       int game = 1;
	        for (String res : Resultados) {
	            System.out.println("Jogo " + game + ": " + res);
	            game++;
	        }
	        }
	

	   
	   public static boolean rematch() {
		   	System.out.println();
		   	
		    System.out.println("1) Para jogar novamente, qualquer outra tecla para voltar ao menu.");
		    int rematchChoice;
		    if (scan.hasNextInt()) {
		        rematchChoice = scan.nextInt();
		        if (rematchChoice == 1) {
		            return true;
		        }
		    } else {
		        scan.next();
		        
		    }
		    return false;
		}

}//fim da class

