MAIN_PATH = \
    ./FONTS/src/main/Dominio/*.java \
	./FONTS/src/main/Dominio/Excepciones/*.java \
	./FONTS/src/main/Dominio/Modelos/*.java \

# Directorio de salida para los .class
CLASS_OUTPUT_MAIN = ./EXE/main
CLASS_OUTPUT_TEST = ./EXE/test

# Objetivo para compilar el código
code:
	javac -d $(CLASS_OUTPUT_MAIN) $(MAIN_PATH)
test:
	javac -d $(CLASS_OUTPUT_TEST) $(TEST_PATH)

# Regla para limpiar el directorio EXE
clean main:
	rm -rf $(CLASS_OUTPUT_MAIN)/*