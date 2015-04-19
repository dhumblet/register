package org.cashregister.webapp.service.impl;

import org.cashregister.domain.Transaction;
import org.cashregister.webapp.service.api.KassaPrintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.print.attribute.standard.PrinterState;
import java.util.Locale;

/**
 * Created by derkhumblet on 27/12/14.
 */
public class KassaPrintServiceImpl implements KassaPrintService {
    private static final Logger LOG = LoggerFactory.getLogger(KassaPrintServiceImpl.class);

    @Override
    public void printTransaction(Transaction transaction) {

    }

    private PrintService findPrinter() {
        DocFlavor flavor = DocFlavor.INPUT_STREAM.POSTSCRIPT;
        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        attributes.add(MediaSizeName.ISO_A4);
        attributes.add(new Copies(1));
        attributes.add(PrinterState.IDLE);
        attributes.add(new PrinterName("KassaPrinter", Locale.getDefault()));
        PrintService[] service = PrintServiceLookup.lookupPrintServices(flavor, attributes);
        if (service.length > 0) {
            LOG.info(service.length + " print services found, using first one.");
            return service[0];
        } else {
            LOG.info("No print services found.");
            return null;
        }
    }
}
