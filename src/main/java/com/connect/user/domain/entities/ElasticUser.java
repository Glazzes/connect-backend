package com.connect.user.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Document(indexName = "users")
@NoArgsConstructor
@AllArgsConstructor
public class ElasticUser {

    @Id
    private String id;

    @Field(name = "username", type = FieldType.Text)
    private String username;

    @Field(name = "lastLogIn", type = FieldType.Date)
    private LocalDateTime lastLogIn;

    @Transient
    private String profilePicture;

    public ElasticUser(String id, String username){
        this.id = id;
        this.username = username;
        this.lastLogIn = LocalDateTime.now();
    }

}