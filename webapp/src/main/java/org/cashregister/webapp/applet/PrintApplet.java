package org.cashregister.webapp.applet;

import org.cashregister.domain.Receipt;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;
import javax.print.attribute.standard.PrinterState;
import javax.swing.*;
import java.applet.Applet;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.util.Locale;

/**
 * Created by derkhumblet on 18/02/15.
 * http://bfo.com/blog/2009/09/28/silently_print_a_pdf_from_a_web_browser.html
 */
public class PrintApplet extends Applet {
    private String callback;
    private Receipt receipt;

    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }
        callback = getParameter("callback");
        if (getParameter("receipt")!=null) {
//            load(getParameter("receipt"));
        }
    }



    /**
     * Print the PDF loaded by the "load" method. This will display a dialog asking
     * the user to select a printer.printer, papersize etc. and start the print. If you want
     * to print immediately without this dialog, you can alter the method to do that
     * quite easily - see the "PrintPDF.java" example supplied with the PDF package.
     */
    public void print() {
        try {
            PrintRequestAttributeSet atts = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;

            PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, atts);
            if (services.length > 0) {
                atts.add(OrientationRequested.PORTRAIT);

                // Choose a print service
                PrintService service =  ServiceUI.printDialog(null, 50, 50, services, null, flavor, atts);
                if (service!=null) {
                    final PrinterJob job = PrinterJob.getPrinterJob();
                    job.setPrintService(service);
                    job.print(atts);
                }
            } else {
                error("No Printers");
            }
        } catch (Exception e) {
            error(e.toString());
        }

        DocFlavor flavor = DocFlavor.INPUT_STREAM.POSTSCRIPT;
        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        attributes.add(MediaSizeName.ISO_A4);
        attributes.add(new Copies(1));
        attributes.add(PrinterState.IDLE);
        attributes.add(new PrinterName("KassaPrinter", Locale.getDefault()));
        PrintService[] service = PrintServiceLookup.lookupPrintServices(flavor, attributes);
        if (service.length > 0) {
            service[0].createPrintJob();
        } else {
            error("No print services found.");
        }
    }

    private void error(String message) {
        System.out.println("ERROR: "+message);
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
