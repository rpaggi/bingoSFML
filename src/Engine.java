import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Engine {
	private List tabuleiro;
	
	public Engine(){
		//Inicializa tabuleiro
		tabuleiro = new ArrayList();
		for(int i=0;i<75;i++){
			tabuleiro.add(Integer.valueOf(i+1));
		}
	}
	
	public void resetGame(){
		//Reinicia tabuleiro
		tabuleiro = new ArrayList();
		for(int i=0;i<75;i++){
			tabuleiro.add(Integer.valueOf(i+1));
		}
	}
	
	public int getNumber(){
		if(tabuleiro.size()>0){
			//Cria variavel randomica
			Random r = new Random();
			
			//Pega numero aleatório de 0 a tamanho atual do tabuleiro
			int posicao = r.nextInt(tabuleiro.size());
			
			//Pega numero na posicão sorteada
			Integer retorno = (Integer) tabuleiro.get(posicao);
			
			//Remove posição sorteada
			tabuleiro.remove(posicao);
			
			//Devolve numero escolhido
			return retorno.intValue();
		}else{
			return -1;
		}
		
	}
}
