INSTALL= install --strip --mode=0755
OC= dune
RM= rm -f

PREFIX?= $(HOME)/.local
INSTALL_EXE= $(PREFIX)/bin/kodumaro-sudoku
SOURCES= bin/* sudoku/* ui/*
TARGET= _build/default/bin/sudoku.exe


################################################################################
.PHONY: clean test install uninstall


all: $(TARGET)


clean:
	$(OC) clean


test:
	$(OC) test


install: $(TARGET)
	$(INSTALL) $< $(INSTALL_EXE)


uninstall:
	$(RM) $(INSTALL_EXE)


$(TARGET): $(SOURCES)
	$(OC) build
