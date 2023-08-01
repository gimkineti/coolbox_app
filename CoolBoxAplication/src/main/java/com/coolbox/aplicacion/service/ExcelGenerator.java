package com.coolbox.aplicacion.service;

import com.coolbox.aplicacion.entity.Productos;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelGenerator {

    public ByteArrayInputStream generateExcel(List<Productos> productosList) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Productos");

            // Estilos para el título
            CellStyle titleCellStyle = workbook.createCellStyle();
            titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font titleFont = workbook.createFont();
            titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 30); // Aumentar el tamaño de fuente a 30 puntos
            titleCellStyle.setFont(titleFont);
            titleCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Crear la fila del título
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Lista de Productos");
            titleCell.setCellStyle(titleCellStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6)); // Unir celdas para el título

            // Estilos para el encabezado
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerCellStyle.setWrapText(true); // Ajustar contenido en orientación horizontal
            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 16);
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Crear la fila del encabezado
            Row headerRow = sheet.createRow(1);
            String[] headers = { "ID del Producto", "Descripción del Producto", "Stock del Producto",
                    "Marca del Producto",
                    "Categoría del Producto", "Precio de Venta", "Fecha de Ingreso" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Estilos para las celdas de datos
            CellStyle dataCellStyle1 = workbook.createCellStyle();
            dataCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            dataCellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
            dataCellStyle1.setWrapText(true); // Ajustar contenido en orientación horizontal
            Font dataFont1 = workbook.createFont();
            dataFont1.setColor(IndexedColors.BLACK.getIndex());
            dataFont1.setFontHeightInPoints((short) 12);
            dataCellStyle1.setFont(dataFont1);
            dataCellStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            dataCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle dataCellStyle2 = workbook.createCellStyle();
            dataCellStyle2.setAlignment(HorizontalAlignment.CENTER);
            dataCellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
            dataCellStyle2.setWrapText(true); // Ajustar contenido en orientación horizontal
            Font dataFont2 = workbook.createFont();
            dataFont2.setColor(IndexedColors.BLACK.getIndex());
            dataFont2.setFontHeightInPoints((short) 12);
            dataCellStyle2.setFont(dataFont2);
            dataCellStyle2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            dataCellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Agregar los datos a la tabla
            int rowNum = 2;
            boolean isAlternateRow = true; // Variable para alternar el color de fondo de las filas
            for (Productos producto : productosList) {
                Row row = sheet.createRow(rowNum++);

                CellStyle dataCellStyle = isAlternateRow ? dataCellStyle1 : dataCellStyle2;
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(dataCellStyle);
                }

                row.getCell(0).setCellValue(producto.getIdProducto());
                row.getCell(1).setCellValue(producto.getDescripcionProducto());
                row.getCell(2).setCellValue(producto.getStockProducto());
                row.getCell(3).setCellValue(producto.getMarcaProducto().getNombreMarca());
                row.getCell(4).setCellValue(producto.getCategoriaProducto().getNombreCategoria());
                row.getCell(5).setCellValue(producto.getPrecioVenta().doubleValue());
                row.getCell(6).setCellValue(producto.getFechaProducto().toString());

                // Cambiar el valor de isAlternateRow para la siguiente fila
                isAlternateRow = !isAlternateRow;
            }

            // Ajustar automáticamente el tamaño de las columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}