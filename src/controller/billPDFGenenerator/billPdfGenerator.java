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
import model.meterReading.MeterReadingModel;
import model.penalty.Penalty;
import model.penalty.PenaltyModel;
import view.settings.Settings;
import view.settings.SettingsModel;
import view.settings.settingPages.SystemLogsModel;
/**
 *
 * @author Merry Ann
 */
public class billPdfGenerator {    
    private BillsModel billModel;
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private SystemLogsModel systemLogsModel = new SystemLogsModel();
    private SettingsModel settingsModel = new SettingsModel();
    private MeterReadingModel meterReadingModel = new MeterReadingModel();
    private PenaltyModel penaltyModel= new PenaltyModel();
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
        Penalty penalty =   penaltyModel.getPenaltyDetailsById(bill.getBillID());                         
        
        Settings settings = settingsModel.getComInfo();
        
        String companyName = settings.getCompanyName();
        String companyAddress = settings.getCompanyPurok()+", "+settings.getCompanyBaranggay()+", "+settings.getCompanyMunicipality()+", "+settings.getCompanyProvince();
        String companyPhone = settings.getContactNo();
        String companyEmail = settings.getEmailAd();

        String customerName = bill.getName();
        String customerAddress = ""+settings.getCompanyBaranggay()+", "+settings.getCompanyMunicipality()+", "+settings.getCompanyProvince();
        String meterNumber = bill.getMeterNo();

      //  String billingPeriod = new SimpleDateFormat("MMM-yyyy").format(bill.getReadingDate());
        String billingPeriod = String.valueOf(bill.getBillingDate());
        String currentMeterReading = String.valueOf(bill.getCurReading());
        String previousMeterReading = String.valueOf(bill.getPrevReading());
        double waterRate = meterReadingModel.getRate();
        String dueDate = String.valueOf(bill.getDueDate());
        int waterUsage = bill.getCurReading() - bill.getPrevReading();
        double totalAmount = waterUsage * waterRate;

        Document document = new Document();
        OutputStream outputStream = new FileOutputStream("invoices/Bills/"+invoiceID+ ".pdf");
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        Image logo = Image.getInstance("src/assets/logo.png");
        logo.scalePercent(10f);
        logo.setAlignment(Image.ALIGN_CENTER);
        document.add(logo);

        Paragraph companyName1 = new Paragraph(companyName);
        companyName1.setAlignment(companyName1.ALIGN_CENTER);
        document.add(companyName1);
        
        Paragraph companyAddress1 = new Paragraph(companyAddress );
        companyAddress1.setAlignment(companyAddress1.ALIGN_CENTER);
        document.add(companyAddress1);
        
        Paragraph companyContacts = new Paragraph( companyPhone +" || " + companyEmail);
        companyContacts.setAlignment(companyContacts.ALIGN_CENTER);
        document.add(companyContacts);

        Paragraph invoiceDetails = new Paragraph("Invoice Number: " + invoiceID, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        invoiceDetails.add("\nInvoice Date: " + new Date());
        document.add(invoiceDetails);;

        // Customer Information
        Paragraph customerInfo = new Paragraph("Bill To:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        document.add(customerInfo);
        customerInfo = new Paragraph(customerName + "\n" + customerAddress + "\n" + "Meter Number: " + meterNumber);
        document.add(customerInfo);

        // Billing Period
        Paragraph billingPeriodSection = new Paragraph("Billing Period:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        billingPeriodSection.add(new Phrase(billingPeriod));
        document.add(billingPeriodSection);

        // Meter Readings and Usage
        Paragraph prevReading = new Paragraph("Previous Reading:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        prevReading.add(new Phrase( previousMeterReading, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(prevReading);

        Paragraph curReading = new Paragraph("Current Reading:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        curReading.add(new Phrase(currentMeterReading, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(curReading);

        Paragraph waterCon = new Paragraph("Water Consumption:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        waterCon.add(new Phrase(String.format("$%d", waterUsage), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(waterCon);

        int penaltyAmount = 0;
        if (penalty != null) {
            Paragraph pAmount = new Paragraph("Penalty Charges:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
            pAmount.add(new Phrase(String.format("P%d", penalty.getPamount()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            document.add(pAmount);

            penaltyAmount = penalty.getPamount();

            Paragraph penaltyDate = new Paragraph("Penalty Date:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
            penaltyDate.add(new Phrase(String.format("P%d", penalty.getPdate()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            document.add(penaltyDate);
        }
        
        Paragraph totalAmountSection = new Paragraph("Total Amount Due:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        totalAmountSection.add(new Phrase(String.format("P%.2f", totalAmount + penaltyAmount), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
        document.add(totalAmountSection);
        
        Paragraph status = new Paragraph("Status: :", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        status.add(new Phrase(String.valueOf(bill.getBillingStatus()), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
        document.add(status);


        // Add payment information and due date
        Paragraph paymentInformation = new Paragraph("Please pay your bill by the due date:");
        paymentInformation.add(new Phrase(String.valueOf(bill.getDueDate()), FontFactory.getFont(FontFactory.HELVETICA, 12, BOLD)));
        document.add(paymentInformation);

        // Close the document
        document.close();
        outputStream.close();
        System.out.println("Water billing invoice generated successfully!");
        try {
            Desktop.getDesktop().open(new File("invoices/Bills/"+invoiceID+".pdf"));
        } catch (IOException e) {
                e.printStackTrace();
        }   
    }
    
}
