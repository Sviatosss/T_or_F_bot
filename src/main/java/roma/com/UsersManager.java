package roma.com;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by sviatosss on 18.12.2018.
 */
public class UsersManager {
    private static UsersManager sInstance;
    public MongoCollection<Document>  mUsersCollection = DataBaseManager.getInstance().getmUsersCollection();

    public static UsersManager getInstance() {
        if (sInstance == null) {
            sInstance = new UsersManager();
        }

        return sInstance;
    }

    public void issetUser(Update update){
        String id = Functions.getInstance().getId(update);
        Document query = new Document("id", id);
        Document user = mUsersCollection.find(query).first();

        if (user == null) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("Ви долучились - " + Functions.getInstance().getDete());
            Document newUser = new Document("id", id)
                    .append("firstName", update.getMessage().getChat().getFirstName())
                    .append("lastName", update.getMessage().getChat().getLastName())
                    .append("username", update.getMessage().getChat().getUserName())
                    .append("points", 0)
                    .append("current_query", "");
            mUsersCollection.insertOne(newUser);
            System.out.println("New user");
        }
    }

    public void updateQuery(Update update, String newQuest){
        String id = Functions.getInstance().getId(update);
        mUsersCollection.updateOne(eq("id", id), new Document("$set", new Document("current_query", newQuest)));
    }
    public String getQuery(Update update){
        return getUserInfoString(update, "current_query");
    }

    public String getUserInfoString(Update update, String field){
        String id = Functions.getInstance().getId(update);
        Document query = new Document("id", id);
        Document user = mUsersCollection.find(query).first();
        return user.getString(field);
    }

    public int getPoints(Update update){
        String id = Functions.getInstance().getId(update);
        Document query = new Document("id", id);
        Document user = mUsersCollection.find(query).first();
        return user.getInteger("points");
    }
    public void addPoint(Update update){
        String id = Functions.getInstance().getId(update);
        mUsersCollection.updateOne(eq("id", id), new Document("$set", new Document("points", (getPoints(update) + 1))));
    }

    public Boolean getUserInfoBoolean(Update update, String field){
        String id = Functions.getInstance().getId(update);
        Document query = new Document("id", id);
        Document user = mUsersCollection.find(query).first();
        return user.getBoolean(field);
    }

    public void updateUserInfo(Update update, String field, String value){
        String id = Functions.getInstance().getId(update);
        mUsersCollection.updateOne(eq("id", id), new Document("$set", new Document(field, value)));
    }
    public void updateUserInfo(String id, String field, boolean value){
        mUsersCollection.updateOne(eq("id", id), new Document("$set", new Document(field, value)));
    }

    public ArrayList<String> getStory(Update update){
        String id = Functions.getInstance().getId(update);
        Document query = new Document("id", id);
        Document user = mUsersCollection.find(query).first();
        return (ArrayList<String>) user.get("story");
    }

    public void addStory(Update update, String newStory){
        ArrayList<String> arrayList = UsersManager.getInstance().getStory(update);
        arrayList.add("Допис \uD83D\uDD8D " + Functions.getInstance().getDete() + " в - " + Functions.getInstance().getTime() + "\n\n" + newStory);
        String id = Functions.getInstance().getId(update);
        mUsersCollection.updateOne(eq("id", id), new Document("$set", new Document("story", arrayList)));
    }
}
