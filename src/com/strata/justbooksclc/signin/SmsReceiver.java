package com.strata.justbooksclc.signin;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle extras = intent.getExtras();
		if (extras == null)
			return;

		// To display a Toast whenever there is an SMS.
		// Toast.makeText(context,"Recieved",Toast.LENGTH_SHORT).show();

		Object[] pdus = (Object[]) extras.get("pdus");
		for (int i = 0; i < pdus.length; i++) {
			SmsMessage SMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
			String body = SMessage.getMessageBody().toString();
			Intent in = new Intent("SmsMessage.intent.MAIN").putExtra("get_msg",body);
			context.sendBroadcast(in);

			/*String sender = SMessage.getOriginatingAddress();
			String[] splited = sender.split("-");
			if (splited[0].equals("AD") || (splited[0].equals("AM") || splited[0].equals("LM") || splited[0].equals("MD") || splited[0].equals("BZ") || splited[0].equals("DM")) && (splited[1].equals("STRATA") || splited[1].equals("JBOOKS"))){
			 A custom Intent that will used as another Broadcast
			}
			 This is used to abort the broadcast and can be used to silently
			 process incoming message and prevent it from further being
			 broadcasted. Avoid this, as this is not the way to program an
			 app.
			 this.abortBroadcast();*/
		}
	}
}
