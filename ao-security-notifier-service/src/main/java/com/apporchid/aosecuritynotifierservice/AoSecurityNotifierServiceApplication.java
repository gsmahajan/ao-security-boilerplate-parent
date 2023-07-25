package com.apporchid.aosecuritynotifierservice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.client.RestTemplate;

@ComponentScan
@SpringBootApplication
public class AoSecurityNotifierServiceApplication {

	public static void main(String[] args) {
		//SpringApplication.run(AoSecurityNotifierServiceApplication.class, args);
		Slack.fire(new LoginNotification(1L, "foo.bar@mailinator.com", "asdad", "asdad","asdad"));
	}
}

@Entity
class Notification {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	Long id;
	String severity;
	String processor;
	String severityInfo;

	LocalDateTime occuredAt;
	LocalDateTime notificationProcessedAt;

	public Notification() {
		super();
	}

	public Notification(Long id, String severity, String processor, String severityInfo, LocalDateTime occuredAt,
			LocalDateTime notificationProcessedAt) {
		super();
		this.id = id;
		this.severity = severity;
		this.processor = processor;
		this.severityInfo = severityInfo;
		this.occuredAt = occuredAt;
		this.notificationProcessedAt = notificationProcessedAt;
	}


	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getSeverityInfo() {
		return severityInfo;
	}

	public void setSeverityInfo(String severityInfo) {
		this.severityInfo = severityInfo;
	}

	public LocalDateTime getOccuredAt() {
		return occuredAt;
	}

	public void setOccuredAt(LocalDateTime occuredAt) {
		this.occuredAt = occuredAt;
	}

	public LocalDateTime getNotificationProcessedAt() {
		return notificationProcessedAt;
	}

	public void setNotificationProcessedAt(LocalDateTime notificationProcessedAt) {
		this.notificationProcessedAt = notificationProcessedAt;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Notification => " + ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}

@RepositoryRestResource
interface NotificationRepository extends PagingAndSortingRepository<Notification, Long>{

}



@Entity
class LoginNotification implements SlackNotification{
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	Long id;
	String email;
	String url;
	String clientId;
	String clientSecret;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public LoginNotification() {
		super();
		// Why JPA why ??
	}
	public LoginNotification(Long id, String email, String url, String clientId, String clientSecret) {
		super();
		this.id = id;
		this.email = email;
		this.url = url;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}
	@Override
	public String toString() {
		return "LoginNotification => " + ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
@RepositoryRestResource
interface LoginNotificationRepository extends CrudRepository<LoginNotification, Long>{

}


@Entity
class SignupNotification implements SlackNotification {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	Long id;
	String email;
	String url;
	final String clientId = UUID.randomUUID().toString().replaceAll("-", "");
	String clientSecret;

	public SignupNotification() {
		super();
		// Why JPA why ??
	}
	public SignupNotification(Long id, String email, String url, String clientSecret) {
		super();
		this.id = id;
		this.email = email;
		this.url = url;
		this.clientSecret = clientSecret;
	}
	@Override
	public String toString() {
		return "SignupNotification => " + ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}

@RepositoryRestResource
interface SignupNotificationRepository extends CrudRepository<SlackNotification, Long>{

}

interface SlackNotification{};


class Slack {
	/**
	 * Slack webhook url - More info {@link} https://apporchid.slack.com/apps/A0F7XDUAZ-incoming-webhooks?page=1
	 */
	private static final String SLACK_DIRTY_CONFIG_WEBHOOK_URL= "https://hooks.slack.com/services/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";

	protected static void fire(LoginNotification lo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("payload", "{\\\"text\\\": \\\"<http://10.7.20.148:18529/cloudseer/peoples/9931300|User (email:"+lo.getEmail()+")> has access security portal\\\"}");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForEntity(SLACK_DIRTY_CONFIG_WEBHOOK_URL, Object.class, null, params);
	}
}