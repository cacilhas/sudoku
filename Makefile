CP= cp -f
INSTALL= install --strip --mode=0755
OC= dune
RM= rm -f

NAME= kodumaro-sudoku
PREFIX?= $(HOME)/.local
INSTALL_EXE= $(PREFIX)/bin/$(NAME)
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
	$(CP) $(NAME).png $(PREFIX)/share/icons/hicolor/128x128/apps/
	$(CP) $(NAME).desktop $(PREFIX)/share/applications/


uninstall:
	$(RM) $(INSTALL_EXE) $(PREFIX)/share/icons/hicolor/128x128/apps/$(NAME).png $(PREFIX)/share/applications/$(NAME).desktop


$(TARGET): $(SOURCES)
	$(OC) build
