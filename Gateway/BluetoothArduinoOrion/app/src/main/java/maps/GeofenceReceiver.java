package maps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import com.was.led.DeviceConnectActivity;
import com.was.led.DeviceList;
import com.was.led.ledControl;

import java.util.List;

/**
 * Created by wasuf on 27/03/2017.
 */

public class GeofenceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        GeofencingEvent geofencingEvent=GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()){
            int errorCode=geofencingEvent.getErrorCode();
            Toast.makeText(context,"Erro no serviço de localização: "+errorCode,
                    Toast.LENGTH_LONG).show();
        }else{
            int transicao=geofencingEvent.getGeofenceTransition();
            if(transicao== Geofence.GEOFENCE_TRANSITION_ENTER || transicao==Geofence.GEOFENCE_TRANSITION_EXIT){
                List<Geofence> geofences=geofencingEvent.getTriggeringGeofences();
                if(geofences.get(0).getRequestId().equals("1")){
                    if(transicao==Geofence.GEOFENCE_TRANSITION_ENTER){
                        Toast.makeText(context,"Entrou no perímetro do Manancial", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context,"Deixando o perímetro do Manancial\n Adicionando dados do Manancial (localizaçao e Tempo)", Toast.LENGTH_LONG).show();
                    }
                }else if(geofences.get(0).getRequestId().equals("2")){
                    if(transicao==Geofence.GEOFENCE_TRANSITION_ENTER){
                        Toast.makeText(context,"Entrou no perímetro da Cisterna", Toast.LENGTH_LONG).show();
                        Log.d("GEO_WAS","Deveria chamar ledControl");
                        Intent it=new Intent(context, DeviceConnectActivity.class);



                        //it.putExtra("entregaId","1");
                        context.startActivity(it);

                    }else{
                        Toast.makeText(context,"Deixando o perímetro da Cisterna", Toast.LENGTH_LONG).show();
                    }
                }

               /* String acao=transicao==1?"Entrou":"Saiu";
                Toast.makeText(context,
                        "geofence ID: "+geofences.get(0).getRequestId()+" "
                                +acao+" do perímetro", Toast.LENGTH_LONG).show();*/
            }
            else{
                Toast.makeText(context,"Erro no Geofence: "+transicao,Toast.LENGTH_LONG).show();
            }

        }
    }
}
