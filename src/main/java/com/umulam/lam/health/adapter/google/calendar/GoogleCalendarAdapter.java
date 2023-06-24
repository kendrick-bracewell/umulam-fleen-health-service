package com.umulam.lam.health.adapter.google.calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Configuration
public class GoogleCalendarAdapter {

  private static final String APPLICATION_NAME = "Lam Health";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final String TOKENS_DIRECTORY_PATH = "tokens";
  private static final List<String> SCOPES = List.of(CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR_EVENTS);
  private static final String CREDENTIALS_FILE_PATH = "/secret/oauth-credential.json";
  private static final String SERVICE_CREDENTIALS_FILE_PATH = "/secret/service-credential.json";

  @Bean
  public Calendar getCalendar() throws GeneralSecurityException, IOException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(getServiceCredential()))
            .setApplicationName(APPLICATION_NAME)
            .build();
  }

  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
    GoogleAuthorizationCodeFlow flow = getGoogleAuthorizationCodeFlow(HTTP_TRANSPORT, getClientSecrets());
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    return new AuthorizationCodeInstalledApp(flow, receiver)
            .authorize("user");
  }

  private static InputStream getCredentialInputStream(String path) throws IOException {
    InputStream in = GoogleCalendarAdapter.class.getResourceAsStream(path);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: ".concat(path));
    }
    return in;
  }

  private static GoogleClientSecrets getClientSecrets() throws IOException {
    return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(getCredentialInputStream(CREDENTIALS_FILE_PATH)));
  }

  private static GoogleAuthorizationCodeFlow getGoogleAuthorizationCodeFlow(
          final NetHttpTransport HTTP_TRANSPORT, GoogleClientSecrets clientSecrets) throws IOException {
    return new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();
  }

  public void empty1() throws IOException {
    GoogleCredentials credential =
            GoogleCredentials.fromStream(
                            Objects.requireNonNull(GoogleCalendarAdapter.class.
                                    getClassLoader().
                                    getResourceAsStream(CREDENTIALS_FILE_PATH)))
                    .createScoped(
                            List.of(CalendarScopes.CALENDAR_EVENTS))
                    .createDelegated("info@volunux.com");
  }

  public GoogleCredentials getServiceCredential() throws IOException {
    GoogleCredentials credentials = GoogleCredentials.fromStream(
            Objects.requireNonNull(getCredentialInputStream(SERVICE_CREDENTIALS_FILE_PATH)))
            .createScoped(
                    List.of(CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_EVENTS))
            .createDelegated("ulam-health@lam-health-390620.iam.gserviceaccount.com");
    credentials.refreshIfExpired();
    System.out.println("Credentials.............................................");
    System.out.println(credentials);
    return credentials;
  }
}
