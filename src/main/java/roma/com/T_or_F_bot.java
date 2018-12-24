package roma.com;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

public class T_or_F_bot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {
        Sending sending = new Sending();
        if (update.hasMessage() && update.getMessage().hasText()) {
            UsersManager.getInstance().issetUser(update);
            String s = update.getMessage().getText();
            if (s.equals("/start")){
                sending.sendMsg(update, "Hi " + update.getMessage().getChat().getFirstName() + " !");
                sending.sendingBootomNavs(update);
            }else if (s.equals("Додати тест")){
                if (DataBaseManager.getInstance().getmTestsCollection().count() > UsersManager.getInstance().getPoints(update)){
                    sending.sendMsg(update, "У вас не достатньо очків ! \nЩоб додавати тести потрібно мін. " + DataBaseManager.getInstance().getmTestsCollection().count()
                    + "\nУ вас - " + UsersManager.getInstance().getPoints(update) + " очків");
                }else {
                    sending.sendMsg(update,"Введіть тест");
                    UsersManager.getInstance().updateQuery(update, "add_test");
                }
            }else if (UsersManager.getInstance().getQuery(update).equals("add_test")){
                UsersManager.getInstance().updateQuery(update, "test="+s);
                sending.sendButtons(update);
            }else if (s.equals("Зіграти в гру")){
                Test test = TestsManager.getInstance().getRendomTest();
                sending.sendTest(update, test);
            }else if (s.equals("Інструкція")){
                sending.sendInstroction(update);
            }
        }if (update.hasCallbackQuery()){
            String callBack = update.getCallbackQuery().getData();
            if ((callBack.equals("set_true") || callBack.equals("set_false")) && UsersManager.getInstance().getQuery(update).contains("test=")){
                Boolean isTrue =  Boolean.parseBoolean(callBack.replace("set_", ""));
                TestsManager.getInstance().addNewTest(UsersManager.getInstance().getQuery(update).replace("test=", ""), isTrue);
                sending.sendMsg(update, "Тест успішно доданий !");
            }else if (callBack.contains("you_")){
                String title;
                if (callBack.equals("you_win")){
                    title = "Правильна відповідь !";
                    UsersManager.getInstance().addPoint(update);
                }else {
                    title = "Ви лузер";
                }
                sending.sendNext(update, title);
            }else if (callBack.equals("random_test")){
                Test test = TestsManager.getInstance().getRendomTest();
                sending.sendTest(update, test);
            }
        }
    }
    public String getBotUsername() {
        return "T_or_F_bot";
    }
    public String getBotToken() {
        return "759766552:AAEoTHNM1gHpXucunJJVb8wd4r3M5lAgx2c";
    }
}