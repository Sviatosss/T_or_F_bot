package roma.com;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by sviatosss on 24.12.2018.
 */
public class TestsManager {
    private static TestsManager sInstance;
    public MongoCollection<Document> mTestsCollection  = DataBaseManager.getInstance().getmTestsCollection();

    public static TestsManager getInstance() {
        if (sInstance == null) {
            sInstance = new TestsManager();
        }
        return sInstance;
    }

    public void addNewTest(String test, Boolean isTrue){
        Document newTest = new Document("id", mTestsCollection.count())
                .append("test", test)
                .append("is_true", isTrue);
        mTestsCollection.insertOne(newTest);
    }
    public Test getRendomTest(){
        Document query = new Document("id", Functions.getInstance().getRandom(0, (int) mTestsCollection.count()-1 ));
        Document test = mTestsCollection.find(query).first();
        return new Test(test.getLong("id"), test.getString("test"), test.getBoolean("is_true"));
    }
}
