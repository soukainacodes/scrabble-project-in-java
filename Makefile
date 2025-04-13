MAIN_PATH =	./FONTS/src/main/Dominio/*.java \
				./FONTS/src/main/Dominio/Controladores/Modelos/Partida/Dawg/*.java \
				./FONTS/src/main/Dominio/Controladores/Modelos/Partida/Bolsa/*.java \
				./FONTS/src/main/Dominio/Controladores/Modelos/Partida/Tablero/*.java \
				./FONTS/src/main/Dominio/Controladores/Modelos/Jugador/*.java \
				./FONTS/src/main/Dominio/Controladores/Modelos/Partida/*.java \
				./FONTS/src/main/Dominio/Controladores/Modelos/*.java \
				./FONTS/src/main/Dominio/Controladores/Modelos/Ranking/*.java \
				./FONTS/src/main/Dominio/Controladores/*.java \
				./FONTS/src/main/Dominio/Controladores/Excepciones/*.java \
				./FONTS/src/main/Presentacion/Drivers/*.java \
				
CLASS_OUTPUT =	./EXE/
	
code:
	javac -d $(CLASS_OUTPUT) $(MAIN_PATH)