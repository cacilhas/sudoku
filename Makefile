CP= cp -f
INSTALL= install --strip --mode=0755
OC= dune
RM= rm -f

NAME= kodumaro-sudoku
PREFIX?= $(HOME)/.local
BIN_DIR= $(PREFIX)/bin/
ICONS_DIR= $(PREFIX)/share/icons/hicolor/128x128/apps/
APPLICATIONS_DIR= $(PREFIX)/share/applications/
INSTALL_EXE= $(BIN_DIR)/$(NAME)
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
	@test -e $(BIN_DIR) || mkdir -p $(BIN_DIR)
	@test -e $(ICONS_DIR) || mkdir -p $(ICONS_DIR)
	@test -e $(APPLICATIONS_DIR) || mkdir -p $(APPLICATIONS_DIR)
	$(INSTALL) $< $(INSTALL_EXE)
	$(CP) $(NAME).png $(ICONS_DIR)
	$(CP) $(NAME).desktop $(APPLICATIONS_DIR)


uninstall:
	$(RM) $(INSTALL_EXE) $(PREFIX)/share/icons/hicolor/128x128/apps/$(NAME).png $(PREFIX)/share/applications/$(NAME).desktop


$(TARGET): $(SOURCES)
	$(OC) build
