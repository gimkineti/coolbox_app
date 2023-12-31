package com.coolbox.aplicacion.service;

import com.coolbox.aplicacion.entity.Productos;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Component
public class PdfGenerator {
    
    public ByteArrayInputStream generatePdf(List<Productos> productosList) {
        Document document = new Document(PageSize.A4.rotate());
        document.setMargins(30, 30, 10, 30);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out); // Colocar aquí la declaración de 'writer'
            TableHeaderEvent event = new TableHeaderEvent();
            writer.setPageEvent(event);

            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD, new BaseColor(139, 0, 0));
            Paragraph title = new Paragraph("Lista de Productos", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);
            addTableHeader(table);
            addRows(table, productosList);
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Stream.of("ID del Producto", "Descripción del Producto", "Stock del Producto", "Marca del Producto",
                  "Categoría del Producto", "Precio de Venta", "Fecha de Ingreso")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(0, 112, 192));
                    header.setBorderWidth(1.5f);
                    header.setBorderColor(BaseColor.WHITE);
                    header.setPadding(8);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    header.setPhrase(new Phrase(columnTitle, headerFont));
                    table.addCell(header);
                });
    
        table.setHeaderRows(1); // Esto permite que el encabezado se repita en cada página
        table.setSpacingBefore(20); // Ajusta el espacio entre el encabezado y la tabla
    }    

    private void addRows(PdfPTable table, List<Productos> productosList) {
        Font contentFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
        boolean isAlternateRow = true;
        for (Productos producto : productosList) {
            PdfPCell cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            cell.setBorderWidth(1.5f);
            cell.setBorderColor(BaseColor.WHITE);

            BaseColor backgroundColor = isAlternateRow ? new BaseColor(230, 244, 255) : BaseColor.WHITE;
            cell.setBackgroundColor(backgroundColor);

            cell.setPhrase(new Phrase(String.valueOf(producto.getIdProducto()), contentFont));
            table.addCell(cell);

            cell.setPhrase(new Phrase(producto.getDescripcionProducto(), contentFont));
            table.addCell(cell);

            cell.setPhrase(new Phrase(String.valueOf(producto.getStockProducto()), contentFont));
            table.addCell(cell);

            cell.setPhrase(new Phrase(producto.getMarcaProducto().getNombreMarca(), contentFont));
            table.addCell(cell);

            cell.setPhrase(new Phrase(producto.getCategoriaProducto().getNombreCategoria(), contentFont));
            table.addCell(cell);

            cell.setPhrase(new Phrase(producto.getPrecioVenta().toString(), contentFont));
            table.addCell(cell);

            cell.setPhrase(new Phrase(producto.getFechaProducto().toString(), contentFont));
            table.addCell(cell);
            
            isAlternateRow = !isAlternateRow;
        }
    }
}