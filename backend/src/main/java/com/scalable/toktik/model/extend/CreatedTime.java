package com.scalable.toktik.model.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class CreatedTime {
    @Column(name = "created", nullable = false, updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
    @CreatedDate
    private LocalDateTime created = LocalDateTime.now();
}
