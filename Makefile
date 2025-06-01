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
	javac -cp ./FONTS/src/main/Persistencia/json-20231013.jar -d ./EXE/main \
	    ./FONTS/src/main/Dominio/Excepciones/*.java \
	    ./FONTS/src/main/Dominio/*.java \
		./FONTS/src/main/*.java \
	    ./FONTS/src/main/Dominio/Modelos/*.java \
		./FONTS/src/main/Presentacion/*.java \
		./FONTS/src/main/Presentacion/Vistas/*.java \
	    ./FONTS/src/main/Persistencia/*.java \
		./FONTS/src/main/Persistencia/Gestores/Utilidades/*.java \
		./FONTS/src/main/Persistencia/Gestores/*.java
	jar cf $(CLASS_OUTPUT_MAIN)/Main.jar -C $(CLASS_OUTPUT_MAIN) .
	
ifeq ($(OS),Windows_NT)
    CPSEP := ;
else
    CPSEP := :
endif

runcode_main:
	@java -cp "$(CLASS_OUTPUT_MAIN)/Main.jar$(CPSEP)$(JSON_JAR)" main.Main



# Regla para limpiar el directorio EXE (tanto main como test)
clean:
	rm -rf "$(CLASS_OUTPUT_MAIN)"/* "$(CLASS_OUTPUT_TEST)"/*



# regla para generar la documentación de JavaDoc
javadoc:
	javadoc -d DOCS/javadoc -sourcepath FONTS/src/main -classpath "FONTS/src/main/Persistencia/json-20231013.jar" Dominio Dominio.Excepciones Dominio.Modelos Persistencia Persistencia.Gestores Persistencia.Gestores.Utilidades Presentacion.Vistas Presentacion