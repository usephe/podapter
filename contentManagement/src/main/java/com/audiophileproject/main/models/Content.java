package com.audiophileproject.main.models;

import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
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
	private String title;
	private String description;
	private String fileUrl;
	@ManyToOne
	@JoinColumn(name = "user_id")
//	@Column(nullable = false)
	private User user;
}
