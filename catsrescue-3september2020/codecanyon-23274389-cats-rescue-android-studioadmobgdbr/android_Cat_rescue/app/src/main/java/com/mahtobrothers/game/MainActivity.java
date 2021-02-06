/*
      All RIght Reserved by Abhishek Mahto
 */

package com.mahtobrothers.game;

import android.os.Bundle;
import org.apache.cordova.*;
import android.util.Log;
import com.google.ads.consent.*;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends CordovaActivity
{
    ConsentForm form;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }

        loadUrl(launchUrl);


        // Here is GDPR Simple Code

        ConsentInformation consentInformation = ConsentInformation.getInstance(MainActivity.this);
        String[] publisherIds = {"pub-9777034540867573"};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                Log.d(TAG,"onConsentInfoUpdated");
                switch (consentStatus){
                    case PERSONALIZED:
                        Log.d(TAG,"PERSONALIZED");
                        ConsentInformation.getInstance(MainActivity.this)
                                .setConsentStatus(ConsentStatus.PERSONALIZED);
                        break;

                    case NON_PERSONALIZED:
                        Log.d(TAG,"NON_PERSONALIZED");
                        ConsentInformation.getInstance(MainActivity.this)
                                .setConsentStatus(ConsentStatus.PERSONALIZED);
                        break;

                    case UNKNOWN:
                        Log.d(TAG,"UNKNOWN");
                        if(ConsentInformation.getInstance(MainActivity.this).isRequestLocationInEeaOrUnknown()){


                            URL privacyUrl = null;
                            try {
                                // TODO: Replace with your app's privacy policy URL.
                                privacyUrl = new URL("https://sites.google.com/view/monkeycrative/accueil");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                                // Handle error.
                            }
                            form = new ConsentForm.Builder(MainActivity.this, privacyUrl)
                                    .withListener(new ConsentFormListener() {
                                        @Override
                                        public void onConsentFormLoaded() {
                                            // Consent form loaded successfully.
                                            Log.d(TAG,"onConsentFormLoaded");
                                            showform();
                                        }

                                        @Override
                                        public void onConsentFormOpened() {
                                            // Consent form was displayed.
                                            Log.d(TAG,"onConsentFormOpened");
                                        }

                                        @Override
                                        public void onConsentFormClosed(
                                                ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                                            // Consent form was closed.
                                            Log.d(TAG,"onConsentFormClosed");
                                        }

                                        @Override
                                        public void onConsentFormError(String errorDescription) {
                                            // Consent form error.
                                            Log.d(TAG,"onConsentFormError");
                                            Log.d(TAG,errorDescription);
                                        }
                                    })
                                    .withPersonalizedAdsOption()
                                    .withNonPersonalizedAdsOption()
                                    .build();

                            form.load();

                        }else{
                            Log.d(TAG,"PERSONALIZED else");
                            ConsentInformation.getInstance(MainActivity.this)
                                    .setConsentStatus(ConsentStatus.PERSONALIZED);
                        }


                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
                Log.d(TAG,"onFailedToUpdateConsentInfo");
                Log.d(TAG,errorDescription);
            }
        });




    }

    private void showform(){
        if (form!=null){
            Log.d(TAG,"show ok");
            form.show();
        }

    }

}

