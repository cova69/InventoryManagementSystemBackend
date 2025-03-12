// ExportService.java
package com.example.inventory_backend.service;

import com.example.inventory_backend.model.Inventory;
import com.example.inventory_backend.model.Supplier;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportService {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SupplierService supplierService;

    public ByteArrayInputStream exportToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            // Create Inventory sheet
            Sheet inventorySheet = workbook.createSheet("Inventory");
            
            // Create header row for inventory
            Row headerRow = inventorySheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Product");
            headerRow.createCell(2).setCellValue("Quantity");
            headerRow.createCell(3).setCellValue("Reorder Level");
            headerRow.createCell(4).setCellValue("Reorder Quantity");
            headerRow.createCell(5).setCellValue("Location");
            headerRow.createCell(6).setCellValue("Last Updated");
            
            // Create data rows for inventory
            List<Inventory> inventoryList = inventoryService.getAllInventory();
            int rowIdx = 1;
            for (Inventory inventory : inventoryList) {
                Row row = inventorySheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(inventory.getId());
                row.createCell(1).setCellValue(inventory.getProduct() != null ? 
                        inventory.getProduct().getName() : "N/A");
                row.createCell(2).setCellValue(inventory.getQuantity());
                row.createCell(3).setCellValue(inventory.getReorderLevel());
                row.createCell(4).setCellValue(inventory.getReorderQuantity());
                row.createCell(5).setCellValue(inventory.getLocation() != null ? 
                        inventory.getLocation() : "N/A");
                row.createCell(6).setCellValue(inventory.getLastUpdated() != null ? 
                        inventory.getLastUpdated().toString() : "N/A");
            }
            
            // Auto size columns for inventory sheet
            for (int i = 0; i < 7; i++) {
                inventorySheet.autoSizeColumn(i);
            }
            
            // Create Suppliers sheet
            Sheet suppliersSheet = workbook.createSheet("Suppliers");
            
            // Create header row for suppliers
            headerRow = suppliersSheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Contact Person");
            headerRow.createCell(3).setCellValue("Email");
            headerRow.createCell(4).setCellValue("Phone");
            headerRow.createCell(5).setCellValue("Address");
            headerRow.createCell(6).setCellValue("Product Count");
            
            // Create data rows for suppliers
            List<Supplier> supplierList = supplierService.getAllSuppliers();
            rowIdx = 1;
            for (Supplier supplier : supplierList) {
                Row row = suppliersSheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(supplier.getId());
                row.createCell(1).setCellValue(supplier.getName());
                row.createCell(2).setCellValue(supplier.getContactName() != null ? 
                        supplier.getContactName() : "N/A");
                row.createCell(3).setCellValue(supplier.getEmail() != null ? 
                        supplier.getEmail() : "N/A");
                row.createCell(4).setCellValue(supplier.getPhone() != null ? 
                        supplier.getPhone() : "N/A");
                row.createCell(5).setCellValue(supplier.getAddress() != null ? 
                        supplier.getAddress() : "N/A");
                row.createCell(6).setCellValue(supplier.getProducts() != null ? 
                        supplier.getProducts().size() : 0);
            }
            
            // Auto size columns for suppliers sheet
            for (int i = 0; i < 7; i++) {
                suppliersSheet.autoSizeColumn(i);
            }
            
            // Write the workbook to the output stream
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}