package TestMaven;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Objects;

public class Person {

        public JSONObject person(JSONObject jsonOb, Document document) throws DocumentException {
            JSONObject p_data = new JSONObject();
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);

            Object PName = jsonOb.get("name");
            JSONArray h = (JSONArray) PName;

            JSONObject name = (JSONObject) h.get(0);
            JSONArray GIVen = (JSONArray) name.get("given");
            String GName=(String) GIVen.get(0);
            String FName=(String) name.get("family");
            p_data.put("GName",GName);
            p_data.put("FName",FName);
            Paragraph Name= new Paragraph(" Name:  " + GName+" "+FName,font);
            document.add(Name);
            Name.setSpacingAfter(8);
            // document.add(new Paragraph("Name: " + GName+" "+FName));

            Object PTelecom = jsonOb.get("telecom");
            JSONArray Pt = (JSONArray) PTelecom;

            for (Object o : Pt) {
                JSONObject p = (JSONObject) o;

                String contactType=(String)p.get("system");
                String Data=(String) p.get("value");

                if (Objects.equals(contactType, "email")) {
                    if (!Objects.equals(Data, "")) {
                        p_data.put("email", p.get("value"));
                        Paragraph Email= new Paragraph(" Email:  " + p.get("value"),font);
                        document.add(Email);
                        Email.setSpacingAfter(8);
                    }
                }
                if (Objects.equals(contactType, "phone")) {
                    if(!Objects.equals(Data, "")) {
                        p_data.put("phone", p.get("value"));
                        Paragraph Phone= new Paragraph(" Phone:  " + p.get("value"),font);
                        document.add(Phone);
                        Phone.setSpacingAfter(8);
                    }
                }
            }

            String Gen= (String) jsonOb.get("gender");
            p_data.put("gender",Gen);
            Paragraph gender= new Paragraph(" Gender:  " + Gen,font);
            document.add(gender);
            gender.setSpacingAfter(8);

            JSONObject DOB= (JSONObject) jsonOb.get("birthDate");
            p_data.put("birthDate",DOB.get("$date"));
            Paragraph dob= new Paragraph(" Birthdate:  " + DOB.get("$date"),font);
            document.add(dob);
            dob.setSpacingAfter(8);

            Object PAdd = jsonOb.get("address");
            JSONArray Padd = (JSONArray) PAdd;
            JSONObject PAddress = (JSONObject) Padd.get(0);
            JSONArray Line = (JSONArray) PAddress.get("line");
            Paragraph Address= new Paragraph(" Address:  " + Line.get(0) +" "+ PAddress.get("city")+", "+ PAddress.get("state") +", "+ PAddress.get("country") +", "+ PAddress.get("postalCode"), font);
            document.add(Address);
            Address.setSpacingAfter(8);
            p_data.put("AddressLine",Line.get(0));
            p_data.put("city",PAddress.get("city"));
            p_data.put("state",PAddress.get("state"));
            p_data.put("country", PAddress.get("country"));
            p_data.put("postalCode", PAddress.get("postalCode"));


            JSONObject MOrg= (JSONObject) jsonOb.get("managingOrganization");
            String MO=(String) MOrg.get("display");
            Paragraph mo= new Paragraph(" Managing Organization:  "+MO,font);
            document.add(mo);
            mo.setSpacingAfter(8);

            return p_data;
        }
}
