package app.gui;

import io.qt.widgets.QApplication;
import io.qt.widgets.QMainWindow;

public class MainWindow extends QMainWindow {
    public MainWindow() {
        setWindowTitle("Organizador de Mangás");
        // TODO: adicionar componentes da interface
    }

    public static void main(String[] args) {
        QApplication.initialize(args);
        MainWindow window = new MainWindow();
        window.show();
        QApplication.exec();
    }
} 