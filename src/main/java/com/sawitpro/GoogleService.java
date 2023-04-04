package com.sawitpro;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleService {
    private static final String APPLICATION_NAME = "Sawit Pro Test";
    private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";
    private Credential credential;

    public Drive getDrive() {
        return new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public Credential getCredentials() throws Exception {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GSON_FACTORY, new InputStreamReader(new FileInputStream(CREDENTIALS_FILE_PATH)));
        FileDataStoreFactory dataStore = new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, GSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(dataStore)
                .setAccessType("offline")
                .build();
        credential = flow.loadCredential("user");
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

    public String uploadImageAsDocument(String imagePath) throws IOException {
        File fileMetadata = new File();
        java.io.File filePath = new java.io.File(imagePath);
        fileMetadata.setName(filePath.getName());
        fileMetadata.setMimeType("application/vnd.google-apps.document");
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        try {
            File file = this.getDrive().files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("File ID: " + file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    public String downloadExtractedText(String id) throws IOException {
        InputStream inputStream = this.getDrive().files().export(id, "text/plain").executeMedia().getContent();
        return new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public void deleteFile(String id) throws IOException {
        this.getDrive().files().delete(id).execute();
    }
}
