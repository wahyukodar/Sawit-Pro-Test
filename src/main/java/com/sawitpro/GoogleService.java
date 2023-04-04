package com.sawitpro;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GoogleService {
    private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    public Credential getCredentials() throws Exception {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GSON_FACTORY, new InputStreamReader(new FileInputStream(CREDENTIALS_FILE_PATH)));
        FileDataStoreFactory dataStore = new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, GSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(dataStore)
                .setAccessType("offline")
                .build();
        Credential credential = flow.loadCredential("user");
        if (credential == null) {
            credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        }

        if (credential == null) {
            throw new Exception("No credential found.");
        } else if (credential.getExpirationTimeMilliseconds() < new Date().getTime()) {
            credential.refreshToken();
            if (credential.getExpirationTimeMilliseconds() < new Date().getTime()) {
                throw new Exception("Credentials expired");
            }
        }
        return credential;
    }
}
