package com.coolbox.aplicacion.service;

import com.coolbox.aplicacion.entity.Productos;
import org.apache.poi.ss.usermodel.*;
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

            // Estilos para el encabezado
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex()); // Usamos un celeste más suave
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setTopBorderColor(IndexedColors.LIGHT_BLUE.getIndex()); // Borde celeste más suave
            headerCellStyle.setRightBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerCellStyle.setBottomBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerCellStyle.setLeftBorderColor(IndexedColors.LIGHT_BLUE.getIndex());

            // Crear la fila del encabezado
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Descripción", "Stock", "Marca", "Categoría", "Precio Venta", "Fecha"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Estilos para las celdas de datos
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
            dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataCellStyle.setBorderTop(BorderStyle.THIN);
            dataCellStyle.setBorderRight(BorderStyle.THIN);
            dataCellStyle.setBorderBottom(BorderStyle.THIN);
            dataCellStyle.setBorderLeft(BorderStyle.THIN);
            dataCellStyle.setTopBorderColor(IndexedColors.LIGHT_BLUE.getIndex()); // Borde celeste más suave
            dataCellStyle.setRightBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
            dataCellStyle.setBottomBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
            dataCellStyle.setLeftBorderColor(IndexedColors.LIGHT_BLUE.getIndex());

            // Agregar los datos a la tabla
            int rowNum = 1;
            for (Productos producto : productosList) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(producto.getIdProducto());
                row.createCell(1).setCellValue(producto.getDescripcionProducto());
                row.createCell(2).setCellValue(producto.getStockProducto());
                row.createCell(3).setCellValue(producto.getMarcaProducto().getNombreMarca());
                row.createCell(4).setCellValue(producto.getCategoriaProducto().getNombreCategoria());
                row.createCell(5).setCellValue(producto.getPrecioVenta().doubleValue());
                row.createCell(6).setCellValue(producto.getFechaProducto().toString());

                // Aplicar estilo a las celdas de datos
                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataCellStyle);
                }
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
