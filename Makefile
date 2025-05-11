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
# Ruta a los archivos de test (ajústala si es necesario)
TEST_PATH = ./FONTS/src/test/testsUnitarios/*.java

# Rutas a las librerías de JUnit y Hamcrest
JUNIT_JAR = ./FONTS/src/test/lib/junit-4.12.jar
HAMCREST_JAR = ./FONTS/src/test/lib/hamcrest-core-1.3.jar

# Rutas a las librerías de Mockito y sus dependencias
MOCKITO_JAR = ./FONTS/src/test/lib/mockito-core-4.9.0.jar
BYTE_BUDDY_JAR = ./FONTS/src/test/lib/byte-buddy-1.12.16.jar
BYTE_BUDDY_AGENT_JAR = ./FONTS/src/test/lib/byte-buddy-agent-1.12.16.jar
OBJENESIS_JAR = ./FONTS/src/test/lib/objenesis-3.3.jar

# Agrupar todas las dependencias en una variable (para Unix; en Windows usa ; como separador)
LIBS = $(JUNIT_JAR):$(HAMCREST_JAR):$(MOCKITO_JAR):$(BYTE_BUDDY_JAR):$(BYTE_BUDDY_AGENT_JAR):$(OBJENESIS_JAR)


CLASSPATH=.:FONTS/src/main/Persistencia/json-20231013.jar

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
		./FONTS/src/main/Presentacion/Drivers/*.java \
        ./FONTS/src/main/Persistencia/*.java
	jar cf $(CLASS_OUTPUT_MAIN)/Driver.jar -C $(CLASS_OUTPUT_MAIN) .
	jar cf $(CLASS_OUTPUT_MAIN)/Main.jar -C $(CLASS_OUTPUT_MAIN) .
	jar cf $(CLASS_OUTPUT_MAIN)/ListarFuentes.jar -C $(CLASS_OUTPUT_MAIN) .

code_drivers:
	javac -d $(CLASS_OUTPUT_MAIN) $(MAIN_PATH)
	javac -d $(CLASS_OUTPUT_MAIN) $(DRIVERS_PATH)
	jar cf $(CLASS_OUTPUT_MAIN)/DriverGestionDiccionariosBolsas.jar -C $(CLASS_OUTPUT_MAIN) . 
	jar cf $(CLASS_OUTPUT_MAIN)/DriverGestionPartidas.jar -C $(CLASS_OUTPUT_MAIN) . 
	jar cf $(CLASS_OUTPUT_MAIN)/DriverGestionJuego.jar -C $(CLASS_OUTPUT_MAIN) . 
	jar cf $(CLASS_OUTPUT_MAIN)/DriverGestionUsuarios.jar -C $(CLASS_OUTPUT_MAIN) .
	
runcode_scrabble:
	java -cp $(CLASS_OUTPUT_MAIN)/Driver.jar:./FONTS/src/main/Persistencia/json-20231013.jar Presentacion.Drivers.Driver

runcode_juego:
	java -cp $(CLASS_OUTPUT_MAIN)/DriverGestionJuego.jar  Presentacion.Drivers.DriverGestionJuego
runcode_main:
	java -cp $(CLASS_OUTPUT_MAIN)/Main.jar  main.Main
lista:
	java -cp $(CLASS_OUTPUT_MAIN)/ListarFuentes.jar  main.ListarFuentes
runcode_usuarios:
	java -cp $(CLASS_OUTPUT_MAIN)/DriverGestionUsuarios.jar Presentacion.Drivers.DriverGestionUsuarios

runcode_partidas:
	java -cp $(CLASS_OUTPUT_MAIN)/DriverGestionPartidas.jar Presentacion.Drivers.DriverGestionPartidas

runcode_diccionarios_bolsas:
	java -cp $(CLASS_OUTPUT_MAIN)/DriverGestionDiccionariosBolsas.jar Presentacion.Drivers.DriverGestionDiccionariosBolsas


# Objetivo para compilar los tests.
# NOTA: Se compila directamente en $(CLASS_OUTPUT_TEST) para que, al tener 
# el package "testsUnitarios" en tus tests, se genere la carpeta correspondiente automáticamente.
test:
	mkdir -p $(CLASS_OUTPUT_TEST)
	javac -d $(CLASS_OUTPUT_TEST) -cp $(CLASS_OUTPUT_MAIN):$(LIBS) $(TEST_PATH)


# Objetivo para ejecutar un test unitarios de JUnit.

runTestFicha:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.FichaTest

runTestBolsa:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.BolsaTest

runTestCelda:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.CeldaTest

runTestTablero:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.TableroTest
runTestJugador:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.JugadorTest

runTestTipoBonificacion:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.TipoBonificacionTest

runTestDawg:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.DawgTest

runTestPair:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.PairTest

runTestNodo:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.NodoTest


runTestPartida:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.PartidaTest

runTestValidador:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.ValidadorTest


runTestAlgoritmo:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.AlgoritmoTest

# Regla para limpiar el directorio EXE (tanto main como test)
clean:
	rm -rf $(CLASS_OUTPUT_MAIN)/* $(CLASS_OUTPUT_TEST)/*


# Se ejecutan todos los tests unitarios en una sola línea
runAllUnitTests: test \
                 runTestFicha runTestBolsa runTestCelda runTestTablero \
                 runTestJugador runTestTipoBonificacion runTestDawg \
                 runTestPair runTestNodo runTestPartida \
                 runTestValidador runTestAlgoritmo
	@echo
	@echo "──────────────────────────────────────────────────────────"
	@echo " ;)  Todos los tests unitarios han terminado"
	@echo "──────────────────────────────────────────────────────────"


# regla para generar la documentación de JavaDoc
javadoc:
	javadoc   -d DOCS/javadoc   -sourcepath FONTS/src/main   Dominio   Dominio.Excepciones   Dominio.Modelos   Persistencia   Presentacion.Drivers