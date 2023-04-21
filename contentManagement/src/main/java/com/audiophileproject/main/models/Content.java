package com.audiophileproject.main.models;

import com.audiophileproject.main.exceptions.UnsupportedContentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.http.MediaType;

import java.net.URL;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contents")
public class Content {
	@Id
	@SequenceGenerator(
			name = "content_id_sequence",
			sequenceName = "content_id_sequence"
	)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "content_id_sequence"
	)
	private Long id;
	@NotNull
	private String title;
	@NotNull
	@Column(columnDefinition = "TEXT")
	private URL url;
	private String contentType;
	private Long length;
	@Lob
	private String description;
	private Date pubDate;
	@ElementCollection
	private List<String> tags;
	@Column(nullable = false)
	private String userId;


	public void setContentType(String contentType) throws UnsupportedContentType {
		this.contentType = contentType;
		if (!isSupportedMediaType(contentType))
			throw new UnsupportedContentType("(" + contentType + ") Unsupported content type");
	}

	private boolean isSupportedMediaType(String type) {
		try {
			MediaType mediaType = MediaType.parseMediaType(type);
			return isSupportedAudioType(mediaType);
		} catch (Exception ex) {
			return false;
		}
	}

	private boolean isSupportedAudioType(MediaType mediaType) {
		return mediaType.getType().equals("audio");
	}

}
