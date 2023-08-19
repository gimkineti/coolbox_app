package com.coolbox.aplicacion.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class TableHeaderEvent extends PdfPageEventHelper {
    private PdfPTable table;

    public void setTable(PdfPTable table) {
        this.table = table;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (table != null) {
            table.writeSelectedRows(0, -1, document.leftMargin(), document.top() + 10, writer.getDirectContent());
        }
    }
}
