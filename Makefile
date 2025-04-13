# Rutas a los archivos fuente del código principal (sin cambios)
MAIN_PATH = \
    ./FONTS/src/main/Dominio/*.java \
	./FONTS/src/main/Dominio/Excepciones/*.java \
	./FONTS/src/main/Dominio/Modelos/*.java

# Directorios de salida
CLASS_OUTPUT_MAIN = ./EXE/main
CLASS_OUTPUT_TEST = ./EXE/test

# Ruta a los archivos de test (ajústala si es necesario)
TEST_PATH = ./FONTS/src/test/testsUnitarios/*.java

# Rutas a las librerías de JUnit y Hamcrest
JUNIT_JAR = ./FONTS/src/test/lib/junit-4.12.jar
HAMCREST_JAR = ./FONTS/src/test/lib/hamcrest-core-1.3.jar

# Objetivo para compilar el código principal (no se toca)
code:
	javac -d $(CLASS_OUTPUT_MAIN) $(MAIN_PATH)

# Objetivo para compilar los tests.
# NOTA: Se compila directamente en $(CLASS_OUTPUT_TEST) para que, al tener 
# el package "testsUnitarios" en FichaTest.java, se genere la carpeta testsUnitarios automáticamente.
testFicha:
	mkdir -p $(CLASS_OUTPUT_TEST)
	javac -d $(CLASS_OUTPUT_TEST) -cp $(CLASS_OUTPUT_MAIN):$(JUNIT_JAR):$(HAMCREST_JAR) $(TEST_PATH)

# Objetivo para ejecutar el test.
# Se asume que FichaTest.java declara el paquete "testsUnitarios" y tiene un método main o está preparado para ser ejecutado.
runTestFicha:
	java -cp $(CLASS_OUTPUT_MAIN):$(CLASS_OUTPUT_TEST):$(JUNIT_JAR):$(HAMCREST_JAR) org.junit.runner.JUnitCore testsUnitarios.FichaTest

# Regla para limpiar el directorio EXE (tanto main como test)
clean:
	rm -rf $(CLASS_OUTPUT_MAIN)/* $(CLASS_OUTPUT_TEST)/*
