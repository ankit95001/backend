package com.ecom.product_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "sequence")
public class Sequence {
    @Id
    //sequence id
    private String id;
    //Sequence number
    private int seq_No;
}
