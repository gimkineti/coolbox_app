package com.coolbox.aplicacion.service;
import com.coolbox.aplicacion.entity.Productos;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Component
public class PdfGenerator {
    
    public ByteArrayInputStream generatePdf(List<Productos> productosList) {
        Document document = new Document(PageSize.A4.rotate());
        document.setMargins(30, 30, 30, 30); // Agregar márgenes de 30 unidades alrededor del contenido
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Añadir contenido al PDF
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100); // Hacer que la tabla ocupe todo el ancho disponible

            // Agregar espacios entre celdas
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
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
        Stream.of("ID", "Descripción", "Stock", "Marca", "Categoría", "Precio Venta", "Fecha")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPadding(8); // Espacio entre el texto y los bordes de la celda
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE); // Centrar el contenido verticalmente
                    header.setPhrase(new Phrase(columnTitle, headerFont));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, List<Productos> productosList) {
        Font contentFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
        for (Productos producto : productosList) {
            PdfPCell cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Centrar el contenido verticalmente
            cell.setPadding(8); // Espacio entre el texto y los bordes de la celda

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
        }
    }
}
