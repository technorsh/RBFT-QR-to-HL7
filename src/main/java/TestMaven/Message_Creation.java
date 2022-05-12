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
import org.json.simple.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message_Creation {


    private MDM_T02 _mdmMessage;
    Sub_App sub=new Sub_App();

    public MDM_T02 createHL7(String Encoded, JSONObject p_data) throws HL7Exception, IOException, java.text.ParseException {
        String currentDateTimeString = getCurrentTimeStamp();
        _mdmMessage = new MDM_T02();
        //you can use the context class's newMessage method to instantiate a message if you want
        _mdmMessage.initQuickstart("MDM", "T02", "P");
        createMshSegment(currentDateTimeString);
        createPidSegment(p_data);
        createObxSegment(Encoded);
        return _mdmMessage;
    }


    private void createMshSegment(String currentDateTimeString) throws DataTypeException {
        MSH mshSegment = _mdmMessage.getMSH();
        mshSegment.getFieldSeparator().setValue("|");
        mshSegment.getEncodingCharacters().setValue("^~\\&");
        mshSegment.getSendingApplication().getNamespaceID().setValue("DayToDay");
        mshSegment.getSendingFacility().getNamespaceID().setValue("DTD Health");
        mshSegment.getReceivingApplication().getNamespaceID().setValue("Hello");
        mshSegment.getReceivingFacility().getNamespaceID().setValue("DTD Receive");
        //mshSegment.getDateTimeOfMessage().getTimeOfAnEvent().setValue(currentDateTimeString);
        //mshSegment.getMessageControlID().setValue(getSequenceNumber());
        mshSegment.getVersionID().getVersionID().setValue("2.7");
    }

    private void createPidSegment(JSONObject person_data) throws DataTypeException, java.text.ParseException {
        PID pid = _mdmMessage.getPID();
        XPN patientName = pid.getPatientName(0);

        patientName.getFamilyName().getSurname().setValue(String.valueOf(person_data.get("FName")));
        patientName.getGivenName().setValue(String.valueOf(person_data.get("GName")));

        XTN[] Tel;
        Tel = new ca.uhn.hl7v2.model.v27.datatype.XTN[2];
        Tel[1] = pid.getPid13_PhoneNumberHome(0);
        Tel[1].getUnformattedTelephoneNumber().setValue(String.valueOf(person_data.get("telecom")));
        Tel[1].getCommunicationAddress().setValue(String.valueOf(person_data.get("email")));

        DTM PBday = pid.getDateTimeOfBirth();
        String dob = String.valueOf(person_data.get("birthDate"));
        dob = dob.replaceAll("[^0-9A-Z.]", "");
        dob = dob.replaceAll("[^0-9.]", "");

        Date date1 = new SimpleDateFormat("yyyyMMddHHmmss").parse(dob);

        PBday.setValue(date1);

        XAD patientAddress = pid.getPatientAddress(0);
        patientAddress.getStreetAddress().getStreetOrMailingAddress().setValue(String.valueOf(person_data.get("AddressLine")));
        patientAddress.getCity().setValue(String.valueOf(person_data.get("city")));
        patientAddress.getStateOrProvince().setValue(String.valueOf(person_data.get("state")));
        patientAddress.getCountry().setValue(String.valueOf(person_data.get("country")));
        patientAddress.getZipOrPostalCode().setValue(String.valueOf(person_data.get("postalCode")));
    }


    private void createObxSegment(String data) throws HL7Exception, IOException {
        MDM_T02_OBSERVATION obr=_mdmMessage.getOBSERVATION();
        OBX ob=obr.getOBX();
        ob.getObx1_SetIDOBX().setValue("1");
        //OBX 2
        ob.getObx2_ValueType().setValue("ED");
        //OBX 3
        CWE obs=ob.getObservationIdentifier();
        obs.getIdentifier().setValue("Sample_QRes.pdf");
        //OBX 5
        Varies[] pdfData= new Varies[2];
        pdfData[1]=ob.getObx5_ObservationValue(0);
        ED cd=new ED(_mdmMessage);
        pdfData[1].setData(cd);
        cd.getEd2_TypeOfData().setValue("APPLICATION");
        cd.getEd3_DataSubtype().setValue("PDF");
        cd.getEd4_Encoding().setValue("BASE64");
        cd.getEd5_Data().setValue(data);

        //OBX 11
        ob.getObx11_ObservationResultStatus().setValue("F");

    }

    private String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
    private String getSequenceNumber() {
        String facilityNumberPrefix = "1234"; // some arbitrary prefix for the facility
        return facilityNumberPrefix.concat(getCurrentTimeStamp());
    }


}
