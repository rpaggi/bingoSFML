import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;
import org.jsfml.window.event.Event;

public class Gameplay {
	public static void main(String[] args) {
		//Variavel temporaria
		String bin_folder = "";
		
		//Cria a janela
		RenderWindow window = new RenderWindow();
		window.create(new VideoMode(1280,768), "Bingo do Barão", WindowStyle.RESIZE);
		window.setFramerateLimit(30);
		
		//Carrega o background
		Sprite backgroud_s = new Sprite();
		Texture background_t = new Texture();
		try {
			background_t.loadFromFile(Paths.get("background.png"));
		} catch (IOException e) {
			System.out.println("Erro ao carregar a textura do background");
			e.printStackTrace();
		}
		backgroud_s.setTexture(background_t);
		
		//Cria a fonte
		Font verdana = new Font();
		try {
		    verdana.loadFromFile(Paths.get("verdanab.ttf"));
		} catch(IOException ex) {
		    System.out.println("Falha ao carregar a fonte");
		    ex.printStackTrace();
		}
		
		//Numeros
		int tX = 0, tY = 0, flag = 0;
		
		List<Text> numeros = new ArrayList<Text>();
		for(int i=0;i<75;i++){
			Text nTemp = new Text(Integer.toString(i+1), verdana, 40);
			nTemp.setColor(new Color(38,9,49));
			if((i+1)%15 != 0){
				if(i+1 < 10){
					nTemp.setPosition(301+75*tX,170+66*tY);
				}else{
					nTemp.setPosition(285+75*tX,170+66*tY);
				}
				if(flag == 1){
					tX--;
					tY++;
					flag = 0;
				}else{
					tX++;
					flag = 1;
				}
			}else{
				nTemp.setPosition(330+75*tX-1,665);
				tY=0;
				tX+=2;
			}
			numeros.add(nTemp);
		}
		
		//Vetor de controle do tabuleiro
		int controle_tabuleiro[] = new int[75];
		//Inicializa controle do tabuleiro
		for(int i=0;i<75;i++){
			controle_tabuleiro[i]=0;
		}
		
		Engine engine = new Engine();
		int numero_temp = 0;
		boolean space_lock = false;
		
		//Sprite da Bola
		Texture ball_t = new Texture();
		Sprite  ball_s = new Sprite();
		try {
			ball_t.loadFromFile(Paths.get("ball.png"));
		} catch (IOException e) {
			System.out.println("Erro ao carregar a textura da bola");
			e.printStackTrace();
		}
		ball_s.setTexture(ball_t);
		ball_s.setPosition(340,84);
		
		//Efeito sonoro da bola
		SoundBuffer ball_b_sound = new SoundBuffer();
		try {
		    ball_b_sound.loadFromFile(Paths.get("cash_sound.wav"));
		} catch(IOException ex) {
		    //Something went wrong
		    System.err.println("Erro para carregar o som da bola");
		    ex.printStackTrace();
		}
		Sound ball_sound = new Sound();
		ball_sound.setBuffer(ball_b_sound);
		
		//Relogio para controlar o tempo
		Clock clock = new Clock();
		Time time;
		
		//Laço do jogo
		while(window.isOpen()){
			time = clock.getElapsedTime();
			
			//Desenha os elementos
			window.clear();
			window.draw(backgroud_s);
			for(int i=0;i<75;i++){
				if(controle_tabuleiro[i] == 1){
					window.draw(numeros.get(i));
				}
			}
			
			if(space_lock){
				Text numero_text = new Text(Integer.toString(numero_temp), verdana, 250);
				String str_letter = "";
				if(numero_temp<=15){
					str_letter = "B";
					ball_s.setColor(new Color(255,255,255));
				}else if(numero_temp>15 && numero_temp<=30){
					str_letter = "I";
					ball_s.setColor(new Color(0,255,0));
				}else if(numero_temp>30 && numero_temp<=45){
					str_letter = "N";
					ball_s.setColor(new Color(0,200,255));
				}else if(numero_temp>45 && numero_temp<=60){
					str_letter = "G";
					ball_s.setColor(new Color(96,50,255));
				}else{
					str_letter = "O";
					ball_s.setColor(new Color(255,255,0));
				}
				
				Text letter = new Text(str_letter, verdana, 100);
				letter.setPosition(610,155);
				
				if(numero_temp<10){
					numero_text.setPosition(550,225);
				}else{
					numero_text.setPosition(465,225);
				}
				
				window.draw(ball_s);
				window.draw(numero_text);
				window.draw(letter);
				
				if(time.asSeconds() > 4){
					space_lock = false;
					controle_tabuleiro[numero_temp-1] = 1;
					clock.restart();
				}
			}

			window.display();
			
			//Verifica os eventos na tela
	    	for (Event event : window.pollEvents()) {
	        	if (event.type == Event.Type.KEY_PRESSED){
	        		if(event.asKeyEvent().key == Keyboard.Key.ESCAPE){
	        			window.close();
	        		}
	        		
	        		if(event.asKeyEvent().key == Keyboard.Key.DELETE){
	        			engine.resetGame();
	        			for(int i=0;i<75;i++){
	        				controle_tabuleiro[i] = 0;
	        			}
	        		}
	        		
	        		if(event.asKeyEvent().key == Keyboard.Key.SPACE && space_lock == false){
	        			numero_temp = engine.getNumber();
	        			if(numero_temp > 0){
	        				clock.restart();
	        				ball_sound.play();
	        				space_lock = true;
	        			}
	        		}
	        	}
	        	if (event.type == Event.Type.CLOSED){
	        		window.close();
	        	}
	    	}
		}
	}
}
