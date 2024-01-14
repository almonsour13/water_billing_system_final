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
import model.payment.Payment;
import model.payment.PaymentModel;
import model.penalty.Penalty;
import model.penalty.PenaltyModel;
import view.settings.Settings;
import view.settings.SettingsModel;
import view.settings.settingPages.SystemLogsModel;

public class generateReceipt {    
    private BillsModel billModel = new BillsModel();
    private LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private SystemLogsModel systemLogsModel = new SystemLogsModel();
    private SettingsModel settingsModel = new SettingsModel();
    private MeterReadingModel meterReadingModel = new MeterReadingModel();
    private PenaltyModel penaltyModel = new PenaltyModel();
    private PaymentModel paymentModel = new PaymentModel();
    
    public generateReceipt(){
        
    }
    public generateReceipt(int id) throws DocumentException, IOException, SQLException {
        Payment payment =  paymentModel.getPaymentsDetailsById(id);
        Bills bill = billModel.getConsumerBillDetails(id);
        System.out.println(payment+"   "+id);
        if (payment != null) {
            generatePdf(payment);
        }
    }

    public void generatePdf(Payment payment) throws FileNotFoundException, DocumentException, IOException, SQLException {
        int receiptID = paymentModel.insertReceipt(payment.getPaymentID()); // Assuming receipt ID is used
        systemLogsModel.insertLog(logAccount.getAccount(), "Generated Receipt ID: " + receiptID + " for Bill ID: " + payment.getBillID() + ", consumer (" + payment.getName() + ")");
        Penalty penalty = penaltyModel.getPenaltyDetailsById(payment.getPaymentID()); 
        Bills bill = billModel.getConsumerBillDetails(payment.getBillID());
        System.out.println(bill);
        Settings settings = settingsModel.getComInfo();

        String companyName = settings.getCompanyName();
        String companyAddress = settings.getCompanyPurok() + ", " + settings.getCompanyBaranggay() + ", " + settings.getCompanyMunicipality() + ", " + settings.getCompanyProvince();
        String companyPhone = settings.getContactNo();
        String companyEmail = settings.getEmailAd();

        String customerName = payment.getName();
        String customerAddress = "" + settings.getCompanyBaranggay() + ", " + settings.getCompanyMunicipality() + ", " + settings.getCompanyProvince();
        String meterNumber = payment.getMeterNumber();

        String billingPeriod = String.valueOf(bill.getBillingDate());
        String currentMeterReading = String.valueOf(bill.getCurReading());
        String previousMeterReading = String.valueOf(bill.getPrevReading());
        double waterRate = meterReadingModel.getRate();

        int waterUsage = bill.getCurReading() - bill.getPrevReading();
        double totalAmount = waterUsage * waterRate;

        Document document = new Document();
        OutputStream outputStream = new FileOutputStream("invoices/Receipts/" + receiptID + ".pdf");
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        Image logo = Image.getInstance("src/assets/logo.png");
        logo.scalePercent(10f);
        logo.setAlignment(Image.ALIGN_CENTER);
        document.add(logo);

        Paragraph companyName1 = new Paragraph(companyName);
        companyName1.setAlignment(companyName1.ALIGN_CENTER);
        document.add(companyName1);

        Paragraph companyAddress1 = new Paragraph(companyAddress);
        companyAddress1.setAlignment(companyAddress1.ALIGN_CENTER);
        document.add(companyAddress1);

        Paragraph companyContacts = new Paragraph(companyPhone + " || " + companyEmail);
        companyContacts.setAlignment(companyContacts.ALIGN_CENTER);
        document.add(companyContacts);

        Paragraph receiptDetails = new Paragraph("Receipt Number: " + receiptID, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        receiptDetails.add("\nReceipt Date: " + new Date());
        document.add(receiptDetails);

        // Customer Information
        Paragraph customerInfo = new Paragraph("Payer:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        document.add(customerInfo);
        customerInfo = new Paragraph(customerName + "\n" + customerAddress + "\n" + "Meter Number: " + meterNumber);
        document.add(customerInfo);

        // Billing Period
        Paragraph billingPeriodSection = new Paragraph("Billing Period:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        billingPeriodSection.add(new Phrase(billingPeriod));
        document.add(billingPeriodSection);

        // Meter Readings and Usage
        Paragraph prevReading = new Paragraph("Previous Reading:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        prevReading.add(new Phrase(previousMeterReading, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(prevReading);

        Paragraph curReading = new Paragraph("Current Reading:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        curReading.add(new Phrase(currentMeterReading, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(curReading);

        Paragraph waterCon = new Paragraph("Water Consumption:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        waterCon.add(new Phrase(String.valueOf(waterUsage), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(waterCon);

        int penaltyAmount = 0;
        if (penalty != null) {
            Paragraph pAmount = new Paragraph("Penalty Charges:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
            pAmount.add(new Phrase(String.valueOf( penalty.getPamount()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            document.add(pAmount);

            penaltyAmount = penalty.getPamount();

            Paragraph penaltyDate = new Paragraph("Penalty Date:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
            penaltyDate.add(new Phrase(String.format("P%d", penalty.getPdate()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            document.add(penaltyDate);
            totalAmount += totalAmount + penaltyAmount;
        }

        Paragraph totalAmountSection = new Paragraph("Total Amount Paid:", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        totalAmountSection.add(new Phrase(String.format("P%.2f", totalAmount ), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
        document.add(totalAmountSection);

        // Add payment information
        document.add(new Paragraph("Payment Amount: " +String.valueOf(payment.getPaymentAmount())));  // Add payment date
        document.add(new Paragraph("Changes: " +String.valueOf(payment.getPaymentAmount()-totalAmount)));  // Add payment date
        document.add(new Paragraph("Payment Received on: " + new Date()));  // Add payment date
        document.add(new Paragraph("Payment method: Cash"));  // Add payment method

        // Close the document
        document.close();
        outputStream.close();
        System.out.println("Payment receipt generated successfully!");
        try {
            Desktop.getDesktop().open(new File("invoices/Receipts/" + receiptID + ".pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}
