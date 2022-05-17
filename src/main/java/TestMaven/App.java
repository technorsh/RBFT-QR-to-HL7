package TestMaven;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v27.message.MDM_T02;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;


// Handler value: example.Handler
public class App implements RequestHandler<String, String>{
    @Override
    public String handleRequest(String qrID, Context context) {

        Sub_App Sub = new Sub_App();

        AmazonS3 s3 = AmazonS3Client.builder().withRegion(Regions.AP_SOUTH_1).withForceGlobalBucketAccessEnabled(true).build();

        String Encoded = null;

        DB_Conn DB = new DB_Conn();
        JSONObject qr = null;
        try {
            qr = DB.qResponse(qrID);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (qr != null) {
            try {
                Encoded = (String) Sub.quesResponse(qr);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            JSONObject p_data= Sub.person_data;
            if (Encoded != null && Encoded != "1" && Encoded != "2") {
                try {
                    Message_Creation MSG=new Message_Creation();
                    MDM_T02 msg = MSG.createHL7(Encoded, p_data);
                    FileWriter myWriter = new FileWriter("/tmp/MDM.txt");
                    myWriter.write(String.valueOf(msg));
                    myWriter.close();
                    s3.putObject(
                            "qr2hl7",
                            "output/MDM.txt",
                            new File("/tmp/MDM.txt")
                    );

                } catch (HL7Exception | IOException | java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
            if (Objects.equals(Encoded, "1")) {
                System.out.println("Questionnaire Data not found!");
            }
            if (Objects.equals(Encoded, "2")) {
                System.out.println("Patient Details not found!");
            }
        }
        else{
            System.out.println("Questionnaire Response not found!");
        }
            return String.valueOf(qrID);
    }

}