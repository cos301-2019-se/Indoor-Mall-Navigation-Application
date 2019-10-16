/*  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author           Changes
 *  --------------------------------------------
 *  10/09/2019  Mpho Mashaba    Original
 *
 *  Functional Description: This program file allows users to add card information for payment
 *  Error Messages: None
 *  Constraints: Can only be used if user adds credit card information
 *  Assumptions: It is assumed that the user will be able to add their card information successfully .
 *
 */

package com.example.navigator;
import android.app.ProgressDialog;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.braintreepayments.cardform.view.CardForm;
import com.example.navigator.utils.DatabaseConn;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.navigator.MainHelpers.GMailSender;

import entities.CartProduct;

import static com.example.navigator.Cart.deviceId;

public class CardInfo extends AppCompatActivity implements View.OnClickListener {
    CardForm cardForm;
    Button buy;
    AlertDialog.Builder alertBuilder;
    PdfWriter writer;
   // final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardinformation);



        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy);

       /* buy.setEnabled(false);
        if(checkPermission(Manifest.permission.SEND_SMS)){
            buy.setEnabled(true);
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
        } */

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(CardInfo.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    alertBuilder = new AlertDialog.Builder(CardInfo.this);
                    alertBuilder.setTitle("Confirm before purchase");
                    alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                            "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                            "Card CVV: " + cardForm.getCvv() + "\n" +
                            "Postal code: " + cardForm.getPostalCode() + "\n" +
                            "Phone number: " + cardForm.getMobileNumber() + "\n" +
                            "Total price: R" + Cart.oTotal );
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Toast.makeText(CardInfo.this, "Thank you for purchase", Toast.LENGTH_LONG).show();

                            String curDateTime = getDateTime();
                            String curFullDateTime = getFullDateTime();
                            createClientInvoice(Cart.products, curDateTime, curFullDateTime, Login.user.getEmail());

                            List<CartProduct> tempArray = Cart.products;
                            List<CartProduct> outgoingArray = new ArrayList<>();
                            while(!tempArray.isEmpty()){
                                String activeShop;
                                outgoingArray.clear();
                                if(tempArray.size() > 1) {
                                    CartProduct curr = tempArray.get(0);
                                    tempArray.remove(0);
                                    outgoingArray.add(curr);
                                    activeShop = curr.getStoreResult();

                                    for (int j = 1; j < tempArray.size(); j++) {
                                        if (tempArray.get(j).getStoreResult().equals(activeShop)) {
                                            outgoingArray.add(tempArray.get(j));
                                            tempArray.remove(j);
                                            j--;
                                        }
                                    }
                                }
                                else{
                                    outgoingArray.add(tempArray.get(0));
                                    activeShop = tempArray.get(0).getStoreResult();
                                    tempArray.remove(0);
                                }
                                createMerchantsReceipt(outgoingArray, curDateTime, curFullDateTime, Login.user.getEmail(), activeShop);
                            }


                            String smsMessage = "Indoor Mall Navigation Payments, Thank You For Shopping With Us!";


                            DatabaseConn data = DatabaseConn.open();
                            data.delete("Cart", deviceId);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));


                            //Sending Text Message
                            if(checkPermission(Manifest.permission.SEND_SMS)){
                                SmsManager.getDefault().sendTextMessage(cardForm.getMobileNumber(),null,smsMessage,null,null);
                            }
                            else{
                                Toast.makeText(CardInfo.this, "SMS Permission Denied", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }


                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(CardInfo.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });
     }
    @Override
    public void onClick(View view) {

    }
/*
    public void sendSmsByManager() {
        try {
            // Get the default instance of the SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber.getText().toString(),
                    null,
                    smsBody.getText().toString(),
                    null,
                    null);
            Toast.makeText(getApplicationContext(), "Your sms has successfully sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),"Your sms has failed...",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
*/

    public  boolean checkPermission(String permission)
    {
        int check = ContextCompat.checkSelfPermission(this,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }


    private void createClientInvoice(List<CartProduct> products, String invoceNum, String date, String customerName){

        Document document = new Document();
        PdfPTable table = new PdfPTable(new float[] { 3, 2, 1, 1, 1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Description");
        table.addCell("Shop");
        table.addCell("Unit Cost");
        table.addCell("Qty");
        table.addCell("Amount");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j=0;j<cells.length;j++){
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }

        for (int i = 0; i < products.size(); i++){
            table.addCell(products.get(i).getName());
            table.addCell(products.get(i).getStoreResult());
            table.addCell("R" + products.get(i).getPrice());
            table.addCell("" + products.get(i).getQuantity());
            double no = Double.valueOf(products.get(i).getPrice()) * Integer.valueOf(products.get(i).getQuantity());
            DecimalFormat dec = new DecimalFormat("#0.00");
            table.addCell("R" + dec.format(no));
        }

        try {

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "Indoor Mall Navigator");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Indoor Mall Navigator/";
                writer = PdfWriter.getInstance(document, new FileOutputStream(directory_path + "Invoice" + invoceNum + ".pdf"));
                document.open();

                Font H1=new Font(Font.FontFamily.TIMES_ROMAN,30.0f, Font.BOLD, BaseColor.BLACK);
                Paragraph p = new Paragraph();

                Drawable d = getResources().getDrawable(R.drawable.logo3);
                BitmapDrawable bitDw = ((BitmapDrawable) d);
                Bitmap bmp = bitDw.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                Chunk c = new Chunk(image, 0, -90);
                p.add(c);
                document.add(p);

                p = new Paragraph("INVOICE", H1);
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);
                p = new Paragraph("Indoor Mall Navigator");
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);
                p = new Paragraph("Invoice No. " + invoceNum);
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);
                p = new Paragraph("Date: " + date);
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);
                p = new Paragraph("Billed To: " + customerName);
                p.setAlignment(Paragraph.ALIGN_LEFT);
                document.add(p);
                p = new Paragraph("Customer Tel.: " + cardForm.getMobileNumber());
                p.setAlignment(Paragraph.ALIGN_LEFT);
                document.add(p);
                p = new Paragraph("-");
                document.add(p);

                absoluteText("---------------------------------------------------------", 360, 40);

                DecimalFormat dec = new DecimalFormat("#0.00");
                absoluteText("Total: R" + dec.format(Cart.oTotal), 400, 20);

                document.add(table);
                document.close();
                Toast.makeText(getBaseContext(), "Invoice Generation Done", Toast.LENGTH_LONG).show();
            } else {
                // Do something else on failure
            }


        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private void createMerchantsReceipt(List<CartProduct> products, String invoceNum, String date, String customerName, String merchantName){

        Document document = new Document();
        PdfPTable table = new PdfPTable(new float[] { 5, 1, 1, 1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Description");
        table.addCell("Unit Cost");
        table.addCell("Qty");
        table.addCell("Amount");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j=0;j<cells.length;j++){
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }

        double activeTotal = 0;
        for (int i = 0; i < products.size(); i++){
            table.addCell(products.get(i).getName());
            table.addCell("R" + products.get(i).getPrice());
            table.addCell("" + products.get(i).getQuantity());
            double no = Double.valueOf(products.get(i).getPrice()) * Integer.valueOf(products.get(i).getQuantity());
            DecimalFormat dec = new DecimalFormat("#0.00");
            table.addCell("R" + dec.format(no));
            activeTotal += Double.parseDouble(products.get(i).getTotalPrice());
        }

        try {
            String filename;
            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "Indoor Mall Navigator/MerchantInvoices");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Indoor Mall Navigator/MerchantInvoices/";
                filename = merchantName + "Invoice" + invoceNum + ".pdf";
                writer = PdfWriter.getInstance(document, new FileOutputStream(directory_path + filename));
                document.open();

                Font H1=new Font(Font.FontFamily.TIMES_ROMAN,30.0f, Font.BOLD, BaseColor.BLACK);
                Paragraph p = new Paragraph();

                Drawable d = getResources().getDrawable(R.drawable.logo3);
                BitmapDrawable bitDw = ((BitmapDrawable) d);
                Bitmap bmp = bitDw.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                Chunk c = new Chunk(image, 0, -90);
                p.add(c);
                document.add(p);

                p = new Paragraph(merchantName.toUpperCase() +" INVOICE", H1);
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);
                p = new Paragraph("Indoor Mall Navigator");
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);
                p = new Paragraph("Invoice No. " + invoceNum);
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);
                p = new Paragraph("Date: " + date);
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);
                p = new Paragraph("Billed To: " + customerName);
                p.setAlignment(Paragraph.ALIGN_LEFT);
                document.add(p);
                p = new Paragraph("-");
                document.add(p);

                absoluteText("---------------------------------------------------------", 360, 40);
                DecimalFormat dec = new DecimalFormat("#0.00");
                absoluteText("Total: R" + dec.format(activeTotal), 400, 20);

                document.add(table);
                document.close();
                //Toast.makeText(getBaseContext(), "Invoice Generation Done", Toast.LENGTH_LONG).show();
                sendMessage(filename);
            } else {
                // Do something else on failure
            }



        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private  final static String getDateTime()
    {
        DateFormat df = new SimpleDateFormat("yyMMddhhmm");
        return df.format(new Date());
    }

    private  final static String getFullDateTime()
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return df.format(new Date());
    }

    private void absoluteText(String text, int x, int y) {
        try {
            PdfContentByte cb = writer.getDirectContent();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.saveState();
            cb.beginText();
            cb.moveText(x, y);
            cb.setFontAndSize(bf, 12);
            cb.showText(text);
            cb.endText();
            cb.restoreState();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(final String filename) {
        /*final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
        dialog.setTitle("Sending Merchant Invoices");
        dialog.setMessage("Please wait");
        dialog.show();*/
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Indoor Mall Navigator/MerchantInvoices/" + filename;
                    GMailSender sender = new GMailSender("brute.force.cos301@gmail.com", "Dr0n3s&Stuff");
                    sender.sendMail("EmailSender App",
                            "Merchant Invoices from Indoor Mall Navigator",
                            "brute.force.cos301@gmail.com",
                            "brute.force.cos301@gmail.com", directory_path);
                    //dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }
}
