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
        document.setMargins(30, 30, 0, 30); // Agregar márgenes de 30 unidades alrededor del contenido
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Agregar el título encima del encabezado de la tabla
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD, new BaseColor(139, 0, 0)); // Color Rojo fuego (#B22222)
            Paragraph title = new Paragraph("Lista de Productos", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Añadir contenido al PDF
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100); // Hacer que la tabla ocupe todo el ancho disponible
            table.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrar la tabla en la página

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
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE); // Color blanco para el texto del encabezado
        Stream.of("ID del Producto", "Descripción del Producto", "Stock del Producto", "Marca del Producto",
                "Categoría del Producto", "Precio de Venta", "Fecha de Ingreso")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(0, 112, 192)); // Azul énfasis 1 - Color de fondo del encabezado
                    header.setBorderWidth(1.5f); // Grosor del borde de la celda (medio)
                    header.setBorderColor(BaseColor.WHITE); // Color blanco para los bordes
                    header.setPadding(8); // Espacio entre el texto y los bordes de la celda
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE); // Centrar el contenido verticalmente
                    header.setPhrase(new Phrase(columnTitle, headerFont));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, List<Productos> productosList) {
        Font contentFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
        boolean isAlternateRow = true; // Variable para alternar el color de fondo de las filas
        for (Productos producto : productosList) {
            PdfPCell cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Centrar el contenido verticalmente
            cell.setPadding(8); // Espacio entre el texto y los bordes de la celda
            cell.setBorderWidth(1.5f); // Grosor del borde de la celda (medio)
            cell.setBorderColor(BaseColor.WHITE); // Color blanco para los bordes

            // Alternar el color de fondo de las filas
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

            // Cambiar el valor de isAlternateRow para la siguiente fila
            isAlternateRow = !isAlternateRow;
        }
    }
}