/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.billPDFGenenerator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import static com.itextpdf.text.Font.BOLD;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import controller.accountLoggedSetter.LoggedAccountSetter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.bills.Bills;
import model.bills.BillsModel;
import view.settings.settingPages.SystemLogsModel;
/**
 *
 * @author Merry Ann
 */
public class billPdfGenerator {    
    private BillsModel billModel;
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private SystemLogsModel systemLogsModel = new SystemLogsModel();
    public billPdfGenerator() {
        billModel = new BillsModel();
    }

    public billPdfGenerator(int id) throws DocumentException, IOException, SQLException {
        billModel = new BillsModel();
        Bills bill = billModel.getConsumerBillDetails(id);
        if (bill != null) {
            generatePdf(bill);
        }
    }

    public void generatePdf(Bills bill) throws FileNotFoundException, DocumentException, IOException, SQLException {
        int invoiceID = billModel.insertInvoice(bill.getBillID());
        systemLogsModel.insertLog(logAccount.getAccount(), "Generated Invoice ID: "+invoiceID+" for Bill ID: "+bill.getId()+", consumer ("+bill.getName()+")");
                                    

        String companyName = "Water Billing System";
        String companyAddress = "123 Main Street, Anytown, CA 12345";
        String companyPhone = "(555) 555-1234";
        String companyEmail = "billing@citywater.com";

        String customerName = bill.getName();
        String customerAddress = "456 Elm Street, Anytown, CA 12345";
        String accountNumber = bill.getMeterNo();

      //  String billingPeriod = new SimpleDateFormat("MMM-yyyy").format(bill.getReadingDate());
       String billingPeriod = String.valueOf(bill.getReadingDate());
        int currentMeterReading = bill.getCurReading();
        int previousMeterReading = bill.getPrevReading();
        double waterRate = 1.00;

        int waterUsage = currentMeterReading - previousMeterReading;
        double waterCharges = waterUsage * waterRate;

        double wastewaterFee = 5.00;
        double latePaymentFee = 0.00;

        Document document = new Document();
        OutputStream outputStream = new FileOutputStream("invoices/Bills/" + customerName + "-" + billingPeriod + "-" + invoiceID + ".pdf");
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        Image logo = Image.getInstance("src/assets/logo.png");
        logo.scalePercent(10f);
        logo.setAlignment(Image.ALIGN_CENTER);
        document.add(logo);

        Paragraph title = new Paragraph("Water Billing Invoice", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        Paragraph companyInfo = new Paragraph(companyName + "\n" + companyAddress + "\n" + companyPhone + "\n" + companyEmail);
        document.add(companyInfo);

        Paragraph invoiceDetails = new Paragraph("Invoice Number: " + invoiceID, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        invoiceDetails.add("\nInvoice Date: " + new Date());
        document.add(invoiceDetails);;

        // Customer Information
        Paragraph customerInfo = new Paragraph("Bill To:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        document.add(customerInfo);
        customerInfo = new Paragraph(customerName + "\n" + customerAddress + "\n" + "Account Number: " + accountNumber);
        document.add(customerInfo);

        // Billing Period
        Paragraph billingPeriodSection = new Paragraph("Billing Period:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        billingPeriodSection.add(new Phrase(billingPeriod));
        document.add(billingPeriodSection);

        // Meter Readings and Usage
        PdfPTable meterReadingsTable = new PdfPTable(3);
        meterReadingsTable.setWidthPercentage(50f);
        meterReadingsTable.addCell(new Phrase("Current Reading", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        meterReadingsTable.addCell(new Phrase("Previous Reading", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        meterReadingsTable.addCell(new Phrase("Water Usage", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        meterReadingsTable.addCell(String.valueOf(currentMeterReading));
        meterReadingsTable.addCell(String.valueOf(previousMeterReading));
        meterReadingsTable.addCell(String.valueOf(waterUsage));
        document.add(meterReadingsTable);
        Paragraph waterChargesSection = new Paragraph("Water Charges:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
         // Continue adding content to the paragraph...
        waterChargesSection.add(new Phrase(String.format("$%.2f", waterCharges), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(waterChargesSection);

        // Add additional charges if applicable
        if (wastewaterFee > 0) {
            Paragraph wastewaterFeeSection = new Paragraph("Wastewater Fee:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
            wastewaterFeeSection.add(new Phrase(String.format("$%.2f", wastewaterFee), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            document.add(wastewaterFeeSection);
        }

        // Add late payment fee if applicable
        if (latePaymentFee > 0) {
            Paragraph latePaymentFeeSection = new Paragraph("Late Payment Fee:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
            latePaymentFeeSection.add(new Phrase(String.format("$%.2f", latePaymentFee), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            document.add(latePaymentFeeSection);
        }

        // Calculate and display total amount due
        double totalAmountDue = waterCharges + wastewaterFee + latePaymentFee;
        Paragraph totalAmountSection = new Paragraph("Total Amount Due:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        totalAmountSection.add(new Phrase(String.format("$%.2f", totalAmountDue), FontFactory.getFont(FontFactory.HELVETICA, 12, BOLD)));
        document.add(totalAmountSection);

        // Add payment information and due date
        Paragraph paymentInformation = new Paragraph("Please pay your bill by the due date:");
        paymentInformation.add(new Phrase(billingPeriod.split("-")[1].trim(), FontFactory.getFont(FontFactory.HELVETICA, 12, BOLD)));
        document.add(paymentInformation);
        document.add(new Paragraph("Payment methods (e.g., online portal, mail-in check)"));

        // Close the document
        document.close();
        outputStream.close();
        System.out.println("Water billing invoice generated successfully!");
        try {
            Desktop.getDesktop().open(new File("invoices/Bills/"+customerName+"-"+billingPeriod+"-"+invoiceID+".pdf"));
        } catch (IOException e) {
                e.printStackTrace();
        }   
    }
}
