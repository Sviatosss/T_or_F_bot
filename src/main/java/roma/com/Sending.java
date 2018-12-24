package roma.com;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * Created by Sviat on 26.10.2018.
 */
public class Sending extends T_or_F_bot {

    public  void sendMsg(Update update, String s){
        if (update.hasCallbackQuery()){
            editMessage(update,s);
        }else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setText(s);
            sendMessage.disableWebPagePreview();
            try {
                execute(sendMessage);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
        }
    }
    public void editMessage(Update update, String answer){
        EditMessageText new_message = new EditMessageText()
                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setMessageId(toIntExact(update.getCallbackQuery().getMessage().getMessageId()))
                .setText(answer);
        try {
            execute(new_message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void keyboards(Update update, ArrayList<String> comandList){
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.enableMarkdown(true);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboar = new ArrayList<>();


        KeyboardRow row = new KeyboardRow();
        for (String comand:  comandList) {
            row.add(comand);
        }
        keyboar.add(row);
        replyKeyboardMarkup.setKeyboard(keyboar);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setText("Вибиріть дію з меню ⬇️ ");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.disableWebPagePreview();
        try {
            execute(sendMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void sendingBootomNavs(Update update){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Зіграти в гру");
        arrayList.add("Додати тест");
        arrayList.add("Інструкція");
        keyboards(update, arrayList);
    }
    public void sendButtons(Update update){
        SendMessage message = InlineKeyboardBuilder.create(update.getMessage().getChat().getId())
                .setText("Правда чи брехня ???")
                .row()
                .button("Правда", "set_true")
                .button("Брехня", "set_false")
                .endRow()
                .build();
        try {
            // Send the message
            sendApiMethod(message);
            //execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendTest(Update update, Test test){
        String answer_one;
        String answer_two;
        if (test.isTrue){
            answer_one = "you_win";
            answer_two = "you_lose";
        }else {
            answer_one = "you_lose";
            answer_two = "you_win";
        }
        Long id = Long.parseLong(Functions.getInstance().getId(update));
        SendMessage message = InlineKeyboardBuilder.create(id)
                .setText(test.test + "\nПравда чи брехня ???")
                .row()
                .button("Правда", answer_one)
                .button("Брехня", answer_two)
                .endRow()
                .build();
        try {
            // Send the message
            sendApiMethod(message);
            //execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendNext(Update update, String title){
        Long id = Long.parseLong(Functions.getInstance().getId(update));
        SendMessage message = InlineKeyboardBuilder.create(id)
                .setText(title)
                .row()
                .button("Ще якийсь випадковий тест", "random_test")
                .endRow()
                .build();
        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendInstroction(Update update){
        String s = "Інструкція\n" +
                "Використовуйте нижнє меню щоб зіграти в гру !\n" +
                "Набравши достаттню кількість очків додавайте власні тести !";
        sendMsg(update, s);
    }
}
