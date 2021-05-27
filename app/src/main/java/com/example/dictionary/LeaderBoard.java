package com.example.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LeaderBoard extends Activity {

    private GoogleSignInClient googleSignInClient;
    private LeaderboardsClient leaderboardsClient;
    // private PlayersClient mPlayersClient;

    private static final int RC_SIGN_IN = 9001;
    private static final int RC_UNUSED = 5001;
    private static final int RC_LEADERBOARD_UI = 9004;



    public void signInSilently() {

        googleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                            onConnected(task.getResult());
                        } else {
                            Toast.makeText(getApplicationContext(), "Başarısız", Toast.LENGTH_SHORT).show();
                            onDisconnected();
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                onConnected(account);
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    Log.e("Hata","giriş yapılamadı!");
                }

                onDisconnected();
            }
        }
    }

    private void signOut() {

        if (!isSignedIn()) {

            return;
        }
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onDisconnected();
                    }
                });
    }
    public void onDisconnected() {

        leaderboardsClient = null;
    }

    public void onConnected(GoogleSignInAccount googleSignInAccount) {
        leaderboardsClient = Games.getLeaderboardsClient(this, googleSignInAccount);
    }



    public void startSignInIntent() {
        startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }


    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }


    public void showLeaderBoard() {
        if(isSignedIn())
            leaderboardsClient.getLeaderboardIntent(getString(R.string.leaderboard_id))
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_LEADERBOARD_UI);
                        }
                    });
    }


    public void submitScore(int score) {
        if(isSignedIn())
            leaderboardsClient.submitScore(getString(R.string.leaderboard_id), score);

    }

}
