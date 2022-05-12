package TestMaven;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DB_Conn {
    //String connectionString = System.getProperty("mongodb.uri");
    MongoClient client = MongoClients.create("mongodb+srv://testuser:vzftoQDZuAO4mxXX@cluster0.byrc8.mongodb.net/test");
    MongoDatabase database = client.getDatabase("stageIndia");

    public JSONObject Person(String ID) throws ParseException {

        MongoCollection<Document> toys = database.getCollection("dTDPerson");
        //System.out.println(toys);
        BasicDBObject obj = new BasicDBObject();
        obj.append("link.target.identifier.value", ID);
        JSONObject json = null;
        FindIterable<Document> iterDoc = toys.find(obj);
        for (Document document : iterDoc) {
            JsonWriterSettings settings = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();
            String q=document.toJson(settings);
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(q);

            //System.out.println(json);

        }
        return json;
    }

    public JSONObject qResponse(String ID) throws ParseException {


        MongoCollection<Document> toys = database.getCollection("dTDQuestionnaireResponse");

        BasicDBObject obj = new BasicDBObject();
        obj.append("_id", ID);
        JSONObject json = null;
        FindIterable<Document> iterDoc = toys.find(obj);
        for (Document document : iterDoc) {
            JsonWriterSettings settings = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();
            String q=document.toJson(settings);
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(q);

            //System.out.println(json);

        }
        return json;
    }


    public JSONObject Questionnaire(String quesRef) throws ParseException {

        MongoCollection<Document> toys = database.getCollection("dTDQuestionnaire");

        BasicDBObject obj = new BasicDBObject();
        obj.append("_id", quesRef);
        JSONObject json = null;
        FindIterable<Document> iterDoc = toys.find(obj);
        for (Document document : iterDoc) {
            JsonWriterSettings settings = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();
            String q=document.toJson(settings);
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(q);

            //System.out.println(json);

        }
        return json;
    }

}
