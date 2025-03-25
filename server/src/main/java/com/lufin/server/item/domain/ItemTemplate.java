package com.lufin.server.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_templates")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_template_id", nullable = false)
	private Integer id;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	public static ItemTemplate create(String name) {
		ItemTemplate template = new ItemTemplate();
		template.name = name;
		return template;
	}

	public void changeName(String name) {
		this.name = name;
	}
}
