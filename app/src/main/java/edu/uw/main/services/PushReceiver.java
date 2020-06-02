package edu.uw.main.services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;

import edu.uw.main.AuthActivity;
import edu.uw.main.R;
import edu.uw.main.ui.chat.ChatMessage;

import edu.uw.main.ui.connection.Add;
import edu.uw.main.ui.connection.Pending;
import edu.uw.main.ui.connection.Sent;
import me.pushy.sdk.Pushy;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

/**
 *  This class will monitor push reciever for messages.
 *  Extends the Broadcast reciever.
 *  @author Group 3
 *  @version 5/19
 */
public class PushReceiver extends BroadcastReceiver {

    public static final String RECEIVED_NEW_MESSAGE = "new message from pushy";

    private static final String CHANNEL_ID = "1";

    public static final String RECEIVED_NEW_REQUEST = "You have been sent a friend request";
    public static final String RECEIVED_NEW_ACCEPTANCE = "Your friend request has been accepted";
    public static final String RECEIVED_NEW_DECLINE = "Your friend reqeuest has been declined";
    public static final String RECEIVED_NEW_REMOVE = "A contact has removed you.";



    @Override
    public void onReceive(Context context, Intent intent) {

        //the following variables are used to store the information sent from Pushy
        //In the WS, you define what gets sent. You can change it there to suit your needs
        //Then here on the Android side, decide what to do with the message you got

        //for the lab, the WS is only sending chat messages so the type will always be msg
        //for your project, the WS needs to send different types of push messages.
        //So perform logic/routing based on the "type"
        //feel free to change the key or type of values.
        Log.e("PUSHY DATA", intent.getStringExtra("message"));

        String typeOfMessage = intent.getStringExtra("type");
        Log.e("PUSHY TYPE1", typeOfMessage);

        ChatMessage message = null;
        int chatId = -1;

        Pending request = null;
        Add accept = null;
        String decline = null;
        String remove = null;

        //Moderate between different Push Receiver Channels.
        if(typeOfMessage.equals("msg")){
            try{
                Log.e("Check", "Message sent");
                message = ChatMessage.createFromJsonString(intent.getStringExtra("message"));
                chatId = intent.getIntExtra("chatid", -1);
            } catch (JSONException e) {
                //Web service sent us something unexpected...I can't deal with this.
                throw new IllegalStateException("Error from Web Service. Contact Dev Support");
            }
        } else if(typeOfMessage.equals("request")){
            try{
                request = Pending.createFromJsonString(intent.getStringExtra("message"));

            } catch (JSONException e) {
                //Web service sent us something unexpected...I can't deal with this.
                throw new IllegalStateException("Error from Web Service. Contact Dev Support");
            }
        } else if(typeOfMessage.equals("accept")){
            try{
                accept = Add.createFromJsonString(intent.getStringExtra("message"));

            } catch (JSONException e) {
                //Web service sent us something unexpected...I can't deal with this.
                throw new IllegalStateException("Error from Web Service. Contact Dev Support");
            }
        }else if(typeOfMessage.equals("decline")){
            decline = intent.getStringExtra("message");
        }else if(typeOfMessage.equals("remove")){
            remove = intent.getStringExtra("message");
        }


        //Handle different results
        if(typeOfMessage.equals("msg")){
            Log.e("CHECK MSG INPUT", "TRUE");
            handleChat(context, intent, message, chatId);

        } else if(typeOfMessage.equals("request")){
            handleRequest(context, intent, request);
        }else if(typeOfMessage.equals("accept")){
            handleAccept(context, intent, accept);
        } else if(typeOfMessage.equals("decline")) {
            handleDecline(context, intent, decline);
        }else if(typeOfMessage.equals("remove")) {
            handleRemove(context, intent, remove);
        }


    }
    private void handleRemove(Context context, Intent intent, String remove){
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        Log.e("REQUEST DATA", remove);

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            //app is in the foreground so send the message to the active Activities
            Log.d("PUSHY", "The following has declined your request: " + remove);

            //create an Intent to broadcast a message to other parts of the app.
            Intent i = new Intent(RECEIVED_NEW_REMOVE);
            i.putExtra("remove", remove);

            i.putExtras(intent.getExtras());
            Log.e("INTENT SENT FROM PUSHY: ", "TRUE");
            context.sendBroadcast(i);

        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "The user has declined your request: " + remove);

            Intent i = new Intent(context, AuthActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_chat_notification)
                    .setContentTitle(remove + "Has removed you from the friends list")
                    .setContentText(remove + "Has been removed from the friends list")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a ChatMessageNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }

    }
    private void handleDecline(Context context, Intent intent, String decline){
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        Log.e("REQUEST DATA", decline);

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            //app is in the foreground so send the message to the active Activities
            Log.d("PUSHY", "The following has declined your request: " + decline);

            //create an Intent to broadcast a message to other parts of the app.
            Intent i = new Intent(RECEIVED_NEW_DECLINE);
            i.putExtra("decline", decline);

            i.putExtras(intent.getExtras());
            Log.e("INTENT SENT FROM PUSHY: ", "TRUE");
            context.sendBroadcast(i);

        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "The user has declined your request: " + decline);

            Intent i = new Intent(context, AuthActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_chat_notification)
                    .setContentTitle(decline + "Has declined your friend request")
                    .setContentText(decline + "Has declined your friend request")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a ChatMessageNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }

    }
    private void handleAccept(Context context, Intent intent, Add request){
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        Log.e("REQUEST DATA", request.toString());

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            //app is in the foreground so send the message to the active Activities
            Log.d("PUSHY", "Request received in foreground: " + request);

            //create an Intent to broadcast a message to other parts of the app.
            Intent i = new Intent(RECEIVED_NEW_ACCEPTANCE);
            i.putExtra("accept", request.getUsername());

            i.putExtras(intent.getExtras());
            Log.e("INTENT SENT FROM PUSHY: ", "TRUE");
            context.sendBroadcast(i);

        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "Message received in background: " + request.getUsername());

            Intent i = new Intent(context, AuthActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_chat_notification)
                    .setContentTitle(request.getUsername() + "Has accepted your friend request")
                    .setContentText(request.getUsername() + "Has accepted your friend request")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a ChatMessageNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }

    }
    private void handleRequest(Context context, Intent intent, Pending request){
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        Log.e("REQUEST DATA", request.toString());

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            //app is in the foreground so send the message to the active Activities
            Log.d("PUSHY", "Request received in foreground: " + request);

            //create an Intent to broadcast a message to other parts of the app.
            Intent i = new Intent(RECEIVED_NEW_REQUEST);
            i.putExtra("username", request.getUsername());
            i.putExtras(intent.getExtras());

            context.sendBroadcast(i);

        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "Message received in background: " + request.getUsername());

            Intent i = new Intent(context, AuthActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_chat_notification)
                    .setContentTitle( "New Friend Request from: " + request.getUsername())
                    .setContentText("You have a pending friend request from: " + request.getUsername())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a ChatMessageNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }

    }

    private void handleChat(Context context, Intent intent, ChatMessage message, int chatId){
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        Log.e("REQUEST DATA", message.toString());

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            //app is in the foreground so send the message to the active Activities
            Log.d("PUSHY", "Message received in foreground: " + message);

            //create an Intent to broadcast a message to other parts of the app.
            Intent i = new Intent(RECEIVED_NEW_MESSAGE);
            i.putExtra("chatMessage", message);
            i.putExtra("chatid", chatId);
            i.putExtras(intent.getExtras());

            context.sendBroadcast(i);

        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "Message received in background: " + message.getMessage());

            Intent i = new Intent(context, AuthActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_chat_notification)
                    .setContentTitle("Message from: " + message.getSender())
                    .setContentText(message.getMessage())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a ChatMessageNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }
    }
}
