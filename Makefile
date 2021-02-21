CP= cp -f
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
	$(CP) kodumaro-sudoku.png $(PREFIX)/share/icons/128x128/apps/
	$(CP) kodumaro-sudoku.desktop $(PREFIX)/share/applications/


uninstall:
	$(RM) $(INSTALL_EXE) $(PREFIX)/share/icons/128x128/apps/kodumaro-sudoku.png $(PREFIX)/share/applications/kodumaro-sudoku.desktop


$(TARGET): $(SOURCES)
	$(OC) build
