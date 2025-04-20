# Rutas a los archivos fuente del código principal (sin cambios)
MAIN_PATH = \
	./FONTS/src/main/Dominio/Excepciones/*.java \
    ./FONTS/src/main/Dominio/*.java \
	./FONTS/src/main/Dominio/Modelos/*.java \
	./FONTS/src/main/Presentacion/Drivers/*.java \
	./FONTS/src/main/Persistencia/*.java \

# Directorios de salida
CLASS_OUTPUT_MAIN = ./EXE/main
CLASS_OUTPUT_TEST = ./EXE/test

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

# Objetivo para compilar el código principal (no se toca)
code:
	javac -d $(CLASS_OUTPUT_MAIN) $(MAIN_PATH)

runcode_solo:
	java -cp $(CLASS_OUTPUT_MAIN) Presentacion.Drivers.DriverPartidaAlgoritmo

runcode_duo:
	java -cp $(CLASS_OUTPUT_MAIN) Presentacion.Drivers.DriverPartidaCon2Jugadores

runcode_usuarios:
	java -cp $(CLASS_OUTPUT_MAIN) Presentacion.Drivers.DriverGestionUsuarios

runcode_partidas:
	java -cp $(CLASS_OUTPUT_MAIN) Presentacion.Drivers.DriverGestionPartidas


# Objetivo para compilar los tests.
# NOTA: Se compila directamente en $(CLASS_OUTPUT_TEST) para que, al tener 
# el package "testsUnitarios" en tus tests, se genere la carpeta correspondiente automáticamente.
test:
	mkdir -p $(CLASS_OUTPUT_TEST)
	javac -d $(CLASS_OUTPUT_TEST) -cp $(CLASS_OUTPUT_MAIN):$(LIBS) $(TEST_PATH)

# Objetivo para ejecutar el test.
# Se asume que FichaTest.java declara el paquete "testsUnitarios" y está preparado para ser ejecutado.
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


runTestRanking:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.RankingTest

runTestCtrlRanking:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(LIBS) org.junit.runner.JUnitCore testsUnitarios.CtrlRankingTest


# Regla para limpiar el directorio EXE (tanto main como test)
clean:
	rm -rf $(CLASS_OUTPUT_MAIN)/* $(CLASS_OUTPUT_TEST)/*
