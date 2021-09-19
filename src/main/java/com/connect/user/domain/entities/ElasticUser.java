package com.connect.user.domain.entities;

import com.connect.shared.enums.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "users")
@NoArgsConstructor
@AllArgsConstructor
public class ElasticUser {

    @Id
    private String id;

    @Field(name = "username", type = FieldType.Text)
    private String nickname;

    @Field(name = "profilePicture", type = FieldType.Text)
    private String profilePicture;

    @Field(name = "connectionStatus", type = FieldType.Auto)
    private ConnectionStatus connectionStatus;

    public ElasticUser(String id, String nickname){
        this.id = id;
        this.nickname = nickname;
        this.connectionStatus = ConnectionStatus.OFFLINE;
    }

}