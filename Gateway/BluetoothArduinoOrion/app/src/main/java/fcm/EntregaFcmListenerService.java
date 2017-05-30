package fcm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.was.controller.RecebimentoActivity;
import com.was.controller.TokenActivity;
import com.was.led.R;

import java.util.Map;

/**
 * Created by wasuf on 24/03/2017.
 */

public class EntregaFcmListenerService extends FirebaseMessagingService{
    public static final int NOTIFICATION_ID = 1;
    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        Map data = message.getData();
        String id= (String) data.get("message");
        int acao=Integer.parseInt((String)data.get("acao"));
        Log.d("ACAO",(String)data.get("acao"));
        //int acao=(int)data.get("acao");
        if (data != null){
            Log.d("was",data.toString());
            dispararNotificacao(id,acao);
            //dispararNotificacao(data.get(new String("mensagem"));
        }
    }
    private void dispararNotificacao(String id,int acao) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        Intent it;
        if(acao==1){
             it = new Intent(this,TokenActivity.class);
        }else{
            it = new Intent(this, RecebimentoActivity.class);
        }

        it.putExtra("entregaId", id);
        startActivity(it);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(it);
        PendingIntent pit = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(false)
                        .setContentIntent(pit)
                        .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.texto_notificacao))
                        .setContentText("Para a Entrega: "+id);
        nm.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
