# Rutas a los archivos fuente del código principal (sin cambios)
MAIN_PATH = \
	./FONTS/src/main/Dominio/Excepciones/*.java \
	./FONTS/src/main/Dominio/*.java \
	./FONTS/src/main/Dominio/Modelos/*.java \
	./FONTS/src/main/Presentacion/* \
	./FONTS/src/main/Persistencia/*.java \

# Directorios de salida
CLASS_OUTPUT_MAIN = ./EXE/main
CLASS_OUTPUT_TEST = ./EXE/test
DRIVERS_PATH = ./FONTS/src/main/Presentacion/Drivers/*.java 


# Path to the JSON library
JSON_JAR = ./FONTS/src/main/Persistencia/json-20231013.jar

# Compilar el código principal y empaquetar en un JAR
code:
	make clean
	javac -Xlint:deprecation -cp ./FONTS/src/main/Persistencia/json-20231013.jar -d ./EXE/main \
	    ./FONTS/src/main/Dominio/Excepciones/*.java \
	    ./FONTS/src/main/Dominio/*.java \
		./FONTS/src/main/*.java \
	    ./FONTS/src/main/Dominio/Modelos/*.java \
		./FONTS/src/main/Presentacion/*.java \
		./FONTS/src/main/Presentacion/Vistas/*.java \
		./FONTS/src/main/Presentacion/Drivers/*.java \
	    ./FONTS/src/main/Persistencia/*.java \
		./FONTS/src/main/Persistencia/Gestores/Utilidades/*.java \
		./FONTS/src/main/Persistencia/Gestores/*.java
	jar cf $(CLASS_OUTPUT_MAIN)/Driver.jar -C $(CLASS_OUTPUT_MAIN) .
	jar cf $(CLASS_OUTPUT_MAIN)/Main.jar -C $(CLASS_OUTPUT_MAIN) .
	
runcode_scrabble:
	java -cp $(CLASS_OUTPUT_MAIN)/Driver.jar:./FONTS/src/main/Persistencia/json-20231013.jar Presentacion.Drivers.Driver

runcode_main:
	java -Xmx1024m -cp $(CLASS_OUTPUT_MAIN)/Main.jar:./FONTS/src/main/Persistencia/json-20231013.jar main.Main

runcode_game:
	java -cp $(CLASS_OUTPUT_MAIN)/UITablero.jar Presentacion.Vistas.VistaScrabble


# Regla para limpiar el directorio EXE (tanto main como test)
clean:
	rm -rf $(CLASS_OUTPUT_MAIN)/* $(CLASS_OUTPUT_TEST)/*


# regla para generar la documentación de JavaDoc
javadoc:
	javadoc -d DOCS/javadoc -sourcepath FONTS/src/main -classpath "FONTS/src/main/Persistencia/json-20231013.jar" Dominio Dominio.Excepciones Dominio.Modelos Persistencia Persistencia.Gestores Persistencia.Gestores.Utilidades