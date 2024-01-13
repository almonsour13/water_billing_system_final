import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateReceipt {
    public void generate(){
        createReceiptPDF("John Doe", "123 Main St", "A123456789", 1000, 1200, 0.25, 30,
                "Credit Card", "TX123456", "Jane Smith", "2024-02-01", 0.05);
    }
    public static void createReceiptPDF(String customerName, String customerAddress, String accountNumber,
                                       int previousReading, int currentReading, double rate, int taxRate,
                                       String paymentMethod, String transactionID, String generatedBy,
                                       String penaltyDate, double penaltyChargePercentage) {

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("WaterBillReceipt.pdf"));
            document.open();

            // Add content to the PDF
            document.add(new Paragraph("----------------------------------------"));
            document.add(new Paragraph("        Water Utility Company", getBoldFont()));
            document.add(new Paragraph("      123 Water Street | City, State, ZIP"));
            document.add(new Paragraph("     Phone: (123) 456-7890 | Email: info@waterutility.com"));

            document.add(new Paragraph("----------------------------------------"));
            document.add(new Paragraph("            WATER BILL RECEIPT", getBoldFont()));

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String currentDate = dateFormat.format(new Date());

            document.add(new Paragraph("Date: " + currentDate));
            document.add(new Paragraph("Receipt #: " + generateReceiptNumber()));

            document.add(new Paragraph("----------------------------------------"));
            document.add(new Paragraph("        CUSTOMER INFORMATION", getBoldFont()));
            document.add(new Paragraph("Name: " + customerName));
            document.add(new Paragraph("Address: " + customerAddress));
            document.add(new Paragraph("Account Number: " + accountNumber));

            document.add(new Paragraph("----------------------------------------"));
            document.add(new Paragraph("         BILLING DETAILS", getBoldFont()));
            document.add(new Paragraph("Meter Reading (Previous): " + previousReading));
            document.add(new Paragraph("Meter Reading (Current): " + currentReading));
            document.add(new Paragraph("Usage: " + (currentReading - previousReading) + " Gallons"));
            document.add(new Paragraph("Rate: $" + rate + " per Gallon"));

            document.add(new Paragraph("----------------------------------------"));
            document.add(new Paragraph("       WATER USAGE CHARGES", getBoldFont()));
            double totalUsageCharges = (currentReading - previousReading) * rate;
            document.add(new Paragraph("Usage Charges: $" + totalUsageCharges));

            // Add Penalty Section based on Penalty Date
            double penaltyAmount = calculatePenalty(totalUsageCharges, penaltyDate, penaltyChargePercentage);
            if (penaltyAmount > 0) {
                document.add(new Paragraph("              PENALTY", getBoldFont()));
                document.add(new Paragraph("Penalty Amount: $" + penaltyAmount));
            }

            document.add(new Paragraph("----------------------------------------"));
            document.add(new Paragraph("       SUBTOTAL:          $" + totalUsageCharges));
            double taxAmount = (taxRate / 100.0) * totalUsageCharges;
            document.add(new Paragraph("       TAX (" + taxRate + "%):          $" + taxAmount));

            // Include penalty in the total amount
            double totalAmount = totalUsageCharges + taxAmount + penaltyAmount;
            document.add(new Paragraph("       TOTAL AMOUNT:      $" + totalAmount));

            document.add(new Paragraph("----------------------------------------"));
            document.add(new Paragraph("        PAYMENT DETAILS", getBoldFont()));
            document.add(new Paragraph("Payment Method: " + paymentMethod));
            document.add(new Paragraph("Transaction ID: " + transactionID));

            document.add(new Paragraph("----------------------------------------"));
            document.add(new Paragraph("      THANK YOU FOR YOUR PAYMENT!", getBoldFont()));

            document.add(new Paragraph("   For any inquiries, please contact:"));
            document.add(new Paragraph("   Customer Support Phone/Email"));

            document.add(new Paragraph("----------------------------------------"));

            // Added the name of the person generating the receipt
            document.add(new Paragraph("Generated by: " + generatedBy));

            document.close();
            System.out.println("Receipt created successfully!");

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateReceiptNumber() {
        // You may implement your own logic to generate a unique receipt number
        return "RCT" + System.currentTimeMillis();
    }

    private static double calculatePenalty(double totalUsageCharges, String penaltyDate, double penaltyChargePercentage) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
            Date penaltyDateParsed = dateFormat.parse(penaltyDate);

            if (currentDate.after(penaltyDateParsed)) {
                // Apply penalty charge if the current date is after the penalty date
                return totalUsageCharges * penaltyChargePercentage;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static Font getBoldFont() throws Exception {
        // Using a base font that supports bold
        BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        return new Font(baseFont, 12);
    }
}
