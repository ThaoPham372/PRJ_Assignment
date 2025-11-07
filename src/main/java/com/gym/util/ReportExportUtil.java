package com.gym.util;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gym.model.Schedule;
import com.gym.model.Trainer;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

/**
 * Utility class để export báo cáo ra PDF và Excel
 */
public class ReportExportUtil {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  /**
   * Export báo cáo trainer ra file Excel
   * 
   * @param trainer           Trainer object
   * @param completedSessions Số buổi hoàn thành
   * @param cancelledSessions Số buổi hủy
   * @param completionRate    Tỷ lệ hoàn thành
   * @param averageRating     Đánh giá trung bình
   * @param totalStudents     Tổng số học viên
   * @param recentSessions    Danh sách buổi tập gần đây
   * @param monthlyData       Dữ liệu theo tháng
   * @param trainingTypeData  Dữ liệu phân loại loại hình tập
   * @param outputStream      OutputStream để ghi file
   * @throws IOException
   */
  public static void exportToExcel(Trainer trainer, int completedSessions, int cancelledSessions,
      float completionRate, float averageRating, int totalStudents, List<Schedule> recentSessions,
      Map<java.time.YearMonth, Long> monthlyData, Map<String, Long> trainingTypeData, OutputStream outputStream)
      throws IOException {

    Workbook workbook = new XSSFWorkbook();
    CellStyle headerStyle = createHeaderStyle(workbook);
    CellStyle dataStyle = createDataStyle(workbook);

    // Sheet 1: Tổng quan
    Sheet overviewSheet = workbook.createSheet("Tổng quan");
    int rowNum = 0;

    // Header
    Row headerRow = overviewSheet.createRow(rowNum++);
    createCell(headerRow, 0, "BÁO CÁO THỐNG KÊ TRAINER", headerStyle);
    headerRow.getCell(0).setCellStyle(headerStyle);

    rowNum++; // Skip a row

    // Trainer info
    if (trainer != null && trainer.getUser() != null) {
      createRow(overviewSheet, rowNum++, "Trainer:", trainer.getUser().getName(), dataStyle);
      createRow(overviewSheet, rowNum++, "Email:", trainer.getUser().getEmail(), dataStyle);
    }

    rowNum++; // Skip a row

    // Stats
    createRow(overviewSheet, rowNum++, "Tổng học viên phụ trách:", String.valueOf(totalStudents), dataStyle);
    createRow(overviewSheet, rowNum++, "Buổi tập hoàn thành:", String.valueOf(completedSessions), dataStyle);
    createRow(overviewSheet, rowNum++, "Buổi tập bị hủy:", String.valueOf(cancelledSessions), dataStyle);
    createRow(overviewSheet, rowNum++, "Tỷ lệ hoàn thành:", String.format("%.2f%%", completionRate * 100), dataStyle);
    createRow(overviewSheet, rowNum++, "Đánh giá trung bình:", String.format("%.1f", averageRating), dataStyle);

    // Sheet 2: Buổi tập gần đây
    if (recentSessions != null && !recentSessions.isEmpty()) {
      Sheet sessionsSheet = workbook.createSheet("Buổi tập gần đây");
      rowNum = 0;

      Row sessionsHeaderRow = sessionsSheet.createRow(rowNum++);
      createCell(sessionsHeaderRow, 0, "Ngày", headerStyle);
      createCell(sessionsHeaderRow, 1, "Giờ", headerStyle);
      createCell(sessionsHeaderRow, 2, "Học viên", headerStyle);
      createCell(sessionsHeaderRow, 3, "Loại tập", headerStyle);
      createCell(sessionsHeaderRow, 4, "Trạng thái", headerStyle);
      createCell(sessionsHeaderRow, 5, "Đánh giá", headerStyle);

      for (Schedule session : recentSessions) {
        Row sessionRow = sessionsSheet.createRow(rowNum++);
        createCell(sessionRow, 0,
            session.getTrainingDate() != null ? session.getTrainingDate().format(DATE_FORMATTER) : "", dataStyle);
        createCell(sessionRow, 1,
            session.getStartTime() != null && session.getEndTime() != null
                ? session.getStartTime() + " - " + session.getEndTime()
                : "",
            dataStyle);
        createCell(sessionRow, 2,
            session.getStudent() != null && session.getStudent().getUser() != null
                ? session.getStudent().getUser().getName()
                : "N/A",
            dataStyle);
        createCell(sessionRow, 3, session.getTrainingType() != null ? session.getTrainingType() : "", dataStyle);
        createCell(sessionRow, 4, session.getStatus() != null ? session.getStatus().name() : "", dataStyle);
        // Rating sẽ được thêm vào Schedule entity sau
        createCell(sessionRow, 5, "", dataStyle);
      }

      // Auto-size columns
      for (int i = 0; i < 6; i++) {
        sessionsSheet.autoSizeColumn(i);
      }
    }

    // Sheet 3: Thống kê theo tháng
    if (monthlyData != null && !monthlyData.isEmpty()) {
      Sheet monthlySheet = workbook.createSheet("Thống kê theo tháng");
      rowNum = 0;

      Row monthlyHeaderRow = monthlySheet.createRow(rowNum++);
      createCell(monthlyHeaderRow, 0, "Tháng", headerStyle);
      createCell(monthlyHeaderRow, 1, "Số buổi tập", headerStyle);

      for (Map.Entry<java.time.YearMonth, Long> entry : monthlyData.entrySet()) {
        Row monthlyRow = monthlySheet.createRow(rowNum++);
        createCell(monthlyRow, 0, entry.getKey().toString(), dataStyle);
        createCell(monthlyRow, 1, String.valueOf(entry.getValue()), dataStyle);
      }

      monthlySheet.autoSizeColumn(0);
      monthlySheet.autoSizeColumn(1);
    }

    // Sheet 4: Phân loại loại hình tập
    if (trainingTypeData != null && !trainingTypeData.isEmpty()) {
      Sheet typeSheet = workbook.createSheet("Loại hình tập");
      rowNum = 0;

      Row typeHeaderRow = typeSheet.createRow(rowNum++);
      createCell(typeHeaderRow, 0, "Loại hình tập", headerStyle);
      createCell(typeHeaderRow, 1, "Số lượng", headerStyle);

      for (Map.Entry<String, Long> entry : trainingTypeData.entrySet()) {
        Row typeRow = typeSheet.createRow(rowNum++);
        createCell(typeRow, 0, entry.getKey(), dataStyle);
        createCell(typeRow, 1, String.valueOf(entry.getValue()), dataStyle);
      }

      typeSheet.autoSizeColumn(0);
      typeSheet.autoSizeColumn(1);
    }

    workbook.write(outputStream);
    workbook.close();
  }

  /**
   * Export báo cáo trainer ra file PDF
   */
  public static void exportToPDF(Trainer trainer, int completedSessions, int cancelledSessions,
      float completionRate, float averageRating, int totalStudents, List<Schedule> recentSessions,
      Map<java.time.YearMonth, Long> monthlyData, Map<String, Long> trainingTypeData, OutputStream outputStream)
      throws IOException {

    PdfWriter writer = new PdfWriter(outputStream);
    PdfDocument pdf = new PdfDocument(writer);
    Document document = new Document(pdf);

    // Title
    Paragraph title = new Paragraph("BÁO CÁO THỐNG KÊ TRAINER")
        .setFontSize(18)
        .setBold()
        .setTextAlignment(TextAlignment.CENTER)
        .setMarginBottom(20);
    document.add(title);

    // Trainer info
    if (trainer != null && trainer.getUser() != null) {
      document.add(new Paragraph("Trainer: " + trainer.getUser().getName()).setMarginBottom(10));
      document.add(new Paragraph("Email: " + trainer.getUser().getEmail()).setMarginBottom(20));
    }

    // Stats
    document.add(new Paragraph("Tổng học viên phụ trách: " + totalStudents).setMarginBottom(5));
    document.add(new Paragraph("Buổi tập hoàn thành: " + completedSessions).setMarginBottom(5));
    document.add(new Paragraph("Buổi tập bị hủy: " + cancelledSessions).setMarginBottom(5));
    document.add(new Paragraph("Tỷ lệ hoàn thành: " + String.format("%.2f%%", completionRate * 100))
        .setMarginBottom(5));
    document.add(new Paragraph("Đánh giá trung bình: " + String.format("%.1f", averageRating))
        .setMarginBottom(20));

    // Recent sessions table
    if (recentSessions != null && !recentSessions.isEmpty()) {
      document.add(new Paragraph("Buổi tập gần đây").setBold().setMarginBottom(10));

      Table table = new Table(UnitValue.createPercentArray(new float[] { 2, 2, 3, 2, 2, 1 }))
          .useAllAvailableWidth();

      // Header
      table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Ngày").setBold()));
      table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Giờ").setBold()));
      table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Học viên").setBold()));
      table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Loại tập").setBold()));
      table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Trạng thái").setBold()));
      table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Đánh giá").setBold()));

      // Data rows
      for (Schedule session : recentSessions) {
        table.addCell(session.getTrainingDate() != null ? session.getTrainingDate().format(DATE_FORMATTER) : "");
        table.addCell(
            session.getStartTime() != null && session.getEndTime() != null
                ? session.getStartTime() + " - " + session.getEndTime()
                : "");
        table.addCell(
            session.getStudent() != null && session.getStudent().getUser() != null
                ? session.getStudent().getUser().getName()
                : "N/A");
        table.addCell(session.getTrainingType() != null ? session.getTrainingType() : "");
        table.addCell(session.getStatus() != null ? session.getStatus().name() : "");
        table.addCell(""); // Rating
      }

      document.add(table);
    }

    document.close();
  }

  // Helper methods
  private static CellStyle createHeaderStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    font.setFontHeightInPoints((short) 12);
    style.setFont(font);
    return style;
  }

  private static CellStyle createDataStyle(Workbook workbook) {
    return workbook.createCellStyle();
  }

  private static void createCell(Row row, int column, String value, CellStyle style) {
    Cell cell = row.createCell(column);
    cell.setCellValue(value);
    if (style != null) {
      cell.setCellStyle(style);
    }
  }

  private static void createRow(Sheet sheet, int rowNum, String label, String value, CellStyle style) {
    Row row = sheet.createRow(rowNum);
    createCell(row, 0, label, style);
    createCell(row, 1, value, style);
  }
}
