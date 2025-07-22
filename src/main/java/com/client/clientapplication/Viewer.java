package com.client.clientapplication;

import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.awt.*;

public class Viewer extends JFrame {

    private JPanel panel;
    private JScrollPane scrollPane;
    AnchorPane anchorPane = new AnchorPane();

//    public Viewer(){
//       // viewPdf("C:\\System generated dat files\\Lpo.pdf");
//        viewPdf("C:\\Users\\USER\\Documents\\Resignation Letter.pdf");
//    }
//
//    public static void main(String[] args) {
//        Viewer pdfViewer = new Viewer();
//        pdfViewer.setContentPane(pdfViewer.panel);
//        pdfViewer.setTitle("Document Viewer");
//        pdfViewer.setExtendedState(MAXIMIZED_BOTH);
//        pdfViewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        pdfViewer.setVisible(true);
//
//    }
//
//    public void viewPdf(String file){
//        SwingController controller = new SwingController();
//        SwingViewBuilder builder = new SwingViewBuilder(controller);
//        JPanel view = builder.buildViewerPanel();
//        ComponentKeyBinding.install(controller,view);
//        controller.getDocumentViewController().setAnnotationCallback(new MyAnnotationCallback(controller
//                .getDocumentViewController()));
//        controller.openDocument(file);
//        //scrollPane.setViewportView(view);
//        JFrame frame = new JFrame();
//        frame.getContentPane().add(view);
//        frame.pack();
//        frame.setTitle("Doc");
//        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
//        frame.setVisible(true);
//
//    }
}
