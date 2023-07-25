package com.apporchid.aosecuritynotifierservice;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@ComponentScan
@SpringBootApplication
public class AoSecurityNotifierServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AoSecurityNotifierServiceApplication.class, args);
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