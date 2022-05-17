package TestMaven;


import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v27.datatype.*;
import ca.uhn.hl7v2.model.v27.group.MDM_T02_OBSERVATION;
import ca.uhn.hl7v2.model.v27.message.MDM_T02;
import ca.uhn.hl7v2.model.v27.segment.MSH;
import ca.uhn.hl7v2.model.v27.segment.OBX;
import ca.uhn.hl7v2.model.v27.segment.PID;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.JsonElement;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

public class Sub_App {

    DB_Conn DB=new DB_Conn();
    String PatientID=null;
    String pdfString=null;
    JSONObject person_data;

    public String questionnaire(JSONObject jsonObject, String ID) throws ParseException {

      try {
            JSONArray questionList = (JSONArray) jsonObject.get("item");

            for (Object objjk : questionList) {

                JSONObject jsonObj = (JSONObject) objjk;
                String li=(String) jsonObj.get("linkId");
                if (Objects.equals(li, ID)){
                    ID= (String) jsonObj.get("text");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ID;
    }


    public Object quesResponse(JSONObject jsonObject) throws ParseException, FileNotFoundException {

        AmazonS3 s3 = AmazonS3Client.builder().withRegion(Regions.AP_SOUTH_1).withForceGlobalBucketAccessEnabled(true).build();

        try {

            Document document = new Document();

            Font Title = new Font(Font.FontFamily.TIMES_ROMAN, 19, Font.UNDERLINE, BaseColor.BLACK);
            Font font1 = new Font(Font.FontFamily.UNDEFINED, 13, Font.UNDERLINE, BaseColor.BLACK);
            Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
            Font QA = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK);

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/tmp/Converted_QRes.pdf"));
            document.setPageSize(PageSize.LETTER);
            document.setMargins(65, 72, 78, 180);

            //Open a blank document

            document.open();

            Rectangle rect= new Rectangle(556,740,50,50); // you can resize rectangle
            rect.enableBorderSide(1);
            rect.enableBorderSide(2);
            rect.enableBorderSide(4);
            rect.enableBorderSide(8);
            rect.setBorderColor(BaseColor.BLACK);
            rect.setBorderWidth(2);
            document.add(rect);

            Paragraph preface = new Paragraph("Questionnaire Response Report",Title);
            preface.setAlignment(Element.ALIGN_CENTER);
            document.add(preface);
            document.add( Chunk.NEWLINE );

            String quesRef;
            quesRef = (String) jsonObject.get("questionnaire");
            String[] qID= quesRef.split("/");
            //System.out.println(ss[3]);
            document.add(new Paragraph("Questionnaire ID:  " + qID[3], font2));
            //System.out.println(qID[3]);
            JSONObject Ques= DB.Questionnaire(qID[3]);
            System.out.println("Questionnaire ID: "+qID[3]);

            String stat = (String) jsonObject.get("status");
            document.add(new Paragraph("Status:  " + stat, font2));
            JSONObject Pat = (JSONObject) jsonObject.get("subject");
            JSONObject Patient= (JSONObject) Pat.get("identifier");
            document.add(new Paragraph("Patient ID:  " + Patient.get("value"), font2));
            System.out.println("Patient ID: "+Patient.get("value"));
            PatientID=(String) Patient.get("value");
            JSONObject person_details= DB.Person(PatientID);
            System.out.println("Person ID: "+person_details.get("_id"));

            if (person_details!=null) {
                if (Ques.get("item") != null) {

                    Person person = new Person();
                    System.out.println("Patient ID: " + PatientID);

                    document.add( Chunk.NEWLINE );
                    Chunk textUnderline = new Chunk("Patient Details",font1);
                    document.add(textUnderline);
                    person_data = person.person(person_details, document);

                    document.add(Chunk.NEWLINE);
                    JSONArray questionList = (JSONArray) jsonObject.get("item");

                    Chunk textUnder = new Chunk("Q&A", font1);
                    document.add(textUnder);

                    for (Object o : questionList) {

                        JSONObject jsonObj = (JSONObject) o;
                        String Link = (String) jsonObj.get("linkId");
                        String ques = questionnaire(Ques, Link);
                        document.add(new Paragraph("->" + " " + ques, QA));
                        JSONArray an = (JSONArray) jsonObj.get("answer");
                        JSONObject answer = (JSONObject) an.get(0);
                        String ans = (String) answer.get("valueString");

                        document.add(new Paragraph("      Ans: " + ans, QA));
                    }

                    document.close();
                    writer.close();
                    s3.putObject(
                            "qr2hl7",
                            "output/Converted_QRes.pdf",
                            new File("/tmp/Converted_QRes.pdf")
                    );

                    // Encoding PDF

                    S3Object objec = s3.getObject(new GetObjectRequest("qr2hl7", "output/Converted_QRes.pdf"));
                    InputStream pdfStream = objec.getObjectContent();
                    byte[] data = pdfStream.readAllBytes();
                    pdfString = Base64.getEncoder().encodeToString(data);
                }else{
                    pdfString="1";
                }
            }else{
                pdfString="2";
            }
            return pdfString;

        } catch (DocumentException | IOException | ParseException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
