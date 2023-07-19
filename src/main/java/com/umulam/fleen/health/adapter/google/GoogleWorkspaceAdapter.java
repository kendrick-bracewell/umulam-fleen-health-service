package com.umulam.fleen.health.adapter.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

import static com.google.api.services.calendar.CalendarScopes.CALENDAR;
import static com.google.api.services.calendar.CalendarScopes.CALENDAR_EVENTS;

@Configuration
public class GoogleWorkspaceAdapter {

  private final String APP_NAME;
  private final String CREDENTIAL_PATH;
  private final String ADMIN_EMAIL;

  public GoogleWorkspaceAdapter(@Value("${app.name}") String appName,
                                @Value("${google.credentials.path}") String credentialPath,
                                @Value("${google.admin.email}") String adminEmail) {
    this.APP_NAME = appName;
    this.CREDENTIAL_PATH = credentialPath;
    this.ADMIN_EMAIL = adminEmail;
  }

  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final List<String> SCOPES = List.of(CALENDAR, CALENDAR_EVENTS);

  @Bean
  public Calendar getCalendar() throws GeneralSecurityException, IOException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(getServiceCredential()))
            .setApplicationName(APP_NAME)
            .build();
  }

  private InputStream getCredentialInputStream() throws IOException {
    InputStream in = GoogleWorkspaceAdapter.class.getResourceAsStream(CREDENTIAL_PATH);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: ".concat(CREDENTIAL_PATH));
    }
    return in;
  }

  private GoogleCredentials getServiceCredential() throws IOException {
    GoogleCredentials credentials = GoogleCredentials.fromStream(
            Objects.requireNonNull(getCredentialInputStream()))
            .createScoped(SCOPES)
            .createDelegated(ADMIN_EMAIL);
    credentials.refreshIfExpired();
    System.out.println(credentials);
    return credentials;
  }
}
