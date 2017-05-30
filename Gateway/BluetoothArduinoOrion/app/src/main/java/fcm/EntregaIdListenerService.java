package fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by wasuf on 24/03/2017.
 */

public class EntregaIdListenerService extends FirebaseInstanceIdService {

    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("was", "Refreshed token: " + refreshedToken);
        //enviarRegistrationIdParaServidor(refreshedToken);

         // TODO: Implement this method to send any registration to your app's servers.

    }
}
